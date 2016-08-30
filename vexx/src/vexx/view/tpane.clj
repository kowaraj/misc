(ns vexx.view.tpane
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            [vexx.controller.controller :as c ]
            [vexx.controller.controller-tpane :as c-tp ]
            ))

(defn- listener-delete-tab
  [e]
  (println "called: view.tpane/listener-delete-tab")
  (let [w-buttonDeleteTab (.getSource e)
        w-panel (.getParent w-buttonDeleteTab)
        tpane (.getParent w-panel)
        name-str (name (ss/config w-buttonDeleteTab :id)) ; get the name of the _TAB_ from the button's _ID_
        ti (.indexOfTab tpane name-str)
        ]
    ;; (println "b = " w-buttonDeleteTab)
    ;; (println "p = " w-panel)
    ;; (println "t = " tpane)
    ;; (println "ti = " ti)
    ;; (ss/alert "Deleting :: ti=" ti)
    (c-tp/delete-tab name-str)
    ;(.removeTabAt tpane ti)    
    ))


(defn- make-tab-delete-button
  [data]
  (ss/button :id (:name data)
             :text "Delete TabX"
             :focusable? false
             :listen [:action #(listener-delete-tab % )]))

(defn- make-tab-main-panel
  [data]
  (let [dtype (:type data)]
    (println "type of tab to make: " dtype)
    (cond (= dtype :text) (ss/text (:content data))
          (= dtype :pic) (ss/canvas :paint #(.drawString %2 "I'm a canvas" 50 50))
          :else (ss/label "other"))))

(defn- make-tab
  [data t]
  (let [bDel (make-tab-delete-button data)
        wMain (make-tab-main-panel data)
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
  (println "called: view.tpane/add-tabs")
  (.removeAll tpane)
  (dorun (map #(add-tab tpane %) sel-data)))
