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

(defn add-watch-list [lb]
  (add-watch vm/list-data nil
             (fn [_ _ _ items] (.setModel lb (make-list-model items)))))


(defn add-watch-textlog [text-log]
  (add-watch vm/log-data nil
             (fn [_ _ _ new-text]
               (println "add-watch-textlog" new-text)
               (ss/text! text-log new-text))))


(defn make-fn-text-in-key-typed [text-log text-in]

  (defn text-in-key-typed [e]
    (let [ch (.getKeyChar e)]
      (if (= ch \newline)
        (let [new-el (ss/text text-in)]
          (vm/add-list-item new-el)
          (ss/config! text-log :text (vm/get-list-data)))
        (ss/config! text-log :text (str "symbol you typed: " (.getKeyChar e)))
        ))))

;(defn make-xlist-listener-key-released [text-log]
(defn xlist-listener-key-released []
  (defn f [e]
    (let [ch (.getKeyChar e)]
      (println "key-released: " ch)
      (if (= java.awt.event.KeyEvent/VK_DELETE (.getKeyCode e))
        (let []
          ;;;(ss/config! text-log :text "delete recognized")
          (vm/set-log-data "del detected!")
          
          )
        ;;(ss/config! text-log :text (str ch))
        (vm/set-log-data (str ch))
        ))))

(defn button-delete-action [name]
  (vm/delete-list-item name))
;java.awt.event.KeyEvent/VK_BACK_SPACE

;; (cj/save-to-file {:a 1 :b 2})
;; ((cj/load-from-file) "a")

(defn get-listbox-data []
  (vm/get-list-data))
