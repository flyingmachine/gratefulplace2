'use strict'

angular.module('gratefulplaceApp')
  .controller 'CredentialRecoveryForgotUsernameCtrl', ($rootScope, $scope, ForgotUsernameRequest) ->
    $scope.success = false
    $scope.forgotUsernameRequest = {}
    $scope.submitForgotUsernameRequest = ->
      forgotUsernameRequest = new ForgotUsernameRequest($scope.forgotUsernameRequest)
      forgotUsernameRequest.$save(->
        $scope.success = true
        $scope.forgotUsernameRequest = {}
        $scope.errorMessages = null
      , (res)->
        $scope.errorMessages = res.data.errors
      )
    
    