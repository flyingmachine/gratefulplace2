(ns gratefulplace.models.mailer
  (:require [gratefulplace.config :refer [config]]
            [postal.core :as email]
            [clojure.java.io :as io]
            [stencil.core :as stencil]
            [gratefulplace.utils :refer :all]))


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
    (filter identity
            [(if html-template
               {:type "text/html"
                :content (stencil/render-string html-template data)})
             (if text-template
               {:type "text/plain"
                :content (stencil/render-string text-template data)})])))

(defn send-email
  [params]
  (if-not (config :send-email)
    params
    (email/send-message ^{:host (config :email :host)
                          :user (config :email :username)
                          :pass (config :email :password)
                          :ssl true}
     params)))

(defn send-reply-notification
  "send an email for a new comment"
  [users post topic]
  (send-email {:from (config :email :from-address)
               :to (map :email users)
               :subject (str "[Grateful Place] Re: " (:title topic))
               :body (body "reply-notification"
                           {:topic-title (:title topic)
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