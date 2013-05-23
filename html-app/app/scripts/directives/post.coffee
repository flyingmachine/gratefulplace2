angular.module('gratefulplaceApp').directive 'post', ->
  restrict: "E"
  scope:
    post: '='
  controller: ['$scope', ($scope)->
    $scope.formatCreatedAt = (date)->
      moment(date).format("MMM D, YYYY h:mma")
  ]
  template: '<div class="post">
      <div class="content" ng-bind-html-unsafe="post.content">
      </div>
      <footer>
        <div class="author">
          <a href="" ng-click="peekAt(post.author)">
            {{post.author.username}}
          </a>
        </div>
        <div class="date">{{formatCreatedAt(post["created-at"])}}</div>
        <div class="like">
          <a href="#">&#9733;</a>
        </div>
      </footer>
    </div>'
  replace: true