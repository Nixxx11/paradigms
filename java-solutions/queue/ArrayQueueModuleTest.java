package queue;

import java.util.Arrays;

public class ArrayQueueModuleTest {
    private static void enqueueMultiple(int n) {
        for (int i = 0; i < n; i++) {
            ArrayQueueModule.enqueue(String.valueOf(i));
        }
    }

    private static void pushMultiple(int n) {
        for (int i = 0; i < n; i++) {
            ArrayQueueModule.push(String.valueOf(i));
        }
    }

    private static void expectDequeue(String str) {
        assert ArrayQueueModule.element().equals(str);
        assert ArrayQueueModule.dequeue().equals(str);
    }

    private static void expectRemove(String str) {
        assert ArrayQueueModule.peek().equals(str);
        assert ArrayQueueModule.remove().equals(str);
    }

    private static void testClear() {
        enqueueMultiple(10);
        ArrayQueueModule.clear();
        assert ArrayQueueModule.size() == 0 && ArrayQueueModule.isEmpty();
    }

    private static void testToArray() {
        enqueueMultiple(5);
        ArrayQueueModule.dequeue();
        Object[] arr = ArrayQueueModule.toArray();
        assert Arrays.equals(arr, new Object[]{"1", "2", "3", "4"});
        ArrayQueueModule.clear();
    }

    private static void test() {
        assert ArrayQueueModule.size() == 0 && ArrayQueueModule.isEmpty();
        enqueueMultiple(15);
        assert ArrayQueueModule.size() == 15 && !ArrayQueueModule.isEmpty();
        for (int i = 0; i < 15; i++) {
            expectDequeue(String.valueOf(i));
            assert ArrayQueueModule.size() == 14 - i;
        }
        assert ArrayQueueModule.isEmpty();

        pushMultiple(15);
        assert ArrayQueueModule.size() == 15 && !ArrayQueueModule.isEmpty();
        for (int i = 0; i < 15; i++) {
            expectRemove(String.valueOf(i));
            assert ArrayQueueModule.size() == 14 - i;
        }

        pushMultiple(15);
        testClear();
        testToArray();
    }

    public static void main(String[] args) {
        test();
        System.out.println("Finished testing ArrayQueueModule");
    }
}
