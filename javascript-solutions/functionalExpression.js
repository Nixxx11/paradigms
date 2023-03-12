"use strict"

function makeBinaryOperation(operator) {
    return (leftOperand, rightOperand) => (x, y, z) => operator(leftOperand(x, y, z), rightOperand(x, y, z));
}

function makeUnaryOperation(operator) {
    return (innerOperand) => (x, y, z) => operator(innerOperand(x, y, z));
}

const cnst = (value) => () => value;

const variable = (name) => (x, y, z) => {
    switch (name) {
        case "x":
            return x;
        case "y":
            return y;
        case "z":
            return z;
    }
}

const add = makeBinaryOperation((n1, n2) => n1 + n2);
const subtract = makeBinaryOperation((n1, n2) => n1 - n2);
const multiply = makeBinaryOperation((n1, n2) => n1 * n2);
const divide = makeBinaryOperation((n1, n2) => n1 / n2);

const negate = makeUnaryOperation((n) => -n);

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