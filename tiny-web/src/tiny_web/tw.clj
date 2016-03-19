(ns tiny-web.tw

  )


(defn handle-request [r]
  (println "handling-request: " (:name r)))
