"use strict"

const Const = function (number) {
    this.number = number;
}
Const.prototype.evaluate = function (...vars) {
    return this.number;
};
Const.prototype.diff = function (varName) {
    return zero;
};
Const.prototype.postfix = Const.prototype.prefix = Const.prototype.toString = function () {
    return this.number.toString();
};

const zero = new Const(0);
const one = new Const(1);
const two = new Const(2);

const Variable = function (name) {
    this.name = name;
}
Variable.names = {
    "x": 0,
    "y": 1,
    "z": 2
};
Variable.prototype.evaluate = function (...vars) {
    return vars[Variable.names[this.name]];
};
Variable.prototype.diff = function (varName) {
    return varName === this.name ? one : zero;
};
Variable.prototype.postfix = Variable.prototype.prefix = Variable.prototype.toString = function () {
    return this.name;
};

const expressions = (function () {
    function AbstractOperation(...operands) {
        this.operands = operands;
    }

    AbstractOperation.prototype.evaluate = function (...vars) {
        return this.operator(...this.operands.map((expression) => expression.evaluate(...vars)));
    }
    AbstractOperation.prototype.toString = function () {
        return this.operands.join(" ") + " " + this.symbol;
    }
    AbstractOperation.prototype.prefix = function () {
        return "(" + this.symbol + " " + this.operands.map((expression) => expression.prefix()).join(" ") + ")";
    }
    AbstractOperation.prototype.postfix = function () {
        return "(" + this.operands.map((expression) => expression.postfix()).join(" ") + " " + this.symbol + ")";
    }

    const operationsList = [];

    function makeOperation(operator, symbol, diffFunction, argsCount = operator.length) {
        const NewOperation = function (...operands) {
            AbstractOperation.call(this, ...operands);
        }
        NewOperation.argsCount = argsCount;
        NewOperation.prototype = Object.create(AbstractOperation.prototype);
        NewOperation.prototype.constructor = NewOperation;
        NewOperation.prototype.operator = operator;
        NewOperation.prototype.symbol = symbol;
        NewOperation.prototype.diff = diffFunction;
        operationsList.push(NewOperation);
        return NewOperation;
    }

    return {makeOperation, operationsList};
})();
const makeOperation = expressions.makeOperation;
const sumExpression = (...operands) => operands.reduce((accumulator, expression) => new Add(accumulator, expression));
const diffFromCoefficients = function (varName, operands, coefficients) {
    return sumExpression(...(operands.map(
        (operand, i) => new Multiply(coefficients[i], operand.diff(varName))
    )));
};

const Negate = makeOperation((n) => -n, "negate", function (varName) {
    return new Negate(this.operands[0].diff(varName));
});

const Add = makeOperation((n1, n2) => n1 + n2, "+", function (varName) {
    return new Add(this.operands[0].diff(varName), this.operands[1].diff(varName));
});

const Subtract = makeOperation((n1, n2) => n1 - n2, "-", function (varName) {
    return new Subtract(this.operands[0].diff(varName), this.operands[1].diff(varName));
});

const Multiply = makeOperation((n1, n2) => n1 * n2, "*", function (varName) {
    return diffFromCoefficients(varName, this.operands, [this.operands[1], this.operands[0]]);
});

const Divide = makeOperation((n1, n2) => n1 / n2, "/", function (varName) {
    return diffFromCoefficients(varName, this.operands, [
        new Divide(
            one,
            this.operands[1]
        ),
        new Negate(
            new Divide(
                this.operands[0],
                new Multiply(
                    this.operands[1],
                    this.operands[1]
                )
            )
        )
    ]);
});

const distances = (function () {
    const sumSqOperator = (...numbers) => numbers.reduce((squareSum, current) => squareSum + current * current, 0);
    const sumSqDiff = function (varName) {
        return new Multiply(
            two,
            diffFromCoefficients(varName, this.operands, this.operands)
        );
    };
    const distanceOperator = (...numbers) => Math.sqrt(sumSqOperator(...numbers));
    const distanceDiff = function (varName) {
        return new Divide(
            diffFromCoefficients(varName, this.operands, this.operands),
            this
        );
    };

    const sumSq = (i) => makeOperation(sumSqOperator, "sumsq" + i, sumSqDiff, i);
    const distance = (i) => makeOperation(distanceOperator, "distance" + i, distanceDiff, i);

    return {sumSq, distance};
})();
const [Sumsq2, Sumsq3, Sumsq4, Sumsq5] = [2, 3, 4, 5].map(distances.sumSq);
const [Distance2, Distance3, Distance4, Distance5] = [2, 3, 4, 5].map(distances.distance);

const sumExpOperator = (...numbers) => numbers.reduce((sumExp, current) => sumExp + Math.exp(current), 0);
const Sumexp = makeOperation(sumExpOperator, "sumexp",
    function (varName) {
        return diffFromCoefficients(varName, this.operands, this.operands.map((operand) => new Sumexp(operand)));
    }
);
const LSE = makeOperation((...numbers) => Math.log(sumExpOperator(...numbers)), "lse",
    function (varName) {
        return new Divide(
            diffFromCoefficients(varName, this.operands, this.operands.map((operand) => new Sumexp(operand))),
            new Sumexp(...this.operands)
        );
    }
);

const parser = (function () {
    // Первая проверка:
    // :NOTE: можно без new Map
    // Вторая проверка:
    // :NOTE: можно сделать new Map(Variable.names.map(...)), так будет проще и читаемее
    // :(
    // справедливо
    const symbols = new Map(Object.keys(Variable.names).map(
        (varName) => [varName, new Variable(varName)]
    ));
    const operations = new Map(expressions.operationsList.map(
        (constructor) => [constructor.prototype.symbol, constructor]
    ));

    function ParsingError(message) {
        this.message = message;
    }

    ParsingError.prototype = Object.create(Error.prototype);
    ParsingError.prototype.name = "ParsingError";
    ParsingError.prototype.constructor = ParsingError;

    function check(condition, position, errorMessage) {
        if (!condition) {
            throw new ParsingError("Token " + position + ": " + errorMessage);
        }
    }

    function parse(expression) {
        const tokens = expression.trim().split(/\s+/);
        const stack = [];
        tokens.forEach((token, i) => {
            if (symbols.has(token)) {
                stack.push(symbols.get(token));
            } else if (operations.has(token)) {
                const Operation = operations.get(token);
                check(
                    Operation.argsCount !== 0,
                    i,
                    "Used operation with unknown number of arguments: '" + Operation.prototype.symbol + "'"
                );
                check(
                    stack.length >= Operation.argsCount,
                    i,
                    "'" + Operation.prototype.symbol + "' does not have enough operands"
                );
                stack.push(new Operation(...stack.splice(-Operation.argsCount)));
            } else {
                const number = parseInt(token);
                check(number.toString() === token, i, token + " is not a valid token");
                stack.push(new Const(number));
            }
        });
        const result = stack.pop();
        check(stack.length === 0, tokens.length, "Excess tokens: " + stack);
        return result;
    }


    const brackets = new Set(["(", ")"]);

    function splitWithParentheses(string) {
        // :NOTE: возьми стандартный split и не мучайся
        // скорее всего такой регулярки хватит /([\(\)\s])/
        const result = [];
        for (let i = 0; i < string.length; i++) {
            if (brackets.has(string.charAt(i))) {
                result.push(string.charAt(i));
            } else if (string.charAt(i) !== " ") {
                const from = i;
                while (i + 1 < string.length && !brackets.has(string.charAt(i + 1)) && string.charAt(i + 1) !== " ") {
                    i++;
                }
                result.push(string.substring(from, i + 1));
            }
        }
        return result;
    }

    function parseWithBrackets(tokens, openingBrackets, reverse) {
        let i = 0;
        const position = reverse ? () => tokens.length - i : () => i;

        function parseExpression() {
            const token = tokens[i];
            i++;
            if (token in openingBrackets) {
                check(i < tokens.length, position(), "No tokens for '" + token + "'");
                const Operation = operations.get(tokens[i]);
                check(Operation !== undefined, position(), "Unknown operation: '" + tokens[i] + "'");
                i++;
                const closingBracket = openingBrackets[token];
                const args = [];
                while (i < tokens.length && tokens[i] !== closingBracket) {
                    args.push(parseExpression());
                }
                check(
                    tokens[i] === closingBracket,
                    position(),
                    "No '" + closingBracket + "' for " + Operation.prototype.symbol
                );
                check(
                    Operation.argsCount === 0 || Operation.argsCount === args.length,
                    position(),
                    Operation.argsCount + " arguments expected for '" + Operation.prototype.symbol +
                    "', got " + args.length
                );
                i++;
                return reverse ? new Operation(...args.reverse()) : new Operation(...args);
            } else {
                if (symbols.has(token)) {
                    return symbols.get(token);
                } else {
                    check(
                        !isNaN(token),
                        position(),
                        "'" + token + "' is not a valid token"
                    );
                    return new Const(parseInt(token));
                }
            }
        }

        const result = parseExpression();
        check(
            i === tokens.length,
            position(),
            "Excess tokens: " + reverse ? tokens.slice(i).reverse() : tokens.slice(i)
        );
        return result;
    }

    function parsePrefix(expression) {
        const tokens = splitWithParentheses(expression);
        return parseWithBrackets(tokens, {"(": ")"}, false);
    }

    function parsePostfix(expression) {
        const tokens = splitWithParentheses(expression);
        return parseWithBrackets(tokens.reverse(), {")": "("}, true);
    }

    return {parse, parsePrefix, parsePostfix, ParsingError};
})();
const parse = parser.parse;
const parsePrefix = parser.parsePrefix;
const parsePostfix = parser.parsePostfix;

// :NOTE: Empty input              : org.graalvm.polyglot.PolyglotException: ParsingError: Token -1: 'undefined' is not a valid token
// undefined в выражении нет, еще и -1 индекс

// :NOTE: Missing )                : org.graalvm.polyglot.PolyglotException: ParsingError: Token 7: '*' is not a valid token
// вполне себе валидный токен

// :NOTE: Excessive info           : org.graalvm.polyglot.PolyglotException: ParsingError: Token 5: (,x,y,+,)
// ничего не поняла

// :NOTE: Empty op                 : org.graalvm.polyglot.PolyglotException: ParsingError: Token 1: Unknown operation: '('
// понятно, что скобки это специальные символы, а не операции
// а что делать с этим пользователю непонятно