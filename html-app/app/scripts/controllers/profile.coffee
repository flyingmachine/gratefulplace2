'use strict'

angular.module('gratefulplaceApp')
  .controller 'ProfileCtrl', ($scope, User, CurrentSession) ->
    User.get id: CurrentSession.get().id, (user)->
      $scope.user = user
    