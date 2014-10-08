package analysis.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Business object containing the listening history of the user.
 *
 */
public class TrackUserBO {
	public TrackUserBO() {
		info = new HashMap<String, TrackInformation>();
	}
	private String userId;
	private int totalplaycount;
	public int getTotalplaycount() {
		return totalplaycount;
	}

	public void setTotalplaycount(int totalplaycount) {
		this.totalplaycount = totalplaycount;
	}
	private Map<String, TrackInformation> info;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, TrackInformation> getInfo() {
		return info;
	}

	public void setInfo(Map<String, TrackInformation> info) {
		this.info = info;
	}
}
