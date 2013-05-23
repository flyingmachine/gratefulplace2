(ns gratefulplace.models.permissions
  (:use gratefulplace.utils
        environ.core))

(def moderator-usernames (env :moderator_names))

(defn moderator? [username]
  (some #(= % username) moderator-usernames))

(defn current-username [auth]
  (:username auth))

(defn current-user-id [auth]
  (:id auth))

(defn current-user-id?
  [id auth]
  (= id (current-user-id auth)))

(defn not-current-user-id?
  [id auth]
  (not (current-user-id? id auth)))

(defn can-modify-profile? [user auth]
  (or
   (= user (current-username auth))
   (= (:username user) (current-username auth))))

(defn can-modify-record?
  [record auth]
  (or
   (= (get-in record [:author :username]) (:username auth))
   (= (get-in record [:author :id]) (:id auth))
   (moderator? (:username auth))))

;; Pretty sure there's something in onlisp about this
(defmacro protect [check & body]
  `(if (not ~check)
     {:status 401}
     (do ~@body)))