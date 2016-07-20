(ns vexx.controller.db-json
  (:require [clojure.data.json :as json]))
;;;To convert to/from JSON strings, use json/write-str and json/read-str:

(def db-filename "data/db.json")

(defn load-from-file []
  (let [data-json (slurp db-filename)]
    (json/read-str data-json)))

(defn save-to-file [data]
  (let [data-json (json/write-str data)]
    (spit db-filename data-json)))

