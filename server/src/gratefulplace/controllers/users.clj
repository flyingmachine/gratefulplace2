(ns gratefulplace.controllers.users
  (:require [gratefulplace.db.validations :as validations])
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

       {:body "success"}
       {:body {:errors errors}}))))