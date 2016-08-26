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

;(name (ss/config (ss/button :id "name1") :id))
;(ssd/show-options (ss/border-panel :id "panel-title" :text "text"))


 (defn- listener-delete-tab
  [e]
  (let [w-buttonDeleteTab (.getSource e)
        w-panel (.getParent w-buttonDeleteTab)
        tpane (.getParent w-panel)
        name-str (name (ss/config w-buttonDeleteTab :id)) ; get the name of the _TAB_ from the button's _ID_
        ;_ (println "name-str= " name-str)
        ti (.indexOfTab tpane name-str)
        ]
    (println "b = " w-buttonDeleteTab)
    (println "p = " w-panel)
    (println "t = " tpane)
    (println "ti = " ti)
    (ss/alert "Deleting :: ti=" ti)
    (c-tp/delete-tab name-str)
    (.removeTabAt tpane ti)
    ))
  
(defn- make-tab
  [data t]
  (let [bDel (ss/button
              :id (:name data)
              :text "Delete Tab"
              :listen [:action #(listener-delete-tab % )]);t (:name data))])
        wMain (let [dtype (:type data)]
                (println "type of tab to make: " dtype)
                (cond (= dtype :text) (ss/text (:content data))
                      (= dtype :pic) (ss/canvas :paint #(.drawString %2 "I'm a canvas" 50 50))
                      :else (ss/label "other")))
        wFooter (ss/text " tags?.. ")
        p (ss/border-panel 
                           :north bDel
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
