(ns vexx.controller.controller-tpane
  (:require
   [vexx.model.model-list :as m-l]
   [vexx.model.model-selitem :as m-sel]
   [vexx.model.data-item :as m-di]
   [vexx.model.utils :as m-u]
   ))

(println "loading vexx.controller.controller-tpane...")


(defn delete-tab
  [name-str]
  (println " : controller.controller_tpane/delete-tab: name = " name-str)
  (m-di/delete-data-item-of-sel-item name-str)
  )

(defn save-tab
  [tab-name data-item-content]
  (println " : controller.controller_tpane/save-tab: name = " tab-name ", content = " data-item-content)
  (m-di/modify-data-item-of-sel-item tab-name data-item-content)
  )

