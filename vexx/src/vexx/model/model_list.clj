(ns vexx.model.model-list
  (:require
   [vexx.model.utils :as m-u]
   [vexx.model.model-db :as m-db]
   [vexx.model.model-selitem :as m-sel]
   )
  )

(println "loading vexx.model.model-list...")            

;;;----------------------------
;;; Model for main listbox
;;;
;;; 1. "list-data" is a ref - to keep the list of strings for listbox
;;; 2. xetters
;;; 3. watcher - to refresh listbox' content when data changed

(def list-data (ref []))


(defn- make-list-model
  [items]
  (let [model (javax.swing.DefaultListModel.)]
    (doseq [item items] (.addElement model item))
    model))

(defn add-watch-listbox
  [lb]
  (add-watch list-data nil
             (fn [_ _ _ items]
               (println "--- !!! --- listbox-watcher triggered!")
               (.setModel lb (make-list-model items)))))


(defn get-data
  "
  Get data of the list - a seq of names of all the items in the db.
  "
  []
  (m-db/get-items-by-name))

(defn get-list-str
  []
  ;;(clojure.string/join "-\n" @m-s/list-data))
  (clojure.string/join ",, " (get-data)))


;; (defn add-list-item
;;   [name]
;;   (let [new-item (make-item :id 48 :name name)
;;         ]
;;     (add-item new-item) ;;change db
;;     (dosync (ref-set m-s/list-data (get-items-name)))))

(defn delete-selected-item
  []
  (let [name (m-sel/get-xlist-sel)]
    (m-db/del-item name) ;;change db
    ))

