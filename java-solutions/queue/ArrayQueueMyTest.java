package queue;

public class ArrayQueueMyTest {
    private static void enqueueMultiple(ArrayQueue queue, int n) {
        for (int i = 0; i < n; i++) {
            queue.enqueue(String.valueOf(i));
        }
    }

    private static void expect(ArrayQueue queue, String str) {
        assert queue.element().equals(str);
        assert queue.dequeue().equals(str);
    }

    private static void testClear(ArrayQueue queue) {
        enqueueMultiple(queue, 10);
        queue.clear();
        assert queue.size() == 0 && queue.isEmpty();
    }

    private static void test(ArrayQueue queue) {
        assert queue.size() == 0 && queue.isEmpty();
        enqueueMultiple(queue, 15);
        assert queue.size() == 15 && !queue.isEmpty();
        for (int i = 14; i >= 0; i--) {
            expect(queue, String.valueOf(i));
            assert queue.size() == i;
        }
        assert queue.isEmpty();
        testClear(queue);
    }

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        test(queue);
        System.out.println("Finished testing ArrayQueue");
    }
}
