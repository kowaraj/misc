(ns vexx.view.tpane
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            ;[vexx.controller.controller :as c ]
            ;[vexx.controller.db-json :as c-db ]
            ))

(println "loading vexx.view.tpane...")

(defn- make-tab
  [data]
  (let [bDel (ss/button
                :text "Delete Tab"
                :listen [:action (fn [event](ss/alert "Deleting: " (:name data) ))])
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
  (.addTab t (:name data) (make-tab data)))

(defn add-tabs
  [tpane sel-data]
  (.removeAll tpane)
  (dorun (map #(add-tab tpane %) sel-data)))
