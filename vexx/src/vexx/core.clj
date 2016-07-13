(ns vexx.core
  (:gen-class)
  (:require  [vexx.view.view :as vv]))

(defn -main
  [& args]
  (vv/create-view))

;(-main)


