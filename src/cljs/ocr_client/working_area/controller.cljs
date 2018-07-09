(ns ocr-client.working-area.controller
  (:require [js-lib.core :as md]
            [ocr-client.working-area.html :as wah]
            [ocr-client.working-area.learning.controller :as lc]
            [ocr-client.working-area.reading.controller :as rc]))

(defn nav-link
  "Process these functions after link is clicked in main menu"
  []
  (md/remove-element-content
    ".content")
  (md/remove-element-content
    ".sidebar-menu")
  (md/append-element
    ".sidebar-menu"
    (wah/nav
      {:onclick {:evt-fn lc/display-learning}}
      {:onclick {:evt-fn rc/display-reading}}))
 )

