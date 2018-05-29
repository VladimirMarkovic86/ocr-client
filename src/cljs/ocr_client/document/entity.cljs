(ns ocr-client.document.entity
 (:require [htmlcss-lib.core :refer [gen crt]]
           [js-lib.core :as md]
           [framework-lib.core :refer [gen-table]]
           [utils-lib.core :refer [round-decimals]]
           [cljs.reader :as reader]))

(def entity-type "document")

(def form-conf
  {:id :_id
   :type entity-type
   :fields {
            :dname {:label "Name"
                    :field-type "input"
                    :data-type "text"}
            :dtype {:label "Document type"
                    :field-type "radio"
                    :data-type "text"
                    :options ["Book page"
                              "Typewriter"]}
            :image {:label "Image"
                    :field-type "image"
                    :data-type "file"}}
   :fields-order [:dname
                  :dtype
                  :image]})

(def columns
 {:projection [:dname
               :dtype
               ;:image
               ]
  :style
   {:dname
     {:content "Name"
      :th {:style {:width "100px"}}
      :td {:style {:width "100px"
                   :text-align "left"}}
      }
    :dtype
     {:content "Document type"
      :th {:style {:width "100px"}}
      :td {:style {:width "100px"
                   :text-align "left"}}
      }
    :image
     {:content "Image"
      :th {:style {:width "100px"}}
      :td {:style {:width "100px"
                   :text-align "left"}}
      }}
   })

(def query
     {:entity-type  entity-type
      :entity-filter  {}
      :projection  (:projection columns)
      :projection-include  true
      :qsort  {:dname 1}
      :pagination  true
      :current-page  0
      :rows  25
      :collation {:locale "sr"}})

(def table-conf
     {:query query
      :columns columns
      :form-conf form-conf
      :actions #{:details :edit :delete}
      :render-in ".content"
      :table-class "entities"
      :table-fn gen-table})

