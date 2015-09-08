(ns fuzz.system
  (:require [com.stuartsierra.component :refer [system-map system-using]]
            [modular.http-kit :refer [new-webserver]]
            [fuzz.server :refer [new-handler]]
            [fuzz.sse :refer [new-outgoing-events]]))

(defn new-system-map []
  (system-map
   :webserver (new-webserver :port 8080)
   :handler (new-handler)
   :sse (new-outgoing-events)))

(defn new-dependency-map
  []
  {:webserver {:request-handler :handler}
   :handler {:sse :sse}})

(defn new-system []
  (system-using (new-system-map)
                (new-dependency-map)))
