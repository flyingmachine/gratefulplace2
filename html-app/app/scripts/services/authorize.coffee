'use strict'
angular.module("gratefulplaceApp").factory "Authorize", (CurrentSession)->
  canModifyContent: (content)->
    sess = CurrentSession.get()
    return false unless sess && content
    content['author-id'] == sess.id