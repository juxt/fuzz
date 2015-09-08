(ns fuzz.app
  (:require [bidi.bidi :as bidi]
            [fuzz.welcome :as welcome]
            [fuzz.dommy-hello :as dommy-hello]
            [fuzz.dommy-hello2 :as dommy-hello2]
            [fuzz.hiccups :as hiccups]
            [fuzz.mustache :as mustache]
            [fuzz.async-dommy :as async-dommy]
            [fuzz.slacky-dommy :as slacky-dommy]
            [fuzz.om-slacky :as om-slacky]
            [fuzz.om-slacky-component :as om-slacky-component]
            [fuzz.om-raw-slacky-component :as om-raw-slacky-component]
            [fuzz.om-simple :as om-simple]
            [fuzz.om-slack-others :as om-slacky-others]
            [fuzz.nav]))

(enable-console-print!)

(def frags [["hello" welcome/handler]
            ["hello-dommy" dommy-hello/handler]
            ["hello-dommy2" dommy-hello2/handler]
            ["hiccups" hiccups/handler]
            ["mustache" mustache/handler]
            ["async-dommy" async-dommy/handler]
            ["slacky-dommy" slacky-dommy/handler]
            ["slacky-om" om-slacky/handler]
            ["slacky-om-component" om-slacky-component/handler]
            ["slacky-raw-om-component" om-raw-slacky-component/handler]
            ["om-simple" om-simple/handler]
            ["om-slacky-all" om-slacky-others/handler]])

(defn pages [] ["/fuzz/" frags])

(defn route []
  (let [target-container (. js/document (getElementById "container"))
        location (not-empty (-> js/document .-location .-pathname))]

    (if-let [{:keys [handler route-params]} (and location (bidi/match-route (pages) location))]
      (handler target-container route-params)
      (js/alert "Route not recognised"))))

(fuzz.nav/init frags)
(route)

;; Todo, defo so SSE (or websockets, using Chord?) Should be easy enough, push from the repl
;; We need a form, perhaps a form to add a name, that goes to a list, comes back through a websocket
