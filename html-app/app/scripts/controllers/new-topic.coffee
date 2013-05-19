'use strict'

angular.module('gratefulplaceApp')
  .controller 'NewTopicCtrl', ($rootScope, $scope, $routeParams, $resource, Topic, CurrentSession) ->
    $scope.submitNewTopic = ->
      topic = new Topic($scope.newTopic)
      topic.$save (t, x)->
        $rootScope.$broadcast('topic.created', t)
        $scope.newTopic = null