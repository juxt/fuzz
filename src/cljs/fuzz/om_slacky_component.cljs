(ns fuzz.om-slacky-component
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [sablono.core :as html :refer-macros [html]]
            [om-tools.core :refer-macros [defcomponent]]
            [fuzz.slacky :as slacky]
            [om.core :as om :include-macros true]))

(defonce app-state (atom {:slacky {:messages []}}))

(defcomponent OmSlacky [{:keys [messages slacky-chan] :as data} owner]
  (will-mount [this]
    (when-not slacky-chan
      (let [slacky-msg-chan (slacky/poll-slacky)]
        (go-loop []
          (when-let [msg (<! slacky-msg-chan)]
            (om/transact! data :messages #(conj % msg)))
          (recur))
        (om/update! data :slacky-chan slacky-msg-chan))))

  (render [this]
    (html
     [:div
      (for [m (reverse messages)]
        [:p m])])))

(defcomponent OmRoot [data owner]
  (render [this]
    (html
     [:div.juxt-div
      (om/build OmSlacky (:slacky data) {})])))

(defn handler [target opts]
  (om/root OmRoot app-state {:target target}))
