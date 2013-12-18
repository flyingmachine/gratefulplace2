(ns gratefulplace.controllers.credential-recovery.forgot-password
  (:require [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.transactions.password-reset :as tx]
            [gratefulplace.email.sending.senders :as email]
            [gratefulplace.controllers.shared :refer :all]
            [liberator.core :refer [defresource]]
            [flyingmachine.webutils.utils :refer :all]))

(defresource show [params]
  :available-media-types ["application/json"]
  :malformed? (validator params validations/password-reset-token)
  :handle-malformed errors-in-ctx
  :handle-ok {})

(defresource create! [params]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  
  :malformed? (validator params validations/forgot-password)
  :handle-malformed errors-in-ctx

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

(defresource update! [params]
  :allowed-methods [:put :post]
  :available-media-types ["application/json"]

  :malformed? (validator (merge params {:new-password params}) validations/password-reset)
  :handle-malformed errors-in-ctx

  :exists? (fn [_] (if-let [user (dj/one [:user/password-reset-token (:token params)])]
                    {:record user}))
  
  :put! (fn [ctx] (tx/consume-token (:record ctx) (:new-password params)))
  :post! (fn [ctx] (tx/consume-token (:record ctx) (:new-password params)))
  :new? false
  :respond-with-entity? false
  :handle-ok {})