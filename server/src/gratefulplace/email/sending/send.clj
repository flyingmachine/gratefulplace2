(ns gratefulplace.email.sending.send
  (:require [gratefulplace.config :refer [config]]
            [postal.core :as email]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [stencil.core :as stencil]
            [gratefulplace.utils :refer :all]
            [gratefulplace.email.sending.content :refer [body]])
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

(defn parse-sender-config
  [sender-config template-name]
  (let [sender-config (into {} (map vec (partition 2 sender-config)))]
    (merge sender-config
           {:body (list body template-name (:body sender-config))})))

(defmacro defsender
  [name args & sender-config]
  (let [defaults (quote {:from (gratefulplace.config/config :email :from-address)
                         :to (:email user)})
        template-name (s/replace name #"^send-" "")
        sender-config (merge defaults (parse-sender-config sender-config template-name))]
    `(defn ~name
       ~(into ['user] args)
       (send-email ~sender-config))))