package awesome.app.handler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ScheduleQuarterHandler extends DefaultHandler {

	private ArrayList<String> currentValue  = new ArrayList<String>();
	private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
	private String attribute = ""; 

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		attribute = atts.getValue(0);
		currentValue = new ArrayList<String>();
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		currentValue.add(attribute);
		data.add(currentValue);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		currentValue.add(new String(ch, start, length));
	}

	public ArrayList<ArrayList<String>> getData() {
		return data;
	}
}
