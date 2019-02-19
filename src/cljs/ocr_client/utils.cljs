(ns ocr-client.utils
  (:require [ajax-lib.core :refer [ajax get-response]]
            [js-lib.core :as md]
            [htmlcss-lib.core :refer [gen div input img textarea
                                      label]]
            [framework-lib.core :as frm]
            [language-lib.core :refer [get-label]]
            [common-middle.request-urls :as rurls]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [ocr-client.document.entity :as docent]
            [ocr-middle.functionalities :as omfns]
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
  (let [response (reader/read-string
                   (.-reason
                     event))
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

(defn gallery-fn
  "Generate HTML gallery"
  [images
   evt-fn
   save-sign-fn]
  (gen
    (let [image-el (fn [id
                        src
                        img-style]
                     (img
                       ""
                       {:id id
                        :src src
                        :style img-style}))
          image-elements (atom [])
          itr (atom 0)]
     (doseq [image images]
       (let [img-style (if (= @itr
                              0)
                         {:display "inline"}
                         {:display "none"})]
         (swap!
           image-elements
           conj
           (image-el
            (str "sign" @itr)
            image
            img-style))
         (swap! itr inc))
      )
     [(input
        ""
        {:id "btnLeft"
         :type "button"
         :class "btn"
         :value "<-"}
        {:onclick {:evt-fn evt-fn
                   :evt-p -}})
      (div
        [(div
           @image-elements
           {:id "divImages"})
         (input
           ""
           {:id "signValue"
            :type "text"})
         (when (contains?
                 @allowed-actions
                 omfns/save-sign)
           (input
             ""
             {:type "button"
              :value (get-label 1019)
              :class "btn"}
             {:onclick {:evt-fn save-sign-fn}}))]
        {:class "divImagesParent"})
      (input
        ""
        {:id "btnRight"
         :type "button"
         :class "btn"
         :value "->"}
        {:onclick {:evt-fn evt-fn
                   :evt-p +}})])
   ))

(defn textarea-fn
  "Generate textarea HTML element"
  [text]
  (gen
    (textarea
      text
      {:readonly "true"}))
 )

(defn btn-fn
  "Generate button HTML element"
  [{evt-fn :evt-fn
    evt-p :evt-p
    value :value
    id :id}]
  (gen
    (input
      ""
      (let [attrs {:value value
                   :type "button"
                   :class "btn"}
            attrs (if id
                    (assoc
                      attrs
                      :id
                      id)
                    attrs)]
        attrs)
      {:onclick {:evt-fn evt-fn
                 :evt-p evt-p}}))
 )

(defn slider-fn
  "Generate slider HTML element"
  [id
   & [attrs
      evts
      input-evts
      label-text]]
  (gen
    (label
      [label-text
       (div
         [(input
            ""
            (conj
              {:id id
               :type "range"
               :min "-128"
               :max "128"
               :value "0"}
              attrs)
            evts)
          (input
            ""
            (conj
              {:id (str id "-value")
               :value "0"}
              attrs)
            input-evts)]
         {:class "sliderParent"})]
     ))
 )

(defn image-fn
  "Generate img HTML element"
  [src
   & [style-attrs]]
  (gen
    [(img
       ""
       {:id "preparedImage"
        :style (conj
                 {:width "100%"}
                 style-attrs)
        :src src})
     (input
       ""
       {:id "hiddenPreparedImage"
        :type "hidden"
        :value src})])
 )

