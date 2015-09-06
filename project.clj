(defproject fuzz "0.1.0-SNAPSHOT"
  :description "Example ClojureScript Project, code-name fuzz"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48" :scope "provided"]

                 [org.webjars.bower/material-design-lite "1.0.4"]

                 [bidi "1.20.3"]

                 [org.omcljs/om "0.9.0"]
                 [sablono "0.3.4" :exclusions [cljsjs/react]]

                 [juxt.modular/http-kit "0.5.4"]]

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :compiler {:output-to     "resources/public/js/app.js"
                                        :output-dir    "resources/public/js/out"
                                        :source-map    "resources/public/js/out.js.map"
                                        :preamble      ["react/react.min.js"]
                                        :optimizations :none
                                        :pretty-print  true}}}}

  :profiles {:dev {:plugins [[lein-figwheel "0.3.9" ;; :exclusions [org.clojure/clojure
                                                    ;;               org.apache.httpcomponents/httpclient
                                                    ;;               org.apache.httpcomponents/httpcore
                                                    ;;               commons-codec
                                                    ;;              org.codehaus.plexus/plexus-utils]
                              ]]
                   :figwheel {:css-dirs ["resources/public/css"]}}})
