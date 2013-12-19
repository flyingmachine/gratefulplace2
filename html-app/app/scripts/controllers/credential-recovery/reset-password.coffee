'use strict'

angular.module('gratefulplaceApp')
  .controller 'CredentialRecoveryResetPasswordCtrl', ($scope, $routeParams, ForgotPasswordRequest) ->
    stage = 'checkingToken'
    $scope.forgotPasswordRequest = token: $routeParams.token
    $scope.stage = (comp)->
      comp == stage

    ForgotPasswordRequest.get $scope.forgotPasswordRequest,
      -> stage = 'validToken',
      -> stage = 'invalidToken'
    
    $scope.success = false
    $scope.resetPassword = ->
      request = new ForgotPasswordRequest($scope.forgotPasswordRequest)
      ForgotPasswordRequest.update(request, ->
        $scope.success = true
        $scope.forgotPasswordRequest = {}
        $scope.errorMessages = null
      , (res)->
        $scope.errors = res.data.errors
      )
    
    