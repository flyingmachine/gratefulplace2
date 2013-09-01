(ns gratefulplace.models.t-mailer
  (:require [gratefulplace.models.mailer :as mailer]
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
    :body (mailer/body "test" template-vars)})

(fact "body returns applied template for text and html"
  (mailer/body "test" template-vars)
  => (contains {:text "Test a b\n"}
               {:html "<p>Test <p>a</p> &lt;p&gt;b&lt;/p&gt;</p>\n"}
               :in-any-order))

(fact "if deliver? is false, return params"
  (mailer/send-email*
   false
   {:from (config :email :from-address)
    :to (config :email :test-recipient)
    :subject "[Grateful Place] test"
    :body (mailer/body "test"
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
  (mailer/send-email* false mail-options)
  => {:body {:html "<p>Test <p>a</p> &lt;p&gt;b&lt;/p&gt;</p>\n"
             :text "Test a b\n"}
      :from "notifications@gratefulplace.com"
      :subject "[Grateful Place] test"
      :to "nonrecursive@gmail.com"})

(fact "if deliver? is true, return true"
  (mailer/send-email* true mail-options)
  => true)