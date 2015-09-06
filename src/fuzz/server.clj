(ns fuzz.server
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [modular.ring :refer [WebRequestHandler]]
            [bidi
             [bidi :as b]
             [ring :as br]]))

(defroutes routes
  (resources "/")

  #_(GET "/*" []
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

(defrecord RequestHandler []
  WebRequestHandler
  (request-handler [{:keys [postgres] :as this}]
    routes))

(defn new-handler []
  (->RequestHandler))
