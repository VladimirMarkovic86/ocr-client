(ns ocr-client.working-area.learning.html
 (:require [htmlcss-lib.core :refer [gen div select option]]))

(defn learning-area-html-fn
 ""
 [{select-data :select-data
   prepare-image-fn :prepare-image-fn}]
 (gen
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
       {:id "selectSource"}
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
        {:id "gallery"
         :style {:height "140px"}})
      (div
        ""
        {:id "resultText"})]
     {:id "results"})]
   {:class "learningArea"}))
 )

