(ns gratefulplace.controllers.credential-recovery.forgot-password
  (:require [gratefulplace.db.query :as db]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.db.transactions.password-reset :as tx]
            [gratefulplace.email.sending.senders :as email]
            [gratefulplace.controllers.shared :refer :all]
            [liberator.core :refer [defresource]]
            [gratefulplace.utils :refer :all]))

(defresource create! [params]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  
  :malformed? (validator params validations/forgot-password)
  :handle-malformed errors-in-ctx

  :exists? (fn [_] (exists? (seq (db/all [:user/username (:username params)]))))
  :can-post-to-missing? false
  :handle-not-found (fn [_] {:errors {:username ["That username isn't in our system"]}})
  
  :post! (fn [ctx]
           (let [[user] (:record ctx)]
             (tx/create-token user)
             (email/send-password-reset-token [(db/ent (:db/id user))]))
           {})
  :handle-created {})