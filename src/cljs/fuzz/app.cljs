(ns fuzz.app
  (:require [bidi.bidi :as bidi]
            [fuzz.welcome :as welcome]
            [fuzz.dommy-hello :as dommy-hello]
            [fuzz.dommy-hello2 :as dommy-hello2]
            [fuzz.hiccups :as hiccups]
            [fuzz.mustache :as mustache]
            [fuzz.async-dommy :as async-dommy]
            [fuzz.slacky-dommy :as slacky-dommy]
            [fuzz.nav]))

(enable-console-print!)

(def frags [["hello" welcome/handler]
            ["hello-dommy" dommy-hello/handler]
            ["hello-dommy2" dommy-hello2/handler]
            ["hiccups" hiccups/handler]
            ["mustache" mustache/handler]
            ["async-dommy" async-dommy/handler]
            ["slacky-dommy" slacky-dommy/handler]])

(defn pages [] ["/fuzz/" frags])

(defn route []
  (let [target-container (. js/document (getElementById "roc-container"))
        location (not-empty (-> js/document .-location .-pathname))]
    (if-let [{:keys [handler route-params]} (and location (bidi/match-route (pages) location))]
      (handler target-container route-params)
      (js/alert "Route not recognised"))))

(fuzz.nav/init frags)
(route)
