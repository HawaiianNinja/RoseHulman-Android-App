package awesome.app;

import java.util.ArrayList;

public class ClassMeeting {
	public String roomNumber;
	public String day;
	public String period;
	
	public ClassMeeting() {
		
	}
	
	public Boolean InSession(String str) {
		if (day.toUpperCase().contains(str))
			return true;
		if (day.toUpperCase().contains(str))
			return true;
		if (day.toUpperCase().contains(str))
			return true;
		if (day.toUpperCase().contains(str))
			return true;
		if (day.toUpperCase().contains(str))
			return true;
		return false;
	}
	
	public Boolean MeetsDuringPeriod(int periodToCheck) {
		if(getTimesInSession(period).contains(periodToCheck))
			return true;
		return false;
	}

	public ArrayList<Integer> getTimesInSession(String str) {
		ArrayList<Integer> returnVal = new ArrayList<Integer>();
		if (str.trim().length() <= 2)
			returnVal.add(new Integer(str));
		else {
			String[] rawNumbers = str.split("-");
			int x = new Integer(rawNumbers[0]);
			int y = new Integer(rawNumbers[1]);
			while (x <= y) {
				returnVal.add(x);
				x++;
			}
		}
		return returnVal;
	}
}
