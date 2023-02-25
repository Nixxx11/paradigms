package queue;

import java.util.Objects;

public class ArrayQueueModule {
    private static final int INITIAL_SIZE = 5;
    private static Object[] arr = new Object[INITIAL_SIZE];
    private static int start = 0;
    private static int size = 0;

    // Model: a[1], ..., a[n]

    // Let:
    // immutable(k) <=> forall i = 1...k: a'[i] == a[i]

    // Pre: obj != null
    // Post: n' == n + 1 && immutable(n) && a'[n'] == obj
    public static void enqueue(Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity(size + 1);
        size++;
        arr[last()] = obj;
    }

    // Pre: obj != null
    // Post: n' == n + 1 && a'[1] = obj && forall i = 1...n: a'[i + 1] == a[i]
    public static void push(Object obj) {
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

    // Pre: n >= 1
    // Post: R == a[1] && n' == n && immutable(n)
    public static Object element() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        return arr[start];
    }

    // Pre: n >= 1
    // Post: R == a[n] && n' == n && immutable(n)
    public static Object peek() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        return arr[last()];
    }

    // Pre: n >= 1
    // Post: R == a[1] && n' == n - 1 && forall i = 1...n': a'[i] == a[i + 1]
    public static Object dequeue() {
        final Object result = element();
        arr[start] = null;
        size--;
        if (start == arr.length - 1) {
            start = 0;
        } else {
            start++;
        }
        return result;
    }

    // Pre: n >= 1
    // Post: R == a[n] && n' == n - 1 && immutable(n')
    public static Object remove() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        final int last = last();
        final Object result = arr[last];
        arr[last] = null;
        size--;
        return result;
    }

    // Pre: true
    // Post: R == n
    public static int size() {
        return size;
    }

    // Pre: true
    // Post: R == (n == 0)
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pre: true
    // Post: n' == 0
    public static void clear() {
        arr = new Object[INITIAL_SIZE];
        start = 0;
        size = 0;
    }

    // Pre: true
    // Post: R == {a[1], ..., a[n]} && n' == n && immutable(n)
    public static Object[] toArray() {
        return toArray(size);
    }

    private static int last() {
        int last = start + size - 1;
        if (last >= arr.length) {
            last -= arr.length;
        }
        return last;
    }

    private static Object[] toArray(int newSize) {
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

    private static void ensureCapacity(int capacity) {
        if (capacity > arr.length) {
            arr = toArray(Math.max(capacity, arr.length * 2));
            start = 0;
        }
    }
}
