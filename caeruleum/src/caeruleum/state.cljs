(ns caeruleum.state
  (:require [reagent.core :as r]))

(defonce state
  (r/atom
   {:file-name ""
    :row-text nil
    :text nil}))
