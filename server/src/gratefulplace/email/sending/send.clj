(ns gratefulplace.email.sending.send
  (:require [gratefulplace.config :refer [config]]
            [postal.core :as email]
            [clojure.java.io :as io]
            [stencil.core :as stencil]
            [gratefulplace.utils :refer :all]
            [gratefulplace.email.send.content :refer [body]])
  (:import org.apache.commons.mail.HtmlEmail))

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

(defmacro defsender
  [name config]
  `(defn ~name
     [~@()]
     (send-email {:from (config :email :from-address)
                  :to (:email user)})))