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
 * This class populates the Album table of DB by reading data from CSV files
 *
 */
public class AlbumCSVToDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBConnect dbObj = new DBConnect();

		try {
			String pathStr = new String("F:\\finalbigdatacsv\\csv\\albums.csv");

			FileReader file = new FileReader(pathStr);
			BufferedReader reader = new BufferedReader(file);
			String line;
			List<String> dupNames = new ArrayList<String>();
			Map<String, String> val = new HashMap<String, String>();

			while ((line = reader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "#");
				while (token.hasMoreTokens()) {

					String albumId = token.nextToken();
					String name = token.nextToken();
					int year = Integer.parseInt(token.nextToken());
					if (dupNames.contains(name)) {
						String oriId = val.get(name);
						System.out.println(albumId + "#" + oriId);
					} else {
						val.put(name, albumId);
						dupNames.add(name);

						String str = "insert into musicanalytics.album (albumID, name, year) values ("
								+ "'"
								+ StringEscapeUtils.escapeSql(albumId)
								+ "',"
								+ "'"
								+ StringEscapeUtils.escapeSql(name)
								+ "',"
								+ year + ");";

						dbObj.connectToDb();
						try {
							dbObj.st.execute(str);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
