(defn apply-vectors-to-vector [f v vs] (apply mapv f v vs))
(defn apply-values-to-vector [f v args] (apply mapv f v (mapv repeat args)))

(def is-scalar? number?)
(defn is-vector? [v] (and (vector? v) (every? is-scalar? v)))
(defn size-equals? [num v]
  {:pre [(vector? v)]}
  (= (count v) num))
(defn equal-sized? [& vs]
  {:pre [(every? vector? vs)]}
  (every? (partial size-equals? (count (first vs))) vs))
(defn equal-sized-vectors? [& vs] (and (every? is-vector? vs) (apply equal-sized? vs)))

(defn v+ [v & vs]
  {:pre [(apply equal-sized-vectors? v vs)]
   :post [(equal-sized-vectors? v %)]}
  (apply-vectors-to-vector + v vs))
(defn v- [v & vs]
  {:pre [(apply equal-sized-vectors? v vs)]
   :post [(equal-sized-vectors? v %)]}
  (apply-vectors-to-vector - v vs))
(defn v* [v & vs]
  {:pre [(apply equal-sized-vectors? v vs)]
   :post [(equal-sized-vectors? v %)]}
  (apply-vectors-to-vector * v vs))
(defn vd [v & vs]
  {:pre [(apply equal-sized-vectors? v vs)]
   :post [(equal-sized-vectors? v %)]}
  (apply-vectors-to-vector / v vs))
(defn v*s [v & ss]
  {:pre [(is-vector? v) (every? is-scalar? ss)]
   :post [(equal-sized-vectors? v %)]}
  (apply-values-to-vector * v ss))

(defn scalar [v & vs]
  {:pre [(apply equal-sized-vectors? v vs)]
   :post [(is-scalar? %)]}
  (apply + (apply v* v vs)))

(defn vect [v & vs]
  {:pre [(apply equal-sized-vectors? v vs) (size-equals? 3 v)]
   :post [(is-vector? %) (size-equals? 3 %)]}
  (reduce
    #(mapv
       (fn [[i1 i2]] (- (* (%1 i1) (%2 i2)) (* (%1 i2) (%2 i1))))
       [[1 2] [2 0] [0 1]])
    v vs))
;;(v1[1]*v2[2] - v1[2]*v2[1], v1[2]*v2[0] - v1[0]*v2[2], v1[0]*v2[1] - v1[1]*v2[0])

(defn is-matrix? [m] (and (vector? m) (apply equal-sized-vectors? m)))
(defn equal-sized-matrices? [& ms] (and
                                     (every? is-matrix? ms)
                                     (apply equal-sized? ms)
                                     (apply equal-sized? (mapv first ms))))

(defn m+ [m & ms]
  {:pre [(apply equal-sized-matrices? m ms)]
   :post [(equal-sized-matrices? m %)]}
  (apply-vectors-to-vector v+ m ms))
(defn m- [m & ms]
  {:pre [(apply equal-sized-matrices? m ms)]
   :post [(equal-sized-matrices? m %)]}
  (apply-vectors-to-vector v- m ms))
(defn m* [m & ms]
  {:pre [(apply equal-sized-matrices? m ms)]
   :post [(equal-sized-matrices? m %)]}
  (apply-vectors-to-vector v* m ms))
(defn md [m & ms]
  {:pre [(apply equal-sized-matrices? m ms)]
   :post [(equal-sized-matrices? m %)]}
  (apply-vectors-to-vector vd m ms))

(defn transpose [m]
  {:pre [(is-matrix? m)]
   :post [(is-matrix? m) (equal-sized? (first m) %)]}
  (apply mapv vector m))

(defn m*s [m & ss]
  {:pre [(is-matrix? m) (every? is-scalar? ss)]
   :post [(equal-sized-matrices? m %)]}
  (apply-values-to-vector v*s m ss))

(defn m*v [m v]
  {:pre [(is-matrix? m) (is-vector? v) (equal-sized? (first m) v)]
   :post [(is-vector? %) (equal-sized? m %)]}
  (mapv (partial scalar v) m))

(defn m*m [m & ms]
  {:pre [(is-matrix? m) (every? is-matrix? ms)
         (apply #(every?
                   (fn [index] (equal-sized? (first (nth %& index)) (nth %& (inc index))))
                   (range (dec (count %&)))) m ms)]
   :post [(is-matrix? %) (equal-sized? m %) (equal-sized? (first (last (apply vector m ms))) (first %))]}
  (reduce #(mapv (partial m*v (transpose %2)) %1) m ms))
