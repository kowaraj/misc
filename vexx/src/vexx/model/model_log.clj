(ns vexx.model.model-log
;  (:require [])
   )

(def log-data (ref "empty log string"))

(defn set-log-data [new-text]
  (dosync (ref-set log-data new-text)))
