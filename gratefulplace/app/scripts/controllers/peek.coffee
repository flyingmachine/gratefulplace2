'use strict'

angular.module('gratefulplaceApp')
  .controller 'PeekCtrl', ($scope, $rootScope, $routeParams, Peek) ->
    $rootScope.$on("peek.new") (event, payload)->
      $scope.peek = payload