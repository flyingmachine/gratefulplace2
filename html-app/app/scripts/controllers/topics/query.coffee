'use strict'

angular.module('gratefulplaceApp').controller 'TopicsQueryCtrl', ($scope, Topic, Watch, User) ->
  $scope.$on 'topic.created', (e, topic)->
    $scope.topics.unshift topic

  watches = null

  addWatchCounts = ->
    if $scope.topics && watches
      _.each watches, (watch)->
        console.log watch
        _.each $scope.topics, (topic)->
          if watch['topic-id'] == topic.id && watch['unread-count']
            topic['unread-count'] = watch['unread-count']
            false

  Topic.query (data)->
    console.log data
    $scope.topics = data
    addWatchCounts()

  Watch.query (data)->
    watches = data
    addWatchCounts()

  

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
