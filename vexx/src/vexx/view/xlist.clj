(ns vexx.view.xlist
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            [vexx.controller.controller :as vc ]
            [vexx.controller.db-json :as vc-db ]))


(defn- add-xtab [t data]
  (.addTab t (:name data) (ss/text (:content data))))

(defn listener-xlist-selection
  [e tpane]
  (let [sel (keyword (ss/selection e))
        sel-data (vc/xlist-listener-selection sel)        ]
    (.removeAll tpane)
    (dorun (map #(add-xtab tpane %) sel-data))))

(defn listener-xlist-keyreleased
  [e tpane] 
  (if (= java.awt.event.KeyEvent/VK_DELETE (.getKeyCode e)) ;; handle DEL pressed
    (let [jl (.getSource e)
          sel-index (.getSelectedIndex jl)
          new-size (dec (.getSize (.getModel jl)))
          new-sel-index (if (>= sel-index new-size) (dec new-size) sel-index)]
      (.setSelectedIndex jl new-sel-index)
      (vc/xlist-delete-item)))  

  (if (= \newline (.getKeyChar e)) ;; handle ENTER pressed
    (let [jl (.getSource e)
          sel-index (.getSelectedIndex jl)
          ]
      (let [t (ss/text :text "t1") 
            result (javax.swing.JOptionPane/showInputDialog
                    (ss/border-panel :size [100 :by 100])
                    t
                    "Input"
                    javax.swing.JOptionPane/QUESTION_MESSAGE
                    nil
                    (to-array [1 2 3 4])
                    "Titan"
                    )
            ]
        (let [a (ss/text t)
              b result]
          (if result
            (let []
              (vc/xlist-add-tab-to-item :tab-name a)
              (.fireSelectionValueChanged jl sel-index sel-index false)
              ))
          )))))
;(def x (create-view))

(defn make-xlist
  []
  [(ss/listbox :id :xlist :size [400 :by 400]
              :model (vc/get-listbox-data)) "span"])

(defn add-behavior
  [xl tpane]
  (ss/listen xl
             :selection #(listener-xlist-selection % tpane)
             :key-released #(listener-xlist-keyreleased % tpane)))
