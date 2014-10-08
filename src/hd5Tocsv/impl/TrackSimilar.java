package hd5Tocsv.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TrackSimilar {
	public static void main(String[] args) {

		JSONParser parser = new JSONParser();

		try {
			File filesimtrack = new File("F:/finalbigdatacsv/csv/similartracks.csv");
			File filetracktag = new File("F:/finalbigdatacsv/csv/trackstags.csv");

			String path = args[0], path1, path2;
			char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
					'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
					'V', 'W', 'X', 'Y', 'Z' };
			char[] outerdir = { 'A', 'B' };


			FileWriter fileWritterst = null;
			BufferedWriter bufferWritterst = null;
			
			FileWriter fileWrittertt = null;
			BufferedWriter bufferWrittertt = null;
			
			fileWritterst = new FileWriter(filesimtrack, true);
			bufferWritterst = new BufferedWriter(fileWritterst);
			
			fileWrittertt = new FileWriter(filetracktag, true);
			bufferWrittertt = new BufferedWriter(fileWrittertt);
			
			for (char z : outerdir) {
				path1 = path + "\\" + z;
				for (char t : alphabet) {

					path2 = path1 + "\\" + t;
					for (char i : alphabet) {
						String pathMSD = path2 + "\\" + i;
						File directory = new File(pathMSD);
						File[] files = directory.listFiles();
						System.out.println(pathMSD);
						/*
						 * if(i == 'Z') { System.out.println("Z"); }
						 */
						if (files != null) {
							for (int index = 0; index < files.length; index++) {
								// Print out the name of files in the directory
								// System.out.println(files[index].toString());
								String filename = files[index].toString();

								Object obj = parser.parse(new FileReader(
										filename));

								JSONObject jsonObject = (JSONObject) obj;

								String track_id = (String) jsonObject
										.get("track_id");
								//System.out.println(track_id);

								// loop array
								JSONArray tags = (JSONArray) jsonObject
										.get("tags");
								Iterator<JSONArray> iterator = tags.iterator();
								while (iterator.hasNext()) {
									JSONArray values = (JSONArray) iterator
											.next();
									//System.out.println(values.get(0));
									bufferWrittertt.write(track_id + "#" + values.get(0));
									bufferWrittertt.flush();
									bufferWrittertt.newLine();
									/*
									 * Iterator<String> valuesiterator =
									 * values.iterator(); while
									 * (valuesiterator.hasNext()) {
									 * System.out.println
									 * (valuesiterator.next()); }
									 */
									// System.out.println();
								}

								JSONArray similars = (JSONArray) jsonObject
										.get("similars");
								Iterator<JSONArray> simiterator = similars
										.iterator();
								while (simiterator.hasNext()) {
									JSONArray similarsvalues = (JSONArray) simiterator
											.next();
									//System.out.println(similarsvalues.get(0));
									bufferWritterst.write(track_id + "#" + similarsvalues.get(0) + "#" + similarsvalues.get(1));
									bufferWritterst.flush();
									bufferWritterst.newLine();

									/*Iterator<Object> valuesiterator = similarsvalues
											.iterator();
									while (valuesiterator.hasNext()) {
										bufferWrittertt.write(track_id + "#" + values.get(0));
										bufferWrittertt.flush();
										System.out.println(valuesiterator
												.next());
									}*/

									// System.out.println();
								}
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}