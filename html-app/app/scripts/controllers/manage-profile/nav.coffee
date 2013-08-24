'use strict'

angular.module('gratefulplaceApp')
  .controller 'ProfileNavCtrl', ($scope, $location)->
    $scope.tab = /\/([^\/]+)$/.exec($location.path())[1]
