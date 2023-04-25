(def constant constantly)
(defn variable [var-name] (fn [vars] (vars var-name)))
(defn call [& args] (fn [f] (apply f args)))
(defn make-operation [f] (fn [& operands] (fn [vars] (apply f (map (call vars) operands)))))

(def add (make-operation +))
(defn fixed-sub
  ([] 0)
  ([& args] (apply - args)))
(def subtract (make-operation fixed-sub))
(def multiply (make-operation *))
(defn fixed-div
  ([] 1)
  ([x] (/ 1.0 x))
  ([x & args] (/ (double x) (apply * args))))
(def divide (make-operation fixed-div))
(def negate (make-operation -))

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
  (toString [] (str (__num this)))
  (evaluate [vars] (__num this))
  (diff [var-name] zero))
(def zero (Constant 0))
(def one (Constant 1))

(defclass
  Variable
  _
  [name]
  (toString [] (__name this))
  (evaluate [vars] (vars (__name this)))
  (diff [var-name] (if (= var-name (__name this)) one zero)))

(defmethods getSymbol, getFunction, getDiff)
(defclass
  AbstractOperation
  _
  [operands]
  (toString [] (str "(" (_getSymbol this) " " (clojure.string/join " " (map _toString (__operands this))) ")"))
  (evaluate [vars] (apply (_getFunction this) (map #(_evaluate % vars) (__operands this))))
  (diff [var-name] (let [operands (__operands this)]
                     ((_getDiff this) operands (map #(_diff % var-name) operands)))))

(def object-map {})
(defn make-operation-class [func symb diff]
  (let [OperationPrototype (assoc AbstractOperation_proto
                             :getSymbol (fn [this] symb)
                             :getFunction (fn [this] func)
                             :getDiff (fn [this] diff))
        OperationConstructor (constructor (fn [this & operands] (assoc this :operands operands)) OperationPrototype)]
    (def object-map (assoc object-map (symbol symb) OperationConstructor))
    OperationConstructor))

(declare Add, Subtract, Multiply, Divide, Negate)
(defn add-mul [coll1 coll2]
  (apply Add (map Multiply coll1 coll2)))

(def Add
  (make-operation-class
    + "+"
    (fn [operands diffs]
      (apply Add diffs))))
(def Subtract
  (make-operation-class
    fixed-sub "-"
    (fn [operands diffs]
      (apply Subtract diffs))))
(def Multiply
  (make-operation-class
    * "*"
    (defn multiply-diff [operands diffs]
      (apply Multiply (add-mul diffs (map Divide operands)) operands))))
(def Divide
  (make-operation-class
    fixed-div "/"
    (fn divide-diff [operands diffs]
      (cond (= (count operands) 0) (zero)
            (= (count operands) 1) (divide-diff
                                     [one (first operands)]
                                     [zero (first diffs)])
            (= (count operands) 2) (let [[dividend divisor] operands]
                                     (add-mul
                                       [(Divide divisor) (Negate (Divide dividend (Multiply divisor divisor)))]
                                       diffs))
            :else (divide-diff
                    [(first operands) (apply Multiply (rest operands))]
                    [(first diffs) (multiply-diff (rest operands) (rest diffs))])))))

(def Negate
  (make-operation-class
    - "negate"
    (fn [operands diffs]
      (apply Negate diffs))))

(defn meansq-operator [& nums] (/ (apply + (map #(* % %) nums)) (count nums)))
(defn rms-operator [& nums] (Math/sqrt (apply meansq-operator nums)))
(def Meansq
  (make-operation-class
    meansq-operator "meansq"
    (fn [operands diffs]
      (Multiply
        (Constant (fixed-div 2 (count operands)))
        (add-mul operands diffs)))))
(def RMS
  (make-operation-class
    rms-operator "rms"
    (fn [operands diffs]
      (Divide
        (add-mul operands diffs)
        (apply RMS operands)
        (Constant (count operands))))))

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
