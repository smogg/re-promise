(ns re-promise.test-runner
  (:require [cljs-test-display.core :as td]
            [cljs.test :as cljs-test :include-macros true]
            [clojure.test :refer-macros [run-tests]]
            [figwheel.main.testing :refer-macros [run-tests-async]]
            [goog.object :as gobj]
            ;; Test Namespaces -------------------------------
            [re-promise.core-test]))

(enable-console-print!)

(defn extra-main []
  (js/console.warn "extra-main")
  (run-tests (td/init! "app-tests")
             're-promise.core-test))

(defn ^:export -main [& args]
  (js/console.warn "-main")
  (run-tests-async 3000))

;; Only run this at NS init time when the user
;; is visiting the extra main page
(when (= "/figwheel-extra-main/tests"
         (gobj/getValueByKeys goog/global "location" "pathname"))
  (extra-main))
