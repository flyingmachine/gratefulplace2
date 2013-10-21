'use strict'

angular.module('gratefulplaceApp')
  .controller 'TopicsShowCtrl', ($rootScope, $scope, $routeParams, Topic, Post, User, Watch, Authorize) ->
    Topic.get id: $routeParams.id, (topic)->
      $scope.topic = topic
      $scope.firstPost = $scope.topic.posts.shift()
      $rootScope.$broadcast "view.topic", topic
  
    findWatch = ->
      if $scope.topic
        _.find $scope.topic.watches, (watch)->
          watch['user-id'] == $scope.currentSession.id
        
    $scope.submitReply = ->
      post = new Post($scope.reply)
      post['topic-id'] = $scope.topic.id
      post.$save((p)->
        $scope.topic.posts.push(p)
        if !findWatch()
          Watch.query (data)->
            newWatch = _.find data, (watch)->
              watch['topic-id'] == $scope.topic.id && watch['user-id'] == $scope.currentSession.id
            $scope.topic.watches.push(newWatch)
  
      , (res)->
        $scope.errors = res.data.errors
      )
      $scope.reply = {}
