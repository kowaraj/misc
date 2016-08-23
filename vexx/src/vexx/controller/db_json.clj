(ns vexx.controller.db-json
  (:require [clojure.data.json :as json]
            [vexx.model.model-db :as m-db]
            [cheshire.core :as ch] ;:refer :all]
            ))

(println "loading vexx.controller.db-json...")

(def db-filename "data/db.json")

(defn load-from-file []
  (let [data-json (slurp db-filename)]
    (m-db/load-db (json/read-str data-json :key-fn keyword))))
;(load-from-file)
;@vm/db

(defn save-to-file []
  (let [data-json (json/write-str @m-db/db)]
    (spit db-filename data-json)))
;(save-to-file)
;@vm/db

;(ch/parse-string (ch/generate-string x))
;; (defn ch-save-to-file []
;;   (let [data-json (ch/generate-string  @vm/db  {:pretty true})]
;;     (spit db-filename data-json)))
;; ;(ch-save-to-file)


