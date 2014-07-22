'use strict'

angular.module('gratefulplaceApp').directive 'likeToggle', ->
  restrict: 'EA'
  scope:
    post: '=likeable'
  controller: ['$scope', 'Like', 'CurrentSession', ($scope, Like, CurrentSession)->
    $scope.currentSession = CurrentSession.get()
    
    newLike = ->
      new Like('post-id': $scope.post.id)
      
    like = ->
      newLike().$save ->
        $scope.post.likers.push $scope.currentSession.id

    unlike = ->
      toDelete = newLike()
      toDelete.id = toDelete['post-id']
      toDelete.$delete ->
        $scope.post.likers = _.without($scope.post.likers, $scope.currentSession.id)

    $scope.toggleLike = ->
      if $scope.liked()
        unlike()
      else
        like()  

    $scope.liked = ->
      if $scope.post
        _.include $scope.post.likers, $scope.currentSession.id
  ]
  template: """
  <div class="like" ng-class="{liked: liked()}" ng-click="toggleLike()">
    <span ng-show="post.likers.length">{{post.likers.length}}</span>
    <i class="icon-thumbs-up"></i>
  </div>
  """
  replace: true
