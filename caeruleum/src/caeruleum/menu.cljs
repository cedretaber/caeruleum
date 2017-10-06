(ns caeruleum.menu)

(defonce remote (.-remote (js/require "electron")))
(defonce app (.-app remote))
(defonce Menu (.-Menu remote))

(defonce dialog (.-dialog remote))
(defonce browser-window (.-BrowserWindow remote))

(defonce fs (js/require "fs"))

(defn load-file [callback]
  (.showOpenDialog
   dialog
   (.getFocusedWindow browser-window)
   (clj->js {:properties ["openFile"]})
   (fn [files]
     (if (or (= files js/undefined) (empty? files))
       ()
       (let [file (first files)]
         (.readFile
          fs
          file
          (fn [error data]
            (if error
              ()
              (callback file data)))))))))



(defn add-file-menu [load-text]
  (let [
        make-role (fn [role] (if (= role "separator") {:type role} {:role role}))
        file {:label "File" :submenu [{:label "Open..." :click #(load-file load-text)}]}
        view-menu (map make-role ["reload" "forcereload" "toggledevtools" "separator" "resetzoom" "zoomin" "zoomout" "separator" "togglefullscreen"])
        view {:label "View" :submenu view-menu}
        help {:role "help" :submenu []}
        ]
    (if (not= (.-platform js/process) "darwin")
      (let [
            edit-menu (map make-role ["undo" "redo" "separator" "cut" "copy" "paste" "pasteandmatchstyle" "delete" "selectall"])
            edit {:label "Edit" :submenu edit-menu}
            window-menu (map make-role ["minimize" "close"])
            window {:label "window" :submenu window-menu}
            template [file edit view window help]
            menu (.buildFromTemplate Menu (clj->js template))
            ]
        (.setApplicationMenu Menu menu))
      (let [
            app-menu (map make-role ["about" "separator" "services" "separator" "hide" "hideothers" "unhide" "separator" "quit"])
            app {:label (.getName app) :submenu app-menu}
            edit-menu (map make-role ["undo" "redo" "separator" "cut" "copy" "paste" "pasteandmatchstyle" "delete" "selectall"])
            added-edit-menu [{:type "separator"} {:label "Speech" :submenu [{:role "startspeaking"} {:role "stopspeaking"}]}]
            edit {:label "Edit" :submenu (concat edit-menu added-edit-menu)}
            window-menu (map make-role ["close" "minimize" "zoom" "separator" "front"])
            window {:label "window" :submenu window-menu}
            template [app file edit view window help]
            menu (.buildFromTemplate Menu (clj->js template))
            ]
        (.setApplicationMenu Menu menu)))))
