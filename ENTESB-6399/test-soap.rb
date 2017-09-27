#!/usr/bin/env ruby

require 'net/http'
require 'uri'

url = 'http://localhost:9000/cxf/HelloWorld'
uri = URI.parse(url)
req = Net::HTTP::Post.new(uri.path)
req.body = <<EOS
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
    <soap:Body>
        <ns2:sayHi xmlns:ns2="http://soap.quickstarts.fabric8.io/">
            <arg0>John Doe</arg0>
        </ns2:sayHi>
    </soap:Body>
</soap:Envelope>
EOS

while true do
  res = Net::HTTP.new(uri.host, uri.port).start { |http| http.request(req) }
  puts "#{url} - #{res.code} #{res.message}"
  sleep 0.1
end
