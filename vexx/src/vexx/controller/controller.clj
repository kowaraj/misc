(ns vexx.controller.controller
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            [vexx.model.model :as vm]
            [vexx.controller.db-json :as cj]
            ))


(defn- make-list-model
  [items]
  (let [model (javax.swing.DefaultListModel.)]
    (doseq [item items] (.addElement model item))
    model))

(defn add-watch-list
  [lb]
  (add-watch vm/list-data nil
             (fn [_ _ _ items] (.setModel lb (make-list-model items)))))


(defn add-watch-textlog
  [text-log]
  (add-watch vm/log-data nil
             (fn [_ _ _ new-text]
               (println "add-watch-textlog" new-text)
               (ss/text! text-log new-text))))

(defn textin-listener-keytyped
  [ch new-el]
  (if (= ch \newline)
    (let [_ nil]
      (vm/add-list-item new-el)
      (vm/set-log-data (vm/get-list-str)))
    (vm/set-log-data (str "Symbol you typed: " ch))
    ))

;; (defn xlist-listener-key-released
;;   [e]
;;   (if (= java.awt.event.KeyEvent/VK_DELETE (.getKeyCode e)) ;; handle DEL pressed
;;     (let [jl (.getSource e)
;;           sel-index (.getSelectedIndex jl)
;;           new-size (dec (.getSize (.getModel jl)))
;;           new-sel-index (if (>= sel-index new-size) (dec new-size) sel-index)]
;;       (.setSelectedIndex jl new-sel-index)
;;       (vm/delete-list-sel-item) ;; update the model
;;       ))
;;   (if (= \newline (.getKeyChar e)) ;; handle + pressed
;;     (let [i (vm/make-data-item :name "new item name" :content "new item content")]
;;       (println "ii = " i)
;;       (vm/add-data-item-to-sel-item i)) ;; update the model
;;     ))
(defn xlist-delete-item
  []
  (vm/delete-list-sel-item)) ;; update the model

(defn xlist-add-tab-to-item
  [& {:keys [tab-name tab-type tab-content]
      :or {tab-name "default tab name" tab-type :text tab-content "default tab content"}}]  
  (let [item (vm/make-data-item :name tab-name :type tab-type :content tab-content)]
    (vm/add-data-item-to-sel-item item))) ;; update the model



(defn xlist-listener-selection
  [sel]
    (vm/set-xlist-sel sel)
    (vm/set-log-data (str "xlist selected value: " sel))
    (let [sel-data (vm/get-item-data sel)]
      sel-data))



(defn get-listbox-data []
  (vm/get-list-data))

