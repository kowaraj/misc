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
;;   :data [{:name <a string> :type <:text, ...> :content <>}]           ;;; data map, like images, links, videos?, audio?
;;   }
;;  }

(def db (ref {}))
;(dosync (ref-set db {"itemname45" {:id 45, :data "useful data str for item45"}, "itemname46" {:id 46, :data "useful data str46"}}))

(defn make-item [& {:keys [id name data]
                    :or {data []}}]
  [name {:id id :data data}])
;(make-item :id 45 :name "itemname")

(defn add-item [item]
  (dosync (alter db assoc-in [(first item)] (second item))))
;; (def a-item (make-item :id 45 :name "itemname45" :data [{:name "X1" :type :text :content nil}]))
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

(defn make-data-item [& {:keys [name type content]
                         :or {name "default name" type :text content "default text"}}]
  {:name name :type type :content content})
;(make-data-item :name (str "new item name!") :content "new item content")

(defn add-data-item-to-sel-item
  [data-item]
  (println "data-item-  = " data-item)
  (let [name (get-xlist-sel)]
    (add-data-item name data-item)))

(defn add-data-item
  [name data-item]
  (println "db = " @db)
  (println "data-item  = " data-item)
  (dosync (alter db update-in [name :data] conj data-item)))

;; (def x {1 {:id 48, :data []}})
;; (conj (:data (get x 1)) {:name new item name, :type :text, :content new item content})
;; (conj [] {:name new item name, :type :text, :content new item content})


;(add-data-item "itemname45" a-data-item)
;(add-data-item "i3i" a-data-item)
;(add-data-item "itemname46" a-data-item)
;@db

(defn get-item-data
  [name]
  (:data (get @db name)))


;(map println @db)
;@db
;@list-data



;; ----------------------------
;; log

(def log-data (ref "empty log string"))
(defn set-log-data [new-text]
  (dosync (ref-set log-data new-text)))



;; ----------------------------
;; xList - the main listbox

(def list-data (ref [])) ;list of strings for listbox (xlist)

(defn get-list-data
  []
  (get-items-name))

(defn get-list-str
  []
  ;;(clojure.string/join "-\n" @list-data))
  (clojure.string/join ",, " (get-items-name)))

(defn add-list-item
  [name]
  (let [new-item (make-item :id 48 :name name)
        ]
    (add-item new-item) ;;change db
    (dosync (ref-set list-data (get-items-name)))))

(defn delete-list-sel-item
  []
  (let [name (get-xlist-sel)]
    (del-item name) ;;change db
;;;  (dosync (alter list-data #(remove (set [name]) %) ))) ;;change list-data
    (dosync (ref-set list-data (get-items-name)))))


;; ----------------------------
;; list selection

(def xlist-sel (ref nil)) ;selected element of the listbox (xlist)
(defn set-xlist-sel [name] (dosync (ref-set xlist-sel name)))
(defn get-xlist-sel [] @xlist-sel)

;(def text-in-data (ref "default text-in string"))


    
