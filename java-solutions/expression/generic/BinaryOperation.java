package expression.generic;

import expression.Order;

public record BinaryOperation<T extends Number>(OperatorBinary<T> operator, Order order) {
}