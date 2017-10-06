(ns caeruleum.main
  (:require
   [reagent.core :as r]))

(defn main [{:keys [file-name text]}]
  (if (nil? text)
    [:div
     [:h1 {:style {:text-align "center"}} "Caeruleum"]
     [:p {:style {:text-align "center"}} "The small Aozora Bunko reader."]]
    [:div text]))
