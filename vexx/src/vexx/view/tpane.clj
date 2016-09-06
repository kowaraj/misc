(ns vexx.view.tpane
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            [vexx.controller.controller :as c ]
            [vexx.controller.controller-tpane :as c-tp ]
            ))


(defn- listener-delete-tab
  [e]
  (println " : view.tpane/listener-delete-tab")
  (let [w-buttonDeleteTab (.getSource e)
        w-panel (.getParent w-buttonDeleteTab)
        tpane (.getParent w-panel)
        name-str (name (ss/config w-buttonDeleteTab :id)) ; get the name of the _TAB_ from the button's _ID_
        ti (.indexOfTab tpane name-str)
        ]
    (c-tp/delete-tab name-str)
    ))


(defn- make-tab-delete-button
  [data]
  (ss/button :id (:name data)
             :text "Delete Tab"
             :focusable? false
             :listen [:action #(listener-delete-tab % )]))

;; (defn- make-tab-save-button
;;   [data]
;;   (ss/button :id :temp-id ;(:name data)
;;              :text "Save Tab"
;;              :focusable? false
;;              ;:listen [:action #(listener-save-tab % )]
;;              ))


(defn- tf--listener-keyreleased
  "
  Listener (key-released) for the JTextField widget of the JTabbedPane (that displays data-item)
  Handles:
    Ctrl+S - to save the content of the text-field into the corresponding data-item of db
  "
  [e]
  (if (and (== (.getModifiers e) java.awt.event.InputEvent/CTRL_MASK)
           (== (.getKeyCode e) java.awt.event.KeyEvent/VK_S))
    (let [w-tf (.getSource e)
          tab-name (name (ss/config w-tf :id)) ; get the name of the _TAB_ from the button's _ID_
          tf-content (ss/text w-tf)
          ]
      (println "Ctrl+S pressed, saving the doc... For tab=" tab-name)
      (println "... content= " tf-content)
      (c-tp/save-tab tab-name tf-content)
      )
    (println "something else pressed")))



(defn- make-tab-main-panel-textfield
  [data]

  (let [
        tf (ss/text :id (:name data)
                    :text (:content data)
                    :editable? true
                    :multi-line? true
                    :wrap-lines? true
                    :columns 10
                    :rows 10)
        ]
    (ss/listen tf :key-released #(tf--listener-keyreleased %))
    tf))
    

(defn- make-tab-main-panel-canvas
  []
  (ss/canvas :paint #(.drawString %2 "I'm a canvas" 50 50)))

(defn- make-tab-main-panel
  [data]
  (let [d-i-type (keyword (:type data))]
    (println "type of tab to make: " d-i-type)
    (let [data-item-widget (cond (= d-i-type :text) (make-tab-main-panel-textfield data)
                                 (= d-i-type :pic) (make-tab-main-panel-canvas)
                                 :else (ss/label "Label, cannot be changed"))
          ]
      ;;(ss/config! data-item-widget :id :data-item-content) ; set the id (common) for the widget that keeps content of data-item
      data-item-widget
      )))

(defn make-tab-footer
  [data]
  (ss/text :text " tags?.. "
           ;:listen [:action #(footer-listener-keyreleased %)]
           :editable? true
           ;:multi-line? true
           ))


(defn- make-tab
  [data t]
  (let [bDel (make-tab-delete-button data)
        wMain (make-tab-main-panel data)
        wFooter (make-tab-footer data)
        p (ss/border-panel
           :north bDel
           :center wMain
           :south wFooter)]
    p))
  
(defn- add-tab
  [t data]
  (prn "data = " data)
  (.addTab t (:name data) (make-tab data t)))

(defn add-tabs
  [tpane sel-data]
  (println " : view.tpane/add-tabs")
  (.removeAll tpane)
  (dorun (map #(add-tab tpane %) sel-data)))
