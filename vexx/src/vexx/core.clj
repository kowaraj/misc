(ns vexx.core
  (:gen-class)
  (:require  [vexx.view.view :as vv])
  )


(defn -main
  [& args]
  (vv/create-view)
  )

;; ;; (defn test-fn
;; ;;   [x]
;; ;;   (let [y (+ x 1)
;; ;;         ]
;; ;; #break    (* x y)))

;; (defn test-fn-2
;;   [x]
;;   (let [y (+ x 1)
;;         ]
;;    (* x y)))

;; (test-fn-2 5)
(-main)


