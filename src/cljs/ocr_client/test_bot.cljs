(ns ocr-client.test-bot
  (:require [js-lib.core :as md]
            [client-test-lib.core :as ctest
                                  :refer [click-elem
                                          close-window
                                          opener-console]]
            [utils-lib.core :as utils]))

(defn select-document-for-reading
  "Select document for reading"
  [window-number
   window-obj]
  (opener-console
    (str
      window-number
      " select-document-for-reading"))
  (let [document (aget window-obj "document")
        select-obj (md/query-selector-on-element
                     document
                     "#selectSource")
        options (md/query-selector-all-on-element
                  document
                  "option")
        chosen-option (get options 1)]
    (aset
      chosen-option
      "selected"
      true)
    (md/dispatch-event
      "change"
      select-obj
      window-obj))
 )

(defn test-cases-fn
  "Main test function contains vector of sub-vectors that contain:
    first element: selector that will be waited for to be loaded
    second element: execute function that will be executed after selector is loaded
                      with two parameters: first one is third element of sub-vector
                                           second one is window-obj
    third element: is optional and its used as first execute function parameter"
  [window-obj]
  (let [window-number (aget
                        window-obj
                        "name")]
    (ctest/execute-vector-when-loaded
      window-obj
      [["#aWorkingAreaId"
        click-elem
        "#aWorkingAreaId"]
       ["#aLearningId"
        click-elem
        "#aLearningId"]
       ["#selectSource"
        select-document-for-reading
        window-number]
       ["#btnRead"
        click-elem
        "#btnRead"]
       ["#btnLeft"
        close-window
        window-number]]))
 )

(defn run-test
  "Runs tests from test-cases-fn function in particular window"
  []
  (ctest/run-tests
    test-cases-fn))

