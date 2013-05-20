(ns gratefulplace.middleware.t-auth
  (:use midje.sweet
        ring.mock.request)
  (:require [gratefulplace.middleware.auth :as auth]
            [gratefulplace.server :as server]))


(fact "posts are successful"
  (server/app
   (request :post "/post" {:content "whatever"
                           :topic "1"}))
  => (contains {:body "success"
                :status 200}))
