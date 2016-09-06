(ns vexx.model.model-list
  (:require
   [vexx.model.utils :as m-u]
   [vexx.model.model-db :as m-db]
   [vexx.model.model-selitem :as m-sel]
   )
  )

;;;----------------------------
;;; Model for main listbox
;;;
;;; 1. "list-data" is a ref - to keep the list of strings for listbox
;;; 2. xetters
;;; 3. watcher - to refresh listbox' content when data changed

(def list-data (ref []))


(defn- make-list-model
  [items]
  (if (not (empty? items))
    (let [model (javax.swing.DefaultListModel.)]
      (doseq [item items] (.addElement model item))
      model)
    (let [model (javax.swing.DefaultListModel.)]
      model)))
;(empty? false)


(defn add-watch-listbox
  [lb]
  (add-watch list-data nil
             (fn [_ _ _ items]
               (println " : model.model_list/watch-listbox--fn (listbox-watcher): items =" items)
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

(defn delete-selected-item
  []
  (println " : model.model-list/delete-selected-item")
  (let [name (m-sel/get-xlist-sel)]
    (println " : model.model-list/delete-selected-item: name=" name)
    (m-db/del-item name) ;;change db
    ))


