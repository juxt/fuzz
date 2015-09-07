(ns fuzz.server
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [modular.ring :refer [WebRequestHandler]]
            [bidi
             [bidi :as b]
             [ring :as br]]))

(defroutes routes
  (resources "/")

  (GET "/static/*" []
       (br/make-handler
        ["/static/"
         [["mdl" (br/resources {:prefix "META-INF/resources/webjars/material-design-lite/1.0.4"})]]])))

(defrecord RequestHandler []
  WebRequestHandler
  (request-handler [{:keys [postgres] :as this}]
    routes))

(defn new-handler []
  (->RequestHandler))
