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
(defclass
  Variable
  _
  [name]
  [toString [] (__name this)]
  [evaluate [vars] (vars (__name this))])
(defclass
  Constant
  _
  [num]
  [toString [] (str (__num this))]
  [evaluate [vars] (__num this)])

(defmethods getSymbol, calculate)
(defclass
  AbstractOperation
  _
  [operands]
  [toString [] (str "(" (_getSymbol this) " " (clojure.string/join " " (map _toString (__operands this))) ")")]
  [evaluate [vars] (_calculate this (map #(_evaluate % vars) (__operands this)))])

(def object-map {})
(defn make-operation-class [func symb]
  (let [OperationPrototype (assoc AbstractOperation_proto :getSymbol (fn [this] symb)
                                                          :calculate (fn [this nums] (apply func nums)))
        OperationConstructor (constructor (fn [this & operands] (assoc this :operands operands)) OperationPrototype)]
    (def object-map (assoc object-map (symbol symb) OperationConstructor))
    OperationConstructor))

(def Add (make-operation-class + "+"))
(def Subtract (make-operation-class - "-"))
(def Multiply (make-operation-class * "*"))
(def Divide (make-operation-class fixed-div "/"))
(def Negate (make-operation-class - "negate"))

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
