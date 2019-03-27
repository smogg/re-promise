(ns re-promise.core-test
  (:require [cljs.test :refer-macros [is deftest async]]
            [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
            [re-promise.core :as core]))

(reg-event-fx ::promise-test
  (fn [_world [_ val]]
    {:promise val}))

(reg-event-db ::good-promise
  (fn [db [_ done data promise-response]]
    (is (= "So good" data) "expected: data passed through")
    (is (= promise-response :good))
    (done)
    db))

(deftest test-successful-promise
  (async done
    (dispatch [::promise-test {:call #(js/Promise.resolve :good)
                               :on-success [::good-promise done "So good"]}])))

(reg-event-db ::bad-promise
  (fn [db [_ done data error]]
    (is (= "So bad" data) "expected: data passed through")
    (is (= error :bad))
    (done)
    db))

(deftest test-failing-promise
  (async done
    (dispatch [::promise-test {:call #(js/Promise.reject :bad)
                               :on-failure [::bad-promise done "So bad"]}])))
