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

function parseOperation(string, getOperands) {
    switch (string) {
        case "x":
        case "y":
        case "z":
            return variable(string);
        case "negate":
            return negate(...getOperands(1));
        case "+":
            return add(...getOperands(2));
        case "-":
            return subtract(...getOperands(2));
        case "*":
            return multiply(...getOperands(2));
        case "/":
            return divide(...getOperands(2));
        default:
            return cnst(parseInt(string));
    }
}

function parse(expression) {
    const tokens = expression.split(" ");
    const stack = [];
    const getOperands = (number) => stack.splice(-number);
    for (const token of tokens) {
        if (token.length !== 0) {
            stack.push(parseOperation(token, getOperands));
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