(ns fuzz.om-simple
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [sablono.core :as html :refer-macros [html]]
            [om-tools.core :refer-macros [defcomponent]]
            [fuzz.slacky :as slacky]
            [om.core :as om :include-macros true]))

(defonce app-state (atom {}))

(defcomponent om-root [data owner]
  (init-state [this]
    {:a (rand-int 10000)})

  (will-mount [this]
    (println "Mounting " (om/get-state owner :a)))

  (will-unmount [this]
    (println "Unmounting " (om/get-state owner :a)))

  (render-state [this {:keys [a]}]
    (println "Rendering")
    (html
     [:div
      [:h2 "Hi " a]])))

(defn handler [target opts]
  (om/root om-root app-state {:target target}))
