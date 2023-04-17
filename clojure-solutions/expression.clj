(def constant constantly)
(defn variable [var-name] (fn [vars] (vars var-name)))
(defn call [& args] (fn [f] (apply f args)))
(defn make-operation [f] (fn [& operands] (fn [vars] (apply f (map (call vars) operands)))))

(def add (make-operation +))
(def subtract (make-operation -))
(def multiply (make-operation *))
(def divide (make-operation #(cond
                               (and (empty? %&) (zero? %)) ##Inf
                               (not-any? zero? %&) (apply / % %&)
                               :else ##Inf)))
(def negate subtract)

(def operation-map {'+      add,
                    '-      subtract,
                    '*      multiply,
                    '/      divide,
                    'negate negate})

(defn parse [expr] (cond
                     (list? expr) (apply (operation-map (first expr)) (map parse (rest expr)))
                     (number? expr) (constant expr)
                     :else (variable (name expr))))
(defn parseFunction [string] (parse (read-string string)))
