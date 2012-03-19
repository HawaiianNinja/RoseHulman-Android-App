package awesome.app;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AraHandler extends DefaultHandler {
	private enum Meal {
		BREAKFAST, LUNCH, DINNER
	};

	private MenuData menuData;
	private Meal currentMeal;
	private ArrayList<String> currentList;
	private String currentValue;

	@Override
	public void startDocument() throws SAXException {
		menuData = new MenuData();
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (localName.equals("breakfast")) {
			currentMeal = Meal.BREAKFAST;
		} else if (localName.equals("lunch")) {
			currentMeal = Meal.LUNCH;
		} else if (localName.equals("dinner")) {
			currentMeal = Meal.DINNER;
		} else if (localName.equals("entree") || localName.equals("side")) {
			currentList = new ArrayList<String>();
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("item")) {
			currentList.add(currentValue);
		} else if (localName.equals("entree")) {
			if (currentMeal == Meal.BREAKFAST) {
				menuData.setBreakfastEntree(currentList);
			} else if (currentMeal == Meal.LUNCH) {
				menuData.setLunchEntree(currentList);
			} else if (currentMeal == Meal.DINNER) {
				menuData.setDinnerEntree(currentList);
			}
		} else if (localName.equals("side")) {
			if (currentMeal == Meal.BREAKFAST) {
				menuData.setBreakfastSides(currentList);
			} else if (currentMeal == Meal.LUNCH) {
				menuData.setLunchSides(currentList);
			} else if (currentMeal == Meal.DINNER) {
				menuData.setDinnerSides(currentList);
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		currentValue = new String(ch, start, length);
	}

	public MenuData getMenu() {
		return menuData;
	}
}