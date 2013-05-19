(ns gratefulplace.controllers.js
  (:require [cemerick.friend :as friend]))

(defn load_session
  [params]
  (let [session-js
        (fn [value]
          {:body (str "angular.module('gratefulplaceApp').value('loadedSession', " value ")")
           :headers {"content-type" "application/javascript"}})]
    (if-let [auth (friend/current-authentication)]
      (session-js (str "{username:'" (:username auth) "'}"))
      (session-js "null"))))
