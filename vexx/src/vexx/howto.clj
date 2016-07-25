"doc"

(clojure.repl/doc juxt)
((juxt + - *) 5 3)


"source"

(clojure.repl/source filter)


"->> ->"

(use 'clojure.walk)
(macroexpand-all '(->> c (+ 3) (/ 2) (- 1))) ; (- 1 (/ 2 (+ 3 c)))
(macroexpand-all '(->  c (+ 3) (/ 2) (- 1))) ; (- (/ (+ c 3) 2) 1)


"use of JList"

(def data (into-array String ["one" "two" "three" "four"]))
data
(import 'javax.swing.JList)
(def myList (JList. data))
(->> myList 
    .getModel 
    ((juxt identity (memfn getSize))) 
    ((fn [[a b]] (map #(.getElementAt a %) (range b)))) 
    (apply vector)
    (#(conj % "five")))



"change seesaw widget in real-time"

(defn debug-add-behavior [root]
  (let [p (ss/select root [:#panel])
        text-in (ss/select p [:#text-in])
        text-log (ss/select p [:#text-log])
        text-in-listener (vc/make-fn-text-in-key-typed text-log text-in)
        ]))

(defn debug-add-behavior [root]
  (let [p (ss/select root [:#panel])
        x (ss/select p [:#xlist])]
    (ss/config! x :visible? true)))


"handle DEL key pressed"

(use 'seesaw.core)

(def xl2 (listbox :id :xl :model '(1 2 3)))
(def xt (text :id :xt :text "hell"))
(def xs (left-right-split (scrollable xl2) (scrollable xt)))                          
(def f (frame :size [200 :by 200]
              :content xs))
(pack! f)
(show! f)

(def unlisten
  (let [remove-previous-listeners (unlisten)]
    (listen xl2
            :selection
            (fn [e] (text! xt (str "selected: " (selection e))))
            :key-typed
            (fn [e] (text! xt (str "typed: " (.getKeyChar e))))
            :key-released ; NB: .getKeyCode only works with :key-released
            (fn [e] (text! xt (str "released: " (.getKeyCode e))))
            )))



" show options, events "

(seesaw.dev/show-events (seesaw/button))



" call only if defined "

(if (resolve 'my-fun)           ;;call if defined
         (eval '(my-fun))
         :false)

(defn my-fun [] (println "hi")) ;;define
(ns-unmap *ns* 'my-fun)         ;;un-define


