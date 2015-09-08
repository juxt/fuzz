(ns fuzz.om-slack-others
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [sablono.core :as html :refer-macros [html]]
            [om-tools.core :refer-macros [defcomponent]]
            [fuzz.slacky :as slacky]
            [om.core :as om :include-macros true]
            [fuzz.om-slacky-component :refer [OmSlacky]]))

(defonce app-state (atom {:slacky {:messages []}}))

(defcomponent OmSlackyCount [{:keys [messages] :as data} owner]
  (render [this]
    (html
     [:div.mdl-grid
      [:div.news-card.mdl-card.mdl-cell.mdl-shadow--2dp
       [:div.mdl-card__title
        [:h2.mdl-card__title-text
         "Messages: "]]
       [:div.mdl-card__title
        [:h2.mdl-card__title-text
         (count messages)]]]])))

(defcomponent OmRoot [data owner]
  (render [this]
    (html
     [:div.juxt-div.mdl-grid
      [:div.mdl-cell.mdl-cell--7-col
       (om/build OmSlacky (:slacky data) {})]
      [:div.mdl-cell.mdl-cell--5-col
       (om/build OmSlackyCount (:slacky data) {})]])))

(defn handler [target opts]
  (om/root OmRoot app-state {:target target}))
