(ns vexx.model.data-item
  (:require
   [vexx.model.model-db :as m-db]
   [vexx.model.model-list :as m-xl]
   [vexx.model.model-selitem :as m-sel]
   ))

(println "loading vexx.model.item...")


(defn- add-data-item
  [item-name data-name]
  (println "db = " @m-db/db)
  (println "data-item  = " data-name)
  (dosync (alter m-db/db update-in [(keyword item-name) :data] conj data-name)))

(defn- del-data-item
  [item-name data-name]
  (println "db = " @m-db/db)
  (println "data-name  = " data-name)
  (dosync (alter m-db/db update-in [(keyword item-name) :data] conj data-name)))
  

(defn add-data-item-to-sel-item
  [data-item]
  (println "data-item-  = " data-item)
  (let [name (m-sel/get-xlist-sel)]
    (add-data-item name data-item)))

(defn get-item-data
  [name]
  (:data 
   (get @m-db/db name)))

(defn delete-data-item-of-sel-item
  [data-name]
  (println "data-name = " data-name)
  (let [item-name (m-sel/get-xlist-sel)]
    (del-data-item item-name data-name)))


;(map println @m-db/db)
;@m-db/db
;@list-data






