package queue;

public class ArrayQueueModuleTest {
    private static void enqueueMultiple(int n) {
        for (int i = 0; i < n; i++) {
            ArrayQueueModule.enqueue(String.valueOf(i));
        }
    }

    private static void expect(String str) {
        assert ArrayQueueModule.element().equals(str);
        assert ArrayQueueModule.dequeue().equals(str);
    }

    private static void testClear() {
        enqueueMultiple(10);
        ArrayQueueModule.clear();
        assert ArrayQueueModule.size() == 0 && ArrayQueueModule.isEmpty();
    }

    private static void test() {
        assert ArrayQueueModule.size() == 0 && ArrayQueueModule.isEmpty();
        enqueueMultiple(15);
        assert ArrayQueueModule.size() == 15 && !ArrayQueueModule.isEmpty();
        for (int i = 14; i >= 0; i--) {
            expect(String.valueOf(i));
            assert ArrayQueueModule.size() == i;
        }
        assert ArrayQueueModule.isEmpty();
        testClear();
    }

    public static void main(String[] args) {
        test();
        System.out.println("Finished testing ArrayQueueModule");
    }
}
