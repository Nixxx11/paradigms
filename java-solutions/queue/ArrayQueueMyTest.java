package queue;

import java.util.Arrays;

public class ArrayQueueMyTest {
    private static void enqueueMultiple(ArrayQueue queue, int n) {
        for (int i = 0; i < n; i++) {
            queue.enqueue(String.valueOf(i));
        }
    }

    private static void pushMultiple(ArrayQueue queue, int n) {
        for (int i = 0; i < n; i++) {
            queue.push(String.valueOf(i));
        }
    }

    private static void expectDequeue(ArrayQueue queue, String str) {
        assert queue.element().equals(str);
        assert queue.dequeue().equals(str);
    }

    private static void expectRemove(ArrayQueue queue, String str) {
        assert queue.peek().equals(str);
        assert queue.remove().equals(str);
    }

    private static void testClear(ArrayQueue queue) {
        enqueueMultiple(queue, 10);
        queue.clear();
        assert queue.size() == 0 && queue.isEmpty();
    }

    private static void testToArray(ArrayQueue queue) {
        enqueueMultiple(queue, 5);
        queue.dequeue();
        Object[] arr = queue.toArray();
        assert Arrays.equals(arr, new Object[]{"1", "2", "3", "4"});
        queue.clear();
    }

    private static void test(ArrayQueue queue) {
        assert queue.size() == 0 && queue.isEmpty();
        enqueueMultiple(queue, 15);
        assert queue.size() == 15 && !queue.isEmpty();
        for (int i = 0; i < 15; i++) {
            expectDequeue(queue, String.valueOf(i));
            assert queue.size() == 14 - i;
        }
        assert queue.isEmpty();

        pushMultiple(queue, 15);
        assert queue.size() == 15 && !queue.isEmpty();
        for (int i = 0; i < 15; i++) {
            expectRemove(queue, String.valueOf(i));
            assert queue.size() == 14 - i;
        }

        pushMultiple(queue, 15);
        testClear(queue);
        testToArray(queue);
    }

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        test(queue);
        System.out.println("Finished testing ArrayQueue");
    }
}
