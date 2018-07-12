#!/bin/bash
cd ../ajax_lib
lein install
cd ../utils_lib
lein install
cd ../htmlcss_lib
lein install
cd ../js_lib
lein install
cd ../framework_lib
lein install
cd ../websocket_lib
lein install
cd ../client_test_lib
lein install
cd ../ocr_client
lein cljsbuild once dev
