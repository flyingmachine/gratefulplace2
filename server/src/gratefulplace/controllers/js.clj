(ns gratefulplace.controllers.js
  (:require [cemerick.friend :as friend]))

(defn load_session
  [params]
  (let [session-js
        (fn [username]
          (str
           "angular.module('gratefulplaceApp').value('loadedSession', {username:'"
           username
           "'})"))]
    (if-let [auth (friend/current-authentication)]
      (session-js (:username auth))
      (session-js ""))))
