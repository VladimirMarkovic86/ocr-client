(ns ocr-client.preferences.controller
  (:require [common-client.preferences.controller :as ccpc]
            [ocr-middle.document.entity :as omde]))

(defn set-specific-preferences-fn
  "Sets preferences specific for this project"
  [preferences]
  (let [specific-preferences (:specific preferences)
        {{{table-rows-d :table-rows
           card-columns-d :card-columns} :document-entity} :display} specific-preferences]
    (reset!
      omde/table-rows-a
      (or table-rows-d
          10))
    (reset!
      omde/card-columns-a
      (or card-columns-d
          0))
   ))

(defn gather-specific-preferences-fn
  "Gathers preferences from common project"
  []
  {:display {:document-entity {:table-rows @omde/table-rows-a
                               :card-columns @omde/card-columns-a}}
   })

(defn popup-specific-preferences-set-fn
  "Gathers specific preferences from popup and sets values in atoms"
  []
  [(ccpc/generic-preferences-set
     "document-entity"
     omde/card-columns-a
     omde/table-rows-a)
   ]
  )

