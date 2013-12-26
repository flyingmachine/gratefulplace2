(ns gratefulplace.controllers.t-posts
  (:require [gratefulplace.controllers.posts :as posts]
            [rabble.test.controller-helpers :refer :all])
  (:use midje.sweet
        gratefulplace.paths))

(setup-db-background)

;; Creation
(fact "creating a valid post with a valid user results in success"
  (response-data :post "/posts" {:content "test" :topic-id (topic-id)} (auth))
  => (contains {"topic-id" (topic-id)}))

(fact "creating a valid post without a user results in failure"
  (res :post "/posts" {:content "test" :topic-id (topic-id)} nil)
  => (contains {:status 401}))

(fact "creating a post without content returns errors"
  (res :post "/posts" {:topic-id (topic-id)} (auth))
  => (contains {:status 400}))

(fact "creating a post without a topic id returns errors"
  (res :post "/posts" {:content "test"} nil)
  => (contains {:status 400}))

(facts "posts can only be updated by their authors or moderators"
  (fact "updating a post as the author results in success"
    (let [username "flyingmachine"
          post-id (post-id username)]
      (response-data :put (post-path post-id) {:content "new content"} (auth username))
      => (contains {"id" post-id
                    "content" "new content"}))))

(facts "posts can only be deleted by their authors or moderators"
  (fact "deleting a post as the author results in success"
    (res :delete (post-path (post-id "flyingmachine")) nil (auth "flyingmachine"))
    => (contains {:status 204}))
  (fact "deleting a post as a moderator results in success"
    (res :delete (post-path (post-id "joebob")) nil (auth "flyingmachine"))
    => (contains {:status 204}))
  (fact "deleting a post as not the author results in failure"
    (res :delete (post-path (post-id "flyingmachine")) nil (auth "joebob"))
    => (contains {:status 401})))

(fact "you can't update a deleted post"
  (let [username "flyingmachine"
        auth (auth username)
        post-id (post-id username)]
    (do (res :delete (post-path post-id) nil auth)
        (res :put (post-path post-id) {:content "new content"} auth))
    => (contains {:status 401})))