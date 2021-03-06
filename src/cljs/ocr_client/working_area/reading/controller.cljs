(ns ocr-client.working-area.reading.controller
  (:require [ajax-lib.core :refer [ajax get-response]]
            [websocket-lib.core :refer [websocket]]
            [js-lib.core :as md]
            [common-middle.request-urls :as rurls]
            [ocr-middle.request-urls :as orurls]
            [ocr-client.document.entity :as docent]
            [ocr-client.utils :as outils]
            [ocr-client.working-area.reading.html :as rh]
            [cljs.reader :as reader]
            [language-lib.core :refer [get-label]]
            [ocr-middle.functionalities :as omfns]
            [common-client.allowed-actions.controller :refer [allowed-actions]]))

(defn process-image-ws-onopen-fn
  "Onopen websocket event gather all needed data from page
    and pass it through websocket to server"
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
        new-image-src (md/get-src
                        "#imageSource")
        learned-image-src (md/get-src
                            "#preparedImage")]
    (.send
      websocket-obj
      (str
        {:light-value light-slider-value
         :contrast-value contrast-slider-value
         :image-srcs [new-image-src
                      learned-image-src]}))
   ))

(defn process-image-ws-onmessage-fn
  "Onmessage websocket event receive message
     when action field is \"update-progress\"
       update progress bar
     when action field is \"image-progressed\"
       update page content and remove progress bar div from body"
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
      (let [[new-image-src
             learned-image-src] (:srcs response)
            new-image-el (md/query-selector
                           "#imageSource")
            learned-image-el (md/query-selector
                               "#preparedImage")]
        (md/set-src
          new-image-el
          new-image-src)
        (md/set-src
          learned-image-el
          learned-image-src))
     ))
 )

(defn process-images-fn
  "Establish websocket connection with server with process-images-ws-url
   register functions for event onopen and onmessage"
  []
  (md/start-progress-bar)
  (websocket
    orurls/process-images-ws-url
    {:onopen-fn process-image-ws-onopen-fn
     :onmessage-fn process-image-ws-onmessage-fn
     :onclose-fn outils/websocket-default-close}
   ))

(defn read-image-ws-onopen-fn
  "Onopen websocket event gather all needed data from page
   and pass it through websocket to server"
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
        image-src (md/get-src
                    "#imageSource")
        {_id :value} (md/get-selected-options
                       "#selectLearnedSource")]
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
           :image-src image-src}))
      (catch js/Error e
        (.error
          js/console
          e))
     ))
 )

(defn read-image-ws-onmessage-fn
  "Onmessage websocket event receive message and when action field is \"read-image\"
   update page content with server results and remove please wait div"
  [event]
  (let [response (reader/read-string
                   (.-data
                     event))
        action (:action response)]
    (when (= action
             "read-image")
      (let [images-of-signs (:images response)
            read-text (:read-text response)
            textarea (outils/textarea-fn
                       read-text)]
        (md/remove-element-content
          "#resultText")
        (md/append-element
          "#resultText"
          textarea))
     ))
 )

(defn read-image-fn
  "Establish websocket connection with server with read-image-ws-url
   register functions for event onopen and onmessage"
  []
  (md/start-please-wait)
  (websocket
    orurls/read-image-ws-url
    {:onopen-fn read-image-ws-onopen-fn
     :onmessage-fn read-image-ws-onmessage-fn
     :onclose-fn outils/websocket-default-close}))

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
        image (outils/image-fn
                src)
        slider-evts (fn [id]
                      {:onchange
                        {:evt-fn outils/slider-value-fn
                         :evt-p id}})
        slider-input-evts (fn [id]
                            {:onchange
                              {:evt-fn outils/slider-input-value-fn
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
        process-btn (outils/btn-fn
                      {:evt-fn process-images-fn
                       :value (get-label 1017)})
        read-btn (outils/btn-fn
                   {:evt-fn read-image-fn
                    :value (get-label 1016)})]
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
    (md/end-please-wait))
 )

(defn prepare-image-fn
  "Call server to return data about chosen document source"
  []
  (md/start-please-wait)
  (let [{_id :value} (md/get-selected-options
                       "#selectLearnedSource")]
   (ajax
     {:url rurls/get-entity-url
      :success-fn prepare-image-fn-success
      :entity {:entity-type docent/entity-type
               :entity-filter {:_id _id}}
      }))
 )

