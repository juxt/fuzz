(ns fuzz.dommy-hello2
  (:require [dommy.core :as dommy :refer-macros [sel1]]))

(defn- snippet []
  "<div class='juxt-div'><h1>Killing in the name of</h1></div>")

(defn handler [container params]
  (dommy/set-html! (sel1 :#container)
                   (str "<div>" (clojure.string/join "" (take 5 (repeat (snippet)))) "</div>")))
