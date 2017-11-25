package fr.turtpol.utils;

public class Utils {

	public static float sqr(float value) {
		return value * value;
	}

	public static boolean isAnInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isAnDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static int getLengthToTargetWord(String[] str, int index) {
		int result = 0;
		for (int i = 0; i < index + 1; i++) {
			result += str[i].length() + 1;
		}
		return result;
	}

	//not used anymore
//	public static float getAngleFromLocations(Location l1, Location l2) {
//		float deltaY = l2.getY() - l1.getY();
//		float deltaX = l2.getX() - l1.getX();
//		return (float) (Math.atan2(deltaY, deltaX) * 180 / Math.PI);
//	}

}
