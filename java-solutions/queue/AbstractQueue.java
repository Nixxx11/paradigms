package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    protected abstract void addLast(Object obj);

    protected abstract Object first();

    protected abstract void removeFirst();

    protected abstract void removeAll();

    @Override
    public void enqueue(final Object obj) {
        Objects.requireNonNull(obj);
        size++;
        addLast(obj);
    }

    @Override
    public Object element() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        return first();
    }

    @Override
    public Object dequeue() {
        final Object result = element();
        size--;
        removeFirst();
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        removeAll();
        size = 0;
    }
}
