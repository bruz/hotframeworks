(ns hotframeworks.views.main
  (:require [clojure.data.json :as json]
            [compojure.route :as route]
            [hotframeworks.views.layout :as layout]
            [hotframeworks.models.db :as db]
            [hotframeworks.models.graphs :as graphs]
            [hotframeworks.models.framework :as framework]))

(defn- framework-link [framework]
  (str "/frameworks/" (:url_identifier framework)))

(defn- mini-ranking [frameworks]
  [:table {:class "table table-striped"}
   [:thead
    [:tr
     [:th "Framework"]
     [:th.score "Score"]]]
   [:tbody
    (map (fn [framework]
           [:tr
            [:td [:a {:href (framework-link framework)} (:name framework)]]
            [:td.score (:latest_score framework)]])
         frameworks)]])

(defn- full-rankings []
  (let [frameworks (framework/latest-scores)]
    [:table {:class "table table-striped"}
     [:thead
      [:tr
       [:th "Framework"]
       [:th.score "Github Score"]
       [:th.score "Stack Overflow Score"]
       [:th.score "Overall Score"]]]
     [:tbody
      (map (fn [framework]
             [:tr
              [:td.score [:a {:href (framework-link framework)}(:name framework)]]
              [:td.score (:github framework)]
              [:td.score (:stackoverflow framework)]
              [:td.score (:combined framework)]])
           frameworks)]]))

(defn- top-data-json []
  (json/write-str
   (graphs/most-popular 10 30)))

(defn- language-data-json [language]
  (json/write-str
   (graphs/for-language language 30)))

(defn- framework-data-json [framework]
  (json/write-str
   (graphs/for-framework framework 30)))

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
      (full-rankings)]]]
   (format "var data = %s;
           Hotframeworks.graph(data);"
           (top-data-json))))

(defn language [identifier]
  (let [language (db/language-for-url-identifier identifier)]
    (if language
      (layout/common
       [:div.row
        [:h1 (:name language)]
        [:div.col-md-4
         (mini-ranking (db/frameworks-for-language language))]
        [:div.col-md-6
         [:div#graph]]
        [:div.col-md-2
         [:div#legend]]]
       (format "var data = %s;
               Hotframeworks.graph(data);"
               (language-data-json language)))
      (route/not-found "Not Found"))))

(defn framework [identifier]
  (let [framework (db/framework-for-url-identifier identifier)
        github (framework/latest-score-for-framework framework "github")
        stackoverflow (framework/latest-score-for-framework framework "stackoverflow")
        combined (framework/latest-score-for-framework framework "combined")
        language (db/language-for-id (:language_id framework))
        language-link (str "/languages/" (:url_identifier language))
        github-link (str "https://github.com/" (:github_owner framework) "/" (:github_repo framework))
        stackoverflow-link (str "http://stackoverflow.com/questions/tagged/" (:stackoverflow_tag framework))]
    (if framework
      (layout/common
       [:div#framework.row
        [:h1 (:name framework)]
        [:br]
        [:div.col-md-6.
         [:div.col-md-4
          [:div.panel.panel-primary
           [:div.panel-heading "GitHub"]
           [:div.panel-body (or github "N/A")]]]
         [:div.col-md-4
          [:div.panel.panel-primary
           [:div.panel-heading "Stack Overflow"]
           [:div.panel-body (or stackoverflow "N/A")]]]
         [:div.col-md-4
          [:div.panel.panel-primary
           [:div.panel-heading "Combined"]
           [:div.panel-body (or combined "N/A")]]]
         (when (:description framework)
           [:p "Description: " (:description framework)])
         [:p "Language: "
          [:a {:href language-link} (:name language)]]
         [:p "Framework Link: "
          [:a {:href (:site_url framework)} (:site_url framework)]]
         (when (:github_repo framework)
           [:p
            "GitHub Link: "
            [:a {:href github-link} github-link]])
         (when (:stackoverflow_tag framework)
           [:p
            "Stack Overflow Questions: "
            [:a {:href stackoverflow-link} stackoverflow-link]])]
        [:div.col-md-4
         [:div#graph]]
        [:div.col-md-2
         [:div#legend]]]
       (format "var data = %s;
               Hotframeworks.graph(data);"
               (framework-data-json framework)))
      (route/not-found "Not Found"))))

(defn faq []
  (layout/common
   [:div
    [:div#faq
     [:h1 "Frequently Asked Questions"]
     [:h3 "How are the different frameworks scored?"]
     [:p
      "Each framework is scored by two separate measures, and these are
      simply averaged. The two measures are:"]
     [:ol
      [:li
       "GitHub score: Based on the number of stars the git repository for a
       framework has on GitHub. Since Hotframeworks can't
       measure this for frameworks not on GitHub, you'll see 'N/A' for those."]
      [:li
       "Stack Overflow score: Based on the number of questions on Stack Overflow
       that are tagged with the name of the framework. Some frameworks don't have an
       unambiguous Stack Overflow tag, and have an 'N/A'."]]
     [:p
      "Since these two measures of popularity are on different scales the final
      scores normalized to a scale of 0-100. The scores are on a log scale since
      the measures cover such a large
      range, so for instance a framework with a score of 90 for Stack Overflow
      may have thousands of questions while a framework with a score of 10-20 might
      just have a handful."]
     [:h3 " Why does my favorite framework have such a low score? "]
     [:p
      " Since scores are simply calculated as an average of the two measures
      described in the previous question, you can investigate the individual scores
      as compared to other frameworks to see why your framework places where it
      does. With Github scores for instance, you can look at how many stars
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
   ""))
