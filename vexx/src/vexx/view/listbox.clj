(ns vexx.view.listbox
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]

            [vexx.view.tpane :as v-tp]
            [vexx.view.tags :as v-tags]

            [vexx.controller.controller :as c ]
            [vexx.controller.controller-listbox :as c-l ]

            [vexx.model.model-selitem :as m-sel] 
            [vexx.model.tags :as m-tags]
            ))


(println "TODO: FIX: remove direct access to MODEL from VIEW !! ")

(defn listener-focusgained
  [e] 
  (println " : view.listbox/listener-focusgained, sel=" (ss/selection e))
  (let [
        jl (.getSource e)
        sel-index (.getSelectedIndex jl)
        ]
    (println " : view.listbox/listener-focusgained: sel-index=" sel-index)
    ))

(defn listener-focuslost
  [e] 
  (println " : view.listbox/listener-focuslost: sel=" (ss/selection e))
  (let [
        jl (.getSource e)
        sel-index (.getSelectedIndex jl)
        ]
    (println " : view.listbox/listener-focuslost: sel-index=" sel-index)
    ))

(defn listener-selection
  [e tpane tf-tags]
  (let [sel (keyword (ss/selection e))]
    ;(println " : view.listbox/listener-selection: sel=" sel)
    (v-tags/update-view tf-tags sel)  ; update tags text-field of selected item

    (if sel
      (let [sel-data (c-l/get-data-for-selection sel)
            sel-index (.getSelectedIndex (.getSource e))
            ]
        (m-sel/set-xlist-sel-index sel-index) ;store the selected index
        ;(println " : view.listbox/listener-selection: (sel) sel-data=" sel-data ", sel-index=" sel-index)
        (v-tp/add-tabs tpane sel-data))
      (let [sel-old (m-sel/get-xlist-sel)
            ;_ (println "sel-old = " sel-old)
            sel-data (c-l/get-data-for-selection sel-old)
            sel-index (m-sel/get-xlist-sel-index) ; retrieve stored index
            ]
        ;(println " : view.listbox/listener-selection: (old) sel-data=" sel-data ", sel-index=" sel-index)
        (.setSelectedIndex (.getSource e) sel-index)
        (v-tp/add-tabs tpane sel-data)))
    ))

;(defn- x-tf-shown  []  (println "!!! x-tf-shown !!!")  )

(defn listener-keyreleased
  [e tpane] 
  (if (= java.awt.event.KeyEvent/VK_DELETE (.getKeyCode e)) ;; handle DEL pressed
    (let [jl (.getSource e)
          sel-index (.getSelectedIndex jl)
          new-size (dec (.getSize (.getModel jl)))
          new-sel-index (if (>= sel-index new-size) (dec new-size) sel-index)]
      (println " : view.listbox/listener-keyreleased: si=" sel-index ", ns=" new-size "ni=" new-sel-index)
      (c-l/delete-item)
      (.setSelectedIndex jl new-sel-index)
      ))

  (if (= \newline (.getKeyChar e)) ;; handle ENTER pressed
    (let [jl (.getSource e)
          sel-index (.getSelectedIndex jl)
          ]
      (let [t (ss/text :text "Enter new Tab name") ; :component x-tf-shown)
            result (javax.swing.JOptionPane/showInputDialog
                    (ss/border-panel :size [100 :by 100])
                    t
                    "Input"
                    javax.swing.JOptionPane/QUESTION_MESSAGE
                    nil
                    (to-array [:text :pic :other])
                    "Titan"
                    )
            ]
        (let [a (ss/text t)
              b result]
          (if result
            (let []
              (c-l/add-tab-to-item :tab-name a :tab-type b)
              (.fireSelectionValueChanged jl sel-index sel-index false)
              ))
          ))
      (.setSelectedIndex jl sel-index)))
  )
;(def x (create-view))


(defn make-listbox
  " Creates listbox widget
    Called from view/make-content
  "
  []
  [(ss/listbox :id :xlist
               :size [400 :by 400]
               :model (c-l/get-data)) "span"])


(defn add-behavior
  [xl tpane tf-tags]
  (ss/listen xl
             :selection #(listener-selection % tpane tf-tags)
             :key-released #(listener-keyreleased % tpane)
             :focus-lost #(listener-focuslost %)
             :focus-gained #(listener-focusgained %)
             ))

