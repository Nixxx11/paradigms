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
    AbstractOperation.prototype.diff = function (varName) {
        return new this.constructor(...this.operands.map((expression) => expression.diff(varName)));
    }
    AbstractOperation.prototype.toString = function () {
        return this.operands.join(" ") + " " + this.symbol;
    }

    const operationsList = [];

    function makeOperation(operator, symbol, argsCount = operator.length) {
        const NewOperation = function (...operands) {
            AbstractOperation.call(this, ...operands);
        }
        NewOperation.argsCount = argsCount;
        NewOperation.overrideDiff = function (diffFunction) {
            this.prototype.diff = diffFunction;
        }
        NewOperation.prototype = Object.create(AbstractOperation.prototype);
        NewOperation.prototype.constructor = NewOperation;
        NewOperation.prototype.operator = operator;
        NewOperation.prototype.symbol = symbol;
        operationsList.push(NewOperation);
        return NewOperation;
    }

    return {makeOperation, operationsList}
})();
const makeOperation = expressions.makeOperation;

const Negate = makeOperation((n) => -n, "negate");

const Add = makeOperation((n1, n2) => n1 + n2, "+");

const Subtract = makeOperation((n1, n2) => n1 - n2, "-");

const Multiply = makeOperation((n1, n2) => n1 * n2, "*");
Multiply.overrideDiff(function (varName) {
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
});

const Divide = makeOperation((n1, n2) => n1 / n2, "/");
Divide.overrideDiff(function (varName) {
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
});

const vectorsLength = (function () {
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
    const distanceDiff = (argsCount) => function (varName) {
        return new Multiply(
            new Divide(
                half,
                this
            ),
            new sumSq[argsCount](...this.operands).diff(varName)
        )
    };

    const sumSq = {};
    const distance = {};
    for (let i = 0; i <= 5; i++) {
        sumSq[i] = makeOperation(sumSqOperator, "sumsq" + i, i);
        sumSq[i].overrideDiff(sumSqDiff);
        distance[i] = makeOperation(distanceOperator, "distance" + i, i);
        distance[i].overrideDiff(distanceDiff(i));
    }

    return {sumSq, distance};
})();
const Sumsq2 = vectorsLength.sumSq[2];
const Sumsq3 = vectorsLength.sumSq[3];
const Sumsq4 = vectorsLength.sumSq[4];
const Sumsq5 = vectorsLength.sumSq[5];
const Distance2 = vectorsLength.distance[2];
const Distance3 = vectorsLength.distance[3];
const Distance4 = vectorsLength.distance[4];
const Distance5 = vectorsLength.distance[5];

const parse = (function () {
    const symbols = new Map(Object.keys(Variable.names).map(
        (varName) => [varName, new Variable(varName)]
    ));
    const operations = new Map(expressions.operationsList.map(
        (constructor) => [constructor.prototype.symbol, constructor]
    ));

    function parseOperation(string, makeExpression) {
        if (symbols.has(string)) {
            return symbols.get(string);
        } else if (operations.has(string)) {
            return makeExpression(operations.get(string));
        } else {
            return new Const(parseInt(string));
        }
    }

    return function (expression) {
        const tokens = expression.split(" ");
        const stack = [];
        const makeExpression = (operation) => {
            return new operation(...stack.splice(-operation.argsCount));
        }
        for (const token of tokens) {
            if (token.length !== 0) {
                stack.push(parseOperation(token, makeExpression));
            }
        }
        return stack.pop();
    };
})();