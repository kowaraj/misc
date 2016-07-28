(ns vexx.view.view
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]
            [vexx.controller.controller :as vc ]))


;(ssd/show-options (ss/combobox :id :stroke :class :style :model [1 2 3 5 8 13 21]))
(ss/native!)


;; ----------------------------
;; Listeners

(defn listener-textin-keytyped
  [e]
  (let [ch (.getKeyChar e)
        w-text-in (.getSource e)
        new-el (ss/text w-text-in)]
    (vc/textin-listener-keytyped ch new-el)))

(defn- add-xtab [t data]
  (.addTab t (:name data) (ss/text (:content data))))

(defn listener-xlist-selection
  [e tpane]
  (let [sel (ss/selection e)
        sel-data (vc/xlist-listener-selection sel)        ]
    (.removeAll tpane)
    (dorun (map #(add-xtab tpane %) sel-data))))

(defn listener-xlist-keyreleased
  [e tpane] 
  (if (= java.awt.event.KeyEvent/VK_DELETE (.getKeyCode e)) ;; handle DEL pressed
    (let [jl (.getSource e)
          sel-index (.getSelectedIndex jl)
          new-size (dec (.getSize (.getModel jl)))
          new-sel-index (if (>= sel-index new-size) (dec new-size) sel-index)]
      (.setSelectedIndex jl new-sel-index)
      (vc/xlist-delete-item)))  

  (if (= \newline (.getKeyChar e)) ;; handle ENTER pressed
    (let [jl (.getSource e)
          sel-index (.getSelectedIndex jl)
          ]
      (let [t (ss/text :text "t1") 
            result (javax.swing.JOptionPane/showInputDialog
                    (ss/border-panel :size [100 :by 100])
                    t
                    "Input"
                    javax.swing.JOptionPane/QUESTION_MESSAGE
                    nil
                    (to-array [1 2 3 4])
                    "Titan"
                    )
            ]
        (let [a (ss/text t)
              b result]
        (if result
          (let []
            (vc/xlist-add-tab-to-item :tab-name a)
            (.fireSelectionValueChanged jl sel-index sel-index false)
            ))
        )))))


      
                    

(let [(new Object) complexMsg[] = { "Above Message", new ImageIcon("yourFile.gif"), new JButton("Hello"),
        new JSlider(), new ImageIcon("yourFile.gif"), "Below Message" };

    JOptionPane optionPane = new JOptionPane();
    optionPane.setMessage(complexMsg);
    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
    JDialog dialog = optionPane.createDialog(null, "Width 100");
    dialog.setVisible(true);

;(def x (create-view))

;; ----------------------------
;; Behavior

(defn add-behavior
  [root]
  (let [p (ss/select root [:#panel])
        text-log (ss/select root [:#text-log])
        xl (ss/select p [:#xlist])
        xt (ss/select p [:#xtext])
        text-in (ss/select p [:#text-in])
        tpane (ss/select root [:#tpane])
        ]
    (vc/add-watch-list xl)
    (vc/add-watch-textlog text-log)
    (ss/listen xl
               :selection #(listener-xlist-selection % tpane)
               :key-released #(listener-xlist-keyreleased % tpane))
    (ss/listen text-in
               :key-typed listener-textin-keytyped)
    root))


(defn make-content
  []
  (let [lp (ssm/mig-panel :id :panel
                          :items[
                                 ["Propeller"        "split, span, gaptop 10"]
                                 [(ss/button :id :buttonDelete :class :tool :text "Delete")]
                                 [(ss/toggle :id :pencil  :class :tool :text "Pencil" )]
                                 [(ss/combobox :id :stroke :class :style
                                               :model [1 2 3 5 8 13 21])"wrap"]
                                 [(ss/text :id :text-in
                                           :text "input" :editable? true :columns 30) "span"]
                                 [(ss/listbox :id :xlist :size [400 :by 400]
                                              :model (vc/get-listbox-data)) "span"]
                                 [(ss/text :id :xtext
                                           :text "Hi Mom!!" :editable? true :columns 30
                                           :multi-line? :true :rows 2) "wrap"]
                                 ])
        rp (ss/tabbed-panel :id :tpane
                            :placement :top
                            :size [400 :by 500]
                            :tabs [
                                   {:title "tab1" :content "content of tab1"}
                                   {:title "tab2" :content "content of tab2"}])

        ]
    (ss/top-bottom-split 
     (ss/left-right-split lp rp)
     (ss/text :id :text-log
              :text "Logging.." :editable? true :columns 10
              :multi-line? true :wrap-lines? true :rows 10)
     )))

(defn make-frame []
  (ss/frame
   :title "Vexx"
   :size [1400 :by 800]
   :content (make-content)))

(defn create-view []
  (let [f (make-frame)]
    (-> f
        add-behavior
        (ss/pack!)
        (ss/show!))
    f))
;(def x (create-view))
;(add-behavior x)



;; (defn add-a-tab
;;   [root]
;;   (let [t (ss/select root [:#tpane])]
;;     (println "tabs = " 
;;     (ss/config! t :tabs [
;;           {:title "tabNew" :content "content of tabNew"}
;;           ])
;;     (ss/config! t :visible? true)
;;     )))
;; ;(add-a-tab x)
  
;; (defn add-a-tab [root]
;;   (let [t (ss/select root [:#tpane])]
;;     (.addTab t "tabX" (ss/text "new component of new tab"))))
;; ;(add-a-tab x)

;; (defn remove-a-tab [root]
;;   (let [t (ss/select root [:#tpane])]
;;     (.removeTabAt t (.indexOfTab t "tab1"))))
;; ;(remove-a-tab x)

