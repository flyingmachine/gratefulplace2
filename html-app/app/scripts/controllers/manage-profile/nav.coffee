'use strict'

angular.module('gratefulplaceApp')
  .controller 'ManageProfileNavCtrl', ($scope, $location)->
    $scope.tab = /\/([^\/]+)$/.exec($location.path())[1]
