(ns vexx.controller.db-json
  (:require [clojure.data.json :as json]
            [vexx.model.model :as vm]
            [cheshire.core :as ch] ;:refer :all]
            ))

(println "loading vexx.controller.db-json...")

;;;To convert to/from JSON strings, use json/write-str and json/read-str:

(def db-filename "data/db.json")

(defn load-from-file []
  (let [data-json (slurp db-filename)]
    (vm/set-db (json/read-str data-json :key-fn keyword))))
;(load-from-file)
(json/read-str (slurp db-filename) :key-fn keyword)
(dosync (ref-set vm/db (json/read-str (slurp db-filename) :key-fn keyword)))
;@vm/db

;; (def jstr (json/write-str {:1 11 :2 22}))
;; (json/read-str jstr :key-fn keyword)

(defn save-to-file []
  (let [data-json (json/write-str @vm/db)]
    (spit db-filename data-json)))
;(save-to-file)
;@vm/db

;(ch/parse-string (ch/generate-string x))

;; (defn ch-save-to-file []
;;   (let [data-json (ch/generate-string  @vm/db  {:pretty true})]
;;     (spit db-filename data-json)))
;; ;(ch-save-to-file)


