package search;

public class BinarySearch {
    // Pre: 0 <= left <= right <= a.length
    // Inv: forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
    // Post: left <= R <= right
    //  && forall i = left...(R - 1): a[i] > x
    //  && (R == right || a[R] <= x)
    //  && forall n in a: n' == n
    public static int iterativeSearch(int[] a, int x, int left, int right) {
        // Inv: 0 <= left <= left' <= right' <= right <= a.length
        //  && (left' == left || a[left' - 1] > x)
        //  && (right' == right || a[right'] <= x)
        //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
        while (left != right) {
            // 0 <= left <= left' < right' <= right <= a.length
            //  && (left' == left || a[left' - 1] > x)
            //  && (right' == right || a[right'] <= x)
            //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
            int middle = (left + right) / 2;
            // 0 <= left <= left' <= middle < right' <= right <= a.length
            //  && (left' == left || a[left' - 1] > x)
            //  && (right' == right || a[right'] <= x)
            //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
            if (a[middle] <= x) {
                // 0 <= left <= left' <= middle < right <= a.length
                //  && (left' == left || a[left' - 1] > x)
                //  && a[middle] <= x
                //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
                right = middle;
                // 0 <= left <= left' <= right' < right <= a.length
                //  && (left' == left || a[left' - 1] > x)
                //  && a[right'] <= x
                //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
            } else {
                // 0 <= left <= middle < right' <= right <= a.length
                //  && a[middle] > x
                //  && (right' == right || a[right'] <= x)
                //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
                left = middle + 1;
                // 0 <= left < left' <= right' <= right <= a.length
                //  && a[left' - 1] > x
                //  && (right' == right || a[right'] <= x)
                //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
            }
            // 0 <= left <= left' < right' <= right <= a.length
            //  && (left' == left || a[left' - 1] > x)
            //  && (right' == right || a[right'] <= x)
            //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
        }
        // 0 <= left <= left' == right' <= right <= a.length
        //  && (left' == left || a[left' - 1] > x)
        //  && (left' == right || a[left'] <= x)
        //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
        return left;
        // 0 <= left <= R <= right <= a.length
        //  && forall i = left...(R - 1): a[i] > x
        //  && (R == right || a[R] <= x)
        //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
    }

    // Inv: forall i = 1...(a.length - 1): a[i - 1] >= a[i]
    // Post: 0 <= R <= a.length
    //  && forall i = 0...(R - 1): a[i] > x
    //  && (R == a.length || a[R] <= x)
    //  && forall n in a: n' == n
    public static int iterativeSearch(int[] a, int x) {
        // forall i = 1...(a.length - 1): a[i - 1] >= a[i]
        return iterativeSearch(a, x, 0, a.length);
        // 0 <= R <= a.length
        //  && forall i = 0...(R - 1): a[i] > x
        //  && (R == a.length || a[R] <= x)
        //  && forall i = 1...(a.length - 1): a[i - 1] >= a[i]
        //  && forall n in a: n' == n
    }

    // Pre: 0 <= left <= right <= a.length
    // Inv: forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
    // Post: left <= R <= right
    //  && forall i = left...(R - 1): a[i] > x
    //  && (R == right || a[R] <= x)
    //  && forall n in a: n' == n
    public static int recursiveSearch(int[] a, int x, int left, int right) {
        // 0 <= left <= right <= a.length
        //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
        if (right == left) {
            // 0 <= left == right <= a.length
            //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
            return left;
            // R == left    // forall i = left...(R - 1): a[i] > x
            //  && R == right
            //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
        }
        // 0 <= left <= right <= a.length
        //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
        int middle = (left + right) / 2;
        // 0 <= left <= middle < right <= a.length
        //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
        if (a[middle] <= x) {
            // 0 <= left <= middle < right <= a.length
            //  && a[middle] <= x
            //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
            return recursiveSearch(a, x, left, middle);
            // left <= R <= middle < right
            //  && forall i = left...(R - 1): a[i] > x
            //  && (R == middle || a[R] <= x)   // a[R] <= x
            //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
        } else {
            // 0 <= left <= middle < right <= a.length
            //  && a[middle] > x    // forall i = left...middle: a[i] > x
            //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
            return recursiveSearch(a, x, middle + 1, right);
            // left < middle + 1 <= R <= right
            //  && forall i = left...middle: a[i] > x
            //  && forall i = (middle + 1)...(R - 1): a[i] > x
            //  && (R == right || a[R] <= x)
            //  && forall i = (left + 1)...(right - 1): a[i - 1] >= a[i]
        }
    }

    // Inv: forall i = 1...(a.length - 1): a[i - 1] >= a[i]
    // Post: 0 <= R <= a.length
    //  && forall i = 0...(R - 1): a[i] > x
    //  && (R == a.length || a[R] <= x)
    //  && forall n in a: n' == n
    public static int recursiveSearch(int[] a, int x) {
        // forall i = 1...(a.length - 1): a[i - 1] >= a[i]
        return recursiveSearch(a, x, 0, a.length);
        // 0 <= R <= a.length
        //  && forall i = 0...(R - 1): a[i] > x
        //  && (R == a.length || a[R] <= x)
        //  && forall i = 1...(a.length - 1): a[i - 1] >= a[i]
        //  && forall n in a: n' == n
    }

    // Pre: args.length >= 1 && forall arg in args: arg can be parsed to integer
    //  && forall i = 2...(args.length - 1): Integer.parseInt(args[i - 1]) >= Integer.parseInt(args[i])
    // Post: 0 <= R <= args.length - 1
    //  && forall i = 0...(R - 1): Integer.parseInt(args[i + 1]) > Integer.parseInt(args[0])
    //  && (R == args.length - 1 || Integer.parseInt(args[R + 1]) <= Integer.parseInt(args[0]))
    public static void main(String[] args) {
        // args.length >= 1 && forall arg in args: arg can be parsed to integer
        //  && forall i = 2...(args.length - 1): Integer.parseInt(args[i - 1]) >= Integer.parseInt(args[i])
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        int i = 1;
        // forall arg in args: arg can be parsed to integer
        // Inv: x == Integer.parseInt(args[0])
        //  && a.length == args.length - 1
        //  && forall j = 0...(i - 2): a[j] == Integer.parseInt(args[j + 1])
        //  && forall j = 2...(args.length - 1): Integer.parseInt(args[j - 1]) >= Integer.parseInt(args[j])
        while (i < args.length) {
            a[i - 1] = Integer.parseInt(args[i]);
            i++;
        }
        // x == Integer.parseInt(args[0])
        //  && a.length == args.length - 1
        //  && forall j = 0...(args.length - 2): a[j] == Integer.parseInt(args[j + 1])
        //  && forall j = 2...(args.length - 1): Integer.parseInt(args[j - 1]) >= Integer.parseInt(args[j])

        // x == Integer.parseInt(args[0])
        //  && a.length == args.length - 1
        //  && forall j = 0...(args.length - 2): a[j] == Integer.parseInt(args[j + 1])
        //  && forall j = 1...(a.length - 1): a[j - 1] >= a[j]
        System.out.println(iterativeSearch(a, x));
        // 0 <= R <= a.length
        //  && forall i = 0...(R - 1): a[i] > x
        //  && (R == a.length || a[R] <= x)
        //  && forall j = 0...(args.length - 2): a[j] == Integer.parseInt(args[j + 1])

        // 0 <= R <= args.length - 1
        //  && forall i = 0...(R - 1): Integer.parseInt(args[i + 1]) > Integer.parseInt(args[0])
        //  && (R == args.length - 1 || Integer.parseInt(args[R + 1]) <= Integer.parseInt(args[0]))
    }
}