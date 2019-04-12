(ns ocr-client.document.entity
  (:require [htmlcss-lib.core :refer [gen crt]]
            [js-lib.core :as md]
            [framework-lib.core :refer [gen-table]]
            [utils-lib.core :refer [round-decimals]]
            [cljs.reader :as reader]
            [language-lib.core :refer [get-label]]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [ocr-middle.collection-names :refer [document-cname]]
            [ocr-middle.document.entity :as omde]))

(def entity-type
     document-cname)

(defn dtype-labels
  "Returns meal type property labels"
  []
  [[(get-label 1022)
    omde/dtype-book-page]
   [(get-label 1023)
    omde/dtype-typewriter]])

(defn form-conf-fn
  "Form configuration for document entity"
  []
  {:id :_id
   :type entity-type
   :entity-name (get-label 1002)
   :fields {:dname {:label (get-label 1003)
                    :input-el "text"
                    :attrs {:placeholder (get-label 1003)
                            :title (get-label 1003)
                            :required true}}
            :dtype {:label (get-label 1004)
                    :input-el "radio"
                    :attrs {:required true}
                    :options (dtype-labels)}
            :image {:label (get-label 1005)
                    :input-el "img"
                    :attrs {:required true}}
            }
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
       :th {:style {:width "35%"}}
       :td {:style {:width "35%"
                    :text-align "left"}}
       }
     :dtype
      {:content (get-label 1003)
       :th {:style {:width "35%"}}
       :td {:style {:width "35%"
                    :text-align "left"}}
       :labels (into
                 #{}
                 (dtype-labels))}
     :image
      {:content (get-label 1004)
       :th {:style {:width "35%"}}
       :td {:style {:width "35%"
                    :text-align "left"}}
       }}
    })

(defn query-fn
  "Table query for document entity"
  []
  {:entity-type entity-type
   :entity-filter {}
   :projection (:projection (columns-fn))
   :projection-include true
   :qsort {:dname 1}
   :pagination true
   :current-page 0
   :rows omde/rows
   :collation {:locale "sr"}})

(defn table-conf-fn
  "Table configuration for document entity"
  []
  {:query (query-fn)
   :columns (columns-fn)
   :form-conf (form-conf-fn)
   :actions [:details :edit :delete]
   :allowed-actions @allowed-actions
   :reports-on true
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

