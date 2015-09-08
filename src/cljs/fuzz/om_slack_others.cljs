(ns fuzz.om-slack-others
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [sablono.core :as html :refer-macros [html]]
            [om-tools.core :refer-macros [defcomponent]]
            [fuzz.slacky :as slacky]
            [om.core :as om :include-macros true]
            [fuzz.om-slacky-component :refer [OmSlacky]]))

(defonce app-state (atom {:slacky {:messages []}
                          :slacky-form {:message ""}}))

(defcomponent OmSlackyForm [{:keys [message] :as data} owner]
  (render [this]
    (html
     [:div.form
      [:div.mdl-textfield.mdl-js-textfield
       [:input#msg.mdl-textfield__input {:type :text :value message :on-change
                                         (fn [e]
                                           (om/update! data :message (.. e -target -value)))}]]
      [:button.mdl-button {:on-click
                           (fn [_]
                             (slacky/send! message)
                             (om/update! data :message ""))} "Send"]])))

(defcomponent OmSlackyCount [{:keys [messages] :as data} owner]
  (render [this]
    (html
     [:div.news-card.mdl-card.mdl-shadow--2dp
      [:div.mdl-card__title
       [:h2.mdl-card__title-text
        "Messages: "]]
      [:div.mdl-card__title
       [:h2.mdl-card__title-text
        (count messages)]]])))

(defcomponent OmRoot [data owner]
  (render [this]
    (html
     [:div.juxt-div.mdl-grid
      [:div.mdl-cell.mdl-cell--8-col
       (om/build OmSlackyForm (:slacky-form data))
       (om/build OmSlacky (:slacky data) {})]
      [:div.mdl-cell.mdl-cell--1-col
       (om/build OmSlackyCount (:slacky data) {})]])))

(defn handler [target opts]
  (om/root OmRoot app-state {:target target}))











(defonce sse-listener
  (let [es (new js/EventSource "/events")]
    (.addEventListener
     es "message"
     (fn [ev]
       (when-let [m (.-data ev)]
         (slacky/send! (str m)))))
    es))
