"use strict"

const Const = function (number) {
    this.number = number;
}
const zero = new Const(0);
const one = new Const(1);
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

const operationsList = [];
const makeOperation = (function () {
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
        return this.operands.reduce(
            (string, expression) => string + expression.toString() + " ",
            ""
        ) + this.symbol;
    }

    return function (operator, symbol) {
        const NewOperation = function (...operands) {
            AbstractOperation.call(this, ...operands);
        }
        NewOperation.argsCount = operator.length;
        NewOperation.prototype = Object.create(AbstractOperation.prototype);
        NewOperation.prototype.constructor = NewOperation;
        NewOperation.prototype.operator = operator;
        NewOperation.prototype.symbol = symbol;
        operationsList.push(NewOperation);
        return NewOperation;
    }
})();

const Negate = makeOperation((n) => -n, "negate");

const Add = makeOperation((n1, n2) => n1 + n2, "+");

const Subtract = makeOperation((n1, n2) => n1 - n2, "-");

const Multiply = makeOperation((n1, n2) => n1 * n2, "*");
Multiply.prototype.diff = function (varName) {
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
};

const Divide = makeOperation((n1, n2) => n1 / n2, "/");
Divide.prototype.diff = function (varName) {
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
};

const parse = (function () {
    const symbols = new Map(Object.keys(Variable.names).map(
        (varName) => [varName, new Variable(varName)]
    ));
    const operations = new Map(operationsList.map(
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