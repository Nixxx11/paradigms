(defn apply-vectors-to-vector [f v vs] (apply mapv f v vs))
(defn apply-values-to-vector [f v args] (apply mapv f v (mapv repeat args)))

(defn is-vector? [v] (and (vector? v) (every? number? v)))
(defn size-equals? [num v] (= (count v) num))
(defn equal-sized? [& colls] (every? (partial size-equals? (count (first colls))) colls))
(defn equal-sized-vectors? [& vs] (and
                                    (every? is-vector? vs)
                                    (apply equal-sized? vs)))

(defn v+ [v & vs]
  {:pre [(apply equal-sized-vectors? v vs)]}
  (apply-vectors-to-vector + v vs))
(defn v- [v & vs]
  {:pre [(apply equal-sized-vectors? v vs)]}
  (apply-vectors-to-vector - v vs))
(defn v* [v & vs]
  {:pre [(apply equal-sized-vectors? v vs)]}
  (apply-vectors-to-vector * v vs))
(defn vd [v & vs]
  {:pre [(apply equal-sized-vectors? v vs)]}
  (apply-vectors-to-vector / v vs))
(defn v*s [v & ss]
  {:pre [(is-vector? v) (every? number? ss)]}
  (apply-values-to-vector * v ss))

(defn scalar [v & vs]
  {:pre [(apply equal-sized-vectors? v vs)]}
  (reduce + (apply v* v vs)))

(defn element-mul-diff [v1 v2 i1 i2] (- (* (v1 i1) (v2 i2)) (* (v1 i2) (v2 i1))))
(defn vect [v & vs]
  {:pre [(apply equal-sized-vectors? v vs) (size-equals? 3 v)]}
  (reduce
    #(vector
       (element-mul-diff %1 %2 1 2)
       (element-mul-diff %1 %2 2 0)
       (element-mul-diff %1 %2 0 1))
    v vs))
;;(v1[1]*v2[2] - v1[2]*v2[1], v1[2]*v2[0] - v1[0]*v2[2], v1[0]*v2[1] - v1[1]*v2[0])

(defn is-matrix? [m] (and (vector? m) (apply equal-sized-vectors? m)))
(defn equal-sized-matrices? [& ms] (and
                                     (every? is-matrix? ms)
                                     (apply equal-sized? ms)
                                     (apply equal-sized? (mapv first ms))))

(defn m+ [m & ms]
  {:pre [(apply equal-sized-matrices? m ms)]}
  (apply-vectors-to-vector v+ m ms))
(defn m- [m & ms]
  {:pre [(apply equal-sized-matrices? m ms)]}
  (apply-vectors-to-vector v- m ms))
(defn m* [m & ms]
  {:pre [(apply equal-sized-matrices? m ms)]}
  (apply-vectors-to-vector v* m ms))
(defn md [m & ms]
  {:pre [(apply equal-sized-matrices? m ms)]}
  (apply-vectors-to-vector vd m ms))

(defn transpose [m]
  {:pre [(is-matrix? m)]}
  (apply mapv vector m))

(defn m*s [m & ss] (apply-values-to-vector v*s m ss))

(defn m*v [m & vs] (apply-values-to-vector scalar m vs))

(defn m*m [m & ms] (reduce #(mapv (partial m*v (transpose %2)) %1) m ms))
