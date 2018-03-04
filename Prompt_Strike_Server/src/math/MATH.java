package math;

/** * Because math is fun */
public final class MATH {
	
	public static double toDegrees(double rad) {
		return rad * (180/Math.PI);
	}

	public static double toRadians(double deg) {
		return deg * (Math.PI/180);
	}
	
	public static float scalaire (float[] vect1, float[] vect2) {
		return vect1[0]*vect2[0] + vect1[1]*vect2[1];
	}
	
	public static double norme(float vect[]) {
		return Math.sqrt(vect[0]*vect[0] + vect[1]*vect[1]);
	}
	
	public static float det(float[] vect1, float[] vect2) {
		return vect1[0] * vect2[1] - vect1[1] * vect2[0];
	}
	
	public static double dist(float[] point1, float[] point2) {
		return Math.sqrt(Math.pow(point2[0]-point1[0], 2) + Math.pow(point2[1]-point1[1], 2));
	}

	/**
	 * Math power :
	 * </br> scalaire(vect1, vect2) = norme(vect1) * norme(vect1) * cos(angle)
	 * </br> d'où cos(angle) = scalaire(vect1, vect2) / (norme(vect1) * norme(vect2))
	 * </br> d'où angle = arcos(scalaire(vect1, vect2) / (norme(vect1) * norme(vect2)))
	 */
	public static double angleBetweenTwoVectors(float[] vect1, float[] vect2) {
		return Math.acos(scalaire(vect1, vect2) / (norme(vect1) * norme(vect2)));
	}
}
