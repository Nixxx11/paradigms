package queue;

import java.util.Arrays;

public class ArrayQueueADTTest {
    private static void enqueueMultiple(ArrayQueueADT queue, int n) {
        for (int i = 0; i < n; i++) {
            ArrayQueueADT.enqueue(queue, String.valueOf(i));
        }
    }

    private static void pushMultiple(ArrayQueueADT queue, int n) {
        for (int i = 0; i < n; i++) {
            ArrayQueueADT.push(queue, String.valueOf(i));
        }
    }

    private static void expectDequeue(ArrayQueueADT queue, String str) {
        assert ArrayQueueADT.element(queue).equals(str);
        assert ArrayQueueADT.dequeue(queue).equals(str);
    }

    private static void expectRemove(ArrayQueueADT queue, String str) {
        assert ArrayQueueADT.peek(queue).equals(str);
        assert ArrayQueueADT.remove(queue).equals(str);
    }

    private static void testClear(ArrayQueueADT queue) {
        enqueueMultiple(queue, 10);
        ArrayQueueADT.clear(queue);
        assert ArrayQueueADT.size(queue) == 0 && ArrayQueueADT.isEmpty(queue);
    }

    private static void testToArray(ArrayQueueADT queue) {
        enqueueMultiple(queue, 5);
        ArrayQueueADT.dequeue(queue);
        Object[] arr = ArrayQueueADT.toArray(queue);
        assert Arrays.equals(arr, new Object[]{"1", "2", "3", "4"});
        ArrayQueueADT.clear(queue);
    }

    private static void test(ArrayQueueADT queue) {
        assert ArrayQueueADT.size(queue) == 0 && ArrayQueueADT.isEmpty(queue);
        enqueueMultiple(queue, 15);
        assert ArrayQueueADT.size(queue) == 15 && !ArrayQueueADT.isEmpty(queue);
        for (int i = 0; i < 15; i++) {
            expectDequeue(queue, String.valueOf(i));
            assert ArrayQueueADT.size(queue) == 14 - i;
        }
        assert ArrayQueueADT.isEmpty(queue);

        pushMultiple(queue, 15);
        assert ArrayQueueADT.size(queue) == 15 && !ArrayQueueADT.isEmpty(queue);
        for (int i = 0; i < 15; i++) {
            expectRemove(queue, String.valueOf(i));
            assert ArrayQueueADT.size(queue) == 14 - i;
        }

        pushMultiple(queue, 15);
        testClear(queue);
        testToArray(queue);
    }

    public static void main(String[] args) {
        ArrayQueueADT queue = ArrayQueueADT.create();
        test(queue);
        System.out.println("Finished testing ArrayQueueADT");
    }
}
