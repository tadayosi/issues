#!/usr/bin/env ruby

require 'net/http'
require 'uri'

url = 'http://localhost:9000/cxf/crm/customerservice/customers/123'

`curl -s -X DELETE #{url}`
`curl -s -X DELETE #{url}`

while true do
  res = Net::HTTP.get_response URI.parse(url)
  puts "#{url} - #{res.code} #{res.message}"
  sleep 0.1
end
