package awesome.app;

import java.util.ArrayList;

public class ScheduleData {
	public String className;
	public String classNumber;
	public String instructor;
	public ArrayList<ClassMeeting> meetings = new ArrayList<ClassMeeting>();
	public String finalData;
	
	public Boolean MeetsOn(String day) {
		for(ClassMeeting meeting : meetings) {
			if(meeting.InSession(day))
				return true;
		}
		return false;
	}
	
	public Boolean MeetingDuringPeriod(int period) {
		for(ClassMeeting meeting : meetings) {
			if(meeting.MeetsDuringPeriod(period))
				return true;
		}
		return false;
	}
}