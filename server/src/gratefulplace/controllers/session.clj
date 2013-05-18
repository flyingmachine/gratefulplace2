(ns gratefulplace.controllers.session
  (:require [cemerick.friend :as friend]))

(defn create!
  [params]
  (if (friend/current-authentication)
    {:body "success"}
    {:body "failure"
     :status 412}))
