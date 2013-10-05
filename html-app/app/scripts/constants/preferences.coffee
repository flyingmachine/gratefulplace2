'use strict'

angular.module('gratefulplaceApp')
  .constant 'preferences', 
    'receive-watch-notifications': "Receive email when someone posts to a topic you're watching"
    'receive-new-topic-notifications': "Receive an email when someone posts a new topic"
    'receive-daily-digest': "Receive a daily digest"
    'receive-weekly-digest': "Receive a weekly digest"
    'receive-like-notifications': "Receive email when someone likes one of your posts"
    # 'receive-signup-notifications': "Receive an email when a new user signs up"