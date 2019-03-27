

## Promise effect handler for re-frame

This re-frame library adds an [Effect Handler](https://github.com/Day8/re-frame/blob/develop/docs/EffectfulHandlers.md) supporting Javascript Promises.

Keyed `:promise`, it translate a simple map into promise call with a `then` and a `catch` firing provided re-frame handlers.

This library is heavily inspired by [re-frame-http-fx](https://github.com/Day8/re-frame-http-fx)

## Quick Start Guide

### Step 1. Add Dependency

Add the following project dependency:

### Step 2. Registration And Use

In the namespace where you register your event handlers, perhaps called `events.cljs`, you have 2 things to do.

**First**, add this "require" to the `ns` to register the `:promise` effect handler with re-frame:

```clj
(ns app.core
  (:require
    ...
    [re-promise]   ;; <-- add this
    ...))
```

**Second**, use the effect in your code:

```clj
(ns app.events              ;; or where ever you define your event handlers
  (:require [re-frame.core :refer [reg-event-fx]]))

(reg-event-fx :handler-using-promise
  (fn [{:keys [db]} _]
    {:promise {:call #(-> (some-api+)
                          (.then (fn [response]
                                   (do-something-with-response response))))
               ;; :your-success-handler will receive the passed arguments as
               ;; well ass the result of calling the above function as last
               ;; argument passed your handler fn
               :on-success [:your-success-handler "some-str"]
               ;; :your-failure-handler will receive the passed arguments as
               ;; well ass the error as last argument passed to your handler fn
               :on-failure [:your-failure-handler {:some :map}]}}))
```
