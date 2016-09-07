(ns vexx.view.tags
  (:require
   [seesaw.core :as ss]

   [vexx.controller.tags :as c-tags]
   
   [vexx.model.model-db :as m-db]

   )
  )

(defn update-view
  [tf-tags sel-item]
  (ss/config! tf-tags :text (c-tags/get-data-for-selection sel-item)))


(defn- listener-keypressed
  [e]
  (let [ch (.getKeyChar e)
        w-tf (.getSource e)]
    (if (= ch \newline)
      (let []
        (println "listener-keypressed: Enter pressed")
        (.consume e))
      (println "listener-keypressed: something else pressed"))))


(defn- listener-keytyped
  
  "
  Listener (key-typed) for the JTextField widget for the tags of selected item in the listbox
  Handles:
    Enter or Ctrl+S - to save the content of the text-field into the corresponding data-item's :tags
  "
  [e]
  (let [ch (.getKeyChar e)
        w-tf (.getSource e)]
    (if (or (= ch \newline)
            (and (== (.getModifiers e) java.awt.event.InputEvent/CTRL_MASK)
                 (== (.getKeyCode e) java.awt.event.KeyEvent/VK_S)))

      (let [tf-content (ss/text w-tf) ]
        (println "view.tags/listener-keytyped: Enter pressed, saving the tags: " tf-content)
        (c-tags/save-tags-for-selection tf-content)
        (.setBackground w-tf java.awt.Color/LIGHT_GRAY)
        (println "listener-keypressed: Enter pressed, consume it!")
        (.consume e) ;not to print \newline in the jTextfield
        )
      (let []
        (println "view.tags/listener-keytyped: Something else pressed")
        (.setBackground w-tf java.awt.Color/WHITE)
        ))))

      
;; (defn- tf--listener-keyreleased
;;   "
;;   Listener (key-released) for the JTextField widget for the tags of selected item in the listbox
;;   Handles:
;;     Ctrl+S - to save the content of the text-field into the corresponding data-item's :tags
;;   "
;;   [e]
;;   (if (and (== (.getModifiers e) java.awt.event.InputEvent/CTRL_MASK)
;;            (== (.getKeyCode e) java.awt.event.KeyEvent/VK_S))
;;     (let [w-tf (.getSource e)
;;           tf-content (ss/text w-tf)
;;           ]
;;       (println "Ctrl+S pressed, saving the tags: " tf-content)
;;       (c-tags/save-tags-for-selection tf-content)
;;       )
;;     (println "something else pressed")))


(defn make-textfield
  " JTextField, contains item's tags (commas separated)"
  []
  [(ss/text :id :tf-tags
            :text ""
            :editable? true
            :multi-line? :true
            :wrap-lines? true
            :columns 30
            :rows 1
            :listen [
                     ;;:key-released #(tf--listener-keyreleased %)
                     :key-typed #(listener-keytyped %)
                     :key-pressed #(listener-keypressed %)
                     ]
            ) "wrap"]
  )
  

