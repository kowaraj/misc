(ns vexx.view.view
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            [vexx.controller.controller :as vc ]))

;(ssd/show-options (ss/combobox :id :stroke :class :style :model [1 2 3 5 8 13 21]))
(ss/native!)

(defn add-behavior [root]
  (let [p (ss/select root [:#panel])
        xl (ss/select p [:#xlist])
        xt (ss/select p [:#xtext])
        text-in (ss/select p [:#text-in])
        text-log (ss/select p [:#text-log])
        button-delete (ss/select p [:#buttonDelete])
        text-in-listener (vc/make-fn-text-in-key-typed text-log text-in)
        ;;;xlist-listener-key-released (vc/make-xlist-listener-key-released text-log)
        ]
    (vc/add-watch-list xl)
    (vc/add-watch-textlog text-log)
    (ss/listen xl
               :selection (fn [event]
                            (println "You selected: "(ss/selection event))
                            (ss/config! xt :text (str "new selected value is: "
                                                      (ss/selection event))))
               :key-released vc/xlist-listener-key-released;xlist-listener-key-released
               )
    (ss/listen text-in
               :key-typed text-in-listener)
    (ss/listen button-delete
               :action (fn [event]
                         (vc/button-delete-action)))

    root))
;(add-behavior x)

(defn make-content []
  (let [lp (ssm/mig-panel
            :id :panel
            :items[
                   ["Propeller"        "split, span, gaptop 10"]
                   [(ss/button :id :buttonDelete :class :tool :text "Delete")]
                   [(ss/toggle :id :pencil  :class :tool :text "Pencil" )]
                   [(ss/combobox :id :stroke :class :style
                                 :model [1 2 3 5 8 13 21])"wrap"]
                   [(ss/text :id :text-in
                             :text "input" :editable? true :columns 30) "span"]
                   [(ss/listbox :id :xlist :size [400 :by 400]
                                :model (vc/get-listbox-data)) "span"]
                   [(ss/text :id :xtext
                             :text "Hi Mom!!" :editable? true :columns 30
                             :multi-line? :true :rows 2) "wrap"]
                   [(ss/text :id :text-log
                             :text "Logging.." :editable? false :columns 30
                             :multi-line? true :rows 5)]
                   ])
        rp (ssm/mig-panel
            :id :panel
            :items[
                   [(ss/text :id :text-log
                             :text "Your text here..." :editable? true :columns 50
                             :multi-line? true :rows 30)]
                   ])]
    (ss/left-right-split lp rp)))
    
  
(defn make-frame []
  (ss/frame
   :title "Vexx Face"
   :size [1400 :by 800]
   :content (make-content)))

(defn create-view []
  (let [f (make-frame)]
    (-> f
        add-behavior
        (ss/pack!)
        (ss/show!))
    f))
;(def x (create-view))
;(add-behavior x)


;; (defn debug-add-behavior [root]
;;   (let [p (ss/select root [:#panel])
;;         x (ss/select p [:#xlist])]
;;     (doseq [l (.getActionListeners x)] (.removeActionListener x ))))

