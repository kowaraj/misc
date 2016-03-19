(ns tiny-web.rendering-strategy)

(defn render-view [model]
  (println "render-view (greetings) : gets 'response-body' from 'model' " model)
  (let [response-body (get-response model)]
    response-body)) ;return response-body

