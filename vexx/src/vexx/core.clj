(ns vexx.core
  (:gen-class))

(require '[vexx.view.face :as face])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (face/make)
  (println "Hello, World!"))
