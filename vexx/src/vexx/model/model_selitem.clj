(ns vexx.model.model-selitem
  ;; (:require
  ;;  [vexx.model.utils :as m-u]
  ;;  )
  )

;;;----------------------------
;;; Selected item in main listbox

(def xlist-sel (ref nil)) ;selected element's name of the listbox (xlist)
(def xlist-sel-index (ref nil)) ;selected element's index of the listbox (xlist)

(defn set-xlist-sel
  [name]
  (dosync (ref-set xlist-sel name)))

(defn get-xlist-sel
  []
  @xlist-sel)

(defn set-xlist-sel-index
  [i]
  (dosync (ref-set xlist-sel-index i)))

(defn get-xlist-sel-index
  []
  @xlist-sel-index)

