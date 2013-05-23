angular.module('gratefulplaceApp').directive 'post', ->
  restrict: "E"
  scope:
    post: '='
  template: '<div class="post">
      <div class="content" ng-bind-html-unsafe="post.content">
      </div>
      <footer>
        <div class="date">{{post.date}}</div>
        <div class="author">
          <a href="" ng-click="peekAt(post.author)">
            {{post.author.username}}
          </a>
        </div>
      </footer>
    </div>'
  replace: true