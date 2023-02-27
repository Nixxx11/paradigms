package queue;

public class LinkedQueue extends AbstractQueue {
    private Node first = null;
    private Node last = null;

    private static class Node {
        private final Object obj;
        private Node next;

        Node(final Object obj, final Node next) {
            this.obj = obj;
            this.next = next;
        }
    }

    @Override
    protected void addLast(Object obj) {
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
}
