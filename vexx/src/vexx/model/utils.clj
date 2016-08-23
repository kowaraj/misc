(ns vexx.model.utils
  ;; (:require
  ;;  [vexx.model.xlist :as m-xl]
  ;;  [vexx.model.item :as m-i] )
   )

(defn transform-s2k
  "From string to keyword"
  [s]
  (keyword s))


(defn transform-k2s
  "From keyword to string"
  [k]
  (name k))


(defn make-item
  "Make item to be added to db: {:i-name ... :i-content ...}"
  [& {:keys [id
             name
             data]
      :or {data []}}
   ]
  (let [item-name (transform-s2k name)
        item-content {:id id :data data}
        ]
    {:i-name item-name
     :i-content item-content}))


(defn make-data-item
  "Create new data item (tab-content)"
  [& {:keys [name
             type
             content]
      :or {name "default name"
           type :text
           content "default text"}}
   ]
  {:name name
   :type type
   :content content})
