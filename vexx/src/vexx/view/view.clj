(ns vexx.view.view
  (:require
   [seesaw.core :as ss]
   [seesaw.dev  :as ssd]
   [seesaw.mig  :as ssm]
   [seesaw.bind :as b]

   [vexx.view.make :as v-make]
   [vexx.controller.listbox :as c-listbox]
   [vexx.controller.textin :as c-textin]


))

(comment (ns vexx.view.view (:require
   [vexx.view.make :as v-make]
   [vexx.controller.listbox :as c-listbox]
   [vexx.controller.textin :as c-textin]

   [vexx.model.item]
   [vexx.model.widgets]
   [vexx.model.data]
   [vexx.model.interface]

                             :reload)))

(ss/native!)

;; ;; ----------------------------
;; ;; Listeners

;; (defn listener-textin-keytyped
;;   [e]
;;   (let [ch (.getKeyChar e)
;;         w-text-in (.getSource e)
;;         new-el (ss/text w-text-in)]
;;     (vc/textin-listener-keytyped ch new-el)))

;; (defn listener-tfsearch-keytyped
;;   [e]
;;   (let [ch (.getKeyChar e)
;;         w-tf-search (.getSource e)
;;         tags-string (ss/text w-tf-search)]
;;     (vc/tfsearch-listener-keytyped ch tags-string)))


;; (defn listener-button-save
;;   [e]
;;   (vc-db/save-to-file))

;; (defn listener-button-load
;;   [e]
;;   (vc-db/load-from-file)
;;   )

;; (defn listener-tpane-keyreleased
;;   [e]
;;   (println " : view.view/listener-tpane-keyreleased")
;;   (if (= java.awt.event.KeyEvent/VK_DELETE (.getKeyCode e)) ;; handle DEL pressed
;;     (println "listener-tpane-keyreleased: DEL pressed")))

;; (defn listener-tpane-focuslost
;;   [e]
;;   (println " : view.view/listener-tpane-focuslost")
;;   )

;; ----------------------------
;; Behavior

(defn add-behavior
  [root]
  (let [p (ss/select root [:#panel])
        ;; text-log (ss/select root [:#text-log])
        xl (ss/select p [:#xlist])
        ;; lb-children (ss/select p [:#xlist-children])
        ;; tf-tags (ss/select p [:#tf-tags])
        text-in (ss/select p [:#text-in])
        ;; tf-search (ss/select p [:#tf-search])
        tpane (ss/select root [:#tpane])
        ;; bsave (ss/select root [:#buttonSave])
        ;; bload (ss/select root [:#buttonLoad])
        ]
    ;; register the widgets  - moved in v-make namespace
    ;; (widgets/add-w :tpane tpane)
    ;; (widgets/add-w :xlist xl)

    ;; add behavior
    (c-textin/add-behavior)
    (c-listbox/add-behavior)
    ;; (c-lb/add-watch-listbox xl)
    ;; (vc/add-watch-textlog text-log)
    ;; (v-lb/add-behavior xl tpane tf-tags)
    ;; (v-lb-children/add-behavior lb-children)
    ;; (ss/listen text-in
    ;;            :key-typed listener-textin-keytyped)
    ;; (ss/listen tf-search
    ;;            :key-typed listener-tfsearch-keytyped)
    ;; (ss/listen bsave
    ;;            :action listener-button-save)
    ;; (ss/listen bload
    ;;            :action listener-button-load)
    ;; (ss/listen tpane
    ;;            :key-released listener-tpane-keyreleased
    ;;            :focus-gained listener-tpane-focuslost)
    root))

(defn make-content
  []
  (let [lp (ssm/mig-panel
            :id :panel
            :items[
                   ;;row
                   ["Propeller"        "split, span, gaptop 10"]

                   ;;row
                   [(v-make/make-button-save)]
                   [(v-make/make-button-load)]
                   [(ss/toggle :id :pencil  :class :tool :text "Pencil" )]
                   [(ss/combobox :id :stroke :class :style
                                 :model [1 2 3 5 8 13 21]) "wrap"]

                   ;;row
                   ["New item:" "gaptop 10"]
                   [(v-make/make-textin) "span, wrap"]
                   
                   ;;row
                   ["Search by tags:" "gaptop 10"]
                   [(ss/text :id :tf-search
                             :text "" :editable? true :columns 25) "span, wrap"]
                   
                   ;;row
                   [(v-make/make-listbox) "span, split 2"] 
                   [(v-make/make-listbox-kids) "wrap"]

                   ;;row
                   ["Tags:" "gaptop 10"]
                   [(v-make/make-textfield) "wrap"]
                   
                   ]) ; end-of-left-panel
        rp (v-make/make-tpane) ; end-of-right-panel
        ] ;end-of-let
    (ss/top-bottom-split 
     (ss/left-right-split lp rp)
     (ss/text :id :text-log
              :text "Logging.." :editable? true :columns 10
              :multi-line? true :wrap-lines? true :rows 3)
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

    ;; init the controller
    ; (vc-db/backup-db)
    ; (vc-db/load-from-file)
    ;; (vc/init-controller)
    
    f))
;(def x (create-view))
