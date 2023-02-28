package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    protected abstract void addLast(Object obj);

    protected abstract Object first();

    protected abstract void removeFirst();

    protected abstract void removeAll();

    protected abstract void startIteration();

    protected abstract void iterateNext();

    protected abstract Object iterateCurrent();

    protected abstract void removeCurrent();

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

    @Override
    public boolean contains(Object obj) {
        startIteration();
        for (int i = 0; i < size; i++) {
            if (iterateCurrent().equals(obj)) {
                return true;
            }
            iterateNext();
        }
        return false;
    }

    @Override
    public boolean removeFirstOccurrence(Object obj) {
        if (contains(obj)) {
            removeCurrent();
            size--;
            return true;
        } else {
            return false;
        }
    }
}
