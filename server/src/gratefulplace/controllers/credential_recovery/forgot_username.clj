(ns gratefulplace.controllers.credential-recovery.forgot-username
  (:require [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.email.sending.senders :as email]
            [gratefulplace.controllers.shared :refer :all]
            [com.flyingmachine.liberator-templates.sets.json-crud
             :refer (defshow defcreate! defupdate!)]
            [flyingmachine.webutils.utils :refer :all]))

(defcreate!
  [params]
  :invalid? (validator params validations/forgot-username)
  :exists? (fn [_] (exists? (seq (dj/all [:user/email (:email params)]))))
  :can-post-to-missing? false
  :handle-not-found (fn [_] {:errors {:email ["That email address doesn't exist"]}})
  :post! (fn [ctx] (future (email/send-forgot-username (:record ctx))))
  :return {})