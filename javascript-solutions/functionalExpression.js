"use strict"

function variable(name) {
    return (x, y, z) => {
        switch (name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }
    }
}

function cnst(value) {
    return () => value;
}

function makeBinaryOperation(operator) {
    return (leftOperand, rightOperand) => (x, y, z) => operator(leftOperand(x, y, z), rightOperand(x, y, z));
}

function makeUnaryOperation(operator) {
    return (innerOperand) => (x, y, z) => operator(innerOperand(x, y, z));
}

const add = makeBinaryOperation((n1, n2) => n1 + n2);
const subtract = makeBinaryOperation((n1, n2) => n1 - n2);
const multiply = makeBinaryOperation((n1, n2) => n1 * n2);
const divide = makeBinaryOperation((n1, n2) => n1 / n2);

const negate = makeUnaryOperation((n) => -n);