package awesome.app.data;

import java.util.ArrayList;

import android.util.Log;

public class ScheduleData {
	public String className = "";
	public String classNumber = "";
	public String instructor = "";
	public ArrayList<ClassMeetingData> meetings = new ArrayList<ClassMeetingData>();
	public String finalData = "";

	public Boolean MeetsOn(int period, String day) {
		for (ClassMeetingData meeting : meetings) {
			if (meeting.MeetsDuringPeriodOnDay(period, day)) {
				return true;
			}
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