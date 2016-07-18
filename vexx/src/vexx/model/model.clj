(ns vexx.model.model
  (:require
   [seesaw.bind :as b]
            ))

                  
            
(def list-data (ref [1 2 3]))

(def text-in-data (ref "default text-in string"))


;;;----------------------------
;;; Database of knowledge items

(def db (ref {
              :items []
              }
              ))

(defn make-item [& {:keys [id name data]
                    :or {data nil}}]
  {:id id :name name :data data})
;(make-item :id 45 :name "itemname" :data "useful data str")

(defn add-item [item]
  (dosync (ref-set db (update-in @db [:items] conj item))))
;; (def a-item (make-item :id 45 :name "itemname" :data "useful data str"))
;; (add-item a-item)
;; (add-item (make-item :id 46 :name "itemname46" :data "useful data str46"))
;; @db

(defn get-item-by-id [id]
  (let [items (@db :items)]
    (filter #(= (% :id) id) items)))
;(get-item-by-id 46)





