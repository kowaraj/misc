(defn rec-step [[x & xs]]
  (if x
    [x (rec-step xs)]
    []))

(rec-step [1 2 3 4])
(rec-step (range 200000)) ;=> Stack overflow

(defn lz-rec-step [s]
  (println [(first s) (rest s)])
  (lazy-seq
    (if (seq s)
      [(first s) (lz-rec-step (rest s))]
      [])))

(lz-rec-step [1 2 3 4]) ;=> (1 (2 (3 (4 ()))))
(class (lz-rec-step [1 2 3 4]))  ;=> clojure.lang.LazySeq
(dorun (lz-rec-step (range 200000)))  ;=> no overflow!
(print (lz-rec-step (range 200000)))  ;=> still overflow !!

(defn rec-step-acc-reversed [s]
  (letfn [(rec-step-fn [s acc]
            ;(println acc)
            (if (not (seq s))
              acc
              (rec-step-fn (rest s) (seq [(first s) acc]))))]
    (rec-step-fn s '())))
(rec-step-acc-reversed [1 2 3 4]) ;=> (4 (3 (2 (1 ()))))
(defn rec-step-acc [s]
  (rec-step-acc-reversed (reverse s)))
(rec-step-acc [1 2 3 4]) ;=> (1 (2 (3 (4 ()))))
(print (rec-step-acc [1 2 3 4]))
(print (rec-step-acc (range 200000)))


(defn rec-step-acc-reversed-2 [s]
  (letfn [(rec-step-fn [s acc]
            (if (not (seq s))
              acc
              (let [new-acc (conj (list acc) (first s) )
                    new-seq (rest s)]
                (recur new-seq new-acc))))]
    (rec-step-fn s '())))

(rec-step-acc-reversed-2 [1 2 3 4]) ;=> (4 (3 (2 (1 ()))))
(rec-step-acc-reversed-2 [1 2 3 4])
(rec-step-acc-reversed-2 (range 200000))

(cons 3 '(2 1))
(cons 3 (list '(2 1)))
(conj (list '(2 1)) 3)

(defn pow [base exp]
            (if (= 1 exp)
              base
              (* base (pow base (dec exp)))))

(defn pow [base exp]
  (letfn [(kapow [base exp acc]
            (if (zero? exp)
              acc
              (recur base (dec exp) (* base acc))))]
    (kapow base exp 1)))
(pow 2N 200000)
