'use strict'
angular.module("gratefulplaceApp").factory "Authorize", (CurrentSession, $location)->
  moderators = ['flyingmachine']
  requireLogin: ()->
    unless CurrentSession.get().loggedIn()
      $location.path "/"
  canModifyContent: (content)->
    sess = CurrentSession.get()
    return false unless sess && content
    (content['author-id'] == sess.id || _.find(moderators, (m)-> m == sess.username))