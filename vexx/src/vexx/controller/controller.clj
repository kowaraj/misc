(ns vexx.controller.controller
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            [vexx.model.model :as vm]
            ))


(defn m-list-model
  [items]
  (let [model (javax.swing.DefaultListModel.)]
    (doseq [item items] (.addElement model item))
    model))


(defn add-watch-list [lb]
  (add-watch vm/list-data nil
             (fn [_ _ _ items] (.setModel lb (m-list-model items)))))


(defn get-listbox-data []
  @vm/list-data)

(defn set-listbox-data [data]
  (dosync (ref-set vm/list-data data)))

(defn make-fn-text-in-key-typed [text-log text-in]
  (defn text-in-key-typed [e]
    (let [ch (.getKeyChar e)]
      (if (= ch \newline)
        (let [new-el (ss/text text-in)
              old-list-data @vm/list-data
              new-list-data (conj old-list-data new-el)]
          (ss/config! text-log :text (str new-list-data))
          (set-listbox-data new-list-data))
        (ss/config! text-log :text (str "symbol you typed: " (.getKeyChar e)))
        ))))



