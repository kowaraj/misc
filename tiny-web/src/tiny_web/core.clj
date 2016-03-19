(ns tiny-web.core  
  (:gen-class)
  (:import (tinyweb HttpRequest HttpRequest$Builder))
  (:import (tinyweb RenderingException ControllerException))
  (:import (other Test01))
  (:import (other Test03))
  (:require [clojure.string :as str])
  )

(require '[tiny-web.view :as view])
(require '[tiny-web.tw :as tw])
(require '[tiny-web.request :as hreq])

;; step1

;(defn test-controller [http-request] {:name (.getBody http-request)})
(def t01 (new Test01 "my-name-of-test01"))
(println "name = " (.get t01))
(def t03 (new Test03 "my-name-of-test03"))
(println "name3 = " (.get t03))

;; (def test-builder (HttpRequest$Builder/newBuilder))
;; (def test-http-request  (.. test-builder (body "Mike") (path "/say-hello") build))
;(defn test-controller-with-map [http-request] {:name (http-request :body)})
;; step2

(defn test-controller [http-request] {:name (http-request :body)})
(defn test-view [model]
  (str "<h1>Hello, " (model :name) "</h1>"))
(def test-request-handler {:controller test-controller :view test-view})
(def test-http-request-map {:body "Mike" :path "/say-hello" :headers {}})
(def test-http-request-map2 {:path "/greeting" :body "Mike,Joe,John,Steve"})
(def test-http-request-map3 {:path "/test" :body "Mike,Joe,John,Steve"})

(defn- render [view model]
  (try
    (view model)
    (catch Exception e (throw (RenderingException. e)))))

(defn- execute-request [http-request handler]
  (println "er")
  (let [controller (handler :controller) view (handler :view)]
    (try
      {:status-code 200
       :body
       (render
        view
        (controller http-request))}
      (catch ControllerException e {:status-code (.getStatusCode e) :body ""})
      (catch RenderingException e {:status-code 500 :body "Exception while rendering"})
      (catch Exception e (.printStackTrace e) {:status-code 500 :body ""}))))

(defn- apply-filters [filters http-request]
  (let [composed-filter (reduce comp (reverse filters))]
    (composed-filter http-request)))

(defn tinyweb [request-handlers filters]
  (fn [http-request]
    (let [filtered-request (apply-filters filters http-request)
          path (http-request :path)
          handler (request-handlers path)]
      (execute-request filtered-request handler))))


(defn handle-greeting [http-request]
  "Controller: Creates our model from the request"
  (let [make-greeting (fn [name]
                        (let [greetings ["Hello" "Greetings" "Salutations" "Hola"]
                              greeting-count (count greetings)]
                          (str (greetings (rand-int greeting-count)) ", " name)))]
    {:greetings (map make-greeting (str/split (:body http-request) #","))}))

(defn greeting-view [model]
  "View: Renders our model"
  (let [render-greeting (fn [greeting] (str "<h2>"greeting"</h2>"))
        rendered-greetings (str/join " " (map render-greeting (:greetings model)))]
    (str "<h1>Friendly Greetings</h1> " rendered-greetings)))

;(greeting-view (handle-greeting test-http-request-map2))

(defn logging-filter [http-request]
  (println (str "In Logging Filter - request for path: " (:path http-request)))
  http-request)


(defn -main
  "I don't do a whole lot ... yet.."
  [& args]
  (let [r {:path "/greeting" :body "Mike,Joe,John,Steve"}
        request-handlers {"/greeting" {:controller handle-greeting :view greeting-view}}
        tw (tinyweb request-handlers [logging-filter])]
    (println (tw test-http-request-map2))
    )

  )

