(ns dev
  (:require [fuzz.system]
            [com.stuartsierra.component :as component]
            [clojure.tools.namespace.repl :refer (refresh refresh-all)]))

(def system nil)

(defn init
  "Constructs the current development system."
  ([]
   (init (fuzz.system/new-system)))
  ([system]
   (alter-var-root #'system
                   (constantly system))))

(defn start
  "Starts the current development system."
  []
  (alter-var-root
   #'system
   component/start))

(defn stop
  "Shuts down and destroys the current development system."
  []
  (alter-var-root #'system
                  (fn [s] (when s (component/stop s)))))

(defn go
  "Initializes the current development system and starts it running."
  []
  (init)
  (start)
  :ok)

(defn reset []
  (stop)
  (refresh :after 'dev/go))
