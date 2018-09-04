(ns ocr-client.document.html
  (:require [htmlcss-lib.core :refer [gen div a]]
            [framework-lib.core :refer [create-entity gen-table]]
            [ocr-client.document.entity :refer [table-conf-fn]]
            [language-lib.core :refer [get-label]]
            [ocr-middle.functionalities :as omfns]
            [common-client.allowed-actions.controller :refer [allowed-actions]]))

(defn nav
 "Generate ul HTML element
  that represents navigation menu"
 []
 (gen
   [(when (contains?
             @allowed-actions
             omfns/document-create)
      (div
        (a
          (get-label 4)
          nil
          {:onclick {:evt-fn create-entity
                     :evt-p (table-conf-fn)}})
       ))
    (when (contains?
             @allowed-actions
             omfns/document-read)
      (div
        (a
          (get-label 5)
          nil
          {:onclick {:evt-fn gen-table
                     :evt-p (table-conf-fn)}})
       ))]
  ))

