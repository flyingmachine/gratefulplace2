'use strict'

angular.module('gratefulplaceApp').controller 'TopicsQueryCtrl', ($scope, Topic, User) ->
  $scope.$on 'topic.created', (e, topic)->
    $scope.topics.unshift topic
  
  Topic.query (data)->
    $scope.topics = data

  $scope.firstPost = (topic)->
    topic.posts[0]

  $scope.formatDateTime = (date)->
    moment(date).format("MMM D, YYYY h:mma")

  $scope.peekAtAuthor = (author)->
    User.get id: author.id, (data)->
      $scope.peek.show("user", data)

  $scope.secondaryNav.show "topics"
  
  $scope.formatPostCount = (postCount)->
    switch postCount
      when 1 then "no replies"
      when 2 then "1 reply"
      else "#{postCount - 1} replies"