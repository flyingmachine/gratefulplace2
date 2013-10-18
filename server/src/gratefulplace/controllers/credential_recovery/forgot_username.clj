(ns gratefulplace.controllers.credential-recovery.forgot-username
  (:require [gratefulplace.db.query :as db]
            [gratefulplace.db.validations :as validations]
            [gratefulplace.email.sending.senders :as email]
            [gratefulplace.controllers.shared :refer :all]
            [liberator.core :refer [defresource]]
            [gratefulplace.utils :refer :all]))

;; TODO send this all here

(defresource create! [params]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  
  :malformed? (validator params validations/forgot-username)
  :handle-malformed errors-in-ctx
  
  :post! (fn [_] {})
  :handle-created {})