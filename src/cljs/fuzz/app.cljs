(ns fuzz.app
  (:require [bidi.bidi :as bidi]
            [fuzz.welcome :as welcome]
            [fuzz.dommy-hello :as dommy-hello]))

(enable-console-print!)

(defn pages [] ["#" [["hello" welcome/handler]
                     ["hello-dommy" dommy-hello/handler]]])

(defn route []
  (let [target-container (. js/document (getElementById "roc-container"))
        location (not-empty (-> js/document .-location .-hash))]
    (if-let [{:keys [handler route-params]} (and location (bidi/match-route (pages) location))]
      (handler target-container route-params)
      (js/alert "Route not recognised"))))

(route)


;; TODO hiccups
;; TODO dommy
;; Core Async
;; React
;; Fighwheel
