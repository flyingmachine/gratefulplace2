(ns gratefulplace.controllers.js
  (:require [cemerick.friend :as friend]))

(defn load_session
  [params]
  (let [session-js
        (fn [username]
          {:body (str
                  "angular.module('gratefulplaceApp').value('loadedSession', {username:'"
                  username
                  "'})")
           :headers {"content-type" "application/javascript"}})]
    (if-let [auth (friend/current-authentication)]
      (session-js (:username auth))
      (session-js ""))))
