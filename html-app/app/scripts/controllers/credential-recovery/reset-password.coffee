'use strict'

angular.module('gratefulplaceApp')
  .controller 'CredentialRecoveryResetPasswordCtrl', ($scope, $routeParams, ForgotPasswordRequest) ->
    stage = 'checkingToken'
    $scope.stage = (comp)->
      comp == stage

    ForgotPasswordRequest.get token: $routeParams.token, ->
      stage = 'validToken'
    , ->
      stage = 'invalidToken'
    
    $scope.success = false
    $scope.forgotPasswordRequest = {}
    $scope.submitForgotPasswordRequest = ->
      forgotPasswordRequest = new ForgotPasswordRequest($scope.forgotPasswordRequest)
      forgotPasswordRequest.$save(->
        $scope.success = true
        $scope.forgotPasswordRequest = {}
        $scope.errorMessages = null
      , (res)->
        $scope.errorMessages = res.data.errors
      )
    
    