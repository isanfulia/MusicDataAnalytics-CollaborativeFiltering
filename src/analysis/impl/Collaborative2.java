package analysis.impl;

import java.util.*;
import database.impl.*;

public class Collaborative2 {
	public ArrayList<ArrayList<Integer>> similarityMatrix;
	public ArrayList<String> trackID2int;
	public DBAccessor accessor;
	public static int NO_OF_RECOMMENDATIONS = 5;

	public Collaborative2() {
		similarityMatrix = new ArrayList<ArrayList<Integer>>();
		trackID2int = new ArrayList<String>();
		accessor = new DBAccessor();
		this.createSimilarityMatrix();
	}

	public void createSimilarityMatrix() {
		this.trackID2int = accessor.getAllTrackID();
		int dim = this.trackID2int.size();
		for (int i = 0; i < dim; i++) {
			similarityMatrix.add(new ArrayList<Integer>());
		}
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				this.similarityMatrix.get(i).add(0);
			}
		}
		this.fillSimilarityMatrix(this.accessor.getListeningHistoryByUser());
	}

	public ArrayList<Track> getUserRecommendations(String uid) {

		ArrayList<Track> history = accessor.getUserTracks(uid);
		ArrayList<Track> suggestions = new ArrayList<Track>();
		int numTracks = this.similarityMatrix.size();
		boolean[] maxflag = new boolean[numTracks];
		for (int i = 0; i < numTracks; i++) {
			maxflag[i] = false;
		}
		int size = history.size();
		if (size == 0) {
			int len = this.trackID2int.size();
			int i = 0;
			while (i < (this.NO_OF_RECOMMENDATIONS * 2)) {
				int rand = (int) (Math.random() * (double) len);
				if (maxflag[rand] == true)
					continue;
				else {
					i++;
					String tid = this.trackID2int.get(rand);
					suggestions
							.add(new Track(tid, accessor.getTrackTitle(tid)));
				}
			}
		} else {
			int count = 0, itr = 0, max = -1;
			String maxtid = "";
			while (count < this.NO_OF_RECOMMENDATIONS) {
				String tid = history.get(itr).id;
				int ind = this.trackID2int.indexOf(tid);
				for (int i = 0; i < numTracks; i++) {
					if (this.similarityMatrix.get(ind).get(i).intValue() > max
							&& maxflag[i] == false) {
						max = this.similarityMatrix.get(ind).get(i).intValue();
						maxtid = this.trackID2int.get(i);
					}
				}
				maxflag[this.trackID2int.indexOf(maxtid)] = true;
				suggestions.add(new Track(maxtid, accessor
						.getTrackTitle(maxtid)));
				itr++;
				count++;
				if (itr >= history.size()) {
					itr = 0;
				}
				max = -1;
			}
		}
		return suggestions;
	}

	public void fillSimilarityMatrix(ArrayList<HasPlayed> history) {
		int count = 0, totalPlayCount = 0;
		int i, j, k, historyLength = history.size();
		int c1, c2, min;
		ArrayList<String> trackGroup = new ArrayList<String>();
		ArrayList<Integer> pcount = new ArrayList<Integer>();
		for (i = 0; i < historyLength; i++) {
			trackGroup.add(history.get(i).trackID);
			pcount.add(new Integer(history.get(i).playcount));
			while (i + 1 < historyLength
					&& history.get(i).userID
							.compareTo(history.get(i + 1).userID) == 0) {
				trackGroup.add(history.get(++i).trackID);
				pcount.add(new Integer(history.get(i).playcount));
				totalPlayCount += history.get(i).playcount;
			}
			count++;
			System.out.println("For UserID " + history.get(i).userID
					+ ", No of tracks = " + trackGroup.size());
			for (j = 0; j < trackGroup.size() - 1; j++) {
				for (k = j + 1; k < trackGroup.size(); k++) {
					String tid1 = trackGroup.get(j);
					String tid2 = trackGroup.get(k);
					int row = this.trackID2int.indexOf(tid1);
					int col = this.trackID2int.indexOf(tid2);
					c1 = pcount.get(j);
					c2 = pcount.get(k);
					min = Math.min(c1, c2);
					this.similarityMatrix.get(row).set(
							col,
							Integer.valueOf(this.similarityMatrix.get(row)
									.get(col).intValue()
									+ (2 * min)));
					this.similarityMatrix.get(col).set(
							row,
							Integer.valueOf(this.similarityMatrix.get(col)
									.get(row).intValue()
									+ (2 * min)));
				}
			}
			trackGroup.clear();
		}
		System.out.println("Total users processed = " + count);
		System.out.println("Total play count = " + totalPlayCount);
	}

	public static void main(String[] args) {
		Collaborative2 c2 = new Collaborative2();
		c2.createSimilarityMatrix();
		ArrayList<Track> ans = c2
				.getUserRecommendations("f2a30cebffa76b5a643442ea8a79bb23c1a20a3e");
		for (int i = 0; i < ans.size(); i++) {
			System.out.println(ans.get(i).id + " -> " + ans.get(i).title);
		}
	}
}
