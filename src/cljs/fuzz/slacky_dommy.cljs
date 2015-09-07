(ns fuzz.slacky-dommy
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]
                   [hiccups.core :refer [html]])
  (:require [cljs.core.async :refer [timeout <!]]
            [cljs-http.client :as http]
            [hiccups.runtime]
            [fuzz.slacky :as slacky]
            [dommy.core :as dommy :refer-macros [sel1]]))

(defn- original-snippet [s]
  (str "<div class='juxt-div' id=\"slack-messages\"><h1>" s "</h1></div>"))

(defn- chat-snippet [text]
  (html
   [:h2 text]))

(defn handler [_ _]
  (dommy/set-html! (sel1 :#container) (original-snippet "Waiting"))

  (let [slacky-msg-chan (slacky/poll-slacky)]
    (go-loop []
      (when-let [msg (<! slacky-msg-chan)]
        (dommy/prepend! (sel1 :#slack-messages)
                        (dommy/set-html! (dommy/create-element :p) msg))
        (recur)))))
