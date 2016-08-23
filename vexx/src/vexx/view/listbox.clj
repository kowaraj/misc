(ns vexx.view.listbox
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            [vexx.controller.controller :as c ]
            [vexx.controller.controller-listbox :as c-l ]
            ;[vexx.controller.db-json :as c-db ]
            [vexx.view.tpane :as v-tp]
            ))

(println "loading vexx.view.listbox...")



(defn listener-selection
  [e tpane]
  (let [sel (keyword (ss/selection e))
        sel-data (c-l/listener-selection sel) ]
    (println "view.listbox/listener-selection: sel=" sel ", sel-data=" sel-data)
    (v-tp/add-tabs tpane sel-data)))

(defn listener-keyreleased
  [e tpane] 
  (if (= java.awt.event.KeyEvent/VK_DELETE (.getKeyCode e)) ;; handle DEL pressed
    (let [jl (.getSource e)
          sel-index (.getSelectedIndex jl)
          new-size (dec (.getSize (.getModel jl)))
          new-sel-index (if (>= sel-index new-size) (dec new-size) sel-index)]
      (println "view.listbox/listener-keyreleased: si=" sel-index ", ns=" new-size "ni=" new-sel-index)
      (c-l/delete-item)
      (.setSelectedIndex jl new-sel-index)
      ))

  (if (= \newline (.getKeyChar e)) ;; handle ENTER pressed
    (let [jl (.getSource e)
          sel-index (.getSelectedIndex jl)
          ]
      (let [t (ss/text :text "Enter new Tab name") 
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
              (c-l/listbox-add-tab-to-item :tab-name a :tab-type b)
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
               :model (c-l/get-listbox-data)) "span"])

(defn add-behavior
  [xl tpane]
  (ss/listen xl
             :selection #(listener-selection % tpane)
             :key-released #(listener-keyreleased % tpane)))
