package analysis.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import database.impl.DBAccessor;

/**
 * 
 * This class is responsible for generating the Recommendation for the user.
 * 
 */
public class Collaborative {
	private Map<String, Integer> genreString = new HashMap<String, Integer>();

	public Collaborative() {
		accessor = new DBAccessor();
		populateGenre();
	}

	/**
	 * Populate the genre in a map against an integer
	 */
	private void populateGenre() {
		genreString.put("hip-hop", 0);
		genreString.put("classic pop", 1);
		genreString.put("rock", 2);
		genreString.put("electronica", 3);
		genreString.put("punk", 4);
		genreString.put("pop", 5);
		genreString.put("reggae", 6);
		genreString.put("soul", 7);
		genreString.put("folk", 8);
		genreString.put("jazz", 9);
		genreString.put("dance", 10);
		genreString.put("blues", 11);
		genreString.put("metal", 12);
		genreString.put("classical", 13);

	}

	private DBAccessor accessor;

	/**
	 * Gets the recommendation for a user
	 * 
	 * @param userId
	 * @param userBO 
	 */
	public Map<String, Collection<TrackInformation>> getRecommendations(
			String userId) {
	    TrackUserBO bo = accessor.getListeningHistory(userId);
		Collection<TrackInformation> suggestions = new ArrayList<TrackInformation>();
		Collection<TrackInformation> listhistory = null;
		Map<String, Collection<TrackInformation>> out = new HashMap<String, Collection<TrackInformation>>();
		if (bo != null) {
			listhistory = bo.getInfo().values();
			out.put("history", listhistory);
			Map<String, TrackInformation> info = bo.getInfo();
			Set<String> keyset = info.keySet();
			Set<String> genres = new HashSet<String>();
			
			for (String track : keyset) {
				TrackInformation information = info.get(track);
				genres.addAll(information.getGenres());
			}

			Map<Double, List<TrackInformation>> scoreMap = new HashMap<Double, List<TrackInformation>>();
			RecommendationOutput output = null;
			if (!genres.isEmpty()) {
				Collection<TrackUserBO> collection = accessor.getUsers(genres,
						userId);
				calculateSimilarity(bo, collection, scoreMap);
				if (!scoreMap.isEmpty()) {
					List<String> trackIds = new ArrayList<String>();
					
					output = getOuputObject(scoreMap);
					Map<Double, List<TrackInformation>> map = output.getRecom();
					Set<Double> doubles = map.keySet();
					ArrayList<Double> list = new ArrayList<Double>();
					for (Double d : doubles) {
						list.add(d);
					}
					int count = 0;
					for (int i = list.size() - 1; i >= 0; i--) {
						Double dou = list.get(i);
						List<TrackInformation> informations = map.get(dou);
						boolean flag = false;
						for (TrackInformation t : informations) {
							boolean check = checkIfNotInHistoryAndSuggestList(t, listhistory, trackIds);
							if (check) {
								suggestions.add(t);
								System.out.println(" Suggestions : " + t.getTrackId() + " : " + dou);
								trackIds.add(t.getTrackId());
								count++;
								if (count == 5) {
									flag = true;
									break;
								}
							}
						}
						if (flag) {
							break;
						}
					}
				}
			}
		} /*else {
			suggestions = accessor.getTracks();
		}*/
		out.put("suggestions", suggestions);
		return out;
	}

	private boolean checkIfNotInHistoryAndSuggestList(TrackInformation t,
			Collection<TrackInformation> listhistory, List<String> trackIds) {
		boolean check = true;
		for (TrackInformation information : listhistory) {
			if (information.getTrackId().equals(t.getTrackId()) || 
					trackIds.contains(t.getTrackId())) {
				check = false;
				break;
			}
		}
		return check;
	}

	/**
	 * Gets the recommendation along with score in the output object
	 * 
	 * @param scoreMap
	 */
	private RecommendationOutput getOuputObject(
			Map<Double, List<TrackInformation>> scoreMap) {
		RecommendationOutput recommendation = new RecommendationOutput();
		recommendation.addToMap(scoreMap);
		return recommendation;
	}

	/**
	 * Calculate the similarity between the user listening history and other
	 * tracks
	 * 
	 * @param bo
	 * @param collection
	 * @param scoreMap
	 */
	private void calculateSimilarity(TrackUserBO bo,
			Collection<TrackUserBO> collection,
			Map<Double, List<TrackInformation>> scoreMap) {

		Map<String, TrackInformation> trackInfo = bo.getInfo();

		if (trackInfo != null && !trackInfo.isEmpty()) {
			Collection<TrackInformation> informations = trackInfo.values();
			double[] values = accessor.getMaxLoudnessAndDuration();

			for (TrackInformation track : informations) {
				compareWithOthers(track, collection, values,
						bo.getTotalplaycount(), scoreMap);
			}
		}
	}

	/**
	 * Find the similarity of user listening history with other tracks by first
	 * creating a length 18 vector for track and then computing the cosine
	 * similarity.
	 * 
	 * @param track
	 * @param collection
	 * @param val
	 * @param totalplaycount
	 * @param scoreMap
	 */
	private void compareWithOthers(TrackInformation track,
			Collection<TrackUserBO> collection, double[] val,
			int totalplaycount, Map<Double, List<TrackInformation>> scoreMap) {
		
		double[] trackVector = getVector(track, totalplaycount, val);
		List<String> completed = new ArrayList<String>();

		for (TrackUserBO trackUserBO : collection) {
			int totpl = trackUserBO.getTotalplaycount();
			Map<String, TrackInformation> map = trackUserBO.getInfo();
			Collection<TrackInformation> trackInformations = map.values();

			for (TrackInformation information : trackInformations) {
				if (!completed.contains(information.getTrackId())) {
					completed.add(information.getTrackId());
					double[] preferenceVector = getVector(information, totpl,
							val);
					if (track.getYear() > 0 && information.getYear() > 0) {
						int tyear = track.getYear() - 1900;
						int pyear = information.getYear() - 1900;
						trackVector[17] = (double) tyear
								/ Math.max(tyear, pyear);
						preferenceVector[17] = (double) pyear
								/ Math.max(tyear, pyear);
					} else {
						trackVector[17] = 0;
						preferenceVector[17] = 0;
					}
					double score = calculateCosineSimilarity(trackVector,
							preferenceVector);
					System.out.println(track.getTrackId() + " : " + information.getTrackId() + " : " + score);
					List<TrackInformation> list = scoreMap.get(score);
					if (list != null && !list.isEmpty()) {
						list.add(information);
						scoreMap.put(score, list);
					} else {
						list = new ArrayList<TrackInformation>();
						list.add(information);
						scoreMap.put(score, list);
					}
				}
			}
		}
	}

	/**
	 * 
	 * Calculates cosine similarity between two vectors
	 * 
	 * @param trackVector
	 * @param preferenceVector
	 * @return
	 */
	private double calculateCosineSimilarity(double[] trackVector,
			double[] preferenceVector) {
		double num = 0, magt = 0, magp = 0, cosineSimilarity = 0;
		for (int i = 0; i < trackVector.length; i++) {
			num += trackVector[i] * preferenceVector[i];
			magt += trackVector[i] * trackVector[i];
			magp += preferenceVector[i] * preferenceVector[i];
		}
		cosineSimilarity = num / (Math.sqrt(magt) * Math.sqrt(magp));

		return cosineSimilarity;
	}

	/**
	 * Generate the vector for the track
	 * 
	 * @param track
	 * @param totalplaycount
	 * @param val
	 * @return
	 */
	private double[] getVector(TrackInformation track, int totalplaycount,
			double[] val) {
		double[] vector = new double[18];
		Set<String> genres = track.getGenres();
		for (String genre : genres) {
			vector[genreString.get(genre)] = 1;
		}
		double maxloud = val[0];
		double maxduration = val[1];
		vector[14] = track.getPlaycount() / totalplaycount;
		double loud = Math.pow(10d, track.getLoudness() / 20);

		vector[15] = loud / maxloud;
		vector[16] = track.getDuration() / maxduration;
		return vector;
	}

	/*public static void main(String[] args) {
		Collaborative collaborative = new Collaborative();
		collaborative
				.getRecommendations("515c8e45f18f8327ed834d694d5fb4c2755afd82");
		
		 * float[] vector = new float[18];
		 * 
		 * System.out.println(""+ vector[0]);
		 
	}*/
}
