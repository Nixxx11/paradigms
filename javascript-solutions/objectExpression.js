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
Const.prototype.prefix = Const.prototype.toString = function () {
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
Variable.prototype.prefix = Variable.prototype.toString = function () {
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

    const sumSq = [];
    const distance = [];
    for (let i = 2; i <= 5; i++) {
        sumSq.push(makeOperation(sumSqOperator, "sumsq" + i, sumSqDiff, i));
        distance.push(makeOperation(distanceOperator, "distance" + i, distanceDiff, i));
    }

    return {sumSq, distance};
})();
const [Sumsq2, Sumsq3, Sumsq4, Sumsq5] = distances.sumSq;
const [Distance2, Distance3, Distance4, Distance5] = distances.distance;

const parser = (function () {
    const symbols = Object.keys(Variable.names).reduce(
        (map, varName) => ({...map, [varName]: new Variable(varName)}),
        {}
    );
    const operations = expressions.operationsList.reduce(
        (map, constructor) => ({...map, [constructor.prototype.symbol]: constructor}),
        {}
    );

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

    function parseSuffix(expression) {
        const tokens = expression.split(" ").filter((token) => token.length > 0);
        const stack = [];
        tokens.forEach((token, i) => {
            if (token in symbols) {
                stack.push(symbols[token]);
            } else if (token in operations) {
                const Operation = operations[token];
                check(
                    stack.length >= Operation.argsCount,
                    i, "'" + Operation.prototype.symbol + "' does not have enough operands"
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


    const parentheses = {"(": ")", ")": "("};

    function splitWithParentheses(string) {
        const result = [];
        for (let i = 0; i < string.length; i++) {
            if (string.charAt(i) in parentheses) {
                result.push(string.charAt(i));
            } else if (string.charAt(i) !== " ") {
                const from = i;
                while (i + 1 < string.length && !(string.charAt(i + 1) in parentheses) && string.charAt(i + 1) !== " ") {
                    i++;
                }
                result.push(string.substring(from, i + 1));
            }
        }
        return result;
    }

    function parsePrefix(expression) {
        const tokens = splitWithParentheses(expression);
        let i = 0;

        function parseOperation() {
            const token = tokens[i];
            i++;
            if (token === "(") {
                check(i < tokens.length, i, "No operation after '" + token + "'");
                const Operation = operations[tokens[i]];
                check(Operation !== undefined, i, "Unknown operation: '" + tokens[i] + "'");
                const args = [];
                i++;
                while (i < tokens.length && tokens[i] !== parentheses[token]) {
                    args.push(parseOperation());
                }
                check(
                    tokens[i] === parentheses[token],
                    i,
                    "No '" + parentheses[token] + "' for " + Operation.prototype.symbol
                );
                check(
                    Operation.argsCount === args.length,
                    i,
                    Operation.argsCount + " arguments expected for '" + Operation.prototype.symbol +
                    "', got " + args.length
                );
                i++;
                return new Operation(...args);
            } else {
                if (token in symbols) {
                    return symbols[token];
                } else {
                    const number = parseInt(token);
                    check(number.toString() === token, i, "'" + token + "' is not a valid token");
                    return new Const(number);
                }
            }
        }

        const result = parseOperation();
        check(i === tokens.length, i, "Excess tokens: " + tokens.slice(i));
        return result;
    }

    return {parseSuffix, parsePrefix, ParsingError};
})();
const parse = parser.parseSuffix;
const parsePrefix = parser.parsePrefix;
