(ns ocr-client.preferences.html
  (:require [htmlcss-lib.core :refer [div label]]
            [language-lib.core :refer [get-label]]
            [common-client.preferences.html :as ccph]
            [ocr-middle.document.entity :as omde]))

(defn build-specific-display-tab-content-fn
  "Builds specific display tab content"
  []
  [(div
     [(label
        (get-label
          1027))
      (div
        (ccph/generate-column-number-dropdown-options
          @omde/card-columns-a))
      (div
        (ccph/generate-row-number-dropdown-options
          @omde/table-rows-a))
      ]
     {:class "parameter"
      :parameter-name "document-entity"})
   ])

