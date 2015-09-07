(ns fuzz.mustache
  (:require [dommy.core :as dommy :refer-macros [sel1]]
            [cljsjs.mustache]))

(defn- snippet []
  (.render js/Mustache "<div class=\"juxt-div\"><h2>HELLO {{name}}</h2></div>" #js {"name" "bob"}))

(defn handler [target opts]
  (dommy/set-html! (sel1 :#container) (snippet)))
