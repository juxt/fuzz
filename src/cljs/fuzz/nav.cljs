(ns fuzz.nav
  (:require [sablono.core :as html :refer-macros [html]]
            [om-tools.core :refer-macros [defcomponent]]
            [om.core :as om :include-macros true]))

(defcomponent nav-items [{:keys [frags owner]}]
  (render [this]
    (html
     [:div
      (interpose " | "
       (for [[frag] frags]
         [:a {:href (str "/fuzz/" frag)} frag]))])))

(defn init [frags]
  (let [target (. js/document (getElementById "nav"))]
    (om/root nav-items (atom {:frags frags}) {:target target})))
