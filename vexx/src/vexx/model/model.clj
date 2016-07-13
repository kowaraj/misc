(ns vexx.model.model
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            ))


(defn m-list-model
  [items]
  (let [model (javax.swing.DefaultListModel.)]
    (doseq [item items] (.addElement model item))
    model))

(def m-list-data (ref [1 2 3]))

(def m-text-in-data (ref "default text-in string"))

(defn add-watch-list [lb]
  (add-watch m-list-data nil
             (fn [_ _ _ items] (.setModel lb (m-list-model items)))))

(defn make-fn-text-in-key-typed [text-log text-in]
  (defn text-in-key-typed [e]
    (let [ch (.getKeyChar e)]
      (if (= ch \newline)
        (let [new-el (ss/text text-in)
              old-list-data @m-list-data
              new-list-data (conj old-list-data new-el)]
          (ss/config! text-log :text (str new-list-data))
          (dosync (ref-set m-list-data new-list-data)))
        (ss/config! text-log :text (str "symbol you typed: " (.getKeyChar e)))
        ))))
