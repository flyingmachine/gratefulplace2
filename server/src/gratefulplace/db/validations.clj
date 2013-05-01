(ns gratefulplace.db.validations
  (:require [gratefulplace.db.query :as db])
  (:import org.mindrot.jbcrypt.BCrypt))

(def user-validations
  {:username
   ["Your username must be between 4 and 24 characters long"
    #(and
      (not (empty? %))
      (>= (count %) 4)
      (<= (count %) 24))
    "That username is already taken"
    #(not (db/one [:user/username %]))]
   
   :password
   ["Your password must be at least 4 characters long"
    #(and
      (not (empty? %))
      (>= (count %) 4))]
   
   :change-password
   ["Your passwords do not match"
    #(= (:new-password %) (:new-password-confirmation %))

    "Your password must be at least 4 characters long"
    #(>= (count (:new-password %)) 4)]
   
   :email
   ["You must enter a valid email address"
    #(and
      (not-empty %)
      (re-find #"@" %))]})

(def user
  {:create (select-keys user-validations [:username :password :email])
   :update-email (select-keys user-validations [:email])
   :change-password (fn [pw]
                      (update-in
                       (select-keys user-validations [:change-password])
                       [:change-password]
                       conj
                       "You didn't enter the correct value for your current password"
                       #(BCrypt/checkpw (:current-password %) pw)))})