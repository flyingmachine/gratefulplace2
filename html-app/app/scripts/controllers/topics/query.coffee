'use strict'

angular.module('gratefulplaceApp').controller 'TopicsQueryCtrl', ($scope, $location, Topic, Watch, User, Utils, Authorize) ->
  currentPage = ->
    if $location.search().page
      parseInt $location.search().page
    else
      1
  
  $scope.topics = []
  $scope.paginationData = {'current-page': currentPage()}
  
  $scope.$on 'topic.created', (e, topic)->
    $scope.topics.unshift topic

  watches = null

  receiveData = (data)->
    _.merge($scope.paginationData, data[0])
    $scope.topics = _.rest(data)
    if !_.isEmpty($scope.topics)
      $scope.stats['last-post-at'] = data[1]['last-posted-to-at']
    Utils.addWatchCountToTopics($scope.topics, watches)

  query = ->
    params = {page: $scope.paginationData['current-page']}
    Topic.query params, receiveData
  query()

  $scope.$on 'auth.logged-in', query

  $scope.newTopicForm =
    show: false
    toggle: ($event)->
      $event && $event.preventDefault()
      $scope.newTopicForm.show = !$scope.newTopicForm.show

  setLoggedInStuff = ->
    if $scope.currentSession.loggedIn() && !$scope.currentSession.lastTopic?
      Watch.query (data)->
        watches = data
        Utils.addWatchCountToTopics($scope.topics, watches)

  $scope.$watch 'currentSession', setLoggedInStuff
  

  $scope.firstPost = (topic)->
    topic.posts[0]

  $scope.formatDateTime = (date)->
    moment(date).format("MMM D, YYYY h:mma")