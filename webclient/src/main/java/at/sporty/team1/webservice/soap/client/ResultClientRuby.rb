# package for SOAP-based services
require 'soap/wsdlDriver'

wsdl_url = 'http://5.35.247.12:9999/ws/getMatchResult?wsdl'

service = SOAP::WSDLDriverFactory.new(wsdl_url).create_rpc_driver

# Invoke service operations, matchID = 15
data1 = service.getResult('15')

# Output results.
puts "MatchResult : #{data1}"
