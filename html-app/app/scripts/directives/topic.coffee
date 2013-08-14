'use strict'

angular.module('gratefulplaceApp').directive 'topic', ->
  restrict: 'E'
  scope:
    topic: '=model'
  controller: ['$scope', 'Authorize', 'Post', 'Topic', 'User', 'Like', 'CurrentSession',  ($scope, Authorize, Post, Topic, User, Like, CurrentSession)->
    $scope.post = $scope.topic['first-post']
    $scope.formatPostCount = (postCount)->
      switch postCount
        when 1 then "no replies"
        when 2 then "1 reply"
        else "#{postCount - 1} replies"
  ]
  template: """
    <div class="post">
      <h3 class="title" ng-show="topic.title">{{topic.title}}</h3>
      <div class="content" ng-bind-html-unsafe="post['formatted-content']"></div>
      <footer>
        <author model="topic.author"></author>
        
        <like-toggle likeable="post"></like-toggle>        
        <a href="/#/topics/{{topic.id}}/" class="comments">
          {{formatPostCount(topic['post-count'])}}
        </a>
        <date data="topic['last-posted-to-at']"></date>
        <span class="unread-count" ng-show="topic['unread-count']">{{topic['unread-count']}} unread</span>
      </footer>
    </div>
    """  
  replace: true
