package csvToDB.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringEscapeUtils;

import database.impl.DBConnect;
/**
 * 
 * This class populates the UserHasPlayed table of DB by reading data from CSV files
 *
 */
public class UserHasPlayedCSVToDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DBConnect dbObj = new DBConnect();
			dbObj.connectToDb();

			String pathStr = new String("F:\\finalbigdatacsv\\csv\\hasplayed.csv");

			FileReader file = new FileReader(pathStr);
			BufferedReader reader = new BufferedReader(file);
			String line, line1;

			while ((line = reader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "#");
				while (token.hasMoreTokens()) {

					String userId = token.nextToken();

					String songid = token.nextToken();

					int playcount = Integer.parseInt(token.nextToken());

					String str;

					str = "insert into musicanalytics.hasplayed (SongID, UserID, playcount) values ("
							+ "'"
							+ StringEscapeUtils.escapeSql(songid)
							+ "',"
							+ "'"
							+ StringEscapeUtils.escapeSql(userId)
							+ "'"
							+ "," + playcount + ");";

					System.out.println(str);
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
