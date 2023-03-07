package queue;

public class LinkedQueue extends AbstractQueue {
    private Node first = null;
    private Node last = null;
    private Node iterationPrevious;
    private Node iterationCurrent;

    private static class Node {
        private final Object obj;
        private Node next;

        Node(final Object obj, final Node next) {
            this.obj = obj;
            this.next = next;
        }
    }

    @Override
    protected void addLast(final Object obj) {
        final Node n = new Node(obj, null);
        if (size == 1) {
            first = n;
        } else {
            last.next = n;
        }
        last = n;
    }

    @Override
    protected Object first() {
        return first.obj;
    }

    @Override
    protected void removeFirst() {
        first = first.next;
    }

    @Override
    protected void removeAll() {
        first = null;
        last = null;
    }

    @Override
    protected void startIteration() {
        iterationCurrent = first;
        iterationPrevious = null;
    }

    @Override
    protected void iterateNext() {
        iterationPrevious = iterationCurrent;
        iterationCurrent = iterationCurrent.next;
    }

    @Override
    protected Object iterateCurrent() {
        return iterationCurrent.obj;
    }

    @Override
    protected void removeCurrent() {
        if (iterationCurrent.next == null) {
            last = iterationPrevious;
        }
        if (iterationPrevious == null) {
            first = iterationCurrent.next;
        } else {
            iterationPrevious.next = iterationCurrent.next;
        }
    }
}
