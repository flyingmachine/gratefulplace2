'use strict'

angular.module('gratefulplaceApp')
  .controller 'CredentialRecoveryForgotUsernameCtrl', ($rootScope, $scope, ForgotUsernameRequest) ->
    $scope.forgotUsernameRequest = {}
    $scope.submitForgotUsernameRequest = ->
      forgotUsernameRequest = new ForgotUsernameRequest($scope.forgotUsernameRequest)
      forgotUsernameRequest.$save(->
        $scope.success = true
        $scope.forgotUsernameRequest = {}
      , (res)->
        $scope.errorMessages = res.data.errors
      )
    
    