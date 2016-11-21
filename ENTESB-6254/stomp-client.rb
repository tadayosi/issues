#!/usr/bin/env ruby

require 'stomp'

QUEUE = "/queue/TEST"

def run(port)
  c = Stomp::Client.new("admin", "admin", "localhost", port)
  c.publish QUEUE, "hello!"
  receive = false
  c.subscribe QUEUE do |m|
    puts m.body
    receive = true
  end
  count = 0
  while !receive && count < 20 do
    sleep 1
    count += 1
  end
  c.close
end

1.step do |i|
  print "#{i} => "
  begin
    run 8888
  rescue => e
    puts e.message
    sleep 5
  end
  puts
end
