(ns fuzz.slacky-dommy
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]
                   [hiccups.core :refer [html]])
  (:require [cljs.core.async :refer [timeout <!]]
            [cljs-http.client :as http]
            [hiccups.runtime]
            [dommy.core :as dommy :refer-macros [sel1]]))

(defn- original-snippet [s]
  (str "<div class='juxt-div' id=\"slack-messages\"><h1>" s "</h1></div>"))

(defn- chat-snippet [text]
  (html
   [:h2 text]))

(defn handler [_ _]
  (dommy/set-html! (sel1 :#container) (original-snippet "Waiting"))

  (go-loop [latest nil]
    (when-let [{:keys [messages]} (:body (<! (http/get "https://slack.com/api/channels.history"
                                                       {:with-credentials? false
                                                        :query-params {"token" "<INSERT HERE>"
                                                                       "channel" "C0522EZ9N"
                                                                       "oldest" (str latest)}})))]
      (doseq [{:keys [text] :as message} (sort-by :ts messages)]
        (dommy/prepend! (sel1 :#slack-messages)
                        (dommy/set-html! (dommy/create-element :p) text)))
      (<! (timeout 1100))
      (recur (or (apply max (map :ts messages)) latest)))))
