(ns vexx.core
  (:gen-class)
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]))

(ss/native!)

(defn handle-key-typed [c]
  (println "handle-key-typed: " c))



(def list-data (ref [1 2 3]))
;; (let [new-el "new text"
;;       old-list-data @list-data
;;       new-list-data (conj old-list-data new-el)]
;;   (prn new-list-data)
;;   )


(defn list-model 
  [items]
  (let [model (javax.swing.DefaultListModel.)]
    (doseq [item items] (.addElement model item))
    model))



(defn add-behavior [root]
  (println "add-behavior called-")
  (let [p (ss/select root [:#panel])
        xl (ss/select p [:#xlist])
        xt (ss/select p [:#xtext])
        text-in (ss/select p [:#text-in])
        text-log (ss/select p [:#text-log])
        ]

    (add-watch list-data nil
               (fn [_ _ _ items] (.setModel xl (list-model items))))


    (ss/listen p
               :key-typed (fn [e]
                            (handle-key-typed (.getKeyChar e) )
                            ))
    
    (ss/listen xl
               :selection (fn [event]
                            (println "You selected: "(ss/selection event))
                            (ss/config! xt :text (str "new selected value is: " (ss/selection event)))
                            ))
    (ss/listen text-in
               :key-typed (fn [e]
                            (let [ch (.getKeyChar e)]
                              (if (= ch \newline)
                                (let [new-el (ss/text text-in)
                                      old-list-data @list-data
                                      new-list-data (conj old-list-data new-el)]
                                  (ss/config! text-log :text (str new-list-data))
                                  (dosync (ref-set list-data new-list-data)))
                                (ss/config! text-log :text (str "symbol you typed: " (.getKeyChar e)))
                                ))))
    ;;;(.requestFocusInWindow p)
    root))
;(-main)
(def v [1 2 3])
(conj v 5)
(str v)

(defn make-content []
  (let [lp (ssm/mig-panel
            :id :panel
            :items[
                   ["Propeller"        "split, span, gaptop 10"]
                   [(ss/toggle :id :pencil  :class :tool :text "Pencil" )]
                   [(ss/toggle :id :line    :class :tool :text "Line")]
                   [(ss/combobox :id :stroke :class :style :model [1 2 3 5 8 13 21])"wrap"]
                   [(ss/text :id :text-in
                             :text "input" :editable? true :columns 30) "span"]
                   [(ss/listbox :id :xlist :model ["aaa", "bbb", "ccc"]) "span"]
                   [(ss/text :id :xtext
                             :text "Hi Mom!!" :editable? true :columns 30 :multi-line? :true :rows 2) "wrap"]
                   [(ss/text :id :text-log
                             :text "Logging.." :editable? false :columns 30 :multi-line? true :rows 5)]
                   ])
        rp (ssm/mig-panel
            :id :panel
            :items[
                   [(ss/text :id :text-log
                             :text "Your text here..." :editable? true :columns 50 :multi-line? true :rows 30)]
                   ])]
    (ss/left-right-split lp rp)))
    

  
(defn make-frame []
  (ss/frame
   :title "Vexx Face"
   :size [1400 :by 800]
   :content (make-content)))

(defn -main
  [& args]
  (-> (make-frame)
      add-behavior
      (ss/pack!)
      (ss/show!)))

;(-main)





;; (defn display
;;   [content]
;;   (let [window (ss/frame :title "Example")]
;;     (-> window
;;         (ss/config! :content content) (ss/pack!) (ss/show!))))
;; (display listbox)

;;
