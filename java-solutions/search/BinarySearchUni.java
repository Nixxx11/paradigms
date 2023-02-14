package search;

public class BinarySearchUni {
    // Inv: exists k:
    //  0 <= k <= a.length && forall i = 0...(k - 1): a[i] < a[i + 1] && forall i = (k + 1)...(a.length - 2): a[i] > a[i + 1]
    // Post: R = min({k: forall i = 0...(k - 1): a[i] < a[i + 1] && forall i = (k + 1)...(a.length - 2): a[i] > a[i + 1]})
    //  && a' == a
    public static int iterativeSearch(int[] a) {
        int left = 0;
        int right = a.length;
        // Inv: exists k:
        //  left <= k <= right && a[k] > a[k - 1] && (k + 1 == a.length || a[k] >= a[k + 1])
        while (left != right) {
            int middle = (left + right) / 2;
            if (middle + 1 == a.length || a[middle] > a[middle + 1]) {
                right = middle;
            } else {
                left = middle + 1;
            }
        }
        return left;
    }

    // Inv: exist k:
    //  left <= k <= right && forall i = 0...(k - 1): a[i] < a[i + 1] && forall i = (k + 1)...(a.length - 2): a[i] > a[i + 1]
    // Post: R = min({k: forall i = 0...(k - 1): a[i] < a[i + 1] && forall i = (k + 1)...(a.length - 2): a[i] > a[i + 1]})
    //  && a' == a
    public static int recursiveSearch(int[] a, int left, int right) {
        if (right == left) {
            return left;
            // a[R] > a[R - 1] && (R + 1 == a.length || a[R] >= a[R + 1])
        }
        int middle = (left + right) / 2;
        if (middle + 1 == a.length || a[middle] > a[middle + 1]) {
            return recursiveSearch(a, left, middle);
        } else {
            return recursiveSearch(a, middle + 1, right);
        }
    }

    // Inv: exists k:
    //  0 <= k <= a.length && forall i = 0...(k - 1): a[i] < a[i + 1] && forall i = (k + 1)...(a.length - 2): a[i] > a[i + 1]
    // Post: R = min({k: forall i = 0...(k - 1): a[i] < a[i + 1] && forall i = (k + 1)...(a.length - 2): a[i] > a[i + 1]})
    //  && a' == a
    public static int recursiveSearch(int[] a) {
        return recursiveSearch(a, 0, a.length);
    }

    // Pre: forall arg in args: arg can be parsed to integer
    //  && exists k:
    //  0 <= k <= args.length
    //  && forall i = 0...(k - 1): Integer.parseInt(args[i]) < Integer.parseInt(args[i + 1])
    //  && forall i = (k + 1)...(a.length - 2): Integer.parseInt(args[i]) > Integer.parseInt(args[i + 1])
    public static void main(String[] args) {
        int[] a = new int[args.length];
        int j = 0;
        int sum = 0;
        while (j < args.length) {
            a[j] = Integer.parseInt(args[j]);
            sum += a[j];
            j++;
        }
        if (sum % 2 == 0) {
            System.out.println(recursiveSearch(a));
        } else {
            System.out.println(iterativeSearch(a));
        }
    }
}
