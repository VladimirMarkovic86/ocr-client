(defproject org.clojars.vladimirmarkovic86/ocr-client "0.2.1"
  :description "Optical character recognition client"
  :url "http://github.com/VladimirMarkovic86/ocr-client"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure	"1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [org.clojars.vladimirmarkovic86/ajax-lib "0.1.14"]
                 [org.clojars.vladimirmarkovic86/websocket-lib "0.1.6"]
                 [org.clojars.vladimirmarkovic86/htmlcss-lib "0.1.9"]
                 [org.clojars.vladimirmarkovic86/js-lib "0.1.20"]
                 [org.clojars.vladimirmarkovic86/framework-lib "0.2.52"]
                 [org.clojars.vladimirmarkovic86/utils-lib "0.4.13"]
                 [org.clojars.vladimirmarkovic86/client-test-lib "0.2.19"]
                 [org.clojars.vladimirmarkovic86/language-lib "0.2.39"]
                 [org.clojars.vladimirmarkovic86/ocr-middle "0.3.13"]
                 [org.clojars.vladimirmarkovic86/common-client "0.3.50"]
                 [org.clojars.vladimirmarkovic86/common-middle "0.2.13"]
                 [org.clojars.vladimirmarkovic86/server-lib "0.3.36"]
                 [org.clojars.vladimirmarkovic86/validator-lib "0.1.41"]
                 ]

  :min-lein-version "2.0.0"
  
  :resource-paths ["resources"]
  :source-paths ["src/clj"]
  
  :main ^:skip-aot ocr-client.core
  
  :uberjar-name "ocr-client-standalone.jar"
  :profiles {:uberjar {:aot :all}}
  
  :plugins [[lein-cljsbuild  "1.1.7"]
            ]

  :cljsbuild
    {:builds
      {:dev
        {:source-paths ["src/cljs"]
         :compiler     {:main ocr-client.controller
                        :output-to "resources/public/assets/js/main.js"
                        :output-dir "resources/public/assets/js/out"
                        :asset-path "assets/js/out"
                        :pretty-print true}}
       :prod
        {:source-paths ["src/cljs"]
         :compiler     {:main ocr-client.controller
                        :output-to "resources/public/assets/jsprod/main.js"
                        :output-dir "resources/public/assets/jsprod/out"
                        :asset-path "assets/jsprod/out"
                        :externs ["externs.js"]
                        :optimizations :advanced}}
       }})

