package analysis.impl;

import java.util.Map;

import database.impl.DBAccessor;

/**
 * 
 * This class is communicating class between the User Interface and Database Accessor
 * 
 *
 */
public class Controller {
	private DBAccessor accessor;
	
	public Controller() {
		accessor = new DBAccessor();
	}
	
	/**
	 * Gets the user data to be displayed on UI
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 */
	public String[][] getData(int offset, int limit) {
		String query = "select userId, name from user limit " + limit +" offset " + offset;
		return accessor.getUserList(query);
	}

	public void updateCountInHasPlayed(String trackId, String userId) {
		accessor.updateCountInHasPlayed(trackId, userId);
	}

	public void updateGenrePreference(String userId, Map<String, Double> mapp) {
		accessor.updateGenrePreference(userId, mapp);
	}

}
