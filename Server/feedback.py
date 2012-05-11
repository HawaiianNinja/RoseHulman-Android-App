import datetime
from mod_python import util

feedbackFileName = "/var/www/RH-Gaming.com/android/feedback.txt"

def index(req):
	feedbackRaw = req.form.getfirst('probe')
	if feedbackRaw != "" or str(feedbackRaw) != 'None': 
		f = open(feedbackFileName, 'a')
		stringToWrite = "Received: "
		stringToWrite += datetime.datetime.now().strftime("%Y-%m-%d %H:%M")
		stringToWrite += "\r\nFeedback:\r\n\t"
		stringToWrite += str(feedbackRaw)
		stringToWrite += "\r\n\r\n---------------------------------------\r\n\r\n"
		f.write(stringToWrite)
		f.close()
		return "201"
	else:
		return "ERROR 0x00 -- NULL INPUT EXCEPTION"