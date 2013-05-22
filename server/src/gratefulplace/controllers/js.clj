(ns gratefulplace.controllers.js
  (:require [cemerick.friend :as friend]))

(defn load-session
  [params auth]
  (let [session-js
        (fn [value]
          {:body (str "angular.module('gratefulplaceApp').value('loadedSession', " value ")")
           :headers {"content-type" "application/javascript"}})]
    (if auth
      (session-js (str "{username:'" (:username auth) "'}"))
      (session-js "null"))))
