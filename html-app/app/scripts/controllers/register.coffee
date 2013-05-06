'use strict'

angular.module('gratefulplaceApp')
  .controller 'RegisterCtrl', ($scope, $location, Users) ->
    $scope.errors = {}
    
    $scope.registration =
      username: ""
      password: ""
      email: ""

    registrationSuccess = ->
      $location.path "/"
      $scope.$apply()

    registrationFailure = (response)->
      $scope.errors = response.data.errors
      
    $scope.submitRegistration = ->
      user = new Users($scope.registration)
      user.$save([], registrationSuccess, registrationFailure)
      