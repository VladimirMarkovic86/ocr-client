(ns ocr-client.working-area.learning.html
  (:require [htmlcss-lib.core :refer [gen div select option label]]
            [ajax-lib.core :refer [sjax get-response]]
            [js-lib.core :as md]
            [common-middle.request-urls :as rurls]
            [ocr-client.document.entity :as docent]
            [language-lib.core :refer [get-label]]
            [ocr-middle.functionalities :as omfns]
            [common-client.allowed-actions.controller :refer [allowed-actions]]))

(defn learning-content-fn
  "Generate learning content"
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
                             {:id "selectSource"}
                             {:onchange {:evt-fn evt-p}})
        learning-form (gen
                        (div
                          [(div
                             (label
                               [(get-label 1024)
                                select-element-map])
                             {:id "source"})
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
                                {:id "unknownSignCountLimit"})
                              (div
                                ""
                                {:id "process"})]
                             {:id "image"})
                           (div
                             [(div
                                ""
                                {:id "gallery"
                                 :style {:height "140px"}})
                              (div
                                ""
                                {:id "resultText"})]
                             {:id "results"})]
                          {:class "learningArea"}))]
    (md/append-element
      ".content"
      learning-form))
 )

(defn nav
  "Generate learning HTML"
  [prepare-image-fn]
  (when (and (contains?
               @allowed-actions
               omfns/document-read)
             (or (contains?
                   @allowed-actions
                   omfns/process-images)
                 (contains?
                   @allowed-actions
                   omfns/read-image)
                 (contains?
                   @allowed-actions
                   omfns/save-sign)
                 (contains?
                   @allowed-actions
                   omfns/save-parameters))
         )
    {:label (get-label 1006)
     :id "working-area-learning-nav-id"
     :evt-fn learning-content-fn
     :evt-p prepare-image-fn}))

