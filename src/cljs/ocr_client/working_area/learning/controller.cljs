(ns ocr-client.working-area.learning.controller
 (:require [ajax-lib.core :refer [ajax get-response]]
           [js-lib.core :as md]
           [ocr-client.utils :as utils]
           [ocr-client.request-urls :as rurls]
           [ocr-client.document.entity :as docent]
           [ocr-client.working-area.html :as wah]
           [ocr-client.working-area.learning.html :as lh]
           [cljs.reader :as reader]))

(defn process-image-fn-success
 "Successful image processing"
 [xhr]
 (let [response (get-response xhr)
       image-src (get (:srcs response) 0)
       image-el (md/query-selector "#preparedImage")]
  (md/set-src image-el image-src)
  (md/end-progress-bar))
 )

(defn process-image-fn
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
       image (md/query-selector "#hiddenPreparedImage")
       image-src (md/get-value image)]
  (ajax
   {:url rurls/process-images-url
    :success-fn process-image-fn-success
    :entity
      {:light-value light-slider-value
       :contrast-value contrast-slider-value
       :image-srcs [image-src]}}))
 )

(defn save-sign-fn-success
 "Sign saved successfully"
 [xhr]
 (let [sign-value (md/query-selector "#signValue")
       sign-image (md/query-selector "#gallery img[style*='display: inline;']")
       sign-image-id (aget sign-image "id")
       current-value (md/replace-single
                       sign-image-id
                       "sign"
                       "")
       current-value (reader/read-string current-value)
       signs-images (md/query-selector-all "img[id*='sign']")
       make-visible-index (if (= current-value
                                 0)
                           1
                           0)
       make-visible-sign-image (get signs-images make-visible-index)]
  (md/remove-element
    (str
      "#sign"
      current-value))
  (doseq [sign-image signs-images]
   (let [sign-image-id (aget sign-image "id")
         doseq-value (md/replace-single
                       sign-image-id
                       "sign"
                       "")
         doseq-value (reader/read-string doseq-value)]
    (when (< current-value
             doseq-value)
     (aset
       sign-image
       "id"
       (str
         "sign"
         (dec doseq-value))
      ))
    ))
  (when-not (nil? make-visible-sign-image)
   (md/set-attr
     make-visible-sign-image
     "style"
     "display: inline;")
   )
  (md/set-value
    sign-value
    "")
  (md/end-please-wait))
 )

(defn save-sign-fn
 "Call server to save sign"
 []
 (md/start-please-wait)
 (let [{_id :value} (md/get-selected-options "#selectSource")
       sign-value (md/get-value "#signValue")
       sign-src (md/get-src "#gallery img[style*='display: inline;']")]
  (ajax
   {:url rurls/save-sign-url
    :success-fn save-sign-fn-success
    :entity {:entity-type docent/entity-type
             :entity-filter {:_id _id}
             :sign-value sign-value
             :sign-image sign-src}}))
 )

(defn read-image-fn-success
 "Image read successfully"
 [xhr]
 (let [response (get-response xhr)
       images-of-signs (:images response)
       read-text (:read-text response)
       signs-count (count images-of-signs)
       evt-fn (fn [direction]
               (let [displayed-image (md/query-selector
                                      "#gallery img[style*='display: inline;']")
                     sign-id (md/get-attr displayed-image "id")
                     current-value (md/replace-single
                                     sign-id
                                     "sign"
                                     "")
                     current-value (reader/read-string current-value)
                     new-value (direction
                                 current-value
                                 1)
                     hidden-signs (md/query-selector-all
                                    "#gallery img[style*='display: none;']")
                     signs-count (inc (count hidden-signs))]
                (when (and (< new-value
                              signs-count)
                           (< -1
                              new-value))
                 (let [hidden-image (md/query-selector (str "#sign" new-value))
                       displayed-image (md/query-selector (str "#sign" current-value))]
                  (md/set-attr hidden-image "style" "display: inline;")
                  (md/set-attr displayed-image "style" "display: none;"))
                 ))
               )
       gallery (wah/gallery-fn
                 images-of-signs
                 evt-fn
                 save-sign-fn)
       textarea (wah/textarea-fn read-text)]
  (md/remove-element-content
    "#gallery")
  (when-not (empty? images-of-signs)
   (md/append-element
     "#gallery"
     gallery))
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
       image (md/query-selector "#hiddenPreparedImage")
       image-src (md/get-value image)
       {_id :value} (md/get-selected-options "#selectSource")]
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

(defn save-parameters-fn
 ""
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
       {_id :value} (md/get-selected-options "#selectSource")]
  (ajax
   {:url rurls/save-parameters-url
    :success-fn (fn [] (md/end-please-wait))
    :entity
     {:_id _id
      :light-value light-slider-value
      :contrast-value contrast-slider-value
      :space-value space-slider-value
      :hooks-value hooks-slider-value
      :matching-value matching-slider-value}}))
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
       image (wah/image-fn src)
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
                     {:evt-fn process-image-fn
                      :value "Process"})
       read-btn (wah/btn-fn
                  {:evt-fn read-image-fn
                   :value "Read"})
       save-parameters-btn (wah/btn-fn
                             {:evt-fn save-parameters-fn
                              :value "Save parameters"})]
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
  (md/append-element
    "#process"
    save-parameters-btn)
  (md/end-please-wait))
 )

(defn prepare-image-fn
 "Call server to return data about chosen document source"
 []
 (md/start-please-wait)
 (let [{_id :value} (md/get-selected-options "#selectSource")
       source-select (md/query-selector "#selectSource")]
  (md/set-attr
    source-select
    "disabled"
    true)
  (ajax
   {:url rurls/get-entity-url
    :success-fn prepare-image-fn-success
    :entity {:entity-type docent/entity-type
             :entity-filter {:_id _id}}
    }))
 )

(defn display-learning
 "Initial function for displaying learning area"
 []
 (md/remove-element-content ".content")
 (utils/retrieve-documents-fn
   lh/learning-area-html-fn
   prepare-image-fn))

