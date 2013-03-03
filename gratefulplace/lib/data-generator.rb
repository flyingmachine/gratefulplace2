#!/usr/bin/env ruby

require 'rubygems'
require 'json'
require 'date'
require 'faker'

directory = File.expand_path(File.dirname(__FILE__))

def pick_random(arr)
  arr[rand(arr.size)]
end

users = Array.new(20){ Faker::Internet.user_name }
i = 0
topics = Array.new(100) {
  i += 1
  {
    id: i,
    title: Faker::Lorem.sentence(rand(5) + 5),
    author: pick_random(users),
    posts: Array.new(rand(30) + 1) {
      {
        author: pick_random(users),
        content: Faker::Lorem.paragraphs(rand(3) + 1).collect{|p| "<p>#{p}</p>"}.join,
        date: Date.today.to_s
      }
    }
  }
}

File.open(directory + "/../app/data/topics.json", 'w+'){ |f| f.puts topics.to_json }
