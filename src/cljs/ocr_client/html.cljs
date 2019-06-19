(ns ocr-client.html
  (:require [htmlcss-lib.core :refer [h2 p div]]
            [ocr-middle.functionalities :as omfns]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [ocr-client.document.html :as dh]
            [ocr-client.working-area.html :as wah]
            [ocr-client.test-bot :refer [run-test]]
            [language-lib.core :refer [get-label]]))

(defn home-page-content
  "Home page content"
  []
  [(div
     [(h2
        (get-label 62))
      (p
        (get-label 63))
      ]
     {:class "row-1-4"})
   (div
     [(div
        nil
        {:class "col-1-4"})
      (div
        nil
        {:class "col-2-4 logo-hi-res"})
      (div
        nil
        {:class "col-1-4"})
      ]
     {:class "row-2-4"})
   (div
     nil
     {:class "row-1-4"})
   ])

(defn custom-menu
  "Render menu items for user that have privilege for them"
  []
  [(dh/nav)
   (wah/nav)
   (when (contains?
           @allowed-actions
           omfns/test-document-entity)
     {:label (get-label 1021)
      :id "test-document-entity-nav-id"
      :evt-fn run-test})
   ])

