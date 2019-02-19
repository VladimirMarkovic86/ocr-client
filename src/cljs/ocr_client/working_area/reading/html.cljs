(ns ocr-client.working-area.reading.html
  (:require [htmlcss-lib.core :refer [gen div select option label]]
            [ajax-lib.core :refer [sjax get-response]]
            [js-lib.core :as md]
            [common-middle.request-urls :as rurls]
            [ocr-client.document.entity :as docent]
            [framework-lib.core :as fw]
            [language-lib.core :refer [get-label]]
            [ocr-middle.functionalities :as omfns]
            [common-client.allowed-actions.controller :refer [allowed-actions]]))

(defn reading-content-fn
  "Generate reading content"
  [evt-p
   element
   event]
  (md/remove-element-content
    ".content")
  (let [xhr (sjax
              {:url rurls/get-entities-url
               :entity docent/query-documents-select-tag})
        response (get-response
                   xhr)
        select-data (:data response)
        select-element-map (select
                             (let [options (atom [(option
                                                    (get-label 33)
                                                    {:value "-1"})])]
                               (doseq [{_id :_id
                                        dname :dname
                                        dtype :dtype} select-data]
                                 (swap!
                                   options
                                   conj
                                   (option
                                     dname
                                     {:dtype dtype
                                      :value _id}))
                                )
                               @options)
                             {:id "selectLearnedSource"}
                             {:onchange {:evt-fn evt-p}})
        reading-form (gen
                       (div 
                         [(label
                            [(get-label 1026)
                             (fw/image-field
                               ""
                               {:id "imageSource"})])
                          (div
                            [(label
                               [(get-label 1025)
                                select-element-map])
                             (div
                               [(div
                                  ""
                                  {:id "processImage"})
                                (div
                                  ""
                                  {:id "light"})
                                (div
                                  ""
                                  {:id "contrast"})
                                (div
                                  ""
                                  {:id "space"})
                                (div
                                  ""
                                  {:id "hooks"})
                                (div
                                  ""
                                  {:id "matching"})
                                (div
                                  ""
                                  {:id "threads"})
                                (div
                                  ""
                                  {:id "rowsThreads"})
                                (div
                                  ""
                                  {:id "process"})]
                               {:id "image"})
                              (div
                                ""
                                {:id "resultText"})])]
                         {:class "readingArea"}))]
    (md/append-element
      ".content"
      reading-form))
 )

(defn nav
  "Generate reading area HTML"
  [prepare-image-fn]
  (when (and (contains?
               @allowed-actions
               omfns/document-read)
             (or (contains?
                   @allowed-actions
                   omfns/process-images)
                 (contains?
                   @allowed-actions
                   omfns/read-image))
         )
    {:label (get-label 1007)
     :id "working-area-reading-nav-id"
     :evt-fn reading-content-fn
     :evt-p prepare-image-fn}))

