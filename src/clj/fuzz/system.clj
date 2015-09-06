(ns fuzz.system
  (:require [com.stuartsierra.component :refer [system-map system-using]]
            [modular.http-kit :refer [new-webserver]]
            [fuzz.server :refer [new-handler]]))

(defn new-system-map []
  (system-map
   :webserver (new-webserver :port 8080)
   :handler (new-handler)))

(defn new-dependency-map
  []
  {:webserver {:request-handler :handler}})

(defn new-system []
  (system-using (new-system-map)
                (new-dependency-map)))
