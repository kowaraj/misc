(ns vexx.view.view
  (:require
   [seesaw.core :as ss]
   [seesaw.dev  :as ssd]
   [seesaw.mig  :as ssm]
   [seesaw.bind :as b]

   [vexx.view.listbox :as v-lb]
   [vexx.view.tags :as v-tags]
   
   [vexx.model.model-db :as m-db]
   [vexx.model.model-list :as m-xl]
   [vexx.model.model-list]
   [vexx.model.data-item]
   
   [vexx.controller.controller :as vc ]
   [vexx.controller.controller-listbox :as c-lb ]
   [vexx.controller.db-json :as vc-db ]
   ))

(comment 
(ns vexx.view.view
  (:require
   [vexx.view.listbox :as v-lb]
   [vexx.view.tags :as v-tags]
   
   [vexx.model.model-db :as m-db]
   [vexx.model.model-list :as m-xl]
   [vexx.model.data-item]
   
   [vexx.controller.controller :as vc ]
   [vexx.controller.controller-listbox :as c-lb ]
   [vexx.controller.db-json :as vc-db ]

   :reload))
)
;(def x (create-view))

;(ss/button :id :buttonSave :class :tool :text "Save" :focusable? false)


(ss/native!)


;; ----------------------------
;; Listeners

(defn listener-textin-keytyped
  [e]
  (let [ch (.getKeyChar e)
        w-text-in (.getSource e)
        new-el (ss/text w-text-in)]
    (vc/textin-listener-keytyped ch new-el)))

(defn listener-tfsearch-keytyped
  [e]
  (let [ch (.getKeyChar e)
        w-tf-search (.getSource e)
        tags-string (ss/text w-tf-search)]
    (vc/tfsearch-listener-keytyped ch tags-string)))


(defn listener-button-save
  [e]
  (vc-db/save-to-file))

(defn listener-button-load
  [e]
  (vc-db/load-from-file)
  )

(defn listener-tpane-keyreleased
  [e]
  (println " : view.view/listener-tpane-keyreleased")
  (if (= java.awt.event.KeyEvent/VK_DELETE (.getKeyCode e)) ;; handle DEL pressed
    (println "listener-tpane-keyreleased: DEL pressed")))

(defn listener-tpane-focuslost
  [e]
  (println " : view.view/listener-tpane-focuslost")
  )

;; ----------------------------
;; Behavior

(defn add-behavior
  [root]
  (let [p (ss/select root [:#panel])
        text-log (ss/select root [:#text-log])
        xl (ss/select p [:#xlist])
        tf-tags (ss/select p [:#tf-tags])
        text-in (ss/select p [:#text-in])
        tf-search (ss/select p [:#tf-search])
        tpane (ss/select root [:#tpane])
        bsave (ss/select root [:#buttonSave])
        bload (ss/select root [:#buttonLoad])
        ]
    (vc/init-controller)
    (c-lb/add-watch-listbox xl)
    (vc/add-watch-textlog text-log)
    (v-lb/add-behavior xl tpane tf-tags)
    ;; (ss/listen xl
    ;;            :selection #(xl/listener-xlist-selection % tpane)
    ;;            :key-released #(xl/listener-xlist-keyreleased % tpane))
    (ss/listen text-in
               :key-typed listener-textin-keytyped)
    (ss/listen tf-search
               :key-typed listener-tfsearch-keytyped)
    (ss/listen bsave
               :action listener-button-save)
    (ss/listen bload
               :action listener-button-load)
    (ss/listen tpane
               :key-released listener-tpane-keyreleased
               :focus-gained listener-tpane-focuslost)
    root))

(defn make-content
  []
  (let [lp (ssm/mig-panel :id :panel
                          :items[
                                 ["Propeller"        "split, span, gaptop 10"]
                                 [(ss/button :id :buttonSave :class :tool :text "Save")]
                                 [(ss/button :id :buttonLoad :class :tool :text "Load")]
                                 [(ss/toggle :id :pencil  :class :tool :text "Pencil" )]
                                 [(ss/combobox :id :stroke :class :style
                                               :model [1 2 3 5 8 13 21]) "wrap"]
                                 [(ss/text :id :text-in
                                           :text "new item" :editable? true :columns 30) "span"]
                                 [(ss/text :id :tf-search
                                           :text "search by ..." :editable? true :columns 30) "span"]
                                 (v-lb/make-listbox)
                                 (v-tags/make-textfield)
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
  (m-db/backup-db)
  (let [f (make-frame)]
    (-> f
        add-behavior
        (ss/pack!)
        (ss/show!))
    f))
;(def x (create-view))
