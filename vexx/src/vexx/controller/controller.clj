(ns vexx.controller.controller
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            [vexx.model.model :as vm]
            [vexx.controller.db-json :as cj]
            ))

(println "loading vexx.controller.controller...")

(defn- make-list-model
  [items]
  (let [model (javax.swing.DefaultListModel.)]
    (doseq [item items] (.addElement model item))
    model))

(defn add-watch-list
  [lb]
  (add-watch vm/list-data nil
             (fn [_ _ _ items] (.setModel lb (make-list-model items)))))


(defn add-watch-textlog
  [text-log]
  (add-watch vm/log-data nil
             (fn [_ _ _ new-text]
               (println "add-watch-textlog" new-text)
               (ss/text! text-log new-text))))

(defn textin-listener-keytyped
  [ch new-el]
  (if (= ch \newline)
    (let [_ nil]
      (vm/add-list-item new-el)
      (vm/set-log-data (vm/get-list-str)))
    (vm/set-log-data (str "Symbol you typed: " ch))
    ))

(defn xlist-delete-item
  []
  (vm/delete-list-sel-item)) ;; update the model

(defn xlist-add-tab-to-item
  [& {:keys [tab-name tab-type tab-content]
      :or {tab-name "default tab name" tab-type :text tab-content "default tab content"}}]  
  (let [item (vm/make-data-item :name tab-name :type tab-type :content tab-content)]
    (vm/add-data-item-to-sel-item item))) ;; update the model

(defn xlist-listener-selection ;rename to get/make-data
  [sel]
  (println "sel = " sel)
  (vm/set-xlist-sel sel)
  (vm/set-log-data (str "xlist selected value: " sel))
  (let [sel-data (vm/get-item-data sel)]
    (println "sel-data = " sel-data)
    sel-data))



(defn get-listbox-data []
  (vm/get-list-data))

