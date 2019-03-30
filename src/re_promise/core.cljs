(ns re-promise.core
  (:require [re-frame.core :refer [reg-fx dispatch]]))

(defn- dispatch-conj [handler data]
  (when handler
    (dispatch (conj handler data))))

(defn handle-re-promise
  [{:keys [call on-failure on-success on-failure-n on-success-n] :as pr}]
  (-> (call)
      (.then (fn [response]
               (dispatch-conj on-success response)
               (doseq [handler on-success-n]
                 (dispatch-conj handler response))))
      (.catch (fn [err]
                (dispatch-conj on-failure err)
                (doseq [handler on-failure-n]
                  (dispatch-conj handler err))
                (when (not (or on-failure on-failure-n))
                  (throw err))))))

(reg-fx :promise handle-re-promise)

(reg-fx :promise-n
  (fn [re-promises]
    (doseq [re-promise re-promises]
      (prn re-promise))
    (doseq [re-promise re-promises]
      (handle-re-promise re-promise))))
