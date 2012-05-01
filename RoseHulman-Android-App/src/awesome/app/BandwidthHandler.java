package awesome.app;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BandwidthHandler extends DefaultHandler {

	private String currentValue;
	private String sentAmount;
	private String receivedAmount;
	private String address;
	private Boolean hasError = false;

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {

		if (localName.equals("sent")) {
			sentAmount = currentValue;
		} else if (localName.equals("received")) {
			receivedAmount = currentValue;
		} else if (localName.equals("policy")) {
			address = currentValue;
		} else if (localName.equals("error")) {
			hasError = true;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		currentValue = new String(ch, start, length);
	}

	public Boolean isError() {
		return hasError;
	}

	public String getSentAmount() {
		return sentAmount;
	}

	public String getReceivedAmount() {
		return receivedAmount;
	}

	public String getAddress() {
		return address;
	}
}
