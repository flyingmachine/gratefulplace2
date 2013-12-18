(ns gratefulplace.controllers.credential-recovery.forgot-username
  (:require [com.flyingmachine.datomic-junk :as dj]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.email.sending.senders :as email]
            [gratefulplace.controllers.shared :refer :all]
            [liberator.core :refer [defresource]]
            [flyingmachine.webutils.utils :refer :all]))

(defresource create! [params]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  
  :malformed? (validator params validations/forgot-username)
  :handle-malformed errors-in-ctx

  :exists? (fn [_] (exists? (seq (dj/all [:user/email (:email params)]))))
  :can-post-to-missing? false
  :handle-not-found (fn [_] {:errors {:email ["That email address doesn't exist"]}})
  
  :post! (fn [ctx] (future (email/send-forgot-username (:record ctx))))
  :handle-created {})