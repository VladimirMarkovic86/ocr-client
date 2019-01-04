(ns ocr-client.working-area.learning.controller
  (:require [ajax-lib.core :refer [ajax get-response]]
            [websocket-lib.core :refer [websocket]]
            [js-lib.core :as md]
            [ocr-client.utils :as utils]
            [common-middle.request-urls :as rurls]
            [ocr-middle.request-urls :as orurls]
            [ocr-client.document.entity :as docent]
            [ocr-client.utils :as outils]
            [cljs.reader :as reader]
            [language-lib.core :refer [get-label]]
            [ocr-middle.functionalities :as omfns]
            [common-client.allowed-actions.controller :refer [allowed-actions]]))

(defn process-image-ws-onopen-fn
  "Onopen websocket event gather data from page and pass it through websocket to server"
  [event]
  (let [websocket-obj (.-target
                        event)
        light-slider (md/query-selector
                       "#lightSlider")
        light-slider-value (md/get-value
                             light-slider)
        contrast-slider (md/query-selector
                          "#contrastSlider")
        contrast-slider-value (md/get-value
                                contrast-slider)
        image (md/query-selector
                "#hiddenPreparedImage")
        image-src (md/get-value
                    image)]
    (.send
      websocket-obj
      (str
        {:light-value light-slider-value
         :contrast-value contrast-slider-value
         :image-srcs [image-src]}))
   ))

(defn process-image-ws-onmessage-fn
  "Onmessage websocket event receive message
     when action is \"update-progress\"
       update progress bar
     when action is \"image-processed\"
       read server's results update page content
       and remove progress bar div from body"
  [event]
  (let [response (reader/read-string
                   (.-data
                     event))
        action (:action response)]
    (when (= action
             "update-progress")
      (let [progress-value (:progress-value response)]
        (md/update-progress-bar
          progress-value))
     )
    (when (= action
             "image-processed")
      (let [image-src (get
                        (:srcs response)
                        0)
            image-el (md/query-selector
                       "#preparedImage")]
        (md/set-src
          image-el
          image-src))
     ))
 )

(defn process-image-fn
  "Establish websocket connection with server by process-images-ws-url
   and register onopen and onmessage functions"
  []
  (md/start-progress-bar)
  (websocket
    orurls/process-images-ws-url
    {:onopen-fn process-image-ws-onopen-fn
     :onmessage-fn process-image-ws-onmessage-fn
     :onclose-fn utils/websocket-default-close}))

(defn save-sign-fn-success
  "Sign saved successfully"
  [xhr]
  (let [sign-value (md/query-selector
                     "#signValue")
        sign-image (md/query-selector
                     "#gallery img[style*='display: inline;']")
        sign-image-id (.-id
                        sign-image)
        current-value (md/replace-single
                        sign-image-id
                        "sign"
                        "")
        current-value (reader/read-string
                        current-value)
        signs-images (md/query-selector-all
                       "img[id*='sign']")
        make-visible-index (if (= current-value
                                  0)
                             1
                             0)
        make-visible-sign-image (get
                                  signs-images
                                  make-visible-index)]
    (md/remove-element
      (str
        "#sign"
        current-value))
    (doseq [sign-image signs-images]
      (let [sign-image-id (.-id
                            sign-image)
            doseq-value (md/replace-single
                          sign-image-id
                          "sign"
                          "")
            doseq-value (reader/read-string
                          doseq-value)]
        (when (< current-value
                 doseq-value)
          (aset
            sign-image
            "id"
            (str
              "sign"
              (dec
                doseq-value))
           ))
       ))
   (when-not (nil?
               make-visible-sign-image)
    (md/set-attr
      make-visible-sign-image
      "style"
      "display: inline;"))
   (md/set-value
     sign-value
     "")
   (md/end-please-wait))
 )

(defn save-sign-fn
  "Call server to save sign"
  []
  (md/start-please-wait)
  (let [{_id :value} (md/get-selected-options
                       "#selectSource")
        sign-value (md/get-value
                     "#signValue")
        sign-src (md/get-src
                   "#gallery img[style*='display: inline;']")]
    (ajax
      {:url orurls/save-sign-url
       :success-fn save-sign-fn-success
       :entity {:entity-type docent/entity-type
                :entity-filter {:_id _id}
                :sign-value sign-value
                :sign-image sign-src}}))
 )

(defn read-image-ws-onopen-fn
  "Onopen websocket event gather data from page and pass it through websocket to server"
  [event]
  (let [websocket-obj (.-target
                        event)
        light-slider (md/query-selector
                       "#lightSlider")
        light-slider-value (md/get-value
                             light-slider)
        contrast-slider (md/query-selector
                          "#contrastSlider")
        contrast-slider-value (md/get-value
                                contrast-slider)
        space-slider (md/query-selector
                       "#spaceSlider")
        space-slider-value (md/get-value
                             space-slider)
        hooks-slider (md/query-selector
                       "#hooksSlider")
        hooks-slider-value (md/get-value
                             hooks-slider)
        matching-slider (md/query-selector
                          "#matchingSlider")
        matching-slider-value (md/get-value
                                matching-slider)
        threads-slider (md/query-selector
                          "#threadsSlider")
        threads-slider-value (md/get-value
                               threads-slider)
        rows-threads-slider (md/query-selector
                              "#rowsThreadsSlider")
        rows-threads-slider-value (md/get-value
                                    rows-threads-slider)
        unknown-sign-count-limit-slider (md/query-selector
                                          "#unknownSignCountLimitSlider")
        unknown-sign-count-limit-slider-value (md/get-value
                                                unknown-sign-count-limit-slider)
        image (md/query-selector
                "#hiddenPreparedImage")
        image-src (md/get-value
                    image)
        {_id :value} (md/get-selected-options
                       "#selectSource")]
    (try
      (.send
        websocket-obj
        (str
          {:_id _id
           :light-value light-slider-value
           :contrast-value contrast-slider-value
           :space-value space-slider-value
           :hooks-value hooks-slider-value
           :matching-value matching-slider-value
           :threads-value threads-slider-value
           :rows-threads-value rows-threads-slider-value
           :unknown-sign-count-limit-value unknown-sign-count-limit-slider-value
           :image-src image-src}))
      (catch js/Error e
        (.error js/console e))
     ))
 )

(defn read-image-ws-onmessage-fn
  "Onmessage websocket event receive message
   and if action is \"read-image\" read server's results update page content
   and remove please wait div from body"
  [event]
  (let [response (reader/read-string
                   (.-data
                     event))
        action (:action response)]
    (when (= action
             "read-image")
      (let [images-of-signs (:images response)
            read-text (:read-text response)
            signs-count (count
                          images-of-signs)
            evt-fn (fn [direction]
                     (let [displayed-image (md/query-selector
                                             "#gallery img[style*='display: inline;']")
                           sign-id (md/get-attr
                                     displayed-image
                                     "id")
                           current-value (md/replace-single
                                           sign-id
                                           "sign"
                                           "")
                           current-value (reader/read-string
                                           current-value)
                           new-value (direction
                                       current-value
                                       1)
                           hidden-signs (md/query-selector-all
                                          "#gallery img[style*='display: none;']")
                           signs-count (inc
                                         (count
                                           hidden-signs))]
                      (when (and (< new-value
                                    signs-count)
                                 (< -1
                                    new-value))
                       (let [hidden-image (md/query-selector
                                            (str
                                              "#sign"
                                              new-value))
                             displayed-image (md/query-selector
                                               (str
                                                 "#sign"
                                                 current-value))]
                        (md/set-attr
                          hidden-image
                          "style"
                          "display: inline;")
                        (md/set-attr
                          displayed-image
                          "style"
                          "display: none;"))
                       ))
                    )
            gallery (outils/gallery-fn
                      images-of-signs
                      evt-fn
                      save-sign-fn)
            textarea (outils/textarea-fn
                       read-text)]
        (md/remove-element-content
          "#gallery")
        (when-not (empty?
                    images-of-signs)
          (md/append-element
            "#gallery"
            gallery))
        (md/remove-element-content
          "#resultText")
        (md/append-element
          "#resultText"
          textarea))
     ))
 )

(defn read-image-fn
  "Establish websocket connection with server by read-image-ws-url
   and register onopen and onmessage functions"
  []
  (md/start-please-wait)
  (websocket
    orurls/read-image-ws-url
    {:onopen-fn read-image-ws-onopen-fn
     :onmessage-fn read-image-ws-onmessage-fn
     :onclose-fn utils/websocket-default-close}
   ))

(defn save-parameters-fn
  "Save parameters from form"
  []
  (md/start-please-wait)
  (let [light-slider (md/query-selector
                       "#lightSlider")
        light-slider-value (md/get-value
                             light-slider)
        contrast-slider (md/query-selector
                          "#contrastSlider")
        contrast-slider-value (md/get-value
                                contrast-slider)
        space-slider (md/query-selector
                       "#spaceSlider")
        space-slider-value (md/get-value
                             space-slider)
        hooks-slider (md/query-selector
                       "#hooksSlider")
        hooks-slider-value (md/get-value
                             hooks-slider)
        matching-slider (md/query-selector
                          "#matchingSlider")
        matching-slider-value (md/get-value
                                matching-slider)
        threads-slider (md/query-selector
                         "#threadsSlider")
        threads-slider-value (md/get-value
                               threads-slider)
        rows-threads-slider (md/query-selector
                              "#rowsThreadsSlider")
        rows-threads-slider-value (md/get-value
                                    rows-threads-slider)
        unknown-sign-count-limit-slider (md/query-selector
                                          "#unknownSignCountLimitSlider")
        unknown-sign-count-limit-slider-value (md/get-value
                                                unknown-sign-count-limit-slider)
        {_id :value} (md/get-selected-options
                       "#selectSource")]
    (ajax
      {:url orurls/save-parameters-url
       :success-fn (fn [] (md/end-please-wait))
       :entity
        {:_id _id
         :light-value light-slider-value
         :contrast-value contrast-slider-value
         :space-value space-slider-value
         :hooks-value hooks-slider-value
         :matching-value matching-slider-value
         :threads-value threads-slider-value
         :rows-threads-value rows-threads-slider-value
         :unknown-sign-count-limit-value unknown-sign-count-limit-slider-value}}))
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
        threads-value (:threads data)
        rows-threads-value (:rows-threads data)
        unknown-sign-count-limit-value (:unknown-sign-count-limit data)
        image (outils/image-fn
                src)
        slider-evts (fn [id]
                      {:onchange
                        {:evt-fn utils/slider-value-fn
                         :evt-p id}})
        slider-input-evts (fn [id]
                            {:onchange
                              {:evt-fn utils/slider-input-value-fn
                               :evt-p id}})
        light-slider-selector "lightSlider"
        light-slider (outils/slider-fn
                       light-slider-selector
                       {:value (or light-value
                                   "33")}
                       (slider-evts
                         light-slider-selector)
                       (slider-input-evts
                         light-slider-selector)
                       (get-label 1009))
        contrast-slider-selector "contrastSlider"
        contrast-slider (outils/slider-fn
                          contrast-slider-selector
                          {:value (or contrast-value
                                      "128")}
                          (slider-evts
                            contrast-slider-selector)
                          (slider-input-evts
                            contrast-slider-selector)
                          (get-label 1010))
        space-slider-selector "spaceSlider"
        space-slider (outils/slider-fn
                       space-slider-selector
                       {:min "0"
                        :max "128"
                        :value (or space-value
                                   "16")}
                       (slider-evts
                         space-slider-selector)
                       (slider-input-evts
                         space-slider-selector)
                       (get-label 1011))
        hooks-slider-selector "hooksSlider"
        hooks-slider (outils/slider-fn
                       hooks-slider-selector
                       {:min "0"
                        :max "128"
                        :value (or hooks-value
                                   "8")}
                       (slider-evts
                         hooks-slider-selector)
                       (slider-input-evts
                         hooks-slider-selector)
                       (get-label 1012))
        matching-slider-selector "matchingSlider"
        matching-slider (outils/slider-fn
                          matching-slider-selector
                          {:min "0"
                           :max "100"
                           :value (or matching-value
                                      "70")}
                          (slider-evts
                            matching-slider-selector)
                          (slider-input-evts
                            matching-slider-selector)
                          (get-label 1013))
        threads-slider-selector "threadsSlider"
        threads-slider (outils/slider-fn
                         threads-slider-selector
                         {:min "1"
                          :max "16"
                          :value (or threads-value
                                     "4")}
                         (slider-evts
                           threads-slider-selector)
                         (slider-input-evts
                           threads-slider-selector)
                         (get-label 1014))
        rows-threads-slider-selector "rowsThreadsSlider"
        rows-threads-slider (outils/slider-fn
                              rows-threads-slider-selector
                              {:min "1"
                               :max "16"
                               :value (or rows-threads-value
                                          "4")}
                              (slider-evts
                                rows-threads-slider-selector)
                              (slider-input-evts
                                rows-threads-slider-selector)
                              (get-label 1015))
        unknown-sign-count-limit-slider-selector "unknownSignCountLimitSlider"
        unknown-sign-count-limit-slider (outils/slider-fn
                                          unknown-sign-count-limit-slider-selector
                                          {:min "0"
                                           :max "50"
                                           :value (or unknown-sign-count-limit-value
                                                      "25")}
                                          (slider-evts
                                            unknown-sign-count-limit-slider-selector)
                                          (slider-input-evts
                                            unknown-sign-count-limit-slider-selector)
                                          (get-label 1020))
        process-btn (outils/btn-fn
                      {:evt-fn process-image-fn
                       :value (get-label 1017)
                       :id "btnProcess"})
        read-btn (outils/btn-fn
                   {:evt-fn read-image-fn
                    :value (get-label 1016)
                    :id "btnRead"})
        save-parameters-btn (outils/btn-fn
                              {:evt-fn save-parameters-fn
                               :value (get-label 1018)})]
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
      "#threads")
    (md/append-element
      "#threads"
      threads-slider)
    (md/remove-element-content
      "#rowsThreads")
    (md/append-element
      "#rowsThreads"
      rows-threads-slider)
    (md/remove-element-content
      "#unknownSignCountLimit")
    (md/append-element
      "#unknownSignCountLimit"
      unknown-sign-count-limit-slider)
    (md/remove-element-content
      "#process")
    (when (contains?
            @allowed-actions
            omfns/process-images)
      (md/append-element
        "#process"
        process-btn))
    (when (contains?
            @allowed-actions
            omfns/read-image)
      (md/append-element
        "#process"
        read-btn))
    (when (contains?
            @allowed-actions
            omfns/save-parameters)
      (md/append-element
        "#process"
        save-parameters-btn))
    (md/end-please-wait))
 )

(defn prepare-image-fn
  "Call server to return data about chosen document source"
  []
  (md/start-please-wait)
  (let [{_id :value} (md/get-selected-options
                       "#selectSource")
        source-select (md/query-selector
                        "#selectSource")]
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

