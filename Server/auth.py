import urllib2, urllib
import re
import base64
from mod_python import util

def index(req):
	req.content_type = "text/plain"
	theurl = 'https://prodweb.rose-hulman.edu/regweb-cgi/reg-sched.pl'

	# Authentication Settings
	username = req.form.getfirst('scv')
	password = base64.b64decode(req.form.getfirst('immortal'))
	
	passman = urllib2.HTTPPasswordMgrWithDefaultRealm()
	passman.add_password(None, theurl, username, password)
	authhandler = urllib2.HTTPBasicAuthHandler(passman)
	opener = urllib2.build_opener(authhandler)
	urllib2.install_opener(opener)
		
	# Get the page.
	try:
		req = urllib2.Request(theurl)
		res = urllib2.urlopen(req)
		page = res.read()
		if(page.find("Schedule Lookup")):
			return '1';
		return '0';
	except IOError, e:
		return '0';