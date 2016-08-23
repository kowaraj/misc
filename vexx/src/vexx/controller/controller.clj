(ns vexx.controller.controller
  (:require
   [seesaw.core :as ss]
   [seesaw.dev  :as ssd]
   [seesaw.mig  :as ssm]
   [seesaw.bind :as b]
   [vexx.model.model-db :as m-db]
   [vexx.model.model-list :as m-l]
   [vexx.model.model-log :as m-log]
   [vexx.model.data-item :as m-di]
   [vexx.model.utils :as m-u]
   [vexx.controller.db-json :as cj]
   ))

(println "loading vexx.controller.controller...")





;; ----------------------------
;; watchers for listbox

(defn- db-watcher
  "
  Watcher - to update the content of listbox when db changed
  "
  [_ _ _ new-state]
  (dosync (ref-set m-l/list-data (m-db/get-items-by-name))))

(defn add-watch-db  [ ]  (add-watch m-db/db nil db-watcher))





(defn handle-add-new-item
  [name]
  (let [new-item (m-u/make-item :id 48 :name name)]
    (println "new-item: " new-item)
    (m-db/add-item new-item) ;;change db
    (m-log/set-log-data (m-l/get-list-str))))


(defn textin-listener-keytyped
  [ch new-el]
  (m-log/set-log-data (str "Symbol you typed: " ch ", new-el: " new-el))
  (if (= ch \newline)
    (handle-add-new-item new-el)
    (println "textin-listener-keytyped: " ch)))




(defn init-controller
  "Kind of a constructor, default inits"
  []
  (add-watch-db)
  )


;; ----------------------------
;; watchers for the log

(defn add-watch-textlog
  [text-log]
  (add-watch m-log/log-data nil
             (fn [_ _ _ new-text]
               (println "watch-textlog: " new-text)
               (ss/text! text-log new-text))))
