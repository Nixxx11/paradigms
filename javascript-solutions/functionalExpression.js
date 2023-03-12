"use strict"

function calculate(x, y, z, ...functions) {
    const result = [];
    for (const f of functions) {
        result.push(f(x, y, z));
    }
    return result;
}

function makeOperation(operator) {
    return (...operands) => (x, y, z) => operator(...calculate(x, y, z, ...operands));
}

const cnst = (value) => (x, y, z) => value;

const variable = (name) => {
    switch (name) {
        case "x":
            return (x, y, z) => x;
        case "y":
            return (x, y, z) => y;
        case "z":
            return (x, y, z) => z;
    }
}

const add = makeOperation((n1, n2) => n1 + n2);
const subtract = makeOperation((n1, n2) => n1 - n2);
const multiply = makeOperation((n1, n2) => n1 * n2);
const divide = makeOperation((n1, n2) => n1 / n2);
const negate = makeOperation((n) => -n);

function parseOperation(string, getOperand) {
    switch (string) {
        case "x":
        case "y":
        case "z":
            return variable(string);
        case "+":
            return createBinaryExpression(add, getOperand);
        case "-":
            return createBinaryExpression(subtract, getOperand);
        case "*":
            return createBinaryExpression(multiply, getOperand);
        case "/":
            return createBinaryExpression(divide, getOperand);
        case "negate":
            return negate(getOperand());
        default:
            return cnst(parseInt(string));
    }
}

function createBinaryExpression(operator, getOperand) {
    const right = getOperand();
    return operator(getOperand(), right);
}

function parse(expression) {
    const tokens = expression.split(" ");
    const stack = [];
    const getOperand = () => stack.pop();
    for (const token of tokens) {
        if (token.length !== 0) {
            stack.push(parseOperation(token, getOperand));
        }
    }
    return getOperand();
}