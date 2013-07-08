'use strict'

angular.module('gratefulplaceApp').directive 'post', ->
  restrict: 'EA'
  scope:
    post: '=model'
    firstPost: '&'
  controller: ['$scope', 'Authorize', 'Post', 'Topic', 'User', ($scope, Authorize, Post, Topic, User)->
    postResource = ->
      new Post($scope.post)

    $scope.showEdit = ->
      $scope.post && !$scope.post.deleted && Authorize.canModifyContent($scope.post)

    $scope.toggleEdit = ($event)->
      if Authorize.canModifyContent($scope.post)
        $scope.post.editing = !$scope.post.editing
      $event && $event.preventDefault()

    $scope.updatePost = ->
      postResource().$save((p)->
        $scope.toggleEdit()
        $scope.post['formatted-content'] = p['formatted-content']
      , (res)->
        $scope.errors = res.data.errors
      )

    deleteSuccess = ->
      $scope.post['formatted-content'] = '<em>deleted</em>'
      $scope.post.deleted = true
      $scope.post.editing = false
    
    $scope.delete = ->
      if confirm "Are you sure you want to delete this post?"
        if $scope.firstPost()
          topic = new Topic(id: $scope.post['topic-id'])
          topic.$delete()
        postResource().$delete deleteSuccess
  ]
  template: """
    <div class="post" ng-class="{editing: post.editing, deleted: post.deleted}">
      <i class="edit icon-pencil" ng-show="showEdit()" ng-click="toggleEdit()"></i>
      <div class="content"
           ng-bind-html-unsafe="post[\'formatted-content\']"
           ng-show="!post.editing">
      </div>
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
        <div author="post.author"></div>
        <date data="post['created-at']"></date>
        <like-toggle likeable="post"></like-toggle>
      </footer>
    </div>
    """
  replace: true
