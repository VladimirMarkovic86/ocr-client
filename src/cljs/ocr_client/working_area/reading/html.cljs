(ns ocr-client.working-area.reading.html
 (:require [htmlcss-lib.core :refer [gen div select option]]
           [framework-lib.core :as fw]))

(defn reading-area-html-fn
 ""
 [{select-data :select-data
   prepare-image-fn :prepare-image-fn}]
 (gen
  (div 
   [(div
      (fw/image-field
        nil
        "imageSource"
        false
        {:height "unset"
         :width "unset"
         :max-width "300px"})
       {:style {:float "left"
                :margin "5px"}})
    (div
      [(div
         (select
          (let [options (atom [(option "- Select -"
                                       {:value "-1"})])]
           (doseq [{_id :_id
                    dname :dname
                    dtype :dtype} select-data]
            (swap!
              options
              conj
              (option dname
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

