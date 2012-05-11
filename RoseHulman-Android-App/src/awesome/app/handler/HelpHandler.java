package awesome.app.handler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import awesome.app.data.HelpItem;

public class HelpHandler extends DefaultHandler{

	private ArrayList<HelpItem> helpItems;
	private String currentValue;
	private HelpItem currentItem;
	
	@Override
	public void startDocument() throws SAXException {
		helpItems = new ArrayList<HelpItem>();
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (localName.equals("helpItem")) {
			currentItem = new HelpItem();
		}
	}
	
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equals("helpName")) {
			currentItem.setName(currentValue);
		} else if (localName.equals("helpString")) {
			currentItem.setInfo(currentValue);
		} else if (localName.equals("helpItem")) {
			helpItems.add(currentItem);
		}
		
	}
	
	public ArrayList<HelpItem> getHelpItems(){
		ArrayList<HelpItem> toReturn = helpItems;
		helpItems = new ArrayList<HelpItem>();
		return toReturn;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		currentValue = new String(ch, start, length);
	}
}
