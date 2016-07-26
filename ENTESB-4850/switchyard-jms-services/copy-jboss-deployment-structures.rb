#!/usr/bin/env ruby

file = "jboss-deployment-structure.xml"
`cp -v jms1/src/main/resources/META-INF/#{file} .`
meta_infs = `find . -name META-INF`.split
meta_infs.each { |x|
    `cp -v #{file} #{x}/`
}
`rm -v #{file}`
