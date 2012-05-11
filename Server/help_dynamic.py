import os
from datetime import datetime, timedelta

def index(req):
	req.content_type = "text/xml"
	path = '/var/www/RH-Gaming.com/android/' 
	helpPath = '/var/www/RH-Gaming.com/android/helpFiles/'
	returnString = '<help name=\"help\">'
	
	if not os.path.isdir(helpPath):
		return returnString	
	files=os.listdir(helpPath)
	for name in files:
		try:
			helpFile = open(os.path.join(helpPath, name))
			helpString = helpFile.read()
		except: 
			helpString = 'Error retrieving help information for this page.'
		returnString += '<helpItem>'
		returnString += '<helpName>' + name[:-4] + '</helpName>'
		returnString += '<helpString>' + helpString + '</helpString>'
		returnString += '</helpItem>'
		helpFile.close()
	return returnString + '</help>'