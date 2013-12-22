(ns gratefulplace.controllers.credential-recovery.forgot-password
  (:require [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.transactions.password-reset :as tx]
            [gratefulplace.email.sending.senders :as email]
            [gratefulplace.controllers.shared :refer :all]
            [flyingmachine.webutils.utils :refer :all]
            [com.flyingmachine.liberator-templates.sets.json-crud
             :refer (defshow defcreate! defupdate!)]))


(defshow
  [params]
  :invalid? (validator params validations/password-reset-token)
  :return {})

(defcreate!
  [params]
  :invalid? (validator params validations/forgot-password)
  
  :exists? (fn [_] (exists? (seq (dj/all [:user/username (:username params)]))))
  :can-post-to-missing? false
  :handle-not-found (fn [_] {:errors {:username ["That username isn't in our system"]}})

  :post! (fn [ctx]
           (let [[user] (:record ctx)]
             (future
               (tx/create-token user)
               (email/send-password-reset-token [(dj/ent (:db/id user))])))
           {})

  :handle-created {})

(defupdate!
  [params]
  :invalid? (validator (merge params {:new-password params}) validations/password-reset)
  :exists? (fn [_] (if-let [user (dj/one [:user/password-reset-token (:token params)])]
                    {:record user}))
  :put! (fn [ctx] (tx/consume-token (:record ctx) (:new-password params)))
  :new? false
  :respond-with-entity? false
  :handle-ok {})