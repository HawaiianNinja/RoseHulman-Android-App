package awesome.app.data;

import java.util.ArrayList;

public class ScheduleData {
	public String className = "";
	public String classNumber = "";
	public String instructor = "";
	public ArrayList<ClassMeetingData> meetings = new ArrayList<ClassMeetingData>();
	public String finalData = "";

	public Boolean MeetsOn(String day) {
		for (ClassMeetingData meeting : meetings) {
			if (meeting.InSession(day))
				return true;
		}
		return false;
	}

	public Boolean MeetingDuringPeriod(int period) {
		for (ClassMeetingData meeting : meetings) {
			if (meeting.MeetsDuringPeriod(period))
				return true;
		}
		return false;
	}

	public String GetMeetingString() {
		String output = "";
		for (ClassMeetingData meeting : meetings) {
			output += meeting.toString();
		}
		return output;
	}
}