'use strict'

angular.module('gratefulplaceApp')
  .controller 'AdminUsersQueryCtrl', ($scope, $routeParams, AdminUser) ->
    $scope.users = AdminUser.query()