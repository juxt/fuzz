(ns fuzz.dommy-hello
  (:require [dommy.core :as dommy :refer-macros [sel1]]))

(defn- snippet []
  "<div class='juxt-div'><h1>Killing in the name of</h1></div>")

(defn handler [container params]
  (dommy/set-html! (sel1 :#container) (snippet)))





;; ;; examples
;; (dommy/set-html! (sel1 :#container)
;;                    (apply str "<div>"
;;                           (concat
;;                            (for [n (range 10)]
;;                              (str "<div class='juxt-div'><h1>Killing in the name of hi" n "</h1></div>"))
;;                            ["</div>"])))
