package search;

public class BinarySearchUni {
    // Let:
    //  "k is border(a)" <=>
    //  0 <= k <= a.length
    //  && forall i = 1...(k - 1): a[i - 1] < a[i]
    //  && forall i = (k + 1)...(a.length - 1): a[i - 1] > a[i]

    // Inv: exists k: k is border(a)
    // Post: R == min({k: k is border(a)})
    //  && a' == a
    public static int iterativeSearch(int[] a) {
        int left = 0;
        int right = a.length;
        // Inv: 0 <= left <= right <= a.length
        //  && right - left < right'' - left''  ('' - previous value)
        //  && exists k: left <= k <= right && k is border(a) && forall i = 0...(k - 1): !(i is border(a))
        while (left != right) {
            int middle = (left + right) / 2;
            // 0 <= left <= middle < right <= a.length
            //  && exists k: left <= k <= right && k is border(a) && forall i = 0...(k - 1): !(i is border(a))
            if (middle + 1 >= a.length || a[middle] > a[middle + 1]) {
                // 0 <= left <= middle < right <= a.length
                //  && forall i = (middle + 1)...a.length: (!(i is border(a)) || (i - 1) is border(a))
                //  && exists k: left <= k <= middle && k is border(a) && forall i = 0...(k - 1): !(i is border(a))
                right = middle;
                // 0 <= left <= right <= a.length
                //  && right - left < right'' - left''
                //  && exists k: left <= k <= right && k is border(a) && forall i = 0...(k - 1): !(i is border(a))
            } else {
                // 0 <= left <= middle < right <= a.length
                //  && forall i = 0...middle: !(i is border(a))
                //  && exists k: middle + 1 <= k <= right && k is border(a) && forall i = 0...(k - 1): !(i is border(a))
                left = middle + 1;
                // 0 <= left <= right <= a.length
                //  && right - left < right'' - left''
                //  && exists k: left <= k <= right && k is border(a) && forall i = 0...(k - 1): !(i is border(a))
            }
            // 0 <= left <= right <= a.length
            //  && right - left < right'' - left''
            //  && exists k: left <= k <= right && k is border(a) && forall i = 0...(k - 1): !(i is border(a))
        }
        // 0 <= left == right <= a.length
        //  && exists k: left <= k <= right && k is border(a) && forall i = 0...(k - 1): !(i is border(a))
        return left;
        // 0 <= R <= a.length && R is border(a) && forall i = 0...(R - 1): !(i is border(a))
    }

    // Pre: 0 <= left <= right <= a.length
    // Inv: exist k: left <= k <= right && k is border(a)
    // Post: R == min({k: left <= k <= right && k is border(a)})
    //  && a' == a
    public static int recursiveSearch(int[] a, int left, int right) {
        if (right == left) {
            return left;
            // 0 <= left == R == right <= a.length
            //  && R is border(a)
        }
        int middle = (left + right) / 2;
        // 0 <= left <= middle < right <= a.length
        //  && exist k: left <= k <= right && k is border(a)
        if (middle + 1 >= a.length || a[middle] > a[middle + 1]) {
            // 0 <= left <= middle < right <= a.length
            //  && forall i = (middle + 1)...right: (!(i is border(a)) || (i - 1) is border(a))
            //  && exists k: left <= k <= middle && k is border(a)
            return recursiveSearch(a, left, middle);
            // R == min({k: left <= k <= right && k is border(a)})
        } else {
            // 0 <= left <= middle < right <= a.length
            //  forall i = left...middle: !(i is border(a))
            //  && exists k: middle + 1 <= k <= right && k is border(a)
            return recursiveSearch(a, middle + 1, right);
            // R == min({k: left <= k <= right && k is border(a)})
        }
    }

    // Inv: exists k: k is border(a)
    // Post: R == min({k: k is border(a)})
    //  && a' == a
    public static int recursiveSearch(int[] a) {
        return recursiveSearch(a, 0, a.length);
    }

    // Pre: forall arg in args: arg can be parsed to integer
    //  && exists k: k is border({arr[i] = Integer.parseInt(args[i])})
    // Post: R == min({k: k is border({arr[i] = Integer.parseInt(args[i])})})
    //  && args' == args
    public static void main(String[] args) {
        int[] a = new int[args.length];
        int j = 0;
        int sum = 0;
        // Inv: forall arg in args: arg can be parsed to integer
        //  && a.length == args.length
        //  && forall i = 0...(j - 1): a[i] == Integer.parseInt(args[i])
        //  && sum = (a[0] + a[1] + ... + a[j - 1])
        //  && exists k: k is border({arr[i] = Integer.parseInt(args[i])})
        while (j < args.length) {
            a[j] = Integer.parseInt(args[j]);
            sum += a[j];
            j++;
        }
        // a.length == args.length
        //  && a = {a[i] = Integer.parseInt(args[i])}
        //  && sum = (a[0] + a[1] + ... + a[a.length - 1])
        //  && exists k: k is border(a)
        if (sum % 2 == 0) {
            System.out.println(recursiveSearch(a));
        } else {
            System.out.println(iterativeSearch(a));
        }
        // R == min({k: k is border({arr[i] = Integer.parseInt(args[i])})})
    }
}
