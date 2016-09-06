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
   [vexx.model.search :as m-search]
   [vexx.controller.db-json :as cj]

   [clojure.string :as str]

   ))


;;;--------------------------- to be placed in sep. file

(defn- item-contains-tags?
  "
  Arguments:
   item - string, item name
   tags - list of strings, tags to search
  "
  [item tags]
  (println "--- item: " item ", tags = " tags)
  (if-let [item-tags-str (:tags ((keyword item) @m-db/db))]
    (let [item-tags (str/split item-tags-str #",")]
      (println "--- set of item-tags   = " (set item-tags))
      (println "--- set of search-tags = " (set tags))
      (= (clojure.set/intersection (set item-tags) (set tags)) (set tags)))
    false))
;;(item-contains-tags? "2" '("tag1" "tag2"))
;;(str/split (:tags ((keyword "4") @m-db/db)) #",")
;; (if-let [item-tags-str (:tags ((keyword "3") @m-db/db))]
;;   (let [item-tags (str/split item-tags-str #",")]
;;     :true)
;;   :false)

(defn- search-result
  "
  Arguments:
   items - list of strings, item names
  "
  [items]
  (let [tags-str @m-search/mode-search
        tags (str/split tags-str #",")]
    (filter #(item-contains-tags? % tags) items)))

;; (filter #(item-contains-tags? % '("tag1")) '("1" "2" "3"))
;; (search-result '("2") '("1" "2" "3"))
;; (filter #(search-fn % "1") '("1" "2" "3"))


(defn- search-watcher
  "
   - when went to search mode
  "
  [_ _ _ tags-str-to-search]
  (println "vexx.controller.controlller/search-watcher: tags-str-to-search = [" tags-str-to-search "]")
  (println "vexx.controller.controlller/search-watcher: empty? = [" (empty? tags-str-to-search) "]")
  (if (empty? tags-str-to-search) ;;TODO: replace by a proper check (for spaces, commas, tabs, newline symbols)
    (dosync (ref-set m-l/list-data                (m-db/get-items-by-name)))
    (dosync (ref-set m-l/list-data (search-result (m-db/get-items-by-name))))
    ))

(defn add-watch-search  [ ]  (add-watch m-search/mode-search nil search-watcher))

(defn- search-fn
  [s sub-s]
  (.contains s sub-s))
;(search-fn "The Band Named Isis" "Isis")

;(:tags ((keyword "1") @m-db/db));
;(str/split "1,2,3" #",")
;(clojure.set/intersection (set [1 2 3 4]) (set [3 2]))
;(= (clojure.set/intersection (set [1 2 3 4]) (set [3 2])) (set [3 2]))
;(= (clojure.set/intersection (set [1   3 4]) (set [3 2])) (set [3 2]))


;; ----------------------------
;; watchers for listbox

(defn- db-watcher
  "
  Watcher - to update the content of listbox when:
   - db changed
   - went to search mode
  "
  [_ _ _ new-state]
  (dosync (ref-set m-l/list-data (m-db/get-items-by-name))))
  ;; (if @m-search/mode-search
  ;;   (dosync (ref-set m-l/list-data (search-result (m-db/get-items-by-name))))
  ;;   (dosync (ref-set m-l/list-data (m-db/get-items-by-name)))))


(defn add-watch-db  [ ]  (add-watch m-db/db nil db-watcher))





(defn- handle-add-new-item
  [name]
  (let [new-item (m-u/make-item :id 48 :name name)]
    ;(println "new-item: " new-item)
    (m-db/add-item new-item) ;;change db
    (m-log/set-log-data (m-l/get-list-str)) ;;logging
    ))


(defn textin-listener-keytyped
  [ch new-el]
  (m-log/set-log-data (str "Symbol you typed: " ch ", new-el: " new-el))
  (if (= ch \newline)
    (handle-add-new-item new-el)
    (println "textin-listener-keytyped: " ch)))


(defn tfsearch-listener-keytyped
  [ch tags-string]
  (println "tfsearch-listener-keytyped: " ch)
  (if (= ch \newline)
    (dosync (ref-set m-search/mode-search tags-string))
    (println "tfsearch-listener-keytyped: not Enter typed")))




(defn init-controller
  "Kind of a constructor, default inits"
  []
  (add-watch-db)
  (add-watch-search)
  )


;; ----------------------------
;; watchers for the log

(defn add-watch-textlog
  [text-log]
  (add-watch m-log/log-data nil
             (fn [_ _ _ new-text]
               (println "watch-textlog: " new-text)
               (ss/text! text-log new-text))))
