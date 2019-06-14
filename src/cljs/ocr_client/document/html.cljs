(ns ocr-client.document.html
  (:require [framework-lib.core :refer [create-entity gen-table]]
            [ocr-client.document.entity :refer [table-conf-fn]]
            [language-lib.core :refer [get-label]]
            [ocr-middle.functionalities :as omfns]
            [common-client.allowed-actions.controller :refer [allowed-actions]]))

(defn nav
  "Returns map of menu item and it's sub items"
  []
  (when (or (contains?
              @allowed-actions
              omfns/document-create)
            (contains?
              @allowed-actions
              omfns/document-read))
    {:label (get-label 1002)
     :id "document-nav-id"
     :sub-menu [(when (contains?
                        @allowed-actions
                        omfns/document-create)
                  {:label (get-label 4)
                   :id "document-create-nav-id"
                   :evt-fn create-entity
                   :evt-p table-conf-fn})
                (when (contains?
                        @allowed-actions
                        omfns/document-read)
                  {:label (get-label 5)
                   :id "document-show-all-nav-id"
                   :evt-fn gen-table
                   :evt-p table-conf-fn})]})
 )

