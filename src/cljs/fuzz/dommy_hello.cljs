(ns fuzz.dommy-hello
  (:require [dommy.core :as dommy :refer-macros [sel1]]))

(defn handler [container params]
  (dommy/set-html! (sel1 :#container) "<div><h1>Killing in the name of</h1></div>"))
