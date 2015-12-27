from pysimplesoap.client import SoapClient
client = SoapClient(wsdl="http://5.35.247.12:9999/ws/getMatchResult?wsdl",trace=False)
response = client.getResult(15) # match number 15..
result = response['MatchResult'] # dunno if this is true
print result