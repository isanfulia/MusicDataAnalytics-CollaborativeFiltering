package csvToDB.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringEscapeUtils;

import database.impl.DBConnect;
/**
 * 
 * This class populates the ArtistTerms table of DB by reading data from CSV files
 *
 */
public class ArtistTermsCSVToDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DBConnect dbObj = new DBConnect();

			String pathStr = new String(
					"F:\\finalbigdatacsv\\csv\\artiststerms.csv");

			FileReader file = new FileReader(pathStr);
			BufferedReader reader = new BufferedReader(file);
			String line;

			while ((line = reader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "#");
				while (token.hasMoreTokens()) {

					String artistID = token.nextToken();

					String artistterm = token.nextToken();

					String str;

					str = "insert into musicanalytics.artist_terms (artistID, artistterms) values ("
							+ "'"
							+ StringEscapeUtils.escapeSql(artistID)
							+ "',"
							+ "'"
							+ StringEscapeUtils.escapeSql(artistterm) + "');";

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
