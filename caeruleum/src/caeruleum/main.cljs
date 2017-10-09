(ns caeruleum.main
  (:require
   [reagent.core :as r]))

(defn main [_]
  (r/create-class
   {:component-did-update
    #(let [title (-> js/document
                     (.getElementsByClassName "title")
                     (aget 0))]
       ;; component-did-updateは初回は呼ばれないが、初回以外でもtitleが存在しない場合を想定.
       (if (nil? title)
         ()
         (-> title
             .getBoundingClientRect
             .-left
             (js/scrollTo 0))))
    :reagent-render
    (fn [{:keys [file-name text]}]
      (if (nil? text)
        [:div.cover
         [:h1 {:style {:text-align "center"}} "Caeruleum"]
         [:p {:style {:text-align "center"}} "The small Aozora Bunko reader."]]
        [:div.book.container text]))}))
