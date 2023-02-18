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

    // Pre: obj != null
    // Post: n' == n + 1 && immutable(n) && a'[n'] == obj
    public static void enqueue(ArrayQueueADT queue, Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity(queue, queue.size + 1);
        int index = queue.start + queue.size;
        if (index >= queue.arr.length) {
            index -= queue.arr.length;
        }
        queue.arr[index] = obj;
        queue.size++;
    }

    // Pre: n >= 1
    // Post: R == a[1] && n' == n && immutable(n)
    public static Object element(ArrayQueueADT queue) {
        if (queue.size == 0) {
            throw new IndexOutOfBoundsException();
        }
        return queue.arr[queue.start];
    }

    // Pre: n >= 1
    // Post: R == a[1] && n' == n - 1 && forall i = 1...n': a'[i] == a[i + 1]
    public static Object dequeue(ArrayQueueADT queue) {
        Object result = element(queue);
        queue.arr[queue.start] = null;
        queue.size--;
        queue.start++;
        if (queue.start == queue.arr.length) {
            queue.start = 0;
        }
        return result;
    }

    // Pre: true
    // Post: R == n
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pre: true
    // Post: R == (n == 0)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pre: true
    // Post: n' == 0
    public static void clear(ArrayQueueADT queue) {
        queue.arr = new Object[INITIAL_SIZE];
        queue.start = 0;
        queue.size = 0;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity < queue.arr.length) {
            return;
        }
        int ind = queue.start;
        int oldSize = queue.size;
        Object[] old = queue.arr;
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
