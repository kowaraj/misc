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


(defn- tf--listener-keyreleased
  "
  Listener (key-released) for the JTextField widget for the tags of selected item in the listbox
  Handles:
    Ctrl+S - to save the content of the text-field into the corresponding data-item's :tags
  "
  [e]
  (if (and (== (.getModifiers e) java.awt.event.InputEvent/CTRL_MASK)
           (== (.getKeyCode e) java.awt.event.KeyEvent/VK_S))
    (let [w-tf (.getSource e)
          tf-content (ss/text w-tf)
          ]
      (println "Ctrl+S pressed, saving the tags: " tf-content)
      (c-tags/save-tags-for-selection tf-content)
      )
    (println "something else pressed")))


(defn make-textfield
  []
  [(ss/text :id :tf-tags
            :text "tags here: ..., ..., ..., ..." :editable? true
            :multi-line? :true :columns 30 :rows 2
            :listen [:key-released #(tf--listener-keyreleased %)]
            ) "wrap"]
  )
  

