'use strict'

angular.module('gratefulplaceApp').directive 'topic', ->
  restrict: 'EA'
  scope:
    topic: '=model'
    peek: '='
  controller: ['$scope', 'Authorize', 'Post', 'Topic', 'User', 'Like', 'CurrentSession', ($scope, Authorize, Post, Topic, User, Like, CurrentSession)->
    $scope.currentSession = CurrentSession.get()    
    
    $scope.post = $scope.topic['first-post']
    
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

    newLike = ->
      new Like(id: $scope.post.id)
      
    like = ->
      newLike().$save ->
        $scope.post.likers.push $scope.currentSession.id

    unlike = ->
      newLike().$delete ->
        $scope.post.likers = _.without($scope.post.likers, $scope.currentSession.id)

    $scope.toggleLike = ->
      if $scope.liked()
        unlike()
      else
        like()  

    $scope.liked = ->
      _.include $scope.post.likers, $scope.currentSession.id
      
  ]
  template: """
    <div class="post">
      <h3 class="title" ng-show="topic.title">{{topic.title}}</h3>
      <div class="content" ng-bind-html-unsafe="post['formatted-content']"></div>
      <footer>
        <div class="author">
          <img ng-src="{{topic.author.gravatar}}" class="gravatar"/>
          <a href="" ng-click="peekAtAuthor(topic.author)">
            {{topic.author.username}}
          </a>
        </div>
        <div class="date">{{formatDateTime(post['created-at'])}}</div>

        <div class="like" ng-class="{liked: liked()}" ng-click="toggleLike()">
          <span ng-show="liked()">{{post.likers.length}}</span>
          <i class="icon-thumbs-up"></i>
        </div>
        
        <a href="/#/topics/{{topic.id}}/" class="comments">
          {{formatPostCount(topic['post-count'])}}
        </a>
        <span class="unread-count" ng-show="topic['unread-count']">{{topic['unread-count']}} unread</span>
      </footer>
    </div>
    """  
  replace: true
