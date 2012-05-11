import urllib2, urllib
import re
import base64
###import os
from mod_python import util
from xml.dom.minidom import Document #XML tool
from datetime import date###, datetime, timedelta

def index(req):
	# Get date from secret variable name
	date = req.form.getfirst('cattlebruiser')
	req.content_type = "text/xml"
	# Attempt to open the cache file if it exists
	fileName = '/var/www/RH-Gaming.com/android/menus/' + date + '.xml'
	file = open(fileName, 'a+')
	result = file.read()
	# If the file is empty or doesn't exist erad the website and store the result
	# If there is somehting in the file assume it is correct and just return the contents of the file
	if result == '':
		result = getMenuInfo(date)
		file.write(result)
	file.close()
	###date_object = datetime.strptime(date, '%m_%d_%Y')
	###if ((date_object != datetime.today()) and (date_object != datetime.today() + timedelta(days=1)) and (date_object != datetime.today() + timedelta(days=2))):
	###	os.remove(fileName)
	return result

def getMenuInfo(date):
	url = 'http://www.campusdish.com/en-US/CSMW/RoseHulman/Locations/HulmanUnionCafeteriaMenu1.htm?LocationName=Hulman%20Union%20Cafeteria%20Menu&OrgID=152318&ShowPrice=False&ShowNutrition=True&Date='
	url += date
	url += '&MealID='
	
	# Check which day of the week it is
	day = checkDate(date)
	result = '<menu date=\"' + date + '\">'
	# Get the data for each meal
	# If it is not saturday get breakfast
	if day != 5:
		result += '<breakfast>' + getMenuItems(url, '1') + '</breakfast>'
	# The ara serves lunch everyday so always get it
	result += '<lunch>' + getMenuItems(url, '16') + '</lunch>'
	# The ara serves dinner only monday - friday
	if day != 5 and day != 6:
		result += '<dinner>' + getMenuItems(url, '17') + '</dinner>'
	return result + '</menu>'

# Returns a int which is the day of the week
# 0 is monday, 6 is sunday
def checkDate(day):
	parts = day.split('_')
	month = int(parts[0])
	day = int(parts[1])
	year = int(parts[2])
	oldDate = date(year, month, day)
	return date.weekday(oldDate)
	
# Gets all of the items for a particular meal
def getMenuItems(url, mealID):
	result = ''
	try:
		res = urllib2.urlopen(url + mealID)
		page = res.read()
		
		rgx = []
		# Get every table with all the stuff still in the original spot
		rgx.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" borderColor=\"#cccccc\">[a-zA-Z0-9 \?\&<>,\(\).\s=\"':;/-_-|+]*</table>")
		# Get the days in each table
		rgx.append("<td valign=\"top\" class=\"menuBorder\">[\n\s]*<table[a-zA-Z0-9 \?\&<>,\(\).\s=\"':;/-_-|+]*?<img")
		# Get the names of the items in the table
		rgx.append("<a href.*>(?P<name>.+)</a>")
		regex = re.compile(rgx[0])
		tables = regex.findall(page)
		# Since each meal has sides/entrees in different order I had to hardcode the way to tag each item
		if mealID == '1':
			result += getBreakfastItems(tables, rgx)
		elif mealID == '16':
			result += getLunchItems(tables, rgx)
		elif mealID == '17':
			result += getDinnerItems(tables, rgx)
	except:
		result = '<entree>No Menu Data for this day.</entree>'
	return result
	
def getBreakfastItems(tables, rgx):
	result = '<entree>'
	for table in tables:
		result += getItems(table, rgx)
	return result + '</entree>'
	
def getLunchItems(tables, rgx):
	result = '<entree>'
	result += getItems(tables[0], rgx)
	result += getItems(tables[1], rgx)
	result += '</entree><side>'
	result += getItems(tables[2], rgx)
	result += getItems(tables[3], rgx)
	return result + '</side>'

def getDinnerItems(tables, rgx):
	result = '<entree>'
	result += getItems(tables[0], rgx)
	result += getItems(tables[1], rgx)
	result += getItems(tables[3], rgx)
	result += '</entree><side>'
	result += getItems(tables[2], rgx)
	return result + '</side>'
	
# This takes one table and returns all the items in the table surronded with <item> tags
def getItems(table, rgx):
	result = ''
	regex = re.compile(rgx[1])
	days = regex.findall(table)
	regex = re.compile(rgx[2])
	for item in regex.finditer(days[0]):
		result += '<item>' + str.replace(item.group('name'), '&amp;', 'and').title() + '</item>'
	return result