(ns ocr-client.utils
  (:require [ajax-lib.core :refer [ajax get-response]]
            [js-lib.core :as md]
            [framework-lib.core :as frm]
            [common-middle.request-urls :as rurls]
            [ocr-client.document.entity :as docent]
            [cljs.reader :as reader]))

(defn retrieve-documents-fn-success
  "Retrieving source documents successful"
  [xhr
   params-map]
  (let [response (get-response xhr)
        select-data (:data response)
        display-fn (:display-fn params-map)
        prepare-image-fn (:prepare-image-fn params-map)]
   (md/append-element
     ".content"
     (display-fn
       {:select-data select-data
        :prepare-image-fn prepare-image-fn}))
   (md/end-please-wait))
 )

(defn retrieve-documents-fn
  "Call server to return all source documents"
  [display-fn
   prepare-image-fn]
  (md/start-please-wait)
  (ajax
    {:url rurls/get-entities-url
     :success-fn retrieve-documents-fn-success
     :entity docent/query-documents-select-tag
     :display-fn display-fn
     :prepare-image-fn prepare-image-fn}))

(defn slider-value-fn
  "Update slider input value as it is on slider"
  [id]
  (let [slider-input-value (md/query-selector (str "#" id "-value"))
        slider (md/query-selector (str "#" id))
        slider-value (md/get-value slider)]
    (md/set-value
      slider-input-value
      slider-value))
 )

(defn slider-input-value-fn
  "Update slider value as it is in slider input value"
  [id]
  (let [slider-input-value (md/query-selector (str "#" id "-value"))
        slider (md/query-selector (str "#" id))
        slider-input-value (md/get-value slider-input-value)]
    (md/set-value
      slider
      slider-input-value))
 )

(defn differ-signs-from-images-fn
  "Separate images from signs"
  [images-of-signs]
  (let [images (atom [])
        signs (atom "")]
    (doseq [image-or-sign images-of-signs]
      (if (> (count image-or-sign)
             10)
        (do
          (swap!
            images
            conj
            image-or-sign)
          (swap!
            signs
            str
            "*"))
        (swap!
          signs
          str
          image-or-sign))
     )
    [@images @signs]))

(defn websocket-default-close
  "Default close of websocket"
  [event]
  (md/end-progress-bar)
  (let [response (reader/read-string (aget event "reason"))
        action (:action response)]
    (when (= action
             "rejected")
      (let [status (:status response)
            message (:message response)]
        (frm/popup-fn
          {:heading status
           :content message}))
     ))
 )

