"use strict"

function substitution(...vars) {
    return (f) => f(...vars);
}

function makeOperation(operator) {
    return (...operands) => (...vars) => operator(...operands.map(substitution(...vars)));
}

const cnst = (value) => (...vars) => value;

const variable = (name) => {
    switch (name) {
        case "x":
            return (...vars) => vars[0];
        case "y":
            return (...vars) => vars[1];
        case "z":
            return (...vars) => vars[2];
    }
}

const add = makeOperation((n1, n2) => n1 + n2);
const subtract = makeOperation((n1, n2) => n1 - n2);
const multiply = makeOperation((n1, n2) => n1 * n2);
const divide = makeOperation((n1, n2) => n1 / n2);
const negate = makeOperation((n) => -n);

const map = {
    "x": [variable("x"), 0],
    "y": [variable("y"), 0],
    "z": [variable("z"), 0],
    "negate": [negate, 1],
    "+": [add, 2],
    "-": [subtract, 2],
    "*": [multiply, 2],
    "/": [divide, 2]
};

function parseOperation(string, makeExpression) {
    if (string in map) {
        return makeExpression(...map[string]);
    } else {
        return cnst(parseInt(string));
    }
}

function parse(expression) {
    const tokens = expression.split(" ");
    const stack = [];
    const makeExpression = (operation, numberOfOperands) => {
        return numberOfOperands === 0 ? operation : operation(...stack.splice(-numberOfOperands));
    }
    for (const token of tokens) {
        if (token.length !== 0) {
            stack.push(parseOperation(token, makeExpression));
        }
    }
    return stack.pop();
}

/*
const test = add(
    subtract(
        multiply(
            variable("x"),
            variable("x")
        ),
        multiply(
            cnst(2),
            variable("x")
        )
    ),
    cnst(1)
);
for (let i = 0; i <= 10; i++) {
    println(test(i));
}
 */