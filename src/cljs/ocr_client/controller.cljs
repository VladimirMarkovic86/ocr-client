(ns ocr-client.controller
  (:require [ajax-lib.core :refer [ajax base-url with-credentials]]
            [websocket-lib.core :refer [base-ws-url]]
            [ocr-client.html :as ht]
            [ocr-middle.functionalities :as fns]
            [common-client.role.entity :as re]
            [common-client.login.controller :refer [redirect-to-login
                                                    main-page
                                                    logout
                                                    custom-menu
                                                    logout-fn
                                                    logout-success
                                                    logout-success-fn]]
            [common-middle.request-urls :as rurls]))

(defn am-i-logged-in
  "Check if session is active"
  []
  (reset!
    base-url
    "https://ocr:1602")
  (reset!
    with-credentials
    true)
  (reset!
    base-ws-url
    "wss://ocr:1622")
  #_(reset!
    base-url
    "/clojure")
  (reset!
    custom-menu
    ht/custom-menu)
  (reset!
    logout-fn
    logout)
  (reset!
    logout-success-fn
    logout-success)
  (reset!
    re/functionalities
    fns/functionalities)
  (ajax
    {:url rurls/am-i-logged-in-url
     :success-fn main-page
     :error-fn redirect-to-login
     :entity {:user "it's me"}}))

(set! (.-onload js/window) am-i-logged-in)

