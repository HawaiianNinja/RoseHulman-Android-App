package awesome.app;

import java.util.ArrayList;

public class ClassMeeting {
	public String roomNumber;
	public String day;
	public String period;
	
	public ClassMeeting() {
		
	}
	
	public int[] getDaysInSession(String str) {
		ArrayList<Integer> returnVal = new ArrayList<Integer>();

		if (str.toUpperCase().contains("M"))
			returnVal.add(1);
		if (str.toUpperCase().contains("T"))
			returnVal.add(2);
		if (str.toUpperCase().contains("W"))
			returnVal.add(3);
		if (str.toUpperCase().contains("R"))
			returnVal.add(4);
		if (str.toUpperCase().contains("F"))
			returnVal.add(5);
		int[] returnArray = new int[returnVal.size()];
		for (int x = 0; x < returnVal.size(); x++) {
			returnArray[x] = returnVal.get(x).intValue();
		}
		return returnArray;
	}

	public int[] getTimesInSession(String str) {
		ArrayList<Integer> returnVal = new ArrayList<Integer>();
		if (1 == str.trim().length())
			return new int[] { Character.getNumericValue(str.charAt(0)) };
		else if (3 == str.trim().length()) {
			int x = Character.getNumericValue(str.charAt(0));
			int y = Character.getNumericValue(str.charAt(2));
			while (x <= y) {
				returnVal.add(x);
				x++;
			}
		} else {
			String[] strings = str.split("-");
			int x = Integer.decode(strings[0]).intValue();
			int y = Integer.decode(strings[1]).intValue();
			if (y > 1710) {
				while (x <= 10) {
					returnVal.add(x);
					x++;
				}
				returnVal.add(11);
			} else {
				int remainder = (55 * x) % 60;
				int numOfHours = (55 * x) / 60;
				int z = 805 + (100 * numOfHours) + remainder;
				if (z <= 1710) {
					// Do Something
				} else {
					returnVal.add(11);
				}
			}
		}
		int[] returnArray = new int[returnVal.size()];
		for (int x = 0; x < returnVal.size(); x++) {
			returnArray[x] = returnVal.get(x).intValue();
		}
		return returnArray;
	}
}
