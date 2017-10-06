(ns caeruleum.action
  (:require [caeruleum.state :as state]
            [clojure.string :as s]))

(defonce iconv (js/require "iconv-lite"))

(defn- prepare-text [row-text]
  (->> row-text
       s/split-lines
       (map-indexed (fn [idx txt] [:p {:key idx} txt]))))

(defn load-file [file-name row-text]
  (let
    [buf (js/Buffer. row-text "binary")
     plain-text (.decode iconv buf "Shift_JIS")
     text (prepare-text plain-text)]
    (reset! state/state
            {:file-name file-name
             :row-text row-text
             :text text})))
