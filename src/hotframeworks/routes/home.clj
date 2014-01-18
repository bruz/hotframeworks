(ns hotframeworks.routes.home
  (:require [compojure.core :refer :all]
            [clojure.data.json :as json]
            [hotframeworks.views.layout :as layout]
            [hotframeworks.models.db :as db]
            [hotframeworks.models.graphs :as graphs]
            [hotframeworks.models.framework :as framework]))

(defn mini-ranking [frameworks]
  [:table {:class "table table-striped"}
   [:thead
    [:tr
     [:th "Framework"]
     [:th.score "Score"]
     [:th.score "Weekly Change"]]]
   [:tbody
    (map (fn [framework]
           [:tr
            [:td (:name framework)]
            [:td.score (:latest_score framework)]
            [:td.score (:latest_delta framework)]])
         frameworks)]])

(defn full-rankings []
  (let [frameworks (framework/latest-scores)]
    [:table {:class "table table-striped"}
     [:thead
      [:tr
       [:th "Framework"]
       [:th.score "Github Score"]
       [:th.score "Stack Overflow Score"]
       [:th.score "Overall Score"]
       [:th.score "Weekly Change"]]]
     [:tbody
      (map (fn [framework]
             [:tr
              [:td.score (:name framework)]
              [:td.score (:github framework)]
              [:td.score (:stackoverflow framework)]
              [:td.score (:combined framework)]
              [:td.score (:delta framework)]])
           frameworks)]]))

(defn top-frameworks-data []
  (json/write-str
   (graphs/most-popular 10 10)))

(defn languages-data []
  (into {}
        (map (fn [language]
               [(:id language)
                {:name (:name language)
                 :data (graphs/for-language language 10)}])
             (db/all-languages-by-name))))

(defn graph-data-json []
  (json/write-str
   {:topFrameworks (graphs/most-popular 10 10)
    :languages (languages-data)}))

(defn home []
  (layout/common
   [:div
    [:div#top-frameworks.row
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
    [:div#rankings.row
     [:div.col-md-12.page-header
      [:h1 "Rankings"]
      (full-rankings)]]
    [:div#languages
     [:h1 "Languages"]
     (map (fn [language]
            (let [name (:name language)]
              [:div.row.language {:id (str "language-" (:id language))}
               [:h2 name]
               [:div.col-md-4
                (mini-ranking (db/frameworks-for-language language))]
               [:div.col-md-6
                [:div {:id (str "language-chart-" (:id language))}]]
               [:div.col-md-2
                [:div {:id (str "language-legend-" (:id language))}]]]))
          (db/all-languages-by-name))]
    [:div#faq
     [:h1 "Frequently Asked Questions"]
     [:h3 "How are the different frameworks scored?"]
     [:p
      "Each framework is scored by four separate measures, and these are
      simply averaged. The four measures are:"]
     [:ol
      [:li
       "GitHub score: Based on the number of stars the git repository for a
       framework has on GitHub. Since Hotframeworks can't
       measure this for frameworks not on GitHub, you'll see 'N/A' for those."]
      [:li
       "Stack Overflow score: Based on the number of questions on Stack Overflow
       that are tagged with the name of the framework."]]
     [:p
      "Since these three measure of popularity are on different scales the final scores normalized to a scale of 0-100. The scores on a log scale since the measures cover such a large
      range, so for instance a framework with a score of 90 for Stack Overflow
      may have thousands of questions while a framework with a score of 10-20 might
      just have a handful."]
     [:h3 " Why does my favorite framework have such a low score? "]
     [:p
      " Since scores are simply calculated as an average of the three measures
      described in the previous question, you can investigate the individual scores
      as compared to other frameworks to see why your framework places where it
      does. With Github scores for instance, you can look at how many watchers
      your framework has and compare it to frameworks with higher scores.
      If you still think something is wrong with the score after doing some
      investigation, please "
      [:a {:href "#suggestion"} "let us know."]]
     [:h3 "Why isn't my favorite framework listed? It's so cool!"]
     [:p
      "I'd love to hear about it. Just "
      [:a {:href "#suggestion"} "send a suggestion"]
      "."]
     [:h3
      "What are the criteria for deciding if something is a \"web framework\"?"]
     [:p
      "As a rough guide, if you can finish the sentence \"I just built this
      sweet new web application with [insert name of framework here]\", then it'd
      probably be considered a web framework. Note that frameworks don't have to
      be \"full-stack\", they can be client-side only (like "
      [:a {:href "http://hotframeworks.com/frameworks/gwt"} "GWT"]
      " ) or server-side only and qualify. Hotframeworks doesn't include JavaScript
      libraries like jQuery and MooTools though since these are typically
      used to enhance web applications that are built using another framework.
      Feel free to "
      [:a {:href "#suggestion"}
       "suggest we add a framework."]]]
    [:div#suggestion
     [:div#wufoo-z1oha8090rgmrix
      "\nFill out my "
      [:a
       {:href "https://bruzilla.wufoo.com/forms/z1oha8090rgmrix"}
       "online form"]
      ".\n"]
     [:script
      {:type "text/javascript"}
      "var z1oha8090rgmrix;(function(d, t) {\nvar s = d.createElement(t), options = {\n'userName':'bruzilla', \n'formHash':'z1oha8090rgmrix', \n'autoResize':true,\n'height':'437',\n'async':true,\n'host':'wufoo.com',\n'header':'show', \n'ssl':true};\ns.src = ('https:' == d.location.protocol ? 'https://' : 'http://') + 'wufoo.com/scripts/embed/form.js';\ns.onload = s.onreadystatechange = function() {\nvar rs = this.readyState; if (rs) if (rs != 'complete') if (rs != 'loaded') return;\ntry { z1oha8090rgmrix = new WufooForm();z1oha8090rgmrix.initialize(options);z1oha8090rgmrix.display(); } catch (e) {}};\nvar scr = d.getElementsByTagName(t)[0], par = scr.parentNode; par.insertBefore(s, scr);\n})(document, 'script');"]]]
   (format "var data = %s;
           Hotframeworks.graphAll(data);"
           (graph-data-json))))

(defroutes home-routes
  (GET "/" [] (home)))
