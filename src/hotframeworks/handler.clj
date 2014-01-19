(ns hotframeworks.handler
  (:require [compojure.core :refer :all]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [hotframeworks.views.main :refer :all]))

(defn init []
  (println "hotframeworks is starting"))

(defn destroy []
  (println "hotframeworks is shutting down"))
  
(defroutes app-routes
  (GET "/" [] (home))
  (GET "/languages/:identifier" [identifier] (language identifier))
  (GET "/faq" [] (faq))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app (handler/site (routes app-routes)))

(def war-handler 
  (-> app    
    (wrap-resource "public") 
    (wrap-base-url)
    (wrap-file-info)))
  

