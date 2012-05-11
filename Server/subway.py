import urllib2, urllib
from mod_python import util

def index(req):
	highQuality = "http://subway-cam.rose-hulman.edu/snapshot.jpg"
	#mediumQuality = "http://subway-cam.rose-hulman.edu/snapshot1.jpg"
	#lowQuality = "http://subway-cam.rose-hulman.edu/snapshot_3gp.jpg"
	selectedURL = highQuality
	#req.content_type = "image/jpg"
	try:
		return urllib.urlopen(selectedURL).read()
	except:
		return 'Error with accessing the Subway Cam'
	#return "<html><body><table style=\"height: 100%; margin: auto;\"><tr style=\"vertical-align: middle; text-align:center;\"><td><img src=\"" + selectedURL + "\" style=\"width:100%;\" /></td></tr></table></body></html>"