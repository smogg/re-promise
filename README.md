

## Promise effect handler for re-frame

This re-frame library adds an [Effect Handler](https://github.com/Day8/re-frame/blob/develop/docs/EffectfulHandlers.md) supporting Javascript Promises.

Keyed `:promise`, it translate a simple map into promise call with a `then` and a `catch` firing provided re-frame handlers.

This library is heavily inspired by [re-frame-http-fx](https://github.com/Day8/re-frame-http-fx)

## Quick Start Guide

### Step 1. Add Dependency

Add the following project dependency:

[![Clojars Project](https://img.shields.io/clojars/v/re-promise.svg)](https://clojars.org/re-promise)


### Step 2. Registration And Use

In the namespace where you register your event handlers, perhaps called `events.cljs`, you have 2 things to do.

**First**, add this "require" to the `ns` to register the `:promise` and `:promise-n` effect handlers with re-frame:

```clj
(ns app.core
  (:require
    ...
    [re-promise]   ;; <-- add this
    ...))
```

**Second**, use the effects in your code:

```clj
(ns app.events              ;; or where ever you define your event handlers
  (:require [re-frame.core :refer [reg-event-fx]]))

(reg-event-fx :handler-using-promise
  (fn [{:keys [db]} _]
    {:promise {:call #(-> (some-api+)
                          (.then (fn [response]
                                   (do-something-with-response response))))

               ;; :your-success-handler will receive the arguments passed below
               ;; as well as the result of the above promise chain as last
               ;; argument to your handler fn
               :on-success [:your-success-handler "some-str"]

               ;; Similarly to :on-success, :your-failure-handler will receive
               ;; the arguments passed here as well as the error as last
               ;; argument  to your handler fn (as if you have used `.catch` in
               ;; above promise chain)
               :on-failure [:your-failure-handler {:some :map}]

               ;; You can also dispatch multiple success handlers, all of them
               ;; will work exactly the same as a single one would:
               :on-success-n [[:first-success-handler]
                              [:second-success-handler]]

               ;; The same is true for failure handlers:
               :on-failure-n [[:first-failure-handler]
                              [:second-failure-handler]]}}))
```


There's also a `promise-n` fx if you want to use multiple promises within one handler. It expects an array of maps that match the structure of the example above.

```clj
(reg-event-fx :handler-firing-multiple-promises
  (fn [{:keys [db]} _]
    {:promise-n [{:call fetch-docs+ :on-success [:success]}
                 {:call #(post-changes+ db) :on-success [:yes] :on-failure [:oh-no]}
                 {:call fire-nukes+}]}))
```
