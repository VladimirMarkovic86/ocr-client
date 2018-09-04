(ns ocr-client.html
  (:require [htmlcss-lib.core :refer [a]]
            [ocr-middle.functionalities :as omfns]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [ocr-client.document.controller :as dc]
            [ocr-client.working-area.controller :as wac]
            [language-lib.core :refer [get-label]]))

(defn custom-menu
  ""
  []
  [(when (contains?
           @allowed-actions
           omfns/document-read)
     (a
       (get-label 1002)
       {:id "aDocumentId"}
       {:onclick {:evt-fn dc/nav-link}}))
   (when (or (contains?
               @allowed-actions
               omfns/process-images)
             (contains?
               @allowed-actions
               omfns/read-image)
             (contains?
               @allowed-actions
               omfns/save-sign)
             (contains?
               @allowed-actions
               omfns/save-parameters))
     (a
       (get-label 1001)
       {:id "aWorkingAreaId"}
       {:onclick {:evt-fn wac/nav-link}}))]
 )
