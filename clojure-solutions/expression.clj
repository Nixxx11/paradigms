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

(def function-map {'+      add,
                   '-      subtract,
                   '*      multiply,
                   '/      divide,
                   'negate negate,
                   'sumexp sumexp,
                   'lse    lse})

(load-file "proto.clj")

(declare zero)
(defclass
  Constant
  _
  [num]
  [toString [] (str (__num this))]
  [evaluate [vars] (__num this)]
  [diff [var-name] zero])
(def zero (Constant 0))
(def one (Constant 1))
(defclass
  Variable
  _
  [name]
  [toString [] (__name this)]
  [evaluate [vars] (vars (__name this))]
  [diff [var-name] (if (= var-name (__name this)) one zero)])

(defmethods getSymbol, calculate)
(defclass
  AbstractOperation
  _
  [operands]
  [toString [] (str "(" (_getSymbol this) " " (clojure.string/join " " (map _toString (__operands this))) ")")]
  [evaluate [vars] (_calculate this (map #(_evaluate % vars) (__operands this)))])

(def object-map {})
(defn make-operation-class [func symb diff]
  (let [OperationPrototype (assoc AbstractOperation_proto
                             :getSymbol (fn [this] symb)
                             :calculate (fn [this nums] (apply func nums))
                             :diff (fn [this var-name] (apply diff var-name (__operands this))))
        OperationConstructor (constructor (fn [this & operands] (assoc this :operands operands)) OperationPrototype)]
    (def object-map (assoc object-map (symbol symb) OperationConstructor))
    OperationConstructor))

(declare Add, Subtract, Multiply, Divide, Negate)
(defn diff-from-coefficients [var-name operands coefficients]
  (apply Add
         (map
           (fn [operand coefficient] (Multiply coefficient (_diff operand var-name)))
           operands coefficients)))

(def Add
  (make-operation-class
    + "+"
    (fn [var-name & operands]
      (apply Add (map #(_diff % var-name) operands)))))
(def Subtract
  (make-operation-class
    - "-"
    (fn [var-name & operands]
      (apply Subtract (map #(_diff % var-name) operands)))))
(def Multiply
  (make-operation-class
    * "*"
    (fn [var-name & operands]
      (apply Multiply (diff-from-coefficients var-name operands (map Divide operands)) operands))))
(def Divide
  (make-operation-class
    fixed-div "/"
    (fn divide-diff
      ([var-name divisor] (divide-diff var-name one divisor))
      ([var-name dividend divisor] (diff-from-coefficients
                                     var-name
                                     [dividend divisor]
                                     [(Divide one divisor) (Negate (Divide dividend (Multiply divisor divisor)))]))
      ([var-name dividend divisor & divisors] (divide-diff var-name dividend (apply Multiply divisor divisors))))))
(def Negate
  (make-operation-class
    - "negate"
    (fn [var-name & operands]
      (apply Negate (map #(_diff % var-name) operands)))))

(defn make-parser [operations-map const-func var-func]
  (fn parse [expr] (cond
                     (list? expr) (apply (operations-map (first expr)) (map parse (rest expr)))
                     (number? expr) (const-func expr)
                     :else (var-func (name expr)))))
(def function-parser (make-parser function-map constant variable))
(def object-parser (make-parser object-map Constant Variable))
(defn parseFunction [string] (function-parser (read-string string)))
(defn parseObject [string] (object-parser (read-string string)))

(def toString _toString)
(def evaluate _evaluate)
(def diff _diff)
