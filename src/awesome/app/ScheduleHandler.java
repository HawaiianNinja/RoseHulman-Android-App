package awesome.app;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class ScheduleHandler extends DefaultHandler {
	private String currentValue;
	private ArrayList<ScheduleData> classList = new ArrayList<ScheduleData>();
	private ScheduleData currentClass;
	private ClassMeeting currentMeeting;

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (localName.equals("class")) {
			currentClass = new ScheduleData();
		} else if (localName.equals("meeting")) {
			currentMeeting = new ClassMeeting();
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("number")) {
			currentClass.classNumber = currentValue;
		} else if (localName.equals("name")) {
			currentClass.className = currentValue;
		} else if (localName.equals("instructer")) {
			currentClass.instructor = currentValue;
		} else if (localName.equals("finalData")) {
			Log.d("Schedule Parser", "Final Data:" + currentValue);
			currentClass.finalData = currentValue;
		} else if (localName.equals("days")) {
			currentMeeting.day = currentValue;
		} else if (localName.equals("hours")) {
			currentMeeting.period = currentValue;
		} else if (localName.equals("room")) {
			currentMeeting.roomNumber = currentValue;
		} else if (localName.equals("meeting")) {
			currentClass.meetings.add(currentMeeting);
		} else if (localName.equals("class")) {
			classList.add(currentClass);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		currentValue = new String(ch, start, length);
	}

	public ArrayList<ScheduleData> getClassList() {
		ArrayList<ScheduleData> toReturn = classList;
		classList = new ArrayList<ScheduleData>();
		return toReturn;
	}
}
