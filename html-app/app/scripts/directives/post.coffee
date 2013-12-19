'use strict'

angular.module('gratefulplaceApp').directive 'post', ->
  restrict: 'E'
  scope:
    post: '=model'
    context: '='
  controller: ['$scope', 'Authorize', 'Post', 'Topic', 'User', ($scope, Authorize, Post, Topic, User)->
    $scope.visibility = {}
    console.log "Context", $scope.context
    if $scope.context == "profile"
      $scope.visibility.topic = true
    else
      $scope.visibility.topic = false
    
    postResource = ->
      new Post($scope.post)

    $scope.showEdit = ->
      $scope.post && !$scope.post.deleted && Authorize.canModifyContent($scope.post)

    $scope.toggleEdit = ($event)->
      if Authorize.canModifyContent($scope.post)
        $scope.post.editing = !$scope.post.editing
      $event && $event.preventDefault()

    $scope.updatePost = ->
      Post.update($scope.post, (p)->
        $scope.toggleEdit()
      , (res)->
        $scope.errors = res.data.errors
      )

    deleteSuccess = ->
      $scope.post['formatted-content'] = '<em>deleted</em>'
      $scope.post.deleted = true
      $scope.post.editing = false

    firstPost = ->
      console.log $scope.post, $scope.$parent.firstPost
      $scope.post == $scope.$parent.firstPost
    
    $scope.delete = ->
      if confirm "Are you sure you want to delete this post?"
        if firstPost()
          topic = new Topic(id: $scope.post['topic-id'])
          topic.$delete()
        postResource().$delete deleteSuccess
  ]
  template: """
    <div class="post" ng-class="{editing: post.editing, deleted: post.deleted}">
      <i class="edit icon-pencil" ng-show="showEdit()" ng-click="toggleEdit()"></i>
      <div class="content" btf-markdown="post['content']" ng-show="!post.editing"></div>
      <div class="content-edit" ng-show="post.editing">
        <div class="error" error-messages="errors.content"></div>
        <form ng-submit="updatePost()">
          <div class="error" error-messages="errors.authorization"></div>
          <textarea ng-model="post.content"></textarea>
          <div class="actions">
            <input type="submit" value="Save" class="save" />
            <a href="#"
               ng-click="toggleEdit($event)"
               class="cancel">Cancel</a>
            <input type="button" value="Delete" class="delete" ng-click="delete()" />
            <a href="#" class="toggle-formatting-help">formatting help</a>
          </div>
        </form>
      </div>
      <footer>
        <author model="post.author"></author>
        <date data="post['created-at']"></date>
        <div class="in-topic" ng-show="visibility.topic">
          in
          <a ng-href="/#/topics/{{post['topic-id']}}">{{post['topic']['title']}}</a>
        </div>
        <like-toggle likeable="post"></like-toggle>
      </footer>
    </div>
    """
  replace: true
