'use strict'

angular.module('gratefulplaceApp').directive 'topic', ->
  restrict: 'EA'
  scope:
    topic: '=model'
    peek: '='
  controller: ['$scope', 'Authorize', 'Post', 'Topic', 'User', ($scope, Authorize, Post, Topic, User)->
    $scope.formatPostCount = (postCount)->
      switch postCount
        when 1 then "no replies"
        when 2 then "1 reply"
        else "#{postCount - 1} replies"
    $scope.formatDateTime = (date)->
      moment(date).format("MMM D, YYYY h:mma")
    
    $scope.peekAtAuthor = (author)->
      User.get id: author.id, (data)->
        $scope.peek.show("user", data)
  ]
  template: """
    <div class="post">
      <h3 class="title" ng-show="topic.title">{{topic.title}}</h3>
      <div class="content" ng-bind-html-unsafe="topic['first-post']['formatted-content']"></div>
      <footer>
        <div class="author">
          <img ng-src="{{topic.author.gravatar}}" class="gravatar"/>
          <a href="" ng-click="peekAtAuthor(topic.author)">
            {{topic.author.username}}
          </a>
        </div>
        <div class="date">{{formatDateTime(topic['first-post']['created-at'])}}</div>
        
        <i class="like icon-thumbs-up"></i>
        <a href="/#/topics/{{topic.id}}/" class="comments">
          {{formatPostCount(topic['post-count'])}}
        </a>
        <span class="unread-count" ng-show="topic['unread-count']">{{topic['unread-count']}} unread</span>
      </footer>
    </div>
    """  
  replace: true
