(ns gratefulplace.models.permissions
  (:use gratefulplace.utils
        environ.core))

(def moderator-usernames (env :moderator-names))

(defmulti moderator? class)
(defmethod moderator? String
  [username] (some #(= % username) moderator-usernames))
(defmethod moderator? clojure.lang.IPersistentMap
  [m] (moderator? (:username m)))

(defn current-username [auth]
  (:username auth))

(defn current-user-id [auth]
  (:id auth))

(defn logged-in? [auth]
  auth)

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

(defn author?
  [record auth]
  (or
   (= (:author-id record) (:id auth))
   (= (get-in record [:author :id]) (:id auth))
   (and
    (not (nil? (:username auth)))
    (= (get-in record [:author :username]) (:username auth)))))

;; Pretty sure there's something in onlisp about this
(defmacro protect [check & body]
  `(if (not ~check)
     {:status 401
      :body {:errors {:authorization ["You are not authorized to do that."]}}}
     (do ~@body)))