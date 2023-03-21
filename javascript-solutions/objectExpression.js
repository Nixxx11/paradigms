"use strict"

const Const = function (number) {
    this.number = number;
}
const zero = new Const(0);
const half = new Const(0.5);
const one = new Const(1);
const two = new Const(2);
Const.prototype.evaluate = function (...vars) {
    return this.number;
};
Const.prototype.diff = function (varName) {
    return zero;
};
Const.prototype.toString = function () {
    return this.number.toString();
};

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
Variable.prototype.toString = function () {
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
        return new Divide(
            new Subtract(
                new Multiply(
                    this.operands[0].diff(varName),
                    this.operands[1]
                ),
                new Multiply(
                    this.operands[0],
                    this.operands[1].diff(varName)
                )
            ),
            new Multiply(
                this.operands[1],
                this.operands[1]
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

const parse = (function () {
    const symbols = Object.keys(Variable.names).reduce(
        (map, varName) => ({...map, [varName]: new Variable(varName)}),
        {}
    );
    const operations = expressions.operationsList.reduce(
        (map, constructor) => ({...map, [constructor.prototype.symbol]: constructor}),
        {}
    );
    const curry = (f) => (arg) => (...args) => f(arg, ...args);

    function parseOperation(makeExpression, string) {
        if (string in symbols) {
            return symbols[string];
        } else if (string in operations) {
            return makeExpression(operations[string]);
        } else {
            return new Const(parseInt(string));
        }
    }

    return function (expression) {
        const tokens = expression.split(" ");
        const stack = [];
        const parseWithStack = curry(parseOperation)(
            (operation) => new operation(...stack.splice(-operation.argsCount))
        );
        for (const token of tokens) {
            if (token.length !== 0) {
                stack.push(parseWithStack(token));
            }
        }
        return stack.pop();
    };
})();