(ns gratefulplace.email.sending.senders
  (:require [gratefulplace.email.sending.send :refer [defsenders]]
            [gratefulplace.utils :refer :all]
            [gratefulplace.config :refer [config]]))

(defsenders
  {:args [users topic]
   :user-doseq [user users]}
  {:from (config :email :from-address)
   :to (:user/email user)
   :body-data {:topic-title (:title topic)
               :topic-id (:id topic)
               :username (:user/username user)}}

  (send-reply-notification
   [post]
   :subject (str "[Grateful Place] Re: " (:title topic))
   :body-data {:content (:content post)
               :formatted-content (md-content post)})
  
  (send-new-topic-notification
   []
   :subject (str "[Grateful Place] " (:title topic))
   :body-data {:content (:content (:post topic))
               :formatted-content (md-content (:post topic))}))
