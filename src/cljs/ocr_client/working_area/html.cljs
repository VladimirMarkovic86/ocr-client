(ns ocr-client.working-area.html
 (:require [htmlcss-lib.core :refer [gen div a select option
                                     img input label]]))

(defn gallery-fn
 ""
 [images
  evt-fn]
 (gen
  (let [image-el (fn [id
                      src
                      img-style]
                  (img
                   ""
                   {:id id
                    :src src
                    :style img-style}))
        image-elements (atom [])
        itr (atom 0)]
   (doseq [image images]
    (let [img-style (if (= @itr
                           0)
                     {:display "inline"}
                     {:display "none"})]
     (swap!
       image-elements
       conj
       (image-el
        (str "sign" @itr)
        image
        img-style))
     (swap! itr inc))
    )
   [(div
     (input
       ""
       {:type "button"
        :value "<-"}
       {:onclick {:evt-fn evt-fn
                  :evt-p -}})
     {:style {:float "left"
              :padding "10px"}})
    (div
     @image-elements
     {:style {:float "left"
              :width "150px"
              :height "70px"
              :padding "10px"}})
    (div
     (input
       ""
       {:type "button"
        :value "->"}
       {:onclick {:evt-fn evt-fn
                  :evt-p +}})
     {:style {:float "left"
              :padding "10px"}})]
   ))
 )

(defn btn-fn
 ""
 [{evt-fn :evt-fn
   evt-p :evt-p
   value :value}]
 (gen
  (input
   ""
   {:value value
    :type "button"}
   {:onclick {:evt-fn evt-fn
              :evt-p evt-p}}))
 )

(defn slider-fn
 ""
 [id
  & [attrs
     evts
     input-evts]]
 (gen
  [(input
    ""
    (conj
     {:id id
      :type "range"
      :min "-128"
      :max "128"
      :value "0"}
     attrs)
    evts)
   (input
     ""
     {:id (str id "-value")
      :value "0"}
     input-evts)]))

(defn image-fn
 ""
 [src]
 (gen
  [(img
     ""
     {:id "preparedImage"
      :style {:width "100%"}
      :src src})
   (input
     ""
     {:id "hiddenPreparedImage"
      :type "hidden"
      :value src})]
  )
 )

(defn working-area-html-fn
 ""
 [{select-data :select-data
   prepare-image-fn :prepare-image-fn
   save-sign-fn :save-sign-fn}]
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
     [(div ""
           {:id "processImage"})
      (div ""
           {:id "light"})
      (div ""
           {:id "contrast"})
      (div ""
           {:id "process"})]
     {:id "image"})
    (div
     [(div
        ""
        {:id "gallery"
         :style {:height "80px"
                 :width "300px"
                 :padding "10px"}})
      (div
        [(input
           ""
           {:id "signValue"
            :type "text"})
         (input
           ""
           {:type "button"
            :value "Save sign"}
           {:onclick {:evt-fn save-sign-fn}}
           )]
        {:id "sign"})
      (div
        (input
          ""
          {:type "button"
           :value "Display text"})
        {:id "resultText"})]
     {:id "results"})]
   {:class "workingArea"})
  )
 )

(defn nav
 "Generate ul HTML element
  that represents navigation menu"
 [evts]
 (gen
  [(div
    (a "Working area"
       nil
       evts))])
 )
                   
