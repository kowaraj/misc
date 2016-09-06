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
;(-main)

;;=========================
;; TODOs
;;=========================
;; *   load by startup
;; *** tags!
;; *   add feedback email / message 
;; .   icon
;; ?   tree-structure view by tags
;; *** add clickable links to other mems from the currently open one
;;     of 2 types:
;;     - 1. related items/mems
;;     - 2. children - additional info about the main mem (not to have too many tabs?)
;;     => also, we need the attributes to specify the "root" of the mem 
;;        otherwise, in the main list there will be toooo many items of the same name
;; *   display version on main view
;; **  add a "source" tabs? special syntax? 
