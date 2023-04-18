(def constant constantly)
(defn variable [var-name] (fn [vars] (vars var-name)))
(defn call [& args] (fn [f] (apply f args)))
(defn make-operation [f] (fn [& operands] (fn [vars] (apply f (map (call vars) operands)))))

(def add (make-operation +))
(def subtract (make-operation -))
(def multiply (make-operation *))
(defn fixed-div
  ([x] (/ 1.0 x))
  ([x & args] (/ (double x) (apply * args))))
(def divide (make-operation fixed-div))
(def negate subtract)

(defn sumexp-operator [& args] (apply + (map #(Math/exp %) args)))
(def sumexp (make-operation sumexp-operator))
(defn lse-operator [& args] (Math/log (apply sumexp-operator args)))
(def lse (make-operation lse-operator))

(def operation-map {'+      add,
                    '-      subtract,
                    '*      multiply,
                    '/      divide,
                    'negate negate,
                    'sumexp sumexp,
                    'lse    lse})

(defn parse [expr] (cond
                     (list? expr) (apply (operation-map (first expr)) (map parse (rest expr)))
                     (number? expr) (constant expr)
                     :else (variable (name expr))))
(defn parseFunction [string] (parse (read-string string)))
