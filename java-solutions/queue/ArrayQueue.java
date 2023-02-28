package queue;

import java.util.Objects;

public class ArrayQueue extends AbstractQueue {
    private static final int INITIAL_SIZE = 5;
    private Object[] arr = new Object[INITIAL_SIZE];
    private int start = 0;
    private int iterationIndex;

    // Model: a[1], ..., a[n]

    // Let:
    // immutable(k) <=> forall i = 1...k: a'[i] == a[i]

    @Override
    protected void addLast(Object obj) {
        ensureCapacity(size);
        arr[last()] = obj;
    }

    // Pre: obj != null
    // Post: n' == n + 1 && a'[1] = obj && forall i = 1...n: a'[i + 1] == a[i]
    public void push(Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity(size + 1);
        if (start == 0) {
            start = arr.length - 1;
        } else {
            start--;
        }
        size++;
        arr[start] = obj;
    }

    @Override
    protected Object first() {
        return arr[start];
    }

    // Pre: n >= 1
    // Post: R == a[n] && n' == n && immutable(n)
    public Object peek() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        return arr[last()];
    }

    @Override
    protected void removeFirst() {
        arr[start] = null;
        incrementStart();
    }

    // Pre: n >= 1
    // Post: R == a[n] && n' == n - 1 && immutable(n')
    public Object remove() {
        final Object result = peek();
        arr[last()] = null;
        size--;
        return result;
    }

    @Override
    protected void removeAll() {
        arr = new Object[INITIAL_SIZE];
        start = 0;
    }

    @Override
    protected void startIteration() {
        iterationIndex = start;
    }

    @Override
    protected void iterateNext() {
        if (iterationIndex == arr.length - 1) {
            iterationIndex = 0;
        } else {
            iterationIndex++;
        }
    }

    @Override
    protected Object iterateCurrent() {
        return arr[iterationIndex];
    }

    @Override
    protected void removeCurrent() {
        if (iterationIndex >= start) {
            System.arraycopy(arr, start, arr, start + 1, iterationIndex - start);
            arr[start] = null;
            incrementStart();
        } else {
            int last = last();
            System.arraycopy(arr, iterationIndex + 1, arr, iterationIndex, last - iterationIndex);
            arr[last] = null;
        }
    }

    // Pre: true
    // Post: R == {a[1], ..., a[n]} && n' == n && immutable(n)
    public Object[] toArray() {
        return toArray(size);
    }

    private int last() {
        int last = start + size - 1;
        if (last >= arr.length) {
            last -= arr.length;
        }
        return last;
    }

    private void incrementStart() {
        if (start == arr.length - 1) {
            start = 0;
        } else {
            start++;
        }
    }

    private Object[] toArray(int newSize) {
        final Object[] result = new Object[newSize];
        if (start + size <= arr.length) {
            System.arraycopy(arr, start, result, 0, size);
        } else {
            int firstHalfSize = arr.length - start;
            System.arraycopy(arr, start, result, 0, firstHalfSize);
            System.arraycopy(arr, 0, result, firstHalfSize, size - firstHalfSize);
        }
        return result;
    }

    private void ensureCapacity(int capacity) {
        if (capacity > arr.length) {
            arr = toArray(Math.max(capacity, arr.length * 2));
            start = 0;
        }
    }
}
