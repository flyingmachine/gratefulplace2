'use strict'

angular.module('gratefulplaceApp')
  .controller 'ProfilePasswordCtrl', ($scope, User, CurrentSession) ->

    $scope.editing = false
    
    User.get id: CurrentSession.get().id, (user)->
      $scope.user = user

    $scope.peekAt "profile/about-nav", {tab: "password"}

    $scope.updateProfile = ->
      $scope.user.$changePassword((u)->
        $scope.successMessage = "Your password was updated successfully"
      , (res)->
        $scope.errorMessages = res.data.errors
      )

    $scope.$on "$destroy", $scope.clearPeek