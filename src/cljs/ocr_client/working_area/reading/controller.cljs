(ns ocr-client.working-area.reading.controller
 (:require [ajax-lib.core :refer [ajax get-response]]
           [js-lib.core :as md]
           [ocr-client.utils :as utils]
           [ocr-client.request-urls :as rurls]
           [ocr-client.document.entity :as docent]
           [ocr-client.working-area.html :as wah]
           [ocr-client.working-area.reading.html :as rh]
           [cljs.reader :as reader]))

(defn process-images-fn-success
 "Successful image processing"
 [xhr]
 (let [response (get-response xhr)
       [new-image-src
        learned-image-src] (:srcs response)
       new-image-el (md/query-selector "#imgimageSource")
       learned-image-el (md/query-selector "#preparedImage")]
  (md/set-src
    new-image-el
    new-image-src)
  (md/set-src
    learned-image-el
    learned-image-src)
  (md/end-progress-bar))
 )

(defn process-images-fn
 "Process image with light and contrast parameters"
 []
 (md/start-progress-bar
   rurls/process-images-url)
 (let [light-slider (md/query-selector
                         "#lightSlider")
       light-slider-value (md/get-value light-slider)
       contrast-slider (md/query-selector
                         "#contrastSlider")
       contrast-slider-value (md/get-value contrast-slider)
       image (md/query-selector "#hiddenimageSource")
       new-image-src (md/get-value image)
       image (md/query-selector "#hiddenPreparedImage")
       learned-image-src (md/get-value image)]
  (ajax
   {:url rurls/process-images-url
    :success-fn process-images-fn-success
    :entity
      {:light-value light-slider-value
       :contrast-value contrast-slider-value
       :image-srcs [new-image-src
                    learned-image-src]}})
  ))

(defn read-image-fn-success
 "Image read successfully"
 [xhr]
 (let [response (get-response xhr)
       images-of-signs (:images response)
       read-text (:read-text response)
       textarea (wah/textarea-fn read-text)]
  (md/remove-element-content
    "#resultText")
  (md/append-element
    "#resultText"
    textarea)
  (md/end-please-wait))
 )

(defn read-image-fn
 "Call server to read image"
 []
 (md/start-please-wait)
 (let [light-slider (md/query-selector
                      "#lightSlider")
       light-slider-value (md/get-value light-slider)
       contrast-slider (md/query-selector
                         "#contrastSlider")
       contrast-slider-value (md/get-value contrast-slider)
       space-slider (md/query-selector
                      "#spaceSlider")
       space-slider-value (md/get-value space-slider)
       hooks-slider (md/query-selector
                      "#hooksSlider")
       hooks-slider-value (md/get-value hooks-slider)
       matching-slider (md/query-selector
                         "#matchingSlider")
       matching-slider-value (md/get-value matching-slider)
       image-src (md/get-src "#imgimageSource")
       {_id :value} (md/get-selected-options "#selectLearnedSource")]
  (ajax
   {:url rurls/read-image-url
    :success-fn read-image-fn-success
    :entity
     {:_id _id
      :light-value light-slider-value
      :contrast-value contrast-slider-value
      :space-value space-slider-value
      :hooks-value hooks-slider-value
      :matching-value matching-slider-value
      :image-src image-src}}))
 )

(defn prepare-image-fn-success
 "Retrieving data about source document successful"
 [xhr]
 (let [response (get-response xhr)
       data (:data response)
       src (:image data)
       light-value (:light data)
       contrast-value (:contrast data)
       space-value (:space data)
       hooks-value (:hooks data)
       matching-value (:matching data)
       image (wah/image-fn
               src
               {:width "unset"
                :max-width "300px"})
       slider-evts (fn [id]
                    {:onchange
                     {:evt-fn utils/slider-value-fn
                      :evt-p id}})
       slider-input-evts (fn [id]
                          {:onchange
                           {:evt-fn utils/slider-input-value-fn
                            :evt-p id}})
       light-slider-selector "lightSlider"
       light-slider (wah/slider-fn
                      light-slider-selector
                      {:value (or light-value
                                  "33")}
                      (slider-evts
                        light-slider-selector)
                      (slider-input-evts
                        light-slider-selector)
                      "Light")
       contrast-slider-selector "contrastSlider"
       contrast-slider (wah/slider-fn
                         contrast-slider-selector
                         {:value (or contrast-value
                                     "128")}
                         (slider-evts
                           contrast-slider-selector)
                         (slider-input-evts
                           contrast-slider-selector)
                         "Contrast")
       space-slider-selector "spaceSlider"
       space-slider (wah/slider-fn
                      space-slider-selector
                      {:min "0"
                       :max "128"
                       :value (or space-value
                                  "16")}
                      (slider-evts
                        space-slider-selector)
                      (slider-input-evts
                        space-slider-selector)
                      "Space")
       hooks-slider-selector "hooksSlider"
       hooks-slider (wah/slider-fn
                      hooks-slider-selector
                      {:min "0"
                       :max "128"
                       :value (or hooks-value
                                  "8")}
                      (slider-evts
                        hooks-slider-selector)
                      (slider-input-evts
                        hooks-slider-selector)
                      "Hooks")
       matching-slider-selector "matchingSlider"
       matching-slider (wah/slider-fn
                         matching-slider-selector
                         {:min "0"
                          :max "100"
                          :value (or matching-value
                                     "70")}
                         (slider-evts
                           matching-slider-selector)
                         (slider-input-evts
                           matching-slider-selector)
                         "Matching")
       process-btn (wah/btn-fn
                     {:evt-fn process-images-fn
                      :value "Process"})
       read-btn (wah/btn-fn
                  {:evt-fn read-image-fn
                   :value "Read"})]
  (md/remove-element-content
    "#processImage")
  (md/append-element
    "#processImage"
    image)
  (md/remove-element-content
    "#light")
  (md/append-element
    "#light"
    light-slider)
  (md/remove-element-content
    "#contrast")
  (md/append-element
    "#contrast"
    contrast-slider)
  (md/remove-element-content
    "#space")
  (md/append-element
    "#space"
    space-slider)
  (md/remove-element-content
    "#hooks")
  (md/append-element
    "#hooks"
    hooks-slider)
  (md/remove-element-content
    "#matching")
  (md/append-element
    "#matching"
    matching-slider)
  (md/remove-element-content
    "#process")
  (md/append-element
    "#process"
    process-btn)
  (md/append-element
    "#process"
    read-btn)
  (md/end-please-wait))
 )

(defn prepare-image-fn
 "Call server to return data about chosen document source"
 []
 (md/start-please-wait)
 (let [{_id :value} (md/get-selected-options "#selectLearnedSource")]
  (ajax
   {:url rurls/get-entity-url
    :success-fn prepare-image-fn-success
    :entity {:entity-type docent/entity-type
             :entity-filter {:_id _id}}
    }))
 )

(defn display-reading
 "Initial function for displaying reading area"
 []
 (md/remove-element-content ".content")
 (utils/retrieve-documents-fn
   rh/reading-area-html-fn
   prepare-image-fn))

