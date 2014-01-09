'use strict'

angular.module('gratefulplaceApp')
  .controller 'ManageProfileEmailCtrl', ($scope, User, CurrentSession, preferences, Authorize) ->
    Authorize.requireLogin()
    User.get id: CurrentSession.get().id, (user)->
      $scope.user = user
      $scope.preferences = user.preferences || []
      
      $scope.checked = (pref)->
        _.contains($scope.user.preferences, pref.key)
        
      $scope.preferences = _.map preferences, (descr, key)->
        descr: descr
        key: key
        checked: $scope.checked(key: key)
        
    $scope.updateEmail = ->
      $scope.user.preferences = _.map(_.filter($scope.preferences, checked: true), 'key')
      $scope.successMessage = null
      $scope.errors = null
      User.update $scope.user, (u)->
        $scope.successMessage = "Your preferences were updated successfully"
      , (res)->
        $scope.errors = res.data.errors
      