(ns gratefulplace.middleware.auth
  (:require [gratefulplace.controllers.users :as users]
            [gratefulplace.db.query :as q]
            [datomic.api :as d]
            [gratefulplace.db.maprules :as mr]
            [flyingmachine.cartographer.core :as c]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])))


(defn user
  [username]
  (if-let [user-ent (-> (d/q '[:find ?e :in $ ?username 
                               :where
                               [?e :user/username ?u] 
                               [(clojure.string/lower-case ?u) ?lower-username]
                               [(= ?lower-username ?username)]], 
                             (q/db)
                             (clojure.string/lower-case username))
                        ffirst
                        q/ent)]
    (c/mapify user-ent mr/ent->userauth)))

(defn credential-fn
  [username]
  (user username))

(defn session-store-authorize [{:keys [uri request-method params session]}]
  (when (nil? (:cemerick.friend/identity session))
    (if-let [username (get-in session [:cemerick.friend/identity :current])]
      (workflows/make-auth (select-keys (user username) [:id :username])))))

(defn auth
  [ring-app]
  (friend/authenticate
   ring-app
   {:credential-fn (partial creds/bcrypt-credential-fn credential-fn)
    :workflows [(workflows/interactive-form
                 :redirect-on-auth? false
                 :login-failure-handler (fn [req] {:body {:errors {:username ["invalid username or password"]}} :status 401}))
                users/attempt-registration
                session-store-authorize]
    :redirect-on-auth? false
    :login-uri "/login"
    :unauthorized-redirect-uri "/login"}))
