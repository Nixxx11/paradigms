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
const half = new Const(0.5);
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

    return {makeOperation, operationsList}
})();
const makeOperation = expressions.makeOperation;

const defaultDiff = function (varName) {
    return new this.constructor(...this.operands.map((expression) => expression.diff(varName)));
};

const Negate = makeOperation((n) => -n, "negate", defaultDiff);

const Add = makeOperation((n1, n2) => n1 + n2, "+", defaultDiff);

const Subtract = makeOperation((n1, n2) => n1 - n2, "-", defaultDiff);

const Multiply = makeOperation((n1, n2) => n1 * n2, "*",
    function (varName) {
        return new Add(
            new Multiply(
                this.operands[0],
                this.operands[1].diff(varName)
            ),
            new Multiply(
                this.operands[0].diff(varName),
                this.operands[1]
            )
        );
    }
);

const Divide = makeOperation((n1, n2) => n1 / n2, "/",
    function (varName) {
        return new Subtract(
            new Divide(
                this.operands[0].diff(varName),
                this.operands[1]
            ),
            new Divide(
                new Multiply(
                    this.operands[0],
                    this.operands[1].diff(varName)
                ),
                new Multiply(
                    this.operands[1],
                    this.operands[1]
                )
            )
        );
    }
);

const distances = (function () {
    const sumSqOperator = (...numbers) => numbers.reduce((squareSum, current) => squareSum + current * current, 0);
    const sumSqDiff = function (varName) {
        return new Multiply(
            this.operands.map((expression) =>
                new Multiply(
                    expression,
                    expression.diff(varName)
                )
            ).reduce((accumulator, expression) => new Add(accumulator, expression)),
            two
        )
    };
    const distanceOperator = (...numbers) => Math.sqrt(sumSqOperator(...numbers));
    const distanceDiff = function (varName) {
        return new Multiply(
            new Divide(
                half,
                this
            ),
            sumSqDiff.call(this, varName)
        )
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
        for (let i = 0; i < tokens.length; i++) {
            const token = tokens[i];
            if (token in symbols) {
                stack.push(symbols[token]);
            } else if (token in operations) {
                const operation = operations[token];
                check(
                    stack.length >= operation.argsCount,
                    i, "'" + operation.prototype.symbol + "' does not have enough operands"
                );
                stack.push(new operation(...stack.splice(-operation.argsCount)));
            } else {
                const number = parseInt(token);
                check(number.toString() === token, i, token + " is not a valid token");
                stack.push(new Const(number));
            }
        }
        const result = stack.pop();
        check(stack.length === 0, tokens.length, "Excess tokens: " + stack);
        return result;
    }

    const parentheses = {"(": "(", ")": ")"};

    function splitWithParentheses(str) {
        const result = [];
        for (let pos = 0; pos < str.length; pos++) {
            if (str.charAt(pos) in parentheses) {
                result.push(str.charAt(pos));
            } else if (str.charAt(pos) !== " ") {
                const from = pos;
                while (pos + 1 < str.length && !(str.charAt(pos + 1) in parentheses) && str.charAt(pos + 1) !== " ") {
                    pos++;
                }
                result.push(str.substring(from, pos + 1));
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
                check(i < tokens.length, i, "'(' at the end of the string");
                check(tokens[i] in operations, i, "Unknown operation: " + tokens[i]);
                const operation = operations[tokens[i]];
                const args = [];
                i++;
                while (i < tokens.length && tokens[i] !== ")") {
                    args.push(parseOperation());
                }
                check(tokens[i] === ")", i, "No ')' for " + operation.prototype.symbol);
                check(
                    operation.argsCount === args.length,
                    i,
                    operation.argsCount + " arguments expected for '" + operation.prototype.symbol + "', got " + args.length
                );
                i++;
                return new operation(...args);
            } else {
                if (token in symbols) {
                    return symbols[token];
                } else {
                    const number = parseInt(token);
                    check(number.toString() === token, i, token + " is not a valid token");
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
