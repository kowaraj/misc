(ns vexx.model.model
  (:require
   [seesaw.bind :as b]
            ))
            

;;;----------------------------
;;; Database of knowledge items

;; db is a ref to the following map:
;; {
;;  "name of list elem" ;;; name of the element (shown in the list)
;;  {
;;   :id 0,             ;;; id, as a timestamps? 
;;   :tags [],          ;;; list of tags
;;   :data {}           ;;; data map, like images, links, videos?, audio?
;;   }
;;  }

(def db (ref {}))

(defn make-item [& {:keys [id name data]
                    :or {data nil}}]
  [name {:id id :data data}])
;(make-item :id 45 :name "itemname" :data "useful data str")

(defn add-item [item]
  (dosync (alter db assoc-in [(first item)] (second item))))
;; (def a-item (make-item :id 45 :name "itemname45" :data "useful data str for item45"))
;; (add-item a-item)
;; (add-item (make-item :id 46 :name "itemname46" :data "useful data str46"))

(defn del-item [name]
  (dosync (alter db dissoc name)))
;;(del-item "x")
;;@db

(defn get-items-name []
  (map first @db))

(defn get-items-str []
   (clojure.string/join "-\n" (map first @db)))

(defn set-item-k-v [item-name, key-name, key-value]
  (dosync (alter db assoc-in [item-name key-name] key-value)))
;(set-item-k-v (first (first @db)) :tags ["tag1", "tag2", "tag666"])

;(map println @db)
;@db
;@list-data



;;;---------------------------- log

(def log-data (ref "empty log string"))
(defn set-log-data [new-text]
  (dosync (ref-set log-data new-text)))


;;;---------------------------- list

(def list-data (ref [])) ;list of strings for listbox (xlist)

(defn get-list-str []
  ;;(clojure.string/join "-\n" @list-data))
  (clojure.string/join "-\n" (get-items-name)))

(defn add-list-item [name]
  (let [new-item (make-item :id 48 :name name :data "useful data str for new item")
        ]
    (add-item new-item) ;;change db
    (dosync (ref-set list-data (get-items-name)))))

(defn delete-list-item [name]
  (del-item name) ;;change db
;;;  (dosync (alter list-data #(remove (set [name]) %) ))) ;;change list-data
  (dosync (ref-set list-data (get-items-name))))
                
(def text-in-data (ref "default text-in string"))


    
