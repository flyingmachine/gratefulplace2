(ns gratefulplace.controllers.users
  (:require [gratefulplace.db.validations :as validations]
            [gratefulplace.db.query :as db]
            [datomic.api :as d]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [gratefulplace.db.query :as q]
            [cemerick.friend :as friend]
            cemerick.friend.workflows)
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        gratefulplace.models.permissions
        gratefulplace.controllers.shared
        gratefulplace.utils))

(defmapifier record mr/ent->user)
(defmapifier authrecord mr/ent->userauth)

(defn registration-success-response
  [params auth]
  "If the request gets this far, it means that user registration was successful."
  (if auth {:body auth}))

(defn attempt-registration
  [req]
  (let [{:keys [uri request-method params session]} req]
    (when (and (= uri "/users")
               (= request-method :post))
      (if-valid
       params (:create validations/user) errors
       (let [user-tempid (d/tempid :db.part/user -1)
             user (mapify-tx-result
                   (q/t [(c/mapify (merge params {:id user-tempid}) mr/user->txdata)])
                   user-tempid
                   mr/ent->user)]
         (cemerick.friend.workflows/make-auth
          user
          {:cemerick.friend/redirect-on-auth? false}))
       (invalid errors)))))

(defn show
  [params]
  {:body (c/mapify
          (db/ent (str->int (:id params)))
          mr/ent->user
          {:exclude [:password]})})

;; TODO more definite way of updating specified fields? perhaps
;; creating a route which passes the fields as an argument to the
;; controller?
(defn update-about!
  [params auth]
  (protect
   (current-user-id? (id) auth)
   (db/t [(c/mapify params mr/user->txdata {:exclude [:user/username :user/email :user/password]})])
   {:body (record (id))}))

(defn update-email!
  [params auth]
  (protect
   (current-user-id? (id) auth)
   (if-valid
    params (:update-email validations/user) errors
    (do
      (db/t [{:db/id (id) :user/email (:email params)}])
      {:body (record (id))})
    (invalid errors))))

(defn- password-params
  [params]
  (let [user (authrecord (id))]
    {:new-password (select-keys params [:new-password :new-password-confirmation])
     :current-password (merge (select-keys params [:current-password]) {:password (:password user)})}))

(defn change-password!
  [params auth]
  (protect
   (current-user-id? (id) auth)
   (if-valid
    (password-params params) validations/change-password errors
    (do
      (db/t [(c/mapify params mr/change-password->txdata)])
      OK)
    (invalid errors))))