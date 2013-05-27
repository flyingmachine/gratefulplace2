angular.module('gratefulplaceApp').directive 'post', ->
  restrict: "E"
  scope:
    post: '='
  controller: ['$scope', 'Authorize', 'Post', ($scope, Authorize, Post)->
    $scope.formatCreatedAt = (date)->
      moment(date).format("MMM D, YYYY h:mma")

    $scope.showEdit = ->
      Authorize.canModifyContent($scope.post)

    $scope.toggleEdit = ($event)->
      if Authorize.canModifyContent($scope.post)
        $scope.post.editing = !$scope.post.editing
      $event && $event.preventDefault()

    $scope.updatePost = ->
      post = new Post($scope.post)
      post.$save((p)->
        $scope.post = p
        $scope.toggleEdit()
      , (res)->
        $scope.errors = res.data.errors
      )
  ]
  template: '
    <div class="post" ng-class="{editing: post.editing}">
      <div class="error" error-messages="errors.authorization"></div>
      <i class="edit icon-pencil"
         ng-show="showEdit()"
         ng-click="toggleEdit()"></i>
      <div class="content"
           ng-bind-html-unsafe="post.content"
           ng-show="!post.editing">
      </div>
      <div class="error" error-messages="errors.content"></div>
      <div class="content-edit" ng-show="post.editing">
        <form ng-submit="updatePost()">
          <textarea ng-model="post.content"></textarea>
          <div class="actions">
            <input type="submit" value="Save" class="save" />
            <a href="#"
               ng-click="toggleEdit($event)"
               class="cancel">Cancel</a>
            <input type="submit" value="Delete" class="delete" />
          </div>
        </form>
      </div>
      <footer>
        <div class="author">
          <a href="" ng-click="peekAt(post.author)">
            {{post.author.username}}
          </a>
        </div>
        <div class="date">{{formatCreatedAt(post["created-at"])}}</div>
        <i class="like icon-thumbs-up"></i>
      </footer>
    </div>'
  replace: true