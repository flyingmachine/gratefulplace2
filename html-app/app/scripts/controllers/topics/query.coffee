'use strict'

angular.module('gratefulplaceApp').controller 'TopicsQueryCtrl', ($scope, $location, Topic, Watch, User, Support, Utils) ->
  currentPage = ->
    if $location.search().page
      parseInt $location.search().page
    else
      1
  
  $scope.topics = []
  $scope.paginationData = {'current-page': currentPage()}
  debugger

  
  $scope.$on 'topic.created', (e, topic)->
    $scope.topics.unshift topic

  watches = null

  receiveData = (data)->
    _.merge($scope.paginationData, data[0])
    $scope.topics = _.rest(data)
    Utils.addWatchCountToTopics($scope.topics, watches)

  query = ->
    params = {page: $scope.paginationData['current-page']}
    Topic.query params, receiveData
  query()

  $scope.newTopicForm =
    show: false
    toggle: ($event)->
      $event && $event.preventDefault()
      $scope.newTopicForm.show = !$scope.newTopicForm.show

  setLoggedInStuff = ->
    if $scope.currentSession.loggedIn() && !$scope.currentSession.lastTopic?
      today = moment().hour(0)
      $scope.currentSession.lastTopicTime = _.find($scope.topics, (topic)->
        topic.author.id == $scope.currentSession.id
      )
      if $scope.currentSession.lastTopicTime
        $scope.currentSession.lastTopicTime = moment($scope.currentSession.lastTopicTime['first-post']['created-at'])
        $scope.currentSession.hasPostedToday = $scope.currentSession.lastTopicTime && today.isBefore($scope.currentSession.lastTopicTime)
      
      if !$scope.currentSession.hasPostedToday || $scope.currentSession.new
        $scope.newTopicForm.show = true
      else
        $scope.newTopicForm.show = false
      Watch.query (data)->
        watches = data
        Utils.addWatchCountToTopics($scope.topics, watches)

  $scope.$watch 'currentSession', setLoggedInStuff
  

  $scope.firstPost = (topic)->
    topic.posts[0]

  $scope.formatDateTime = (date)->
    moment(date).format("MMM D, YYYY h:mma")

  Support.secondaryNav.show "topics", $scope.newTopicForm