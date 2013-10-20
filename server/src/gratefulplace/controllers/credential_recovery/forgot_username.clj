(ns gratefulplace.controllers.credential-recovery.forgot-username
  (:require [gratefulplace.db.query :as db]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.email.sending.senders :as email]
            [gratefulplace.controllers.shared :refer :all]
            [liberator.core :refer [defresource]]
            [gratefulplace.utils :refer :all]))

(defresource create! [params]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  
  :malformed? (validator params validations/forgot-username)
  :handle-malformed errors-in-ctx

  :exists? (fn [_] (exists? (seq (db/all [:user/email (:email params)]))))
  :can-post-to-missing? false
  :handle-not-found (fn [_] {:errors {:email ["That email address doesn't exist"]}})
  
  :post! (fn [ctx]
           (future (email/send-forgot-username (:record ctx))))
  :handle-created {})