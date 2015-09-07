(defproject fuzz "0.1.0-SNAPSHOT"
  :description "Example ClojureScript Project, code-name fuzz"
  :url "https://github.com/juxt/fuzz"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48" :scope "provided"]
                 [com.stuartsierra/component "0.2.2"]

                 [compojure "1.4.0"]
                 [org.webjars.bower/material-design-lite "1.0.4"]

                 [bidi "1.20.3"]

                 [org.omcljs/om "0.9.0"]
                 [sablono "0.3.4" :exclusions [cljsjs/react]]

                 [juxt.modular/http-kit "0.5.4"]

                 ;; Painting
                 [prismatic/dommy "1.1.0"]]

  :resource-paths ["resources"]
  :source-paths ["src/clj"]

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :figwheel true
                             :compiler {:output-dir    "resources/public/js"
                                        :output-to     "resources/public/js/app.js"
                                        ;; :source-map    "resources/public/js/out.js.map"
                                        ;;:preamble      ["react/react.min.js"]
                                        :asset-path "/js"
                                        :main           "fuzz.app"
                                        :optimizations :none
                                        :pretty-print  true}}}}

  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.10"]]
                   :source-paths ["dev"]
                   :plugins [[lein-cljsbuild "1.1.0"]
                             [lein-figwheel "0.3.9" :exclusions [org.clojure/clojure
                                                                  org.apache.httpcomponents/httpclient
                                                                  org.apache.httpcomponents/httpcore
                                                                  commons-codec
                                                                 org.codehaus.plexus/plexus-utils]
                              ]]
                   :figwheel {:css-dirs ["resources/public/css"]}}})
