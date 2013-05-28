#!/usr/bin/env ruby

require 'rubygems'
require 'json'
require 'date'
require 'faker'

directory = File.expand_path(File.dirname(__FILE__))

def pick_random(arr)
  arr[rand(arr.size)]
end
i = 0
users = Array.new(20){
  i += 1
  {
    id: i,
    username: Faker::Internet.user_name,
    about: "I like to eat cherry pie"
  }
}
i = 0
topics = Array.new(100) {
  i += 1
  h = {
    id: i,
    title: Faker::Lorem.sentence(rand(5) + 5),
    author: pick_random(users),
    "created-at" => Time.now.to_s,
    posts: Array.new(rand(30) + 1) {
      user = pick_random(users)
      user = {
        id: user[:id],
        username: user[:username]
      }
      content = Faker::Lorem.paragraphs(rand(3) + 1).collect{|p| "#{p}"}.join("\n\n")
      {
        author: user,
        content: content,
        "formatted-content" => content,
        "created-at" => Time.now.to_s
      }
    }
  }
  h['first-post'] = h[:posts].first
  h['post-count'] = h[:posts].size
  h
}

File.open(directory + "/../app/data/topics.json", 'w+'){ |f| f.puts topics.to_json }

users.each{|u| u['recent_posts'] = pick_random(topics)[:posts][0...3]}

File.open(directory + "/../app/data/users.json", 'w+'){ |f| f.puts users.to_json }
