import urllib2, urllib
import re
import base64
from mod_python import util

def index(req):
	req.content_type = "text/xml"
	theurl = 'https://prodweb.rose-hulman.edu/regweb-cgi/reg-sched.pl'
	
	# Search Data
	userToLookup = req.form.getfirst('infestor')
	term = req.form.getfirst('templar')
	view = 'table'
	bt1 = 'ID/Username'
	id4 = '' # Needed for form to submit
	id5 = '' # Same as above
	
	# Authentication Settings
	username = req.form.getfirst('scv')
	password = base64.b64decode(req.form.getfirst('immortal'))
	
	passman = urllib2.HTTPPasswordMgrWithDefaultRealm()
	passman.add_password(None, theurl, username, password)
	authhandler = urllib2.HTTPBasicAuthHandler(passman)
	opener = urllib2.build_opener(authhandler)
	urllib2.install_opener(opener)
	
	# The Schedule Lookup Part
	postData = urllib.urlencode([('termcode', term), ('id1', userToLookup), ('view', view), ('bt1', bt1), ('id4', id4), ('id5', id5)])
	req = urllib2.Request(theurl, postData)
	req.add_header("Content-type", "application/x-www-form-urlencoded")
	
	# Get the page.
	res = urllib2.urlopen(req)
	page = res.read()
	# pageFixed = page.replace("&", " and ");
	
	# Search for the table rows we need. The '<A' section removes useless information from the top.
	rgx = "<TR><TD><A.+?</TR>";
	regex = re.compile(rgx, re.DOTALL)
	scheduleRows = regex.findall(page)
	outString = "<schedule>"
	for listItem in scheduleRows:
		outString += parseClassData(listItem) + "\n"
	outString += "</schedule>"
	return outString;
	
def parseClassData(raw):
	noDoubles = re.sub('><', '', str.replace(raw, '\n', '')); # removes double tags and
	clean = re.sub('<.+?>', '|', str.replace(str.replace(noDoubles, '&nbsp', ''), '&', ' and ')); 
	detailsList = clean.split('|');
	roomList = detailsList[8].split(':'); # For classes that meet on a weird schedule
	# detailsList
	# 1 - Class Number
	# 3 - Class Name
	# 4 - Instructer
	# 8 - Days/Hour/Room
	#		meetingDetails
	#		0 - Days
	#		1 - Hour(s)
	#		2 - Room
	# 10 - Final Details
	xml = '<number>' + detailsList[1] + '</number>';
	xml += '<name>' + detailsList[3] + '</name>';
	xml += '<instructer>' + detailsList[4] + '</instructer>';
	for meeting in roomList: # Classes that meet on a weird schedule
		meetingDetails = meeting.split('/');
		if (len(meetingDetails) > 1):
			xml += '<meeting>'
			xml += '<days>' + meetingDetails[0] + '</days>';
			xml += '<hours>' + meetingDetails[1] + '</hours>';
			xml += '<room>' + meetingDetails[2] + '</room>';
			xml += '</meeting>'
		else:
			xml += '<meeting>'
			xml += '<days>' + meetingDetails[0] + '</days>';
			xml += '<hours>' + meetingDetails[0] + '</hours>';
			xml += '<room>' + meetingDetails[0] + '</room>';
			xml += '</meeting>'
	xml += '<finalData>' + detailsList[10] + ' </finalData>';
	return '<class>' + xml + '</class>';