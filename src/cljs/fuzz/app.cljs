(ns fuzz.app
  (:require [bidi.bidi :as bidi]
            [fuzz.welcome :as welcome]))

(enable-console-print!)

(defn pages [] ["#" [["hi" welcome/handler]]])

(defn route []
  (when-let [location (not-empty (-> js/document .-location .-hash))]
    (let [{:keys [handler route-params]} (bidi/match-route (pages) location)]
      (handler route-params))))

(route)


;; TODO hiccups
;; TODO dommy
;; Core Async
;; React
;; Fighwheel
