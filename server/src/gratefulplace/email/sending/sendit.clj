;; sendit is silly but clojure won't let me use "send"
(ns gratefulplace.email.sending.sendit
  (:require [gratefulplace.config :refer [config]]
            [postal.core :as email]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [stencil.core :as stencil]
            [gratefulplace.utils :refer :all]
            [gratefulplace.email.sending.contents :refer [body]])
  (:import org.apache.commons.mail.HtmlEmail))

(defn send-email*
  [for-reals? params]
  (if for-reals?
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

(defn final-sender-params
  [defaults addl template-name]
  (let [final (merge defaults addl)
        body-data (merge (:body-data defaults) (:body-data addl))]
    (-> final
        (merge {:body (list body template-name body-data)})
        (dissoc :body-data))))

(defn defsender
  [varnames sender-param-defaults sender]
  (let [{:keys [args user-doseq]} varnames
        [sender-name addl-args & sender-params] sender
        template-name (s/replace sender-name #"^send-" "")
        sender-params (final-sender-params sender-param-defaults
                                           (apply hash-map sender-params)
                                           template-name)
        args (into args addl-args)]
    
    (comment (list 'defn sender-name args
                   (list 'doseq user-doseq
                         (list 'send-email sender-params))))
    `(defn ~sender-name
       ~args
       (doseq ~user-doseq
         (send-email ~sender-params)))))

(defmacro defsenders
  [varnames sender-param-defaults & senders]
  `(do ~@(map #(defsender varnames sender-param-defaults %) senders)))