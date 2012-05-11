import urllib2, urllib
import os
from mod_python import util

feedbackHelpFileName = os.path.join(os.path.dirname(__file__), "helpFiles/feedbackHelp.txt")
studentHelpFileName = os.path.join(os.path.dirname(__file__), "helpFiles/studentHelp.txt")
scheduleHelpFileName = os.path.join(os.path.dirname(__file__), "helpFiles/scheduleHelp.txt")
araHelpFileName = os.path.join(os.path.dirname(__file__), "helpFiles/araHelp.txt")
bandwidthHelpFileName = os.path.join(os.path.dirname(__file__), "helpFiles/bandwidthHelp.txt")
subwayHelpFileName = os.path.join(os.path.dirname(__file__), "helpFiles/subwayHelp.txt")

def index(req):
	helpRequested = req.form.getfirst('hellion')
	try:
		f = {
			'feedback': open(feedbackHelpFileName, 'r'),
			'student':	open(studentHelpFileName, 'r'),
			'schedule': open(scheduleHelpFileName, 'r'),
			'ara':		open(araHelpFileName, 'r'),
			'bandwidth':open(bandwidthHelpFileName, 'r'),
			'subway':open(subwayHelpFileName, 'r')
		}[helpRequested]
		outstring = f.read()
		f.close()
	except:
		outstring = "NOOBLORD"
	return outstring