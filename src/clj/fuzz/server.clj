(ns fuzz.server
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [modular.ring :refer [WebRequestHandler]]
            [stencil.core :refer [render-file]]
            [fuzz.sse :as sse]
            [stencil.loader]
            [clojure.core.cache]
            [environ.core :refer [env]]
            [bidi
             [bidi :as b]
             [ring :as br]]))

(stencil.loader/set-cache (clojure.core.cache/ttl-cache-factory {} :ttl 0))

(defroutes routes
  (GET "/" []
       {:status 302
        :headers {"Location" "/fuzz/hello"}})

  (GET "/fuzz/*" []
       (render-file "public/index.html" {:slack-token (env :slack-token)}))

  (resources "/")

  (GET "/static/*" []
       (br/make-handler
        ["/static/"
         [["mdl" (br/resources {:prefix "META-INF/resources/webjars/material-design-lite/1.0.4"})]]]))

  (GET "/events" {:keys [sse] :as request}
       (sse/handle-sse sse request)))

(defn- wrap-system-components [handler deps]
  (fn [req]
    (handler (merge req deps))))

(defrecord RequestHandler []
  WebRequestHandler
  (request-handler [{:keys [postgres] :as this}]
    (-> routes (wrap-system-components this))))

(defn new-handler []
  (->RequestHandler))
