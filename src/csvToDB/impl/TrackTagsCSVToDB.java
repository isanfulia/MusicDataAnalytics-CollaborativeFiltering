package csvToDB.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringEscapeUtils;

import database.impl.DBConnect;
/**
 * 
 * This class populates the TrackTags table of DB by reading data from CSV files
 *
 */
public class TrackTagsCSVToDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DBConnect dbObj = new DBConnect();
			dbObj.connectToDb();

			String pathStr = new String(
					"F:\\finalbigdatacsv\\csv\\trackstags.csv");

			FileReader file = new FileReader(pathStr);
			BufferedReader reader = new BufferedReader(file);
			String line, line1;

			while ((line = reader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "#");
				while (token.hasMoreTokens()) {

					String trackId = token.nextToken();

					String tag = token.nextToken();

					String str;

					str = "insert into musicanalytics.track_tags (trackID, tags) values ("
							+ "'"
							+ StringEscapeUtils.escapeSql(trackId)
							+ "',"
							+ "'"
							+ StringEscapeUtils.escapeSql(tag)
							+ "'"
							+ ");";

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
