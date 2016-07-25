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
          (ss/config! text-log :text (vm/get-list-str)))
        (ss/config! text-log :text (str "symbol you typed: " (.getKeyChar e)))
        ))))

(defn xlist-listener-key-released [e]
  (let [ch (.getKeyChar e)]
    ;(println "key-released: ! " ch)
    (if (= java.awt.event.KeyEvent/VK_DELETE (.getKeyCode e))
      (let [jl (.getSource e)
            sel-index (.getSelectedIndex jl)
            new-size (dec (.getSize (.getModel jl)))
            new-sel-index (if (>= sel-index new-size) (dec new-size) sel-index)]
        
        (vm/delete-list-sel-item)
        (println "new size = " new-size)
        (println "new sel index = " new-sel-index)
        
        (.setSelectedIndex jl new-sel-index))
      )))

(defn xlist-listener-selection [e]
  (let [sel (ss/selection e)]
    ;(println "You selected: " sel)
    (vm/set-xlist-sel sel)
    (vm/set-log-data (str "xlist selected value: " sel "\n" (.getSelectedIndex (.getSource e)) "\n" e))))


(defn get-listbox-data []
  (vm/get-list-data))
