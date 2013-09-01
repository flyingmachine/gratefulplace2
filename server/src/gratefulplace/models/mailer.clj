(ns gratefulplace.models.mailer
  (:require [gratefulplace.config :refer [config]]
            [postal.core :as email]
            [clojure.java.io :as io]
            [stencil.core :as stencil]
            [gratefulplace.utils :refer :all])
  (:import org.apache.commons.mail.HtmlEmail))


(defn template-path
  [template-name extension]
  (str "email-templates/" template-name "." extension))

(defn slurp-if-exists
  [x]
  (if x (slurp x)))

(defn template
  [name extension]
  (-> name
      (template-path extension)
      io/resource
      slurp-if-exists))

(defn body
  [template-name data]
  (let [html-template (template template-name "html")
        text-template (template template-name "txt")]
    {:text(stencil/render-string text-template data)
     :html (stencil/render-string html-template data)}))

(defn send-email*
  [deliver? params]
  (if deliver?
    (do
      (doto (HtmlEmail.)
        (.setHostName "smtp.gmail.com")
        (.setSslSmtpPort "465")
        (.setSSL true)
        (.addTo (:to params))
        (.setFrom (config :email :from-address) (config :email :from-name))
        (.setSubject (:subject params))
        (.setTextMsg (:text (:body params)))
        (.setHtmlMsg (:html (:body params)))
        (.setAuthentication (config :email :username) (config :email :password))
        (.send))
      true)
    params))

(defn send-email
  [params]
  (send-email* (config :send-email) params))

(defn send-reply-notification
  "send an email for a new comment"
  [user post topic]
  (send-email {:from (config :email :from-address)
               :to (:email user)
               :subject (str "[Grateful Place] Re: " (:title topic))
               :body (body "reply-notification"
                           {:topic-title (:title topic)
                            :username (:username user)
                            :topic-id (:id topic)
                            :content (:content post)
                            :formatted-content (md-content post)})}))

(defn send-topic-notification
  [users topic]
  (send-email {:from (config :email :from-address)
               :to (map :email users)
               :subject (str "[Grateful Place] " (:title topic))
               :body (body "topic"
                           {:topic-id (:id topic)
                            :content (:content (:post topic))
                            :formatted-content (md-content (:post topic))})}))