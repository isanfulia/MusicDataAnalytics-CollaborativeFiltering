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
 * This class populates the Artist table of DB by reading data from CSV files
 *
 */
public class ArtistCSVToDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DBConnect dbObj = new DBConnect();

			String pathStr = new String("F:\\finalbigdatacsv\\csv\\artists.csv");

			FileReader file = new FileReader(pathStr);
			BufferedReader reader = new BufferedReader(file);
			String line;
			List<String> dupNames = new ArrayList<String>();

			while ((line = reader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "#");
				while (token.hasMoreTokens()) {

					String artistID = token.nextToken();

					String location = token.nextToken();

					String name = token.nextToken();

					if (dupNames.contains(artistID)) {
						System.out.println(artistID);
					} else {
						dupNames.add(artistID);
						String str;
						if (location.equals("null")) {
							str = "insert into musicanalytics.artist (artistID, name) values ("
									+ "'"
									+ StringEscapeUtils.escapeSql(artistID)
									+ "',"
									+ "'"
									+ StringEscapeUtils.escapeSql(name) + "');";
						} else {
							str = "insert into musicanalytics.artist (artistID, name, location) values ("
									+ "'"
									+ StringEscapeUtils.escapeSql(artistID)
									+ "',"
									+ "'"
									+ StringEscapeUtils.escapeSql(name)
									+ "',"
									+ "'"
									+ StringEscapeUtils.escapeSql(location)
									+ "');";
						}

						dbObj.connectToDb();
						try {
							dbObj.st.execute(str);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}
			dbObj.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
