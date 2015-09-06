(ns fuzz.server
  (:require [clojure.java.io :as io]
            [sm-chest-demo.dev :refer [is-dev? inject-devmode-html browser-repl start-figwheel]]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [net.cgrand.enlive-html :refer [deftemplate]]
            [net.cgrand.reload :refer [auto-reload]]
            [ring.middleware.reload :as reload]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [environ.core :refer [env]]
            [bidi
             [bidi :as b]
             [ring :as br]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defroutes routes
  (GET "/*" []
       (br/make-handler
        ["/"
         [[(b/alts "index.html" "") (fn [req] (found "/platform/index.html#home"))]
          ;; Global cljs source base
          ["js" (br/resources {:prefix "js"})]
          ["platform" [["/css" (br/resources-maybe {:prefix "css"})]

                       ;; Only bring in resources from the platform subdirectory
                       ;; TODO better explain what the point of this is
                       ["" (br/resources {:prefix "platform"})]]]

          ["events" (fn [{:keys [outgoing-events] :as request}]
                      (outgoing-events/handle-sse outgoing-events request))]]])))
