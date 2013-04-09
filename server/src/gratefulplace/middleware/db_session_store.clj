(ns gratefulplace.middleware.db-session-store
  (:require [gratefulplace.models.user-session :as user-session])
  (:use ring.middleware.session.store))

(deftype DbSessionStore []
  SessionStore
  (read-session [_ key]
    (:data (user-session/one {:key key})))
  (write-session [_ key data]
    (:key
     (if key
       (user-session/update! key {:data data})
       (user-session/create! {:data data}))))
  (delete-session [_ key]
    (user-session/destroy! key)
    nil))

(defn db-session-store
  []
  (DbSessionStore.))