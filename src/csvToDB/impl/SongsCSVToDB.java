package csvToDB.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringEscapeUtils;

import database.impl.DBConnect;
/**
 * 
 * This class populates the Songs table of DB by reading data from CSV files
 *
 */
public class SongsCSVToDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DBConnect dbObj = new DBConnect();

			String pathStr = new String("F:\\finalbigdatacsv\\csv\\songs.csv");

			FileReader file = new FileReader(pathStr);
			BufferedReader reader = new BufferedReader(file);
			String line;
			List<String> dupNames = new ArrayList<String>();

			while ((line = reader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "#");
				while (token.hasMoreTokens()) {

					String songID = token.nextToken();
					int year = Integer.parseInt(token.nextToken());
					String title = token.nextToken();

					if (dupNames.contains(songID)) {
						System.out.println(title + " " + songID);

					} else {
						dupNames.add(songID);

						String str = "insert into musicanalytics.song (songID, title, year) values ("
								+ "'"
								+ StringEscapeUtils.escapeSql(songID)
								+ "',"
								+ "'"
								+ StringEscapeUtils.escapeSql(title)
								+ "',"
								+ year + ");";
						System.out.println(str);
						dbObj.connectToDb();
						dbObj.st.execute(str);

					}
				}

			}
			dbObj.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
