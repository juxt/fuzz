(ns fuzz.om-raw-slacky-component
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [sablono.core :as html :refer-macros [html]]
            [om-tools.core :refer-macros [defcomponent]]
            [fuzz.slacky :as slacky]
            [om.core :as om :include-macros true]))

(def app-state (atom {:slacky {:messages []}}))

(defn om-slacky
  [{:keys [messages] :as data} owner]
  (reify
    om/IWillMount
    (will-mount [this]
      (let [slacky-msg-chan (slacky/poll-slacky)]
        (go-loop []
          (when-let [msg (<! slacky-msg-chan)]
            (om/transact! data :messages #(conj % msg)))
          (recur))))

    om/IRender
    (render [this]
      (html
       [:div
        (for [m (reverse messages)]
          [:p m])]))))

(defn om-root
  [data owner]
  (reify
    om/IRender
    (render [this]
      (html
       [:div.juxt-div
        (om/build om-slacky (:slacky data) {})]))))

(defn handler [target opts]
  (om/root om-root app-state {:target target}))
