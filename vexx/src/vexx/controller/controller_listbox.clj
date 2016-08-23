(ns vexx.controller.controller-listbox
  (:require
   ;; [seesaw.core :as ss]
   ;; [seesaw.dev  :as ssd]
   ;; [seesaw.mig  :as ssm]
   ;; [seesaw.bind :as b]
   ;; [vexx.model.db :as m-db]
   ;; [vexx.model.data-item :as m-di]
   ;; [vexx.model.state :as m-s]
   ;; [vexx.model.utils :as m-u]
   [vexx.model.model-list :as m-l]
   [vexx.model.model-log :as m-log]
   [vexx.model.model-selitem :as m-sel]
   [vexx.model.data-item :as m-di]
   [vexx.model.utils :as m-u]

   ))

(println "loading vexx.controller.controller-listbox...")

(defn add-watch-listbox
  [lb]
  (m-l/add-watch-listbox lb))


(defn delete-item
  []
  (m-l/delete-selected-item)) ;; update the model


(defn listbox-add-tab-to-item ;TODO: rename - removed listbox- prefix
  [& {:keys [tab-name tab-type tab-content]
      :or {tab-name "default tab name" tab-type :text tab-content "default tab content"}}]
  (let [item (m-u/make-data-item :name tab-name :type tab-type :content tab-content)]
    (m-di/add-data-item-to-sel-item item))) ;; update the model


(defn listener-selection
  [sel]
  (println "sel = " sel)
  (m-sel/set-xlist-sel sel)
  (m-log/set-log-data (str "xlist selected value: " sel))
  (let [sel-data (m-di/get-item-data sel)]
    (println "sel-data = " sel-data)
    sel-data))


(defn get-listbox-data ;TODO: rename - removed listbox- prefix
  " Returns data for xlist's model. Called from view.xlist/make-xlist"
  []
  (m-l/get-data))

