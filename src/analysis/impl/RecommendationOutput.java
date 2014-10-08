package analysis.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Output class for recommendations for the user along with similarity score
 * 
 * 
 */
public class RecommendationOutput {
	String userId;
	Map<Double, List<TrackInformation>> recom = new TreeMap<Double, List<TrackInformation>>();

	/**
	 * Adds the results of recommendations to the output object map
	 * 
	 * @param scoreMap
	 */
	public void addToMap(Map<Double, List<TrackInformation>> scoreMap) {
		//recom  = new TreeMap<Double, List<TrackInformation>>();
		recom.putAll(scoreMap);
		
		/*Set<Double> doubles = tempMap.keySet();
		ArrayList<Double> list = new ArrayList<Double>();
		for (Double d : doubles) {
			list.add(d);
		}
		for (int i = list.size() - 1; i >= 0; i--) {
			Double dou = list.get(i);
			recom.put(dou, tempMap.get(dou));
		}
		*/
		//Set<Double> va = recom.keySet();
		/*for (Double v : va) {
			ArrayList<TrackInformation> li = (ArrayList<TrackInformation>) recom
					.get(v);
			for (TrackInformation i : li) {
				System.out.println(v + " " + i.getTitle() + " "
						+ i.getTrackId());
			}
		}*/
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<Double, List<TrackInformation>> getRecom() {
		return recom;
	}

	public void setRecom(Map<Double, List<TrackInformation>> recom) {
		this.recom = recom;
	}
}
