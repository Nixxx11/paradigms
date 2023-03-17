"use strict"

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


function Operation(operator, symbol, ...operands) {
    this.operator = operator;
    this.symbol = symbol;
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

function inherit(child, parent) {
    child.prototype = Object.create(parent.prototype);
    child.prototype.constructor = child;
}


function Negate(operand) {
    Operation.call(
        this,
        (n) => -n,
        "negate",
        operand
    );
}

inherit(Negate, Operation);


function Add(operand1, operand2) {
    Operation.call(
        this,
        (n1, n2) => n1 + n2,
        "+",
        operand1, operand2
    );
}

inherit(Add, Operation);


function Subtract(operand1, operand2) {
    Operation.call(
        this,
        (n1, n2) => n1 - n2,
        "-",
        operand1, operand2
    );
}

inherit(Subtract, Operation);


function Multiply(operand1, operand2) {
    Operation.call(
        this,
        (n1, n2) => n1 * n2,
        "*",
        operand1, operand2
    );
}

inherit(Multiply, Operation);
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
    Operation.call(
        this,
        (n1, n2) => n1 / n2,
        "/",
        operand1, operand2
    );
}

inherit(Divide, Operation);
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

const symbols = {
    "x": new Variable("x"),
    "y": new Variable("y"),
    "z": new Variable("z")
}

const operations = {
    "negate": Negate,
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide
};

function parseOperation(string, makeExpression) {
    if (string in symbols) {
        return symbols[string];
    } else if (string in operations) {
        return makeExpression(operations[string]);
    } else {
        return new Const(parseInt(string));
    }
}

function parse(expression) {
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