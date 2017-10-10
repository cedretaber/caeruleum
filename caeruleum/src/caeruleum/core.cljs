(ns caeruleum.core
  (:require [reagent.core :as r]
            [caeruleum.main :as main]
            [caeruleum.menu :as menu]
            [caeruleum.state :as state]
            [caeruleum.action :as action]))

(defn root []
  [main/main @state/state])

(defn mount-root []
  (menu/add-file-menu action/load-file)
  (r/render [root] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
