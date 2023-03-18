"use strict"

const expressions = (function () {
    function Const(number) {
        this.number = number;
    }

    const zero = new Const(0);
    const one = new Const(1);

    Const.prototype.evaluate = function (...vars) {
        return this.number;
    }
    Const.prototype.diff = function (varName) {
        return zero;
    }
    Const.prototype.toString = function () {
        return this.number.toString();
    }


    function Variable(name) {
        this.name = name;
    }

    Variable.prototype.evaluate = function (...vars) {
        switch (this.name) {
            case "x":
                return vars[0];
            case "y":
                return vars[1];
            case "z":
                return vars[2];
        }
    }
    Variable.prototype.diff = function (varName) {
        return varName === this.name ? one : zero;
    }
    Variable.prototype.toString = function () {
        return this.name;
    }


    function Operation(...operands) {
        this.operands = operands;
    }

    Operation.prototype.evaluate = function (...vars) {
        return this.operator(...this.operands.map((expression) => expression.evaluate(...vars)));
    }
    Operation.prototype.diff = function (varName) {
        return new this.constructor(...this.operands.map((expression) => expression.diff(varName)));
    }
    Operation.prototype.toString = function () {
        return this.operands.reduce(
            (string, expression) => string + expression.toString() + " ",
            ""
        ) + this.symbol;
    }

    function inherit(constructor, operator, symbol) {
        constructor.prototype = Object.create(Operation.prototype);
        constructor.prototype.constructor = constructor;
        constructor.prototype.operator = operator;
        constructor.prototype.symbol = symbol;
    }


    function Negate(operand) {
        Operation.call(this, operand);
    }

    inherit(Negate, (n) => -n, "negate");


    function Add(operand1, operand2) {
        Operation.call(this, operand1, operand2);
    }

    inherit(Add, (n1, n2) => n1 + n2, "+");


    function Subtract(operand1, operand2) {
        Operation.call(this, operand1, operand2);
    }

    inherit(Subtract, (n1, n2) => n1 - n2, "-");


    function Multiply(operand1, operand2) {
        Operation.call(this, operand1, operand2);
    }

    inherit(Multiply, (n1, n2) => n1 * n2, "*");
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
    }


    function Divide(operand1, operand2) {
        Operation.call(this, operand1, operand2);
    }

    inherit(Divide, (n1, n2) => n1 / n2, "/");
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
    }
    return {Const, Variable, operations: {Negate, Add, Subtract, Multiply, Divide}, variables: ["x", "y", "z"]};
})();

const Const = expressions.Const;
const Variable = expressions.Variable;
const Negate = expressions.operations.Negate;
const Add = expressions.operations.Add;
const Subtract = expressions.operations.Subtract;
const Multiply = expressions.operations.Multiply;
const Divide = expressions.operations.Divide;

const parse = (function () {
    const symbols = new Map(expressions.variables.map(
        (varName) => [varName, new Variable(varName)]
    ));

    const operations = new Map(Object.values(expressions.operations).map(
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
            return new operation(...stack.splice(-operation.length));
        }
        for (const token of tokens) {
            if (token.length !== 0) {
                stack.push(parseOperation(token, makeExpression));
            }
        }
        return stack.pop();
    }
})();