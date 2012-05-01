package awesome.app;

import java.util.List;

public class MenuData {
	List<String> breakfastEntrees;
	List<String> breakfastSides;
	List<String> lunchEntrees;
	List<String> lunchSides;
	List<String> dinnerEntrees;
	List<String> dinnerSides;

	public MenuData() {
	}

	public void setBreakfastEntree(List<String> data) {
		breakfastEntrees = data;
	}

	public void setBreakfastSides(List<String> data) {
		breakfastSides = data;
	}

	public void setLunchEntree(List<String> data) {
		lunchEntrees = data;
	}

	public void setLunchSides(List<String> data) {
		lunchSides = data;
	}

	public void setDinnerEntree(List<String> data) {
		dinnerEntrees = data;
	}

	public void setDinnerSides(List<String> data) {
		dinnerSides = data;
	}
}
