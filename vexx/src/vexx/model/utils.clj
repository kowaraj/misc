(ns vexx.model.utils)

(defn transform-s2k
  "From string to keyword"
  [s]
  (keyword s))


(defn transform-k2s
  "From keyword to string"
  [k]
  (name k))


(defn make-db
  "Make default empty db"
  []
  {:mems {}
   :info "no info available"}
  )

(defn make-mem
  "Make item to be added to db. Single record dict:  {name content}"
  [a-name]
  (let [mem-name (transform-s2k a-name)
        mem-content {:path (data/current-path) ; to define parent and kids
                     :from [] ;list of links from other mems
                     :to [] ;;list of links to
                     :data { ;list of this mem's data
                            :name-of-tab1 {:type "type-of-tab"
                                           :content "content of tab"
                                           }
                            :name-of-tab2 {}
                            }
                     } ]
    {mem-name mem-content}))


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
