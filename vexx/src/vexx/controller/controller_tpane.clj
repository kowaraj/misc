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
  (println "called: controller.controller_tpane/delete-tab: name = " name-str)
  (m-di/delete-data-item-of-sel-item name-str)
  )

