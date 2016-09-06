(ns vexx.controller.controller-listbox
  (:require
   [vexx.model.model-list :as m-l]
   [vexx.model.model-log :as m-log]
   [vexx.model.model-selitem :as m-sel]
   [vexx.model.data-item :as m-di]
   [vexx.model.utils :as m-u]
   ))


(defn add-watch-listbox
  [lb]
  (m-l/add-watch-listbox lb))


(defn delete-item
  []
  (m-l/delete-selected-item)) ;; update the model


(defn add-tab-to-item 
  [& {:keys [tab-name tab-type tab-content]
      :or {tab-name "default tab name"
           tab-type :text
           tab-content "default tab content"}
      }
   ]
  (let [item (m-u/make-data-item :name tab-name
                                 :type tab-type
                                 :content tab-content)]
    (m-di/add-data-item-to-sel-item item))) ;; update the model


(defn get-data-for-selection
  [sel]
  (println " : controller.controller_listbox/get-data-for-selection:  sel= " sel)
  (m-log/set-log-data (str "xlist selected value: " sel))

  (if sel
    (m-sel/set-xlist-sel sel)
    (println " : controller.controller_listbox/get-data-for-selection:  nothing selected"))

  (let [sel (m-sel/get-xlist-sel)
        sel-data (m-sel/get-data-of-sel-item sel)]
    (println " : controller.controller_listbox/get-data-for-selection:  sel-data = " sel-data)
    sel-data))


(defn get-data 
  "
  Returns data for xlist's model.
  Called from view.xlist/make-xlist
  "
  []
  (m-l/get-data))

