package csvToDB.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;

import database.impl.DBConnect;
/**
 * 
 * This class populates the ListenTo table of DB by reading data from CSV files
 *
 */
public class ListenToCSVToDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DBConnect dbObj = new DBConnect();
			dbObj.connectToDb();

			String pathStr = new String("F:\\finalbigdatacsv\\csv\\qu.csv");

			FileReader file = new FileReader(pathStr);
			BufferedReader reader = new BufferedReader(file);

			FileReader file1 = new FileReader(
					"F:\\finalbigdatacsv\\csv\\quar.csv");
			BufferedReader reader1 = new BufferedReader(file1);
			List<String> artists = new ArrayList<String>();
			List<String> users = new ArrayList<String>();

			String line, line1;
			while ((line1 = reader1.readLine()) != null) {
				// String[] ars = line1.split("#");
				// String artistId = ars[0];
				artists.add(line1);
			}

			while ((line = reader.readLine()) != null) {
				// String[] ars = line1.split("#");
				// String artistId = ars[0];
				users.add(line);
			}

			int size = artists.size();
			int us = users.size();
			int i = 0, j = 0;
			while (true) {
				String userId = users.get(j);
				j = (j + 1) % us;
				String ar = artists.get(i);
				i = (i + 1) % size;

				String str;

				str = "insert into musicanalytics.listensto (ArtistID, UserID) values ("
						+ "'"
						+ StringEscapeUtils.escapeSql(ar)
						+ "',"
						+ "'"
						+ StringEscapeUtils.escapeSql(userId) + "'" + ");";

				System.out.println(str);
				try {
					dbObj.st.execute(str);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// dbObj.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}