(ns vexx.model.model-selitem
  ;; (:require
  ;;  [vexx.model.utils :as m-u]
  ;;  )
  )

;;;----------------------------
;;; Selected item in main listbox

(def xlist-sel (ref nil)) ;selected element of the listbox (xlist)

(defn set-xlist-sel
  [name]
  (dosync (ref-set xlist-sel name)))

(defn get-xlist-sel
  []
  @xlist-sel)

