package queue;

public interface Queue {
    // Model: a[1], ..., a[n]

    // Let:
    // immutable(k) <=> forall i = 1...k: a'[i] == a[i]

    // Pre: obj != null
    // Post: n' == n + 1 && immutable(n) && a'[n'] == obj
    void enqueue(Object obj);

    // Pre: n >= 1
    // Post: R == a[1] && n' == n && immutable(n)
    Object element();

    // Pre: n >= 1
    // Post: R == a[1] && n' == n - 1 && forall i = 1...n': a'[i] == a[i + 1]
    Object dequeue();

    // Pre: true
    // Post: R == n
    int size();

    // Pre: true
    // Post: R == (n == 0)
    boolean isEmpty();

    // Pre: true
    // Post: n' == 0
    void clear();
}
