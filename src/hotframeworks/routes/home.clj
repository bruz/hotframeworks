(ns hotframeworks.routes.home
  (:require [compojure.core :refer :all]
            [clojure.data.json :as json]
            [hotframeworks.views.layout :as layout]
            [hotframeworks.models.db :as db]
            [hotframeworks.models.graphs :as graphs]))

(defn mini-ranking [frameworks]
  [:table {:class "table table-striped"}
   [:thead
    [:tr
     [:th "Framework"]
     [:th "Score"]
     [:th "Weekly Change"]]]
   [:tbody
    (map (fn [framework]
           [:tr
            [:td (:name framework)]
            [:td (:latest_score framework)]
            [:td (:latest_delta framework)]])
         frameworks)]])

(defn full-rankings []
  (let [frameworks (db/latest-framework-scores)]
    [:table {:class "table table-striped"}
     [:thead
      [:tr
       [:th "Framework"]
       [:th "Language"]
       [:th "Github Score"]
       [:th "Link Score"]
       [:th "Traffic Score"]
       [:th "Stackoverflow Score"]
       [:th "Overall Score"]
       [:th "Weekly Change"]]]
     [:tbody
      (map (fn [framework]
             [:tr
              [:td (:name framework)]
              [:td (:latest_score framework)]
              [:td (:latest_delta framework)]])
           frameworks)]]))

(defn top-frameworks-json []
  (json/write-str
   (graphs/most-popular 10 10)))

(defn home []
  (layout/common
   [:div
    [:div.row
     [:div#about.col-md-12.page-header
      [:h1 "Find your new favorite web framework"
       [:br]
       [:small "Measuring web framework popularity so you can find interesting frameworks to check out"]]]]
    [:div.row
     [:div.col-md-9
      [:div#graph-container
       [:div#graph]]]
     [:div.col-md-3
      [:div#legend]]]
    [:div.row
     [:div.col-md-12.page-header
      [:h1 "Rankings"]]]]
   (format "var data = %s;
           Hotframeworks.graph('#graph', '#legend', data);"
           (top-frameworks-json))))

(defroutes home-routes
  (GET "/" [] (home)))
