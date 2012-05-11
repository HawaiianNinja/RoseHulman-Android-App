import os
from datetime import datetime, timedelta

def index(req):
	path = '/var/www/RH-Gaming.com/android/' 
	menuPath = '/var/www/RH-Gaming.com/android/menus/'	
	numDeleted = 0
	if not os.path.isdir(menuPath):
		return numDeleted

	files=os.listdir(menuPath)
	for name in files:
		date_object = datetime.strptime(name[:-4], '%m_%d_%Y')
		if ((date_object != datetime.today()) and (date_object != datetime.today() + timedelta(days=1)) and (date_object != datetime.today() + timedelta(days=2))):
			try:
				os.remove(os.path.join(menuPath, name))
				numDeleted+=1
			except:
				numDeleted+=0
	return numDeleted