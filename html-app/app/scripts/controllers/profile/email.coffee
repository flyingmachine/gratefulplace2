'use strict'

angular.module('gratefulplaceApp')
  .controller 'ProfileEmailCtrl', ($scope, User, CurrentSession) ->
    User.get id: CurrentSession.get().id, (user)->
      $scope.user = user

    $scope.updateEmail = ->
      $scope.successMessage = null
      $scope.errors = null
      $scope.user.$save((u)->
        $scope.successMessage = "Your preferences were updated successfully"
      , (res)->
        $scope.errors = res.data.errors
      )

    $scope.support.secondaryNav.show "profile/about-nav", {tab: "email"}
