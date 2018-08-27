(ns ocr-client.working-area.html
  (:require [htmlcss-lib.core :refer [gen div a input label
                                      textarea img]]
            [language-lib.core :refer [get-label]]))

(defn gallery-fn
  "Generate HTML gallery"
  [images
   evt-fn
   save-sign-fn]
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
          {:id "btnLeft"
           :type "button"
           :value "<-"}
          {:onclick {:evt-fn evt-fn
                     :evt-p -}})
        {:style {:float "left"
                 :height "70px"
                 :display "grid"
                 :justify-content "center"
                 :align-content "center"}})
      (div
        [(div
           @image-elements
           {:id "divImages"
            :style {:width "200px"
                    :height "70px"
                    :display "grid"
                    :justify-content "center"
                    :align-content "center"}})
         (div
           (input
             ""
             {:id "signValue"
              :type "text"
              :style {:text-align "center"}})
           {:style {:width "200px"
                    :display "grid"
                    :justify-content "center"
                    :align-content "center"}})
         (div
           (input
             ""
             {:type "button"
              :value (get-label 53)
              :style {:margin-left "unset"}}
             {:onclick {:evt-fn save-sign-fn}})
           {:style {:width "200px"
                    :display "grid"
                    :justify-content "center"}})]
        {:style {:float "left"
                 :height "140px"
                 :padding-left "5px"}})
      (div
        (input
          ""
          {:id "btnRight"
           :type "button"
           :value "->"}
          {:onclick {:evt-fn evt-fn
                     :evt-p +}})
        {:style {:float "left"
                 :height "70px"
                 :display "grid"
                 :justify-content "center"
                 :align-content "center"}})])
   ))

(defn textarea-fn
  "Generate textarea HTML element"
  [text]
  (gen
    (textarea
      text
      {:readonly "true"
       :style {:width "400px"
               :height "250px"
               :resize "none"}}))
 )

(defn btn-fn
  "Generate button HTML element"
  [{evt-fn :evt-fn
    evt-p :evt-p
    value :value
    id :id}]
  (gen
    (input
      ""
      (let [attrs {:value value
                   :type "button"}
            attrs (if id
                    (assoc
                      attrs
                      :id
                      id)
                    attrs)]
        attrs)
      {:onclick {:evt-fn evt-fn
                 :evt-p evt-p}}))
 )

(defn slider-fn
  "Generate slider HTML element"
  [id
   & [attrs
      evts
      input-evts
      label-text]]
  (gen
    [(div
       (label
         label-text)
       {:style {:float "left"
                :width "100px"}})
     
     (input
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
       (conj
         {:id (str id "-value")
          :value "0"
          :style {:width "50px"}}
         attrs)
       input-evts)])
 )

(defn image-fn
  "Generate img HTML element"
  [src
   & [style-attrs]]
  (gen
    [(img
       ""
       {:id "preparedImage"
        :style (conj
                 {:width "100%"}
                 style-attrs)
        :src src})
     (input
       ""
       {:id "hiddenPreparedImage"
        :type "hidden"
        :value src})])
 )

(defn nav
  "Generate ul HTML element
   that represents navigation menu"
  [learning-evts
   reading-evts]
  (gen
    [(div
       (a
         (get-label 40)
         {:id "aLearningId"}
         learning-evts))
     (div
       (a
         (get-label 41)
         {:id "aReadingId"}
         reading-evts))]
   ))

