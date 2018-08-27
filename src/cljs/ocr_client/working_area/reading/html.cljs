(ns ocr-client.working-area.reading.html
  (:require [htmlcss-lib.core :refer [gen div select option]]
            [framework-lib.core :as fw]
            [language-lib.core :refer [get-label]]))

(defn reading-area-html-fn
  "Generate reading area HTML"
  [{select-data :select-data
    prepare-image-fn :prepare-image-fn}]
  (gen
    (div 
      [(div
         (fw/image-field
           ""
           {:id "imageSource"
            :style {:max-height "200px"
                    :max-width "300px"}})
          {:style {:float "left"
                   :margin "5px"}})
       (div
         [(div
            (select
              (let [options (atom [(option
                                     (get-label 42)
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
              {:onchange {:evt-fn prepare-image-fn}})
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
                {:id "process"})]
             {:id "image"})
           (div
             [(div
                ""
                {:id "resultText"})]
             {:id "results"})]
         {:style {:float "left"
                  :margin "5px"}})]
      {:class "readingArea"}))
 )

