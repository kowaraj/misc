;; (ns vexx.howto
;;   (:require [seesaw.core :as ss]
;;             [seesaw.dev  :as ssd]))

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




" add a tab "

(defn add-a-tab
  [root]
  (let [t (ss/select root [:#tpane])]
    (println "tabs = " (ss/value t))
    (ss/config! t :tabs [
          ;; {:title "tab5" :content "content of tab1"}
          ;; {:title "tab6" :content "content of tab2"}
          ])
    (ss/config! t :visible? true)
    
    ))
(add-a-tab x)


" bug or feature? "


(defn make-2 [& {:keys [name type content]
                 :or {type :text content "default text"}
                 }
              ]
    (prn "name = " name)
    (prn "name-type = " (type name))
)
;(make-2 :name "new item name!" :content "new item content")



" pop-up question"

(let [p (ss/border-panel :size [100 :by 100])
      f (ss/frame :title "Pop up ask" :size [200 :by 300] :content p)]
  (ss/pack! f)
  (ss/show! f)
  (javax.swing.JOptionPane/showInputDialog p "Enter printer name:"))
(ssd/show-options (ss/border-panel))

(javax.swing.JOptionPane/showInputDialog
                    (ss/border-panel :size [100 :by 100])
                    "New tab name:"
                    "Input"
                    javax.swing.JOptionPane/QUESTION_MESSAGE
                    nil
                    (to-array [1 2 3 4])
                    "Titan"
                    )

(let [t (ss/text :text "t1")
      ]
  (javax.swing.JOptionPane/showInputDialog
                    (ss/border-panel :size [100 :by 100])
                    t
                    "Input"
                    javax.swing.JOptionPane/QUESTION_MESSAGE
                    nil
                    (to-array [1 2 3 4])
                    "Titan"
                    )
  (println "tet = " (ss/text t)))



(let [op (new javax.swing.JOptionPane)
      t1 (ss/text :text "t1")
      t2 (ss/text :text "t2")
      msg (to-array [  t1 t2 ])
      ]
  (.setMessage op msg)
  ;(.setMessageType op javax.swing.JOptionPane/INFORMATION_MESSAGE)
  (.setMessageType op javax.swing.JOptionPane/QUESTION_MESSAGE)
  (let [d (.createDialog op nil "width100")]
    (println "d = " d)
    (.setVisible d true)
    (println "t1 = " (ss/text t1))
    (println "t2 = " (ss/text t2))
    (println "d = " d)
    ))




""
(ssd/show-options (ss/button))
(let [bpw (ss/label
          :text "Tab1")
      bpe (ss/button
          :text "X"
          :size [20 :by 20]
          :listen [:action (fn [event](ss/alert "Next!" ))])

      p (ss/border-panel :east bpe
                         :west bpw)


      t (ss/tabbed-panel :id :tpane
                         :placement :top
                         :size [400 :by 500]
                         :tabs [
                                {:title  p   
                                 :content "content of tab1"}
                                
                                {:title "tab2"
                                    :content "content of tab2"}])      
      window (ss/frame
              :title "First Example" :content t :width 200
              :height 50)]
  (println "tabs = " t)
  (ss/pack! window)
  (ss/show! window))
