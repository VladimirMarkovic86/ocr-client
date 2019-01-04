(ns ocr-client.working-area.html
  (:require [language-lib.core :refer [get-label]]
            [ocr-middle.functionalities :as omfns]
            [ocr-client.working-area.learning.html :as walh]
            [ocr-client.working-area.reading.html :as warh]
            [ocr-client.working-area.learning.controller :as walc]
            [ocr-client.working-area.reading.controller :as warc]
            [common-client.allowed-actions.controller :refer [allowed-actions]]))

(defn nav
  "Returns map of menu item and it's sub items"
  []
  (when (and (contains?
               @allowed-actions
               omfns/document-read)
             (or (contains?
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
         )
    {:label (get-label 1001)
     :id "working-area-nav-id"
     :sub-menu [(walh/nav
                  walc/prepare-image-fn)
                (warh/nav
                  warc/prepare-image-fn)]})
 )

