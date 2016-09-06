(ns vexx.controller.tags
  (:require
   [vexx.model.model-db :as m-db]
   [vexx.model.tags :as m-tags]
   [vexx.model.model-selitem :as m-sel]
   )
  )

(defn get-data-for-selection
  [sel-item]
  (:tags (get @m-db/db sel-item)))
;(dosync (alter m-db/db assoc-in [:1 :tags] "new-tags, old-tags, ..."))


(defn save-tags-for-selection
  [tags-string]
  (let [sel-item (m-sel/get-xlist-sel)]
        (println "sel-item    =" sel-item)
        (println "tags-string =" tags-string)
        (m-db/set-tags-for-selection sel-item tags-string)
        ))
