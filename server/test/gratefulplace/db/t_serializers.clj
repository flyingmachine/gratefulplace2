(ns gratefulplace.db.t-serializers
  (:use midje.sweet
        gratefulplace.db.connection)
  (:require [gratefulplace.db.serializers :as ss])
  (:require [flyingmachine.serialize.core :as s]))

(fact "topic serializer correctly serializes attributes"
  (let [topic (s/serialize (one [:topic/title "First topic"]) ss/ent->topic)]
    topic => (contains {:id 17592186045420 :title "First topic" :post-count 2 :author-id 17592186045418})))

(fact "topic serializer correctly serializes relationships"
  (let [first-post (:first-post (s/serialize (one [:topic/title "First topic"]) ss/ent->topic {:include :first-post}))]
    first-post => (contains {:content "T1 First post content"})))
