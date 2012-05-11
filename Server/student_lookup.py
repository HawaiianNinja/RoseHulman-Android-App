import urllib2, urllib
import re
import base64
from mod_python import util
from xml.dom.minidom import Document #XML tool

def index(req):
	searchFor = req.form.getfirst('infestor')
	req.content_type = "text/xml"
	theurl = 'http://sunder.rose-hulman.edu/cgi-bin/secure/search.cgi'
	
	# The Student Lookup Part
	postData = urllib.urlencode([('search', searchFor)])
	req = urllib2.Request(theurl, postData)
	req.add_header("Content-type", "application/x-www-form-urlencoded")
	
	# Create the minidom document
	xml = Document()
	doc = xml.createElement("StudentLookup")
	doc.setAttribute("searchQuery", searchFor)
	xml.appendChild(doc)
	
	# Get the page
	res = urllib2.urlopen(req)
	page = res.read()
	rgx = []
	rgx.append("<hr>\s*(?P<name>.+?) [(].+[)]<br>\s*")
	rgx.append("Username: (?P<username>.+)<br>\s*")
	rgx.append("CM (?P<cm>[0-9]+)<br>\s*")
	rgx.append("Phone: (?P<phone>.+)<br>\s*")
	rgx.append("Room: (?P<room>.+)<br>\s*")
	rgx.append("Status: (?P<status>.+?) in (?P<dept>.+?) Department<br>")
	regex = re.compile(rgx[0] + rgx[1] + rgx[5])                             #no CM, phone, or Room
	for item in regex.finditer(page):
		doc.appendChild(createXMLItem(item, xml))
	regex = re.compile(rgx[0] + rgx[1] + rgx[2] + rgx[5])                    #only CM
	for item in regex.finditer(page):
		doc.appendChild(createXMLItem(item, xml))
	regex = re.compile(rgx[0] + rgx[1] + rgx[2] + rgx[3] + rgx[5])           #CM and phone
	for item in regex.finditer(page):
		doc.appendChild(createXMLItem(item, xml))
	regex = re.compile(rgx[0] + rgx[1] + rgx[2] + rgx[3] + rgx[4] + rgx[5])  #CM, phone and room
	for item in regex.finditer(page):
		doc.appendChild(createXMLItem(item, xml))

	# Print our newly created XML
	#return xml.toprettyxml(indent="  ")
	return xml.toxml

	#return (info, len(info));
	
def createXMLItem(regexItem, xml):
	# Create Student Element
	student = xml.createElement("Student")
	
	# Get group dictionary
	studentInfo = regexItem.groupdict()
	
	# Put info into Student Element
	for infoItem in studentInfo.keys():
		info = xml.createElement(infoItem)
		detail = xml.createTextNode(studentInfo[infoItem])
		info.appendChild(detail)
		student.appendChild(info)
	
	return student