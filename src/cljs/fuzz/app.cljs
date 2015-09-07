(ns fuzz.app
  (:require [bidi.bidi :as bidi]
            [fuzz.welcome :as welcome]
            [fuzz.dommy-hello :as dommy-hello]
            [fuzz.dommy-hello2 :as dommy-hello2]
            [fuzz.hiccups :as hiccups]))

(enable-console-print!)

(defn pages [] ["#" [["hello" welcome/handler]
                     ["hello-dommy" dommy-hello/handler]
                     ["hello-dommy2" dommy-hello2/handler]
                     ["hiccups" hiccups/handler]]])

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
