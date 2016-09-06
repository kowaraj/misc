(ns vexx.model.data-item
  (:require
   [vexx.model.model-db :as m-db]
   [vexx.model.model-list :as m-xl]
   [vexx.model.model-selitem :as m-sel]
   ))



(defn- add-data-item
  [item-name data-name]
  (println "db = " @m-db/db)
  (println "data-item  = " data-name)
  (dosync (alter m-db/db update-in [(keyword item-name) :data] conj data-name)))




(defn- del-data-item
  [item-name data-name]

  (letfn [(fn-del-tab-of-item
            [db item-name tab-name]
            (let [
                  item (item-name db)
                  data (:data item)
                  new-data (filter #(not (= (:name %) tab-name)) data)
                  new-item (assoc item :data (apply vector new-data))
                  ]
              ;; (println "fn: new-item=" new-item ", new-data=" new-data)
              (assoc db item-name new-item)))
          ]
    ;; (println "db= " @m-db/db)
    ;; (println "model.data_item/del-data-item: item=" item-name ", data-item=" data-name)
    (dosync (alter m-db/db
                   fn-del-tab-of-item (keyword item-name) data-name))))


(defn- modify-data-item--fn
  [db item-name d-i-name d-i-content]
  (let [
        item (item-name db)
        data (:data item)
        data-minus-di (filter #(not (= (:name %) d-i-name)) data)
        old-di (filter #(= (:name %) d-i-name) data)
        _ (println "old-di = " old-di)
        _ (println "old-di*= " (first old-di))
        new-di (assoc (first old-di) :content d-i-content)
        new-data (conj data-minus-di new-di)
        new-item (assoc item :data (apply vector new-data))
        ]
    (println "! old-item = " item)
    (println "! new-item = " new-item)
    (assoc db item-name new-item)))

(defn- modify-data-item
  [item-name d-i-name d-i-content]
  (dosync (alter m-db/db
                 modify-data-item--fn (keyword item-name) d-i-name d-i-content)))

(defn add-data-item-to-sel-item
  [data-item]
  (println "data-item-  = " data-item)
  (let [name (m-sel/get-xlist-sel)]
    (add-data-item name data-item)))

(defn delete-data-item-of-sel-item
  [data-name]
  (println "model.data-item/delete-data-item-of-sel-item: data=" data-name)
  (let [item-name (m-sel/get-xlist-sel)]
    (del-data-item item-name data-name)))

(defn modify-data-item-of-sel-item
  [d-i-name d-i-content]
  (println "model.data-item/modify-data-item-of-sel-item: d-i-name=" d-i-name ", d-i-content=" d-i-content)
  (let [item-name (m-sel/get-xlist-sel)]
    (modify-data-item item-name d-i-name d-i-content)))


;(map println @m-db/db)
;@m-db/db
;@list-data






