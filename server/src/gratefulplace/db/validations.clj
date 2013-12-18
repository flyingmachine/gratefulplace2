(ns gratefulplace.db.validations
  (:require [com.flyingmachine.datomic-junk :as dj]
            [flyingmachine.webutils.utils :refer :all]
            [clj-time.core :as time])
  (:import org.mindrot.jbcrypt.BCrypt
           (org.joda.time DateTime)))

(def user-validations
  {:username
   ["Your username must be between 4 and 24 characters long"
    #(and
      (not-empty %)
      (>= (count %) 4)
      (<= (count %) 24))
    "That username is already taken"
    #(or
      (empty? %)
      (not (dj/one [:user/username %])))]
   
   :password
   ["Your password must be at least 4 characters long"
    #(and
      (not-empty %)
      (>= (count %) 4))]
   
   :email
   ["You must enter a valid email address"
    #(and
      (not-empty %)
      (re-find #"@" %))

    "That email address is already taken"
    #(or
      (empty? %)
      (not (dj/one [:user/email %])))]})

(def user
  {:create (select-keys user-validations [:username :password :email])
   :update (select-keys user-validations [:email])})

(defn email-update
  [existing-user]
  (let [email-check (into [] (take 3 (:email user-validations)))]
    {:email
     (conj email-check
           (fn [email]
             (or
              (empty? email)
              (if-let [user (dj/one [:user/email email])]
                (= (:id existing-user) (:db/id user))
                true))))}))

(def change-password
  {:new-password
   ["Your passwords do not match"
    #(= (:new-password %) (:new-password-confirmation %))

    "Your password must be at least 4 characters long"
    #(>= (count (:new-password %)) 4)]
   
   :current-password
   ["You didn't enter the correct value for your current password"
    #(BCrypt/checkpw (:current-password %) (:password %))]})

(def post-validations
  {:content
   ["Your post can't be blank."
    #(not-empty %)]

   :topic-id
   ["Your post must have a topic"
    #(not (nil? %))]})

(def post
  {:create post-validations
   :update (select-keys post-validations [:content])})

(def topic
  {:content
   ["Your post can't be blank."
    #(not-empty %)]})

(def forgot-username
  {:email
   ["Your email address can't be blank."
    #(not-empty %)

    "That doesn't really look like an email address."
    #(or
      (empty? %)
      (re-seq #"@" %))]})

(def forgot-password
  {:username
   ["Please enter a username"
    #(not-empty %)]})

(def password-reset-token
  {:token
   ["Your password reset token is invalid. Please go through the password reset process again."
    (fn [token]
      (if-let [user (dj/one [:user/password-reset-token token])]
        ;; 24 hour expiration
        (time/after? (time/plus (DateTime. (:user/password-reset-token-generated-at user))
                                (time/days 1))
                     (time/now))))]})

(def password-reset
  (merge password-reset-token
         (select-keys change-password [:new-password])))