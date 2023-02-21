package queue;

import java.util.Objects;

public class ArrayQueueADT {
    private static final int INITIAL_SIZE = 5;
    private Object[] arr = new Object[INITIAL_SIZE];
    private int start;
    private int size;

    // Model: a[1], ..., a[n]

    // Let:
    // immutable(k) <=> forall i = 1...k: a'[i] == a[i]

    public static ArrayQueueADT create() {
        ArrayQueueADT queue = new ArrayQueueADT();
        queue.arr = new Object[INITIAL_SIZE];
        return queue;
    }

    // Pre: obj != null && queue != null
    // Post: n' == n + 1 && immutable(n) && a'[n'] == obj
    public static void enqueue(ArrayQueueADT queue, Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity(queue, queue.size + 1);
        queue.size++;
        queue.arr[last(queue)] = obj;
    }

    // Pre: obj != null && queue != null
    // Post: n' == n + 1 && a'[1] = obj && forall i = 1...n: a'[i + 1] == a[i]
    public static void push(ArrayQueueADT queue, Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity(queue, queue.size + 1);
        if (queue.start == 0) {
            queue.start = queue.arr.length - 1;
        } else {
            queue.start--;
        }
        queue.size++;
        queue.arr[queue.start] = obj;
    }

    // Pre: queue != null && n >= 1
    // Post: R == a[1] && n' == n && immutable(n)
    public static Object element(ArrayQueueADT queue) {
        if (queue.size == 0) {
            throw new IndexOutOfBoundsException();
        }
        return queue.arr[queue.start];
    }

    // Pre: queue != null && n >= 1
    // Post: R == a[n] && n' == n && immutable(n)
    public static Object peek(ArrayQueueADT queue) {
        if (queue.size == 0) {
            throw new IndexOutOfBoundsException();
        }
        return queue.arr[last(queue)];
    }

    // Pre: queue != null && n >= 1
    // Post: R == a[1] && n' == n - 1 && forall i = 1...n': a'[i] == a[i + 1]
    public static Object dequeue(ArrayQueueADT queue) {
        final Object result = element(queue);
        queue.arr[queue.start] = null;
        queue.size--;
        if (queue.start == queue.arr.length - 1) {
            queue.start = 0;
        } else {
            queue.start++;
        }
        return result;
    }

    // Pre: queue != null && n >= 1
    // Post: R == a[n] && n' == n - 1 && immutable(n')
    public static Object remove(ArrayQueueADT queue) {
        if (queue.size == 0) {
            throw new IndexOutOfBoundsException();
        }
        final int last = last(queue);
        final Object result = queue.arr[last];
        queue.arr[last] = null;
        queue.size--;
        return result;
    }

    // Pre: queue != null
    // Post: R == n
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pre: queue != null
    // Post: R == (n == 0)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pre: queue != null
    // Post: n' == 0
    public static void clear(ArrayQueueADT queue) {
        queue.arr = new Object[INITIAL_SIZE];
        queue.start = 0;
        queue.size = 0;
    }

    // Pre: queue != null
    // Post: R == {a[1], ..., a[n]} && n' == n && immutable(n)
    public static Object[] toArray(ArrayQueueADT queue) {
        final Object[] result = new Object[queue.size];
        int index = queue.start;
        for (int i = 0; i < queue.size; i++) {
            result[i] = queue.arr[index];
            index++;
            if (index == queue.arr.length) {
                index = 0;
            }
        }
        return result;
    }

    private static int last(ArrayQueueADT queue) {
        int last = queue.start + queue.size - 1;
        if (last >= queue.arr.length) {
            last -= queue.arr.length;
        }
        return last;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity < queue.arr.length) {
            return;
        }
        int ind = queue.start;
        final int oldSize = queue.size;
        final Object[] old = queue.arr;
        queue.arr = new Object[Math.max(capacity, old.length * 2)];
        queue.start = 0;
        queue.size = 0;
        for (int i = 0; i < oldSize; i++) {
            enqueue(queue, old[ind]);
            ind++;
            if (ind == old.length) {
                ind = 0;
            }
        }
    }
}
