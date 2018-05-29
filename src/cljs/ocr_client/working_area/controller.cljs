(ns ocr-client.working-area.controller
  (:require [ajax-lib.core :refer [ajax get-response]]
            [js-lib.core :as md]
            [ocr-client.working-area.html :as ohtml]
            [cljs.reader :as reader]))

(def get-entities-url "/clojure/get-entities")

(def get-entity-url "/clojure/get-entity")

(def read-image-url "/clojure/read-image")

(def save-sign-url "/clojure/save-sign")

(def entity-type "document")

(def query-documents
     {:entity-type  entity-type
      :entity-filter  {}
      :projection  [:dname :dtype]
      :projection-include  true
      :qsort  {:dname 1}
      :pagination  false
      :collation {:locale "sr"}})

(defn process-image-fn-success
 "Successful image processing"
 [xhr]
 (let [response (get-response xhr)
       image-src (:src response)
       image-el (md/query-selector "#preparedImage")]
  (md/set-src image-el image-src))
 )

(defn process-image-fn
 "Process image with light and contrast parameters"
 []
 (let [light-slider (md/query-selector
                         "#lightSlider")
       light-slider-value (md/get-value light-slider)
       contrast-slider (md/query-selector
                         "#contrastSlider")
       contrast-slider-value (md/get-value contrast-slider)
       image (md/query-selector "#hiddenPreparedImage")
       image-src (md/get-value image)]
  (ajax
   {:url "/clojure/process-image"
    :success-fn process-image-fn-success
    :entity
      {:light-value light-slider-value
       :contrast-value contrast-slider-value
       :image-src image-src}}))
 )

(defn slider-value-fn
 "Update slider input value as it is on slider"
 [id]
 (let [slider-input-value (md/query-selector (str "#" id "-value"))
       slider (md/query-selector (str "#" id))
       slider-value (md/get-value slider)]
  (md/set-value slider-input-value slider-value))
 )

(defn slider-input-value-fn
 "Update slider value as it is in slider input value"
 [id]
 (let [slider-input-value (md/query-selector (str "#" id "-value"))
       slider (md/query-selector (str "#" id))
       slider-input-value (md/get-value slider-input-value)]
  (md/set-value slider slider-input-value))
 )

(defn read-image-fn-success
 "Image read successfully"
 [xhr]
 (let [response (get-response xhr)
       images-of-signs (:images response)
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
       gallery (ohtml/gallery-fn images-of-signs evt-fn)]
  (md/append-element
    "#gallery"
    gallery))
 )

(defn read-image-fn
 "Call server to read image"
 []
 (let [light-slider (md/query-selector
                         "#lightSlider")
       light-slider-value (md/get-value light-slider)
       contrast-slider (md/query-selector
                         "#contrastSlider")
       contrast-slider-value (md/get-value contrast-slider)
       image (md/query-selector "#hiddenPreparedImage")
       image-src (md/get-value image)
       {_id :value} (md/get-selected-options "#selectSource")]
  (ajax
   {:url read-image-url
    :success-fn read-image-fn-success
    :entity
     {:_id _id
      :light-value light-slider-value
      :contrast-value contrast-slider-value
      :image-src image-src}}))
 )

(defn prepare-image-fn-success
 "Retrieving data about source document successful"
 [xhr]
 (let [response (get-response xhr)
       {src :image} (:data response)
       image (ohtml/image-fn src)
       slider-evts (fn [id]
                    {:onchange
                     {:evt-fn slider-value-fn
                      :evt-p id}})
       slider-input-evts (fn [id]
                          {:onchange
                           {:evt-fn slider-input-value-fn
                            :evt-p id}})
       light-slider-selector "lightSlider"
       light-slider (ohtml/slider-fn
                         light-slider-selector
                         nil
                         (slider-evts
                           light-slider-selector)
                         (slider-input-evts
                           light-slider-selector))
       contrast-slider-selector "contrastSlider"
       contrast-slider (ohtml/slider-fn
                         contrast-slider-selector
                         nil
                         (slider-evts
                           contrast-slider-selector)
                         (slider-input-evts
                           contrast-slider-selector))
       process-btn (ohtml/btn-fn
                   {:evt-fn process-image-fn
                    :value "Process"})
       read-btn (ohtml/btn-fn
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
    "#process")
  (md/append-element
    "#process"
    process-btn)
  (md/append-element
    "#process"
    read-btn))
 )

(defn prepare-image-fn
 "Call server to return data about chosen document source"
 []
 (let [{_id :value} (md/get-selected-options "#selectSource")]
  (ajax
   {:url get-entity-url
    :success-fn prepare-image-fn-success
    :entity {:entity-type entity-type
             :entity-filter {:_id _id}}
    }))
 )

(defn save-sign-fn-success
 "Sign saved successfully"
 [xhr]
 (let []
  
  )
 )

(defn save-sign-fn
 "Call server to save sign"
 []
 (let [{_id :value} (md/get-selected-options "#selectSource")
       sign-value (md/get-value "#signValue")
       sign-src (md/get-src "#gallery img[style*='display: inline;']")]
  (ajax
   {:url save-sign-url
    :success-fn save-sign-fn-success
    :entity {:entity-type entity-type
             :entity-filter {:_id _id}
             :sign-value sign-value
             :sign-image sign-src}}))
 )

(defn retrieve-documents-fn-success
 "Retrieving source documents successful"
 [xhr]
 (let [response (get-response xhr)
       select-data (:data response)]
  (md/append-element
    ".content"
    (ohtml/working-area-html-fn
     {:select-data select-data
      :prepare-image-fn prepare-image-fn
      :save-sign-fn save-sign-fn}))
  ))

(defn retrieve-documents-fn
 "Call server to return all source documents"
 [] 
 (ajax
  {:url get-entities-url
   :success-fn retrieve-documents-fn-success
   :entity query-documents}))

(defn display-working-area
 "Initial function for displaying working area"
 [select-data]
 (md/remove-element-content ".content")
 (retrieve-documents-fn))

(defn nav-link
  "Process these functions after link is clicked in main menu"
  []
  (md/remove-element-content ".content")
  (md/remove-element-content ".sidebar-menu")
  (md/append-element
    ".sidebar-menu"
    (ohtml/nav
     {:onclick {:evt-fn display-working-area}}))
 )

