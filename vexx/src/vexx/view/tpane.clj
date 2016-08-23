(ns vexx.view.tpane
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            [vexx.controller.controller :as c ]
            [vexx.controller.controller-tpane :as c-tp ]
            ;[vexx.controller.db-json :as c-db ]
            ))

(println "loading vexx.view.tpane...")

(defn- listener-delete-tab
  [e]
  (let [w-button (.getSource e)
        w-panel (.getParent w-button)
        tpane (.getParent w-panel)
        name-str "fake name str TODO: replace"
        ]
    (println "b = " w-button)
    (println "p = " w-panel)
    (println "t = " tpane)
    (ss/alert "Deleting :: "  )
    (c-tp/delete-tab name-str)
    (.removeTab tpane (.indexOfTab tpane name-str))
    ))
  
(defn- make-tab
  [data t]
  (let [bDel (ss/button
                :text "Delete Tab"
                :listen [:action #(listener-delete-tab % )]);t (:name data))])
        wMain (let [dtype (:type data)]
                (println "type of tab to make: " dtype)
                (cond (= dtype :text) (ss/text (:content data))
                      (= dtype :pic) (ss/canvas :paint #(.drawString %2 "I'm a canvas" 50 50))
                      :else (ss/label "other")))
        wFooter (ss/text " tags?.. ")
        p (ss/border-panel :north bDel
                           :center wMain
                           :south wFooter)]
    p))
  
(defn- add-tab
  [t data]
  (prn "data = " data)
  (.addTab t (:name data) (make-tab data t)))

(defn add-tabs
  [tpane sel-data]
  (.removeAll tpane)
  (dorun (map #(add-tab tpane %) sel-data)))
