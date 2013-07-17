'use strict'

angular.module('gratefulplaceApp').controller 'TopicsQueryCtrl', ($scope, Topic, Watch, User, Support, Utils) ->
  $scope.$on 'topic.created', (e, topic)->
    $scope.topics.unshift topic

  watches = null

  Topic.query (data)->
    $scope.topics = data
    Utils.addWatchCountToTopics($scope.topics, watches)

  $scope.$watch 'currentSession', ->
    if $scope.currentSession
      Watch.query (data)->
        watches = data
        Utils.addWatchCountToTopics($scope.topics, watches)

  $scope.firstPost = (topic)->
    topic.posts[0]

  $scope.formatDateTime = (date)->
    moment(date).format("MMM D, YYYY h:mma")

  Support.secondaryNav.show "topics"
  
  $scope.formatPostCount = (postCount)->
    switch postCount
      when 1 then "no replies"
      when 2 then "1 reply"
      else "#{postCount - 1} replies"
