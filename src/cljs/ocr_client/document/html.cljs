(ns ocr-client.document.html
 (:require [htmlcss-lib.core :refer [gen div a]]
           [framework-lib.core :refer [create-entity gen-table]]
           [ocr-client.document.entity :refer [table-conf]]))

(defn nav
 "Generate ul HTML element
  that represents navigation menu"
 []
 (gen
  [(div
    (a "Create"
       nil
       {:onclick {:evt-fn create-entity
                  :evt-p table-conf}}))
   (div
    (a "Show all"
       nil
       {:onclick {:evt-fn gen-table
                  :evt-p table-conf}}))])
 )
                   
