(ns vexx.test
  (:require [seesaw.core :as ss]
            [seesaw.dev  :as ssd]
            [seesaw.mig  :as ssm]
            [seesaw.bind :as b]

            ))

(def d (ref {:1 {:id 48, :data []},
             :2 {:id 48, :data []},
             :3 {:id 48, :data []},
             :4 {:id 48, :data [{:name "4-1", :type "text", :content "tab41 content"}
                                {:name "4-2", :type "pic", :content "tab42 content"}]}
             :5 {:id 48, :data [{:name "Tab51", :type :text, :content "tab51 content"}]}}))

(:5 @d)
(keys @d)

(def ar [{:name "a" :val 1} {:name "b" :val 2} {:name "c" :val 3}])
;;(filter #(not (= %1 %2 )) [1 2 3] )
(filter (fn [ar-el] (not (= (:name ar-el) "b")))    ar)
(apply vector (filter (fn [ar-el] (not (= (:name ar-el) "b")))    ar))
               
(defn mod-tab-of-item
  [db-ref item-name tab-name new-val]
  nil)

(assoc (:1 @d) :data "new-data-of-1")

(defn del-tab-of-item
  [db item-name tab-name]
  (let [item (item-name db)
        data (:data item)]
    (let [new-data (filter #(not (= (:name %) tab-name)) data)
          new-item (assoc item :data (apply vector new-data))]
      (assoc db item-name new-item)
      )))

(dosync (alter d del-tab-of-item :4 "4-2"))
@d

  

(dosync (alter m-db/db update-in [(keyword item-name) :data] conj data-name)))


(defn make-dialog
 []
 (let [x-tf-shown (fn [e]
                        (println "x-tf-shown-listener: " (.getSource e))
                        (.requestFocusInWindow (.getSource e))
                        )
       ;x-tf-keypressed (fn [e] (println "x-tf-keypressed-listener"))
       bp (ss/border-panel :size [100 :by 100])
       t (ss/text :text "Enter new Tab name")
       _ (ss/listen t :component x-tf-shown)
       ;_ (ss/listen t :key-pressed x-tf-keypressed)
       
       result (javax.swing.JOptionPane/showInputDialog
               bp
               t
               "Input"
               javax.swing.JOptionPane/QUESTION_MESSAGE
               nil
               (to-array [:text :pic :other])
               "Titan"
               )
            
            ]
   (println "a = " (ss/text t))
   (println "b = " result)
   )
 )
(make-dialog)
