package csvToDB.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringEscapeUtils;

import database.impl.DBConnect;
/**
 * 
 * This class populates the Composes table of DB by reading data from CSV files
 *
 */
public class ComposesCSVToDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DBConnect dbObj = new DBConnect();
			dbObj.connectToDb();

			String pathStr = new String(
					"F:\\finalbigdatacsv\\csv\\composes.csv");

			FileReader file = new FileReader(pathStr);
			BufferedReader reader = new BufferedReader(file);
			String line, line1;

			while ((line = reader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "#");
				while (token.hasMoreTokens()) {

					String songId = token.nextToken();

					String artistId = token.nextToken();

					String str;

					str = "insert into musicanalytics.composes (songId, artistId) values ("
							+ "'"
							+ StringEscapeUtils.escapeSql(songId)
							+ "',"
							+ "'"
							+ StringEscapeUtils.escapeSql(artistId)
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
