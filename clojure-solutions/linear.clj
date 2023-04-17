(def is-scalar? number?)
(defn is-vector? [v] (and (vector? v) (every? is-scalar? v)))
(defn equal-sized? [& vs] (apply == (map count vs)))
(defn equal-sized-vectors? [& vs] (and (every? is-vector? vs) (apply equal-sized? vs)))

(defn make-vector-function [scalar-function] (fn [v & vs]
                                               {:pre  [(apply equal-sized-vectors? v vs)]
                                                :post [(equal-sized-vectors? v %)]}
                                               (apply mapv scalar-function v vs)))

(def v+ (make-vector-function +))
(def v- (make-vector-function -))
(def v* (make-vector-function *))
(def vd (make-vector-function /))

(defn v*s [v & ss]
  {:pre  [(is-vector? v) (every? is-scalar? ss)]
   :post [(equal-sized-vectors? v %)]}
  (apply mapv * v (map repeat ss)))

(defn scalar [v & vs]
  {:pre  [(apply equal-sized-vectors? v vs)]
   :post [(is-scalar? %)]}
  (apply + (apply v* v vs)))

(defn vect [v & vs]
  {:pre  [(apply equal-sized-vectors? v vs) (= (count v) 3)]
   :post [(is-vector? %) (= (count %) 3)]}
  (reduce
    #(mapv
       (fn [[i1 i2]] (- (* (%1 i1) (%2 i2)) (* (%1 i2) (%2 i1))))
       [[1 2] [2 0] [0 1]])
    v vs))

(defn is-matrix? [m] (and (vector? m) (apply equal-sized-vectors? m)))
(defn equal-sized-matrices? [& ms] (and
                                     (every? is-matrix? ms)
                                     (apply equal-sized? ms)
                                     (apply equal-sized? (map first ms))))
(defn m-width==v-height? [m v] (equal-sized? (first m) v))

(defn make-matrix-function [vector-function] (fn [m & ms]
                                               {:pre  [(apply equal-sized-matrices? m ms)]
                                                :post [(equal-sized-matrices? m %)]}
                                               (apply mapv vector-function m ms)))

(def m+ (make-matrix-function v+))
(def m- (make-matrix-function v-))
(def m* (make-matrix-function v*))
(def md (make-matrix-function vd))

(defn transpose [m]
  {:pre  [(is-matrix? m)]
   :post [(is-matrix? m) (m-width==v-height? m %)]}
  (apply mapv vector m))

(defn m*s [m & ss]
  {:pre  [(is-matrix? m) (every? is-scalar? ss)]
   :post [(equal-sized-matrices? m %)]}
  (apply mapv v*s m (map repeat ss)))

(defn m*v [m v]
  {:pre  [(is-matrix? m) (is-vector? v) (m-width==v-height? m v)]
   :post [(is-vector? %) (equal-sized? m %)]}
  (mapv (partial scalar v) m))

(defn m*m [m & ms]
  {:pre  [(is-matrix? m) (every? is-matrix? ms)
          ((fn [previous remaining] (cond
                                      (empty? remaining) true
                                      (not (m-width==v-height? previous (first remaining))) false
                                      :else (recur (first remaining) (rest remaining))))
           m ms)]
   :post [(is-matrix? %) (equal-sized? m %) (equal-sized? (first (last (cons m ms))) (first %))]}
  (reduce #(mapv (partial m*v (transpose %2)) %1) m ms))

(defn equal-sized-tensors? [& ts] (or
                                    (every? is-scalar? ts)
                                    (and
                                      (every? vector? ts)
                                      (apply equal-sized? ts)
                                      (apply equal-sized-tensors? (apply concat ts)))))
(defn make-tensor-function [scalar-function]
  (fn f [t & ts]
    {:pre  [(apply equal-sized-tensors? t ts)]
     :post [(equal-sized-tensors? t %)]}
    (if (is-scalar? t)
      (apply scalar-function t ts)
      (apply mapv f t ts))))

(def t+ (make-tensor-function +))
(def t- (make-tensor-function -))
(def t* (make-tensor-function *))
(def td (make-tensor-function /))
