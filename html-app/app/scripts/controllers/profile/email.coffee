'use strict'

angular.module('gratefulplaceApp')
  .controller 'ProfileEmailCtrl', ($scope, User, CurrentSession) ->
    User.get id: CurrentSession.get().id, (user)->
      $scope.user = user

    $scope.updateEmail = ->
      $scope.user.$saveEmail((u)->
        $scope.user['email'] = u['email']
        $scope.successMessage = "Your email was updated successfully"
      , (res)->
        $scope.errors = res.data.errors
      )

    $scope.secondaryNav.show "profile/about-nav", {tab: "email"}