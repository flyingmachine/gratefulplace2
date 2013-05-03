(ns gratefulplace.controllers.users
  (:require [gratefulplace.db.validations :as validations]
            [gratefulplace.db.serialize :as s]
            [gratefulplace.db.serializers :as ss]
            [gratefulplace.db.query :as q])
  (:use [flyingmachine.webutils.validation :only (if-valid)]))

(defn create!
  [req]
  (let [{:keys [uri request-method params]} req]
    (when (and (= uri "/users")
               (= request-method :post))
      (if-valid
       params
       (:create validations/user)
       errors

       (do
         (q/t [(s/serialize params ss/user->txdata)])
         (cemerick.friend.workflows/make-auth (select-keys params [:username :email])))
       {:body {:errors errors}
        :status 412}))))