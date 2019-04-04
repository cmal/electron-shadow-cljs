(ns app.main.core
  (:require ["electron" :refer [app BrowserWindow crashReporter]]))

(def main-window (atom nil))

(defn init-browser []
  (reset! main-window (BrowserWindow.
                        (clj->js {:width 800
                                  :height 600})))
  ; Path is relative to the compiled js file (main.js in our case)
  (.loadURL @main-window (str "file://" js/__dirname "/public/index.html"))
  (.. @main-window -webContents (openDevTools))
  (.on @main-window "closed" #(reset! main-window nil)))

(defn maybe-create-window []
  (when-not @main-window
    (init-browser)))

(defn maybe-quit []
  (when-not (= js/process.platform "darwin")
    (.quit app)))

(defn main []
  ; CrashReporter can just be omitted
  #_(.start crashReporter
          (clj->js
            {:companyName "MyAwesomeCompany"
             :productName "MyAwesomeApp"
             :submitURL "https://example.com/submit-url"
             :autoSubmit false}))

  (.on app "ready" init-browser)
  (.on app "activate" maybe-create-window)
  (.on app "window-all-closed" maybe-quit))
