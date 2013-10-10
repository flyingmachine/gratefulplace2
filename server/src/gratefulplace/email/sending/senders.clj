(ns gratefulplace.email.sending.senders
  (:require [gratefulplace.email.sending.send :refer [defsenders]]
            [gratefulplace.utils :refer :all]))

(defsenders
  {:args [users topic]
   :user-doseq [user users]}
  {:from (config :email :from-address)
   :to (:email user)
   :body-data {:topic-title (:title topic)
               :topic-id (:id topic)
               :username (:username user)}}

  (send-reply-notification
   [post topic]
   :subject (str "[Grateful Place] Re: " (:title topic))
   :body-data {:topic-title (:title topic)
               :topic-id (:id topic)
               :username (:username user)
               :content (:content post)
               :formatted-content (md-content post)})
  
  (send-new-topic-notification
   [topic]
   :subject (str "[Grateful Place] " (:title topic))
   :body-data {:topic-title (:title topic)
               :topic-id (:id topic)
               :username (:username user)
               :content (:content (:post topic))
               :formatted-content (md-content (:post topic))}))