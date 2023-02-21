package queue;

import java.util.Objects;

public class ArrayQueue {
    private static final int INITIAL_SIZE = 5;
    private Object[] arr = new Object[INITIAL_SIZE];
    private int start = 0;
    private int size = 0;

    // Model: a[1], ..., a[n]

    // Let:
    // immutable(k) <=> forall i = 1...k: a'[i] == a[i]

    // Pre: obj != null
    // Post: n' == n + 1 && immutable(n) && a'[n'] == obj
    public void enqueue(Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity(size + 1);
        int end = start + size;
        if (end >= arr.length) {
            end -= arr.length;
        }
        arr[end] = obj;
        size++;
    }

    // Pre: obj != null
    // Post: n' == n + 1 && a'[1] = obj && forall i = 1...n: a'[i + 1] == a[i]
    public void push(Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity(size + 1);
        start--;
        if (start < 0) {
            start += arr.length;
        }
        arr[start] = obj;
        size++;
    }

    // Pre: n >= 1
    // Post: R == a[1] && n' == n && immutable(n)
    public Object element() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        return arr[start];
    }


    // Pre: n >= 1
    // Post: R == a[n] && n' == n && immutable(n)
    public Object peek() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        int last = start + size - 1;
        if (last >= arr.length) {
            last -= arr.length;
        }
        return arr[last];
    }

    // Pre: n >= 1
    // Post: R == a[1] && n' == n - 1 && forall i = 1...n': a'[i] == a[i + 1]
    public Object dequeue() {
        Object result = element();
        arr[start] = null;
        size--;
        start++;
        if (start == arr.length) {
            start = 0;
        }
        return result;
    }

    // Pre: n >= 1
    // Post: R == a[n] && n' == n - 1 && immutable(n')
    public Object remove() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        int last = start + size - 1;
        if (last >= arr.length) {
            last -= arr.length;
        }
        Object result = arr[last];
        arr[last] = null;
        size--;
        return result;
    }

    // Pre: true
    // Post: R == n
    public int size() {
        return size;
    }

    // Pre: true
    // Post: R == (n == 0)
    public boolean isEmpty() {
        return size == 0;
    }

    // Pre: true
    // Post: n' == 0
    public void clear() {
        arr = new Object[INITIAL_SIZE];
        start = 0;
        size = 0;
    }

    // Pre: true
    // Post: Rn' == n && immutable(n)
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = start;
        for (int i = 0; i < size; i++) {
            result[i] = arr[index];
            index++;
            if (index == arr.length) {
                index = 0;
            }
        }
        return result;
    }

    private void ensureCapacity(int capacity) {
        if (capacity < arr.length) {
            return;
        }
        int ind = start;
        int oldSize = size;
        Object[] old = arr;
        arr = new Object[Math.max(capacity, old.length * 2)];
        start = 0;
        size = 0;
        for (int i = 0; i < oldSize; i++) {
            enqueue(old[ind]);
            ind++;
            if (ind == old.length) {
                ind = 0;
            }
        }
    }
}
