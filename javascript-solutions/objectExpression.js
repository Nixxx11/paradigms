"use strict"

function Const(number) {
    this.number = number;
}

Const.prototype.evaluate = function (...vars) {
    return this.number;
}
Const.prototype.diff = function (varName) {
    return new Const(0);
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
    if (varName === this.name) {
        return new Const(1);
    } else {
        return new Const(0);
    }
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


function Negate(operand) {
    Operation.call(
        this,
        (n) => -n,
        "negate",
        operand
    );
}

Negate.prototype = Object.create(Operation.prototype);
Negate.prototype.constructor = Negate;


function Add(operand1, operand2) {
    Operation.call(
        this,
        (n1, n2) => n1 + n2,
        "+",
        operand1, operand2
    );
}

Add.prototype = Object.create(Operation.prototype);
Add.prototype.constructor = Add;


function Subtract(operand1, operand2) {
    Operation.call(
        this,
        (n1, n2) => n1 - n2,
        "-",
        operand1, operand2
    );
}

Subtract.prototype = Object.create(Operation.prototype);
Subtract.prototype.constructor = Subtract;


function Multiply(operand1, operand2) {
    Operation.call(
        this,
        (n1, n2) => n1 * n2,
        "*",
        operand1, operand2
    );
}

Multiply.prototype = Object.create(Operation.prototype);
Multiply.prototype.constructor = Multiply;
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

Divide.prototype = Object.create(Operation.prototype);
Divide.prototype.constructor = Divide;
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


const map = {
    "x": [new Variable("x"), 0],
    "y": [new Variable("y"), 0],
    "z": [new Variable("z"), 0],
    "negate": [Negate, 1],
    "+": [Add, 2],
    "-": [Subtract, 2],
    "*": [Multiply, 2],
    "/": [Divide, 2]
};

function parseOperation(string, makeExpression) {
    if (string in map) {
        return makeExpression(...map[string]);
    } else {
        return new Const(parseInt(string));
    }
}

function parse(expression) {
    const tokens = expression.split(" ");
    const stack = [];
    const makeExpression = (operation, numberOfOperands) => {
        return numberOfOperands === 0 ? operation : new operation(...stack.splice(-numberOfOperands));
    }
    for (const token of tokens) {
        if (token.length !== 0) {
            stack.push(parseOperation(token, makeExpression));
        }
    }
    return stack.pop();
}