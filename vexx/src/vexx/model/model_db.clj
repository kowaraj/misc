(ns vexx.model.model-db
  (:require
   [vexx.model.utils :as m-u]
   ))

(def db (ref {})) ; complete database


(defn get-items-by-name 
  "Get seq of names of all the items. Called from model.xlist/..."
  [ ]
  (map m-u/transform-k2s (map first @db)))


(defn load-db
  [data]
  (dosync (ref-set db data))
  )


(defn add-item
  "Add item to db"
  [item]
  (dosync (alter db assoc-in [(:i-name item)] (:i-content item))))



(defn del-item
  "Delete item from db"
  [name]
  (dosync (alter db dissoc (m-u/transform-s2k name))))




;; (defn get-items-str
;;   []
;;   (clojure.string/join "-\n" (map first @m-s/db)))

;; (defn set-item-k-v [item-name, key-name, key-value]
;;   (dosync (alter m-s/db assoc-in [item-name key-name] key-value)))
;; ;(set-item-k-v (first (first @m-s/db)) :tags ["tag1", "tag2", "tag666"])






    
