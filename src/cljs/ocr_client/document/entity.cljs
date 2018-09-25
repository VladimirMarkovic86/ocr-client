(ns ocr-client.document.entity
  (:require [htmlcss-lib.core :refer [gen crt]]
            [js-lib.core :as md]
            [framework-lib.core :refer [gen-table]]
            [utils-lib.core :refer [round-decimals]]
            [cljs.reader :as reader]
            [language-lib.core :refer [get-label]]
            [common-client.allowed-actions.controller :refer [allowed-actions]]))

(def entity-type
     "document")

(defn form-conf-fn
  "Form configuration for document entity"
  []
  {:id :_id
   :type entity-type
   :entity-name (get-label 1002)
   :fields {:dname {:label (get-label 1003)
                    :input-el "text"
                    :attrs {:required "required"}}
            :dtype {:label (get-label 1004)
                    :input-el "radio"
                    :attrs {:required "required"}
                    :options ["Book page"
                              "Typewriter"]}
            :image {:label (get-label 1005)
                    :input-el "img"}}
   :fields-order [:dname
                  :dtype
                  :image]})

(defn columns-fn
  "Table columns for document entity"
  []
  {:projection [:dname
                :dtype
                ;:image
                ]
   :style
    {:dname
      {:content (get-label 1002)
       :th {:style {:width "100px"}}
       :td {:style {:width "100px"
                    :text-align "left"}}
       }
     :dtype
      {:content (get-label 1003)
       :th {:style {:width "100px"}}
       :td {:style {:width "100px"
                    :text-align "left"}}
       }
     :image
      {:content (get-label 1004)
       :th {:style {:width "100px"}}
       :td {:style {:width "100px"
                    :text-align "left"}}
       }}
    })

(defn query-fn
  "Table query for document entity"
  []
  {:entity-type  entity-type
   :entity-filter  {}
   :projection  (:projection (columns-fn))
   :projection-include  true
   :qsort  {:dname 1}
   :pagination  true
   :current-page  0
   :rows  25
   :collation {:locale "sr"}})

(defn table-conf-fn
  "Table configuration for document entity"
  []
  {:query (query-fn)
   :columns (columns-fn)
   :form-conf (form-conf-fn)
   :actions [:details :edit :delete]
   :allowed-actions @allowed-actions
   :search-on true
   :search-fields [:dname :dtype]
   :render-in ".content"
   :table-class "entities"
   :table-fn gen-table})

(def query-documents-select-tag
     {:entity-type entity-type
      :entity-filter {}
      :projection [:dname :dtype]
      :projection-include true
      :qsort {:dname 1}
      :pagination false
      :collation {:locale "sr"}})

