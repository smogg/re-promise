(ns re-promise.core-test
  (:require [cljs.test :refer-macros [is deftest async]]
            [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
            [re-promise.core :as core]))

(reg-event-fx ::promise-test
  (fn [_ [_ val]]
    {:promise val}))

(reg-event-fx ::promise-n-test
  (fn [_ [_ val]]
    {:promise-n val}))

(reg-event-db ::good-promise
  (fn [db [_ done data promise-response]]
    (is (= "So good" data) "expected: data passed through")
    (is (= promise-response :good))
    (done)
    db))

(reg-event-db ::bad-promise
  (fn [db [_ done data error]]
    (is (= "So bad" data) "expected: data passed through")
    (is (= error :bad))
    (done)
    db))

(deftest test-successful-promise
  (async done
    (dispatch [::promise-test {:call #(js/Promise.resolve :good)
                               :on-success [::good-promise done "So good"]}])))

(deftest test-failing-promise
  (async done
    (dispatch [::promise-test {:call #(js/Promise.reject :bad)
                               :on-failure [::bad-promise done "So bad"]}])))

(deftest test-on-failure-n
  (async done
    (dispatch [::promise-test {:call #(js/Promise.reject :bad)
                               :on-failure-n [[::bad-promise (fn []) "So bad"]
                                              [::bad-promise done "So bad"]]}])))

(deftest test-on-success-n
  (async done
    (dispatch [::promise-test {:call #(js/Promise.resolve :good)
                               :on-success-n [[::good-promise (fn []) "So good"]
                                              [::good-promise done "So good"]]}])))

(deftest test-promise-n-failures
  (async done
    (dispatch [::promise-n-test [{:call #(js/Promise.reject :bad)
                                  :on-failure [::bad-promise (fn []) "So bad"]}
                                 {:call #(js/Promise.reject :bad)
                                  :on-failure [::bad-promise done "So bad"]}]])))

(deftest test-promise-n-succeeding
  (async done
    (dispatch [::promise-n-test [{:call #(js/Promise.resolve :good)
                                  :on-failure [::good-promise (fn []) "So good"]}
                                 {:call #(js/Promise.resolve :good)
                                  :on-failure [::good-promise done "So good"]}]])))
