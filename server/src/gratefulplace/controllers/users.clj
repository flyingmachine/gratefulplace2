(ns gratefulplace.controllers.users
  (:require [gratefulplace.db.validations :as validations]
            [gratefulplace.db.query :as db]
            [datomic.api :as d]
            [gratefulplace.db.maprules :as mr]
            [gratefulplace.db.transactions :as t]
            [flyingmachine.cartographer.core :as c]
            [cemerick.friend :as friend]
            cemerick.friend.workflows)
  (:use [flyingmachine.webutils.validation :only (if-valid)]
        [liberator.core :only [defresource]]
        gratefulplace.models.permissions
        gratefulplace.db.mapification
        gratefulplace.controllers.shared
        gratefulplace.utils))

(defmapifier record mr/ent->user {:exclude [:password]})
(defmapifier authrecord mr/ent->userauth)

(defn attempt-registration
  [req]
  (let [{:keys [uri request-method params session]} req]
    (when (and (= uri "/users")
               (= request-method :post))
      (if-valid
       params (:create validations/user) errors
       (cemerick.friend.workflows/make-auth
        (mapify-tx-result (t/create-user params) record)
        {:cemerick.friend/redirect-on-auth? false})
       (invalid errors)))))

(defn registration-success-response
  [params auth]
  "If the request gets this far, it means that user registration was successful."
  (if auth {:body auth}))

(defresource show [params]
  :available-media-types ["application/json"]
  :exists? (exists? (record (id)))
  :handle-ok record-in-ctx)

(defn update!*
  [params]
  (db/t [(c/mapify
          params
          mr/user->txdata
          {:exclude [:user/username :user/password]})]))

(defresource update! [params auth]
  :allowed-methods [:put :post]
  :available-media-types ["application/json"]

  :malformed? (validator params (:update validations/user))
  :handle-malformed errors-in-ctx

  :authorized? (current-user-id? (id) auth)
  :exists? record-in-ctx
  
  :post! (fn [_] (update!* params))
  :new? false
  :respond-with-entity? true
  :handle-ok (fn [_] (record (id))))


(defn- password-params
  [params]
  (let [user (authrecord (id))]
    {:new-password (select-keys params [:new-password :new-password-confirmation])
     :current-password (merge (select-keys params [:current-password]) {:password (:password user)})}))

(defresource change-password! [params auth]
  :allowed-methods [:put :post]
  :available-media-types ["application/json"]

  :malformed? (validator (password-params params) validations/change-password)
  :handle-malformed errors-in-ctx
  
  :authorized? (fn [_] (current-user-id? (id) auth))
  
  :post! (fn [_] (db/t [(c/mapify params mr/change-password->txdata)])))