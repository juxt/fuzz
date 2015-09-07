(ns fuzz.server
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [modular.ring :refer [WebRequestHandler]]
            [stencil.core :refer [render-file]]
            [environ.core :refer [env]]
            [bidi
             [bidi :as b]
             [ring :as br]]))

(defroutes routes
  (GET "/" []
       {:status 302
        :headers {"Location" "/index.html#hello"}})

  (GET "/index.html" []
       (render-file "public/index.html" {:slack-token (env :slack-token)}))

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
