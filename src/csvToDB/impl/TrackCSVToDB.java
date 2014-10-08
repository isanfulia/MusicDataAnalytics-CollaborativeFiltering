package csvToDB.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringEscapeUtils;

import database.impl.DBConnect;
/**
 * 
 * This class populates the Track table of DB by reading data from CSV files
 *
 */
public class TrackCSVToDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DBConnect dbObj = new DBConnect();

			String pathStr = new String("F:\\finalbigdatacsv\\csv\\tracks.csv");

			FileReader file = new FileReader(pathStr);
			BufferedReader reader = new BufferedReader(file);
			String line, line1;
			List<String> dupNames = new ArrayList<String>();
			Map<String, String> val = new HashMap<String, String>();
			Map<String, String> map = new HashMap<String, String>();

			FileReader file1 = new FileReader(
					"F:\\finalbigdatacsv\\csv\\duplicates\\dupalbum_ori.txt");
			BufferedReader reader1 = new BufferedReader(file1);

			while ((line1 = reader1.readLine()) != null) {
				String[] split = line1.split("#");
				map.put(split[0], split[1]);
				dupNames.add(split[0]);
			}
			while ((line = reader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "#");
				while (token.hasMoreTokens()) {

					String trackID = token.nextToken();
					float duration = Float.parseFloat(token.nextToken());
					float loudness = Float.parseFloat(token.nextToken());

					float energy = Float.parseFloat(token.nextToken());

					String title = token.nextToken();

					String albumID = token.nextToken();

					String songid = token.nextToken();

					if (dupNames.contains(albumID)) {
						albumID = map.get(albumID);
					}

					String str = "insert into musicanalytics.track (trackID, duration, loudness, energy, title, albumID, songid) values ("
							+ "'"
							+ StringEscapeUtils.escapeSql(trackID)
							+ "',"
							+ duration
							+ ","
							+ loudness
							+ ","
							+ energy
							+ ","
							+ "'"
							+ StringEscapeUtils.escapeSql(title)
							+ "',"
							+ "'"
							+ StringEscapeUtils.escapeSql(albumID)
							+ "',"
							+ "'" + StringEscapeUtils.escapeSql(songid) + "');";
					System.out.println(str);
					dbObj.connectToDb();
					try {
						dbObj.st.execute(str);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
			dbObj.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
