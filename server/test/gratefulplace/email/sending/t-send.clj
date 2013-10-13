(ns gratefulplace.email.sending.t-send
  (:require [gratefulplace.email.sending.send :as email]
            [gratefulplace.email.sending.contents :as email-content]
            [gratefulplace.config :refer [config]])
  (:use midje.sweet))

(def template-vars
  {:var1 "a"
   :var2 "b"
   :htmlvar1 "<p>a</p>"
   :htmlvar2 "<p>b</p>"})

(def mail-options
  {:from (config :email :from-address)
    :to (config :email :test-recipient)
    :subject "[Grateful Place] test"
    :body (email-content/body "test" template-vars)})

(fact "if deliver? is false, return params"
  (email/send-email*
   false
   {:from (config :email :from-address)
    :to (config :email :test-recipient)
    :subject "[Grateful Place] test"
    :body (email-content/body "test"
                              {:var1 "a"
                               :var2 "b"
                               :htmlvar1 "<p>a</p>"
                               :htmlvar2 "<p>b</p>"})})
  => {:body {:html "<p>Test <p>a</p> &lt;p&gt;b&lt;/p&gt;</p>\n"
             :text "Test a b\n"}
      :from "notifications@gratefulplace.com"
      :subject "[Grateful Place] test"
      :to "nonrecursive@gmail.com"})

(fact "if deliver? is false, return params"
  (email/send-email* false mail-options)
  => {:body {:html "<p>Test <p>a</p> &lt;p&gt;b&lt;/p&gt;</p>\n"
             :text "Test a b\n"}
      :from "notifications@gratefulplace.com"
      :subject "[Grateful Place] test"
      :to "nonrecursive@gmail.com"})

(fact "if deliver? is true, return true"
  (email/send-email* true mail-options)
  => true)