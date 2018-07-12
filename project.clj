(defproject ocr-client "0.1.0"
  :description    "Optical character recognition client"
  :url            "http://gitlab:1610/VladimirMarkovic86/ocr-client"
  :license        {:name "Eclipse Public License"
                   :url "http://www.eclipse.org/legal/epl-v10.html"}
  :resource-paths ["resources"]
  :dependencies   [[org.clojure/clojure    "1.9.0"]
                    ; https://clojure.org/api/api
                   [org.clojure/clojurescript "1.9.946"]
                    ; https://clojurescript.org/guides/quick-start#clojurescript-compiler
                    ; https://clojurescript.org/reference/dependencies
                   [org.vladimir/ajax-lib "0.1.0"]
                   [org.vladimir/websocket-lib "0.1.0"]
                   [org.vladimir/htmlcss-lib "0.1.0"]
                   [org.vladimir/js-lib "0.1.0"]
                   [org.vladimir/framework-lib "0.1.0"]
                   [org.vladimir/utils-lib "0.1.0"]
                   [org.vladimir/client-test-lib "0.1.0"]
                   ]
  
  :plugins [[lein-figwheel  "0.5.14"]
             ; https://github.com/bhauman/lein-figwheel
            [lein-cljsbuild  "1.1.7"]
             ; https://github.com/emezeske/lein-cljsbuild
            ]
  
  :cljsbuild
   {:builds
    {:dev
     {:source-paths ["src/cljs"]
      :compiler     {:main         ocr-client.core
                     :output-to    "resources/public/js/main.js"
                     :output-dir   "resources/public/js/out"
                     :asset-path   "js/out"
                     :pretty-print true}}
     :figwheel
     {:source-paths ["src/cljs"]
      :figwheel     {:open-urls    ["http://localhost:3449/index.html"] }
      :compiler     {:main         ocr-client.core
                     :output-to    "resources/public/jsfig/main.js"
                     :output-dir   "resources/public/jsfig/out"
                     :asset-path   "jsfig/out"
                     :pretty-print true}}
     :prod
     {:source-paths ["src/cljs"]
      :compiler     {:main          ocr-client.core
                     :output-to     "resources/public/jsprod/main.js"
                     :output-dir    "resources/public/jsprod/out"
                     :asset-path    "jsprod/out"
                     :optimizations :advanced}}
     }}
  )

