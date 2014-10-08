package database.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import analysis.impl.TrackInformation;
import analysis.impl.TrackUserBO;
import analysis.impl.HasPlayed;
import analysis.impl.Track;
/**
 * 
 * This class is responsible for performing all DB related queries.
 *
 */
public class DBAccessor {
	private DBConnect connect;

	public DBAccessor() {
		connect = new DBConnect();
		init();
	}

	private void init() {
		connect.connectToDb();
	}

	/**
	 * Get all the tracks for the user passed .i.e get the listening history of user.
	 * Function get the tracks from DB for the user and converts the DB result to business object
	 * 
	 * @param userId
	 * @return {@link TrackUserBO}
	 */
	public TrackUserBO getListeningHistory(String userId) {
		String query = "select s.userId, s.name, h.playcount ,t.trackId, tg.genre,t.duration, t.loudness, so.year,t.title from  User s, track_genre tg, "
				+ "hasplayed h, song so, track t where s.userId=h.userId and h.songId=so.songId and tg.trackId=t.trackId and so.songId=t.songId and s.userId="
				+ "'" + userId + "'";
		
		ResultSet set;
		Map<String, TrackUserBO> map = new HashMap<String, TrackUserBO>();
		try {
			set = connect.getSt().executeQuery(query);
			while (set.next()) {
				convertToTrackUserBO(set, map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		TrackUserBO bo = map.get(userId);
		return bo;
	}

	/**
	 * 
	 * Based on the genre of the user, get the tracks from other users 
	 * who share atleast one common genre
	 * 
	 * @param genres
	 * @param userId
	 * @return
	 */
	public Collection<TrackUserBO> getUsers(Set<String> genres, String userId) {
		String query = "select s.userId, s.name, h.playcount ,t.trackId, tg.genre, t.duration, t.loudness, so.year,t.title from  User s, "
				+ "hasplayed h, song so, track t, track_genre tg where s.userId=h.userId and tg.trackId=t.trackId and h.songId=so.songId and "
				+ "so.songId=t.songId and s.userId<>"
				+ "'"
				+ userId
				+ "'"
				+ " and tg.genre in (";

		Iterator<String> iterator = genres.iterator();
		while (true) {
			query = query + "'" + iterator.next() + "'";
			if (iterator.hasNext()) {
				query = query + ",";
			} else {
				query = query + ")";
				break;
			}
		}
		Map<String, TrackUserBO> map = new HashMap<String, TrackUserBO>();
		ResultSet set;
		try {
			set = connect.getSt().executeQuery(query);
			while (set.next()) {
				convertToTrackUserBO(set, map);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map.values();
	}

	/**
	 * Convert the resultset obtained from DB to business object containing the 
	 * listening history of retrieved users.
	 * 
	 * @param set
	 * @param map
	 */
	private void convertToTrackUserBO(ResultSet set,
			Map<String, TrackUserBO> map) {
		try {
			String user = set.getString(1);
			String name = set.getString(2);
			int playcount = set.getInt(3);
			String trackId = set.getString(4);
			String genre = set.getString(5);
			double duration = set.getDouble(6);
			double loudness = set.getDouble(7);
			int year = set.getInt(8);
			String title = set.getString(9);

			// genres.add(genre);
			System.out.println(user + " " + name + " " + playcount + " "
					+ trackId + "  " + genre);
			TrackUserBO bo = null;
			if (map.containsKey(user)) {
				bo = map.get(user);
			} else {
				bo = new TrackUserBO();
				map.put(user, bo);
			}
			Map<String, TrackInformation> info = bo.getInfo();
			TrackInformation t = info.get(trackId);
			if (t == null) {
				t = new TrackInformation();
				t.setPlaycount(playcount);
				t.setTrackId(trackId);
				t.getGenres().add(genre);
				t.setYear(year);
				t.setDuration(duration);
				t.setLoudness(loudness);
				t.setTitle(title);
				bo.setTotalplaycount(bo.getTotalplaycount() + playcount);
				info.put(trackId, t);
			} else {
				t.getGenres().add(genre);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Gets the maximum value of loudness and duration attribute from DB
	 * 
	 */
	public double[] getMaxLoudnessAndDuration() {
		String query = "select max(loudness) as maxLoudness, max(duration) as maxDuration from track";
		ResultSet set;
		double[] values = new double[2];
		try {
			set = connect.getSt().executeQuery(query);
			set.next();
			double maxlo = set.getDouble(1);

			double maxloud = Math.pow(10d, maxlo / 20);
			double maxdu = set.getDouble(2);
			values[0] = maxloud;
			values[1] = maxdu;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return values;

	}

	/**
	 * Based on the query, retrieve the user list and return the userId and name
	 * 
	 * @param query
	 * @return
	 */
	public String[][] getUserList(String query) {
		ResultSet set;
		String[][] userdata = new String[10][];

		try {
			set = connect.getSt().executeQuery(query);
			int i = 0;
			while (set.next()) {
				String userId = set.getString(1);
				String name = set.getString(2);
				userdata[i] = new String[2];
				userdata[i][0] = userId;
				userdata[i][1] = name;
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userdata;
	}

	/**
	 * 
	 * @return
	 */
	public List<TrackInformation> getTracks() {
		String query = "select trackId, title from track limit 5";
		ResultSet set;
		List<TrackInformation> sugg = new ArrayList<TrackInformation>();
		try {
			set = connect.getSt().executeQuery(query);
			while (set.next()) {
				String trackId = set.getString(1);
				String name = set.getString(2);
				TrackInformation info = new TrackInformation();
				info.setTitle(name);
				info.setTrackId(trackId);
				sugg.add(info);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sugg;
	}

	/**
	 * 
	 * @param trackId
	 * @param userId
	 */
	public void updateCountInHasPlayed(String trackId, String userId) {
		String query = "select songId from track where trackId='"+trackId+"'";
		ResultSet set;
		try {
			String songId = null;
			set = connect.getSt().executeQuery(query);
			while (set.next()) {
				songId = set.getString(1);
				break;
			}
			if(songId != null) {
				int playcount = 1;
				String q = "select songId, userId, playcount from hasPlayed where userId='" +userId+"'" + " and songId='" + songId+"'";
				set = connect.getSt().executeQuery(q);
				String qu = null;
				if(set.next()) {
					playcount = set.getInt(3) + 1;
					 qu = "update hasPlayed set playcount = " + playcount + " where songId = '" + songId+"'" + 
					" and userId='" + userId+"'";
				} else {
					 qu = "insert into hasPlayed (SongID, UserID, playcount) values ("
							+ "'"
							+ StringEscapeUtils.escapeSql(songId)
							+ "',"
							+ "'"
							+ StringEscapeUtils.escapeSql(userId)
							+ "'"
							+ "," + playcount + ");";
				}
				connect.getSt().execute(qu);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        
        public ArrayList<String> getAllTrackID(){
            String query = "select trackID from track";
            ResultSet set;
            ArrayList<String> trackIDs = new ArrayList<String>();
            int cnt=0;
            try{
                set = connect.getSt().executeQuery(query);
                while(set.next()){
                    trackIDs.add(set.getString("trackID"));
                    cnt++;
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            System.out.println(cnt);
            return trackIDs;
        }
        
        public ArrayList<HasPlayed> getListeningHistoryByUser(){
            String query = "select trackID,UserID,playcount from hasplayed h,track t where h.SongID = t.songid order by UserID";
            ResultSet set;
            ArrayList<HasPlayed> ans = new ArrayList<HasPlayed>();
            int cnt=0;
            try{
                set = connect.getSt().executeQuery(query);
                while(set.next()){
                    ans.add(new HasPlayed(set.getString("UserID"),set.getString("trackID"),set.getInt("playcount")));
                    cnt++;
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            System.out.println(cnt);
            return ans;
        }
        
        public ArrayList<Track> getUserTracks(String uid){
            String query = "select t.trackID,t.title from hasplayed h, track t where t.songid = h.SongID and h.UserID = '"+uid+"'";
            ArrayList<Track> ans = new ArrayList<Track>();
            ResultSet set;
            int cnt=0;
            try{
                set = connect.getSt().executeQuery(query);
                while(set.next()){
                    ans.add(new Track(set.getString("trackID"),set.getString("title")));
                    cnt++;
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            System.out.println(cnt);
            return ans;
        }
        
        public String getTrackTitle(String tid){
            String query = "select title from track where trackID = '"+tid+"'";
            String ans="";
            ResultSet set;
            int cnt=0;
            try{
                set = connect.getSt().executeQuery(query);
                while(set.next()){
                    ans = set.getString("title");
                    cnt++;
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            System.out.println(ans);
            return ans;
        }
        
        public static void main(String[] args){
            DBAccessor a = new DBAccessor();
            //a.getAllTrackID();
            //a.getListeningHistoryByUser();
            //a.getUserTracks("0002236e33c94f6d300fa52a045e0c7ae1e19ccf");
            a.getTrackTitle("TRAICHS128E0785545");
        }

        /**
         * 
         * @param userId
         * @param mapp
         */
		public void updateGenrePreference(String userId, Map<String, Double> mapp) {
			Set<String> genres = mapp.keySet();
			for(String genre : genres) {
				String query = "select * from genrepreference where genrepreference = '" + 
			genre + "' and userId = '" + userId + "'";
				ResultSet set;
				try{
	                set = connect.getSt().executeQuery(query);
	                if(set.next()) {
	                	String updatequery = "update genrepreference set confidence=" + mapp.get(genre) +" where userId = "
								+ "'"
								+ StringEscapeUtils.escapeSql(userId)
								+ "' and genrepreference = "
								+ "'"
								+ StringEscapeUtils.escapeSql(genre)
								+ "'";
	                	connect.getSt().execute(updatequery);
	                } else {
	                	String insertquery = "insert into genrepreference values ("
								+ "'"
								+ StringEscapeUtils.escapeSql(userId)
								+ "',"
								+ "'"
								+ StringEscapeUtils.escapeSql(genre)
								+ "',"
								+ mapp.get(genre) + ");";
	                	connect.getSt().execute(insertquery);
	                }
	            }catch(SQLException e){
	                e.printStackTrace();
	            }
			}
		}
}
