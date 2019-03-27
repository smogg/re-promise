(ns re-promise.core
  (:require [re-frame.core :refer [reg-fx dispatch]]))

(reg-fx :promise
  (fn [{:keys [call on-failure on-success]}]
    (-> (call)
        (.then (fn [response]
                 (dispatch (conj on-success response))))
        (.catch (fn [err]
                  (dispatch (conj on-failure err)))))))
