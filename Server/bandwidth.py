import urllib2, urllib
import re
import base64
import datetime
from mod_python import util
from ntlm import HTTPNtlmAuthHandler

def index(req):
	req.content_type = "text/xml"
	theurl = 'https://netreg.rose-hulman.edu/tools/networkUsage.pl'
	
	policyURL = 'http://www.rose-hulman.edu/TSC/policies/bandwidth_utilization/'
	
	# create a header
	user_agent = 'Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)'
	header = { 'Connection' : 'Keep-alive', 'User-Agent' : user_agent}
	
	username = req.form.getfirst('scv')
	password = base64.b64decode(req.form.getfirst('immortal'))
	
	# Authentication Settings
	domain = 'ROSE-HULMAN.EDU\\'
	authName = domain + username
	passman = urllib2.HTTPPasswordMgrWithDefaultRealm()
	passman.add_password(None, theurl, authName, password)
	auth_NTLM = HTTPNtlmAuthHandler.HTTPNtlmAuthHandler(passman)
	opener = urllib2.build_opener(auth_NTLM)
	urllib2.install_opener(opener)
	
	# Get the page.
	try:
		pageReq = urllib2.Request(theurl, None, header)
		res = urllib2.urlopen(pageReq)
	except IOError, e:
		if hasattr(e, 'code'):
			return "<error>" + e.code + "</error>"
		else:
			return "<error>I has an error.</error>"
	page = res.read()
	
	rgxMain = "<div class=\"mainContainer\">.+?</div>";
	regex = re.compile(rgxMain, re.DOTALL)
	mainContainer = regex.findall(page)	
	
	rgxFloat = "[0-9,]*.[0-9]{2} MB";
	regex = re.compile(rgxFloat, re.DOTALL)
	totals = regex.findall(mainContainer[0])
	now = datetime.datetime.now()
	outputString = "<bandwidth username=\""+username+"\" time=\""+now.strftime("%Y-%m-%d %H:%M")+"\"><received>"+totals[0]+"</received><sent>"+totals[1]+"</sent><policy>"+policyURL+"</policy></bandwidth>"
	return outputString