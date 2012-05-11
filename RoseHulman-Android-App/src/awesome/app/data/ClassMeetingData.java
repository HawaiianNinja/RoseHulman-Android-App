package awesome.app.data;

import java.util.ArrayList;

public class ClassMeetingData {
	public String roomNumber;
	public String day;
	public String period;

	public Boolean MeetsDuringPeriodOnDay(int periodToCheck, String dayChecking) {
		return (getTimesInSession(period).contains(periodToCheck) && day.toUpperCase().contains(dayChecking));
		
	}

	public ArrayList<Integer> getTimesInSession(String str) {
		str = str.replace("TBA", "11");
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

	public String toString() {
		return day.toUpperCase() + "/" + period + "/" + roomNumber;
	}
}
