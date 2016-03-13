(ns vexx.model.model
  (:use [seesaw.core] [seesaw.font]
        )
  )

(defn get-model []
  (let [m {:a 0 :b 1}]
    (ref m)))
