; Copyright (c) 2020 <>

; Permission is hereby granted, free of charge, to any person obtaining a copy
; of this software and associated documentation files (the "Software"), to deal
; in the Software without restriction, including without limitation the rights
; to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
; copies of the Software, and to permit persons to whom the Software is
; furnished to do so, subject to the following conditions:

; The above copyright notice and this permission notice shall be included in all
; copies or substantial portions of the Software.

; THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
; IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
; FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
; AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
; LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
; OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
; SOFTWARE.

(ns de.trema02.sample-gui.core
  (:gen-class)
  (:require [de.trema02.jfx-wrapper [core :as wr]])
  (:import [javafx.stage Modality]
           [javafx.application Platform]))

(declare config config-popup close-popup)

(def app (atom nil))
(def app-root (atom nil))
(def popup-root (atom nil))
(def popup-stage (atom nil))

(defn app-data []
  {:config-fn config
   :title "Sample GUI"
   :mk-root-fn nil
   :css-name "/resources/app.css"
   :x-size 100
   :y-size 100
   :image-name "/resources/app.png"
   :fxml-name "/resources/app.fxml"
   :end-fn nil
   :debug true})

(defn popup-data []
  {:config-fn config-popup
   :title "Sample popup"
   :mk-root-fn nil
   :css-name "/resources/app.css"
   :x-size 100
   :y-size 100
   :image-name "/resources/app.png"
   :fxml-name "/resources/popup.fxml"
   :end-fn (wr/event-handler close-popup)
   :debug false})

(defn popup [cfg data]
  (wr/set-app-data @app (assoc data :config-fn cfg))
  (Platform/runLater #(let [stage (wr/start-stage)]
                        (reset! popup-stage stage))))

(defn app-button-cb [_]
  (popup config-popup (popup-data)))

(defn close-popup [stage]
  (.close @popup-stage)
  (reset! popup-stage nil))

(defn popup-button-cb [_]
  (when @popup-stage 
    (close-popup @popup-stage)
    (reset! popup-stage nil)))

(defn config [root mc]
  (reset! app-root root)
  (reset! app @wr/gui)
  (let [button (.-button mc)
        button-cb (wr/event-handler app-button-cb)]
    (.setOnAction button button-cb)))

(defn config-popup [root mc]
  (reset! popup-root root)
  (.initModality root Modality/WINDOW_MODAL)
  (let [button (.-button mc)
        button-cb (wr/event-handler popup-button-cb)]
    (.setOnAction button button-cb)))

(defn -main [& _]
  (wr/setup (app-data)))

(comment
  (-main)
  ;
  )