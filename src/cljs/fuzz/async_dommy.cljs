(ns fuzz.async-dommy
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :refer [timeout <!]]
            [dommy.core :as dommy :refer-macros [sel1]]))

(defn- original-snippet []
  (str "<div class='juxt-div'><h1>Killing in the name of <span id=\"bleh\"></span></h1></div>"))

(defn handler [_ _]
  (dommy/set-html! (sel1 :#container) (original-snippet))

  (go-loop [n 0]
    (<! (timeout 1000))
    (when-let [el (sel1 :#bleh)]
      (dommy/set-html! el (str n))
      (recur (inc n)))))
