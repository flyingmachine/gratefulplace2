(ns gratefulplace.email.sending.senders
  (:require [gratefulplace.email.sending.send :refer [defsenders]]
            [gratefulplace.utils :refer :all]
            [gratefulplace.config :refer [config]]))

;; Topics/posts
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
   :body-data {:content (:content (:first-post topic))
               :formatted-content (md-content (:first-post topic))}))


(defsenders
  {:args [users]
   :user-doseq [user users]}
  {:from (config :email :from-address)
   :to (:user/email user)
   :body-data {:username (:user/username user)}}

  (send-forgot-username
   []
   :subject (str "Your Grateful Place username"))

  (send-password-reset-token
   []
   :subject (str "Grateful Place password reset")
   :body-data {:token (:user/password-reset-token user)}))

