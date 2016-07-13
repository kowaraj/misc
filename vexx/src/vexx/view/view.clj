(ns vexx.view.view
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            [vexx.controller.controller :as vc ]))

(ss/native!)


(defn add-behavior [root]
  (let [p (ss/select root [:#panel])
        xl (ss/select p [:#xlist])
        xt (ss/select p [:#xtext])
        text-in (ss/select p [:#text-in])
        text-log (ss/select p [:#text-log])
        text-in-listener (vc/make-fn-text-in-key-typed text-log text-in)
        ]

    (vc/add-watch-list xl)

    (ss/listen xl
               :selection (fn [event]
                            (println "You selected: "(ss/selection event))
                            (ss/config! xt :text (str "new selected value is: " (ss/selection event)))
                            ))
    (ss/listen text-in
               :key-typed text-in-listener)
    ;;;(.requestFocusInWindow p)
    root))


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
                   [(ss/listbox :id :xlist :model (vc/get-listbox-data)) "span"]
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

(defn create-view
  []
  (-> (make-frame)
      add-behavior
      (ss/pack!)
      (ss/show!)))


