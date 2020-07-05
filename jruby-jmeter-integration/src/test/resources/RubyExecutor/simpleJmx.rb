require 'rubygems'
require 'ruby-jmeter'

test do
  threads count: 1, rampup: 0, loops: 1, scheduler: false do
    beanshell_sampler query:'log.info("********Hello World********");'
  end
end.jmx(file: 'target/test-classes/RubyExecutor/simpleJmx.jmx')