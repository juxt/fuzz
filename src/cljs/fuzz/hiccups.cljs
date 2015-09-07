(ns fuzz.hiccups
  (:require-macros [hiccups.core :as hiccups :refer [html]])
  (:require [dommy.core :as dommy :refer-macros [sel1]]))

(defn- snippet []
  (html
   [:div.juxt-div
    [:h1 "Hello from Hiccup"]]))

(defn handler [target opts]
  (dommy/set-html! (sel1 :#container) (snippet)))
