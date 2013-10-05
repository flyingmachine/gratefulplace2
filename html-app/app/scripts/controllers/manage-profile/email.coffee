'use strict'

angular.module('gratefulplaceApp')
  .controller 'ManageProfileEmailCtrl', ($scope, User, CurrentSession, preferences) ->
    User.get id: CurrentSession.get().id, (user)->
      $scope.user = user
      $scope.preferences = _.map preferences, (descr, key)->
        descr: descr
        key: key
      console.log $scope.preferences
    $scope.updateEmail = ->
      $scope.successMessage = null
      $scope.errors = null
      $scope.user.$save((u)->
        $scope.successMessage = "Your preferences were updated successfully"
      , (res)->
        $scope.errors = res.data.errors
      )