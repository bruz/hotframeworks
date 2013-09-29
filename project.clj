(defproject hotframeworks "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://hotframeworks.com"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.3"]
                 [ring-server "0.2.8"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [postgresql "9.1-901.jdbc4"]
                 [lobos "1.0.0-SNAPSHOT"]
                 [korma "0.3.0-RC5"]
                 [tentacles "0.2.6"]
                 [org.clojars.bruz/stacktraces "0.1.0"]
                 [clj-amazon-awis "0.1.3"]
                 [org.clojure/data.zip "0.1.1"]
                 [org.clojure/data.xml "0.0.6"]]
  :plugins [[lein-ring "0.8.5"]]
  :profiles {:dev {:dependencies [[midje "1.5.1"]
                                  [lein-localrepo "0.3"]
                                  [lein-marginalia "0.7.1"]
                                  [ring-mock "0.1.3"]
                                  [ring/ring-devel "1.1.8"]]
                   :plugins [[lein-midje "3.1.1"]]}
             :production
             {:ring
              {:open-browser? false
               :stacktraces? false
               :auto-reload? false}}}
  :ring {:handler hotframeworks.handler/war-handler
         :init hotframeworks.handler/init
         :destroy hotframeworks.handler/destroy})
