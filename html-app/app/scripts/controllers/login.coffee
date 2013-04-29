'use strict'

angular.module('gratefulplaceApp')
  .controller 'LoginCtrl', ($scope, Users) ->
    $scope.registration =
      username: ""
      password: ""
      email: ""
    $scope.submitRegistration = ->
      user = new Users($scope.registration)
      user.$save()
      