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
                            (handle-key-typed (.getKeyChar e))
                            (dosync (ref-set list-data [4 5 6 7 0]))
                            ))
    ;;;(.requestFocusInWindow p)
    root))


(defn make-content []
  (ssm/mig-panel
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
          [(ss/text :id :logtext
                    :text "Logging.." :editable? false :columns 30 :multi-line? true :rows 10)]
          ]))
  
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
