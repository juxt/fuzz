(ns fuzz.slacky
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]
                   [hiccups.core :refer [html]])
  (:require [cljs.core.async :refer [timeout <! chan]]
            [cljs-http.client :as http]))

(defn poll-slacky
  "Poll slack"
  []
  (let [msg-chan (chan)]
    (go-loop [latest nil]
      (when-let [{:keys [messages]} (:body (<! (http/get "https://slack.com/api/channels.history"
                                                         {:with-credentials? false
                                                          :query-params {:token js/slackToken
                                                                         :channel "C0522EZ9N"
                                                                         :oldest (str latest)}})))]
        (doseq [{:keys [text] :as message} (sort-by :ts messages)]
          (<! (timeout 50))
          (>! msg-chan text))
        (<! (timeout 1100))
        (recur (or (apply max (map :ts messages)) latest))))
    msg-chan))

(defn send!
  "Send to slack"
  [msg]
  (go
    (when-let [{:keys [status] :as resp} (<! (http/post "https://slack.com/api/chat.postMessage"
                                                        {:with-credentials? false
                                                         :query-params {:token js/slackToken
                                                                        :channel "C0522EZ9N"
                                                                        :as_user true
                                                                        :text msg}}))]
      (if (= 200 status)
        (println "Success!")
        (println "Fail" resp)))))
