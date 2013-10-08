(ns gratefulplace.email.send.senders
  (:require [gratefulplace.email.sending.send :refer [defsender]]
            [gratefulplace.utils :refer :all]))

(defsender send-reply-notification
  [post topic]
  :subject (str "[Grateful Place] Re: " (:title topic))
  :body {:topic-title (:title topic)
         :username (:username user)
         :topic-id (:id topic)
         :content (:content post)
         :formatted-content (md-content post)})

(defsender send-new-topic-notification
  [topic]
  :subject (str "[Grateful Place] " (:title topic))
  :body {:topic-title (:title topic)
         :topic-id (:id topic)
         :username (:username user)
         :content (:content (:post topic))
         :formatted-content (md-content (:post topic))})