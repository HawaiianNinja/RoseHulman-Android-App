import urllib2, urllib
import re
import base64
from mod_python import util

def index(req):
	req.content_type = "text/XML"
	theurl = 'https://prodweb.rose-hulman.edu/regweb-cgi/reg-sched.pl'
	username = req.form.getfirst('scv')
	password = base64.b64decode(req.form.getfirst('immortal'))
	
	passman = urllib2.HTTPPasswordMgrWithDefaultRealm()
	passman.add_password(None, theurl, username, password)
	authhandler = urllib2.HTTPBasicAuthHandler(passman)
	opener = urllib2.build_opener(authhandler)
	urllib2.install_opener(opener)
	req = urllib2.Request(theurl)
	res = urllib2.urlopen(req)
	page = res.read()
	
	rgx = "<SELECT  NAME=\"termcode\">.+?</SELECT>";
	regex = re.compile(rgx, re.DOTALL)
	result = regex.findall(page)
	
	return result[0].replace("selected", "")
	