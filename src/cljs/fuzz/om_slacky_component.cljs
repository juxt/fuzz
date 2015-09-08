(ns fuzz.om-slacky-component
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [sablono.core :as html :refer-macros [html]]
            [om-tools.core :refer-macros [defcomponent]]
            [fuzz.slacky :as slacky]
            [om.core :as om :include-macros true]))

(def app-state (atom {:slacky {:messages []}}))

(defcomponent OmSlacky [{:keys [messages] :as data} owner]
  (will-mount [this]
    (let [slacky-msg-chan (slacky/poll-slacky)]
      (go-loop []
        (when-let [msg (<! slacky-msg-chan)]
          (om/transact! data :messages #(conj % msg)))
        (recur))))

  (render [this]
    (html
     [:div.juxt-div
      (for [m (reverse messages)]
        [:p m])])))

(defcomponent OmRoot [data owner]
  (render [this]
    (html
     [:div
      (om/build OmSlacky (:slacky data) {})])))

(defn handler [target opts]
  (om/root OmRoot app-state {:target target}))
