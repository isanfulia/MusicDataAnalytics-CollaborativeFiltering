package hd5Tocsv.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Track_Genre {
	public static void main(String[] args) {
		String pathStr=new String("C:\\Users\\Isankumar\\Desktop\\newgenreset.txt");

		FileReader file;
		try {
			file = new FileReader(pathStr);
			File filetg = new File("F:/finalbigdatacsv/csv/tracks_genre.csv");

			BufferedReader reader= new BufferedReader(file);
			String line, line1;
			FileWriter fileWrittertg = null;
			BufferedWriter bufferWrittertg = null;
			
				fileWrittertg = new FileWriter(filetg, true);
				bufferWrittertg = new BufferedWriter(fileWrittertg);
			try {
				while((line=reader.readLine())!=null)
				 {
					System.out.println(line);
					String[] splits = line.split(",");
					String[] genres = splits[0].split(" and ");
					
					String trackId = splits[1];
					//bufferWrittertg.write(trackId + "#");
					for(String genre : genres) {
						bufferWrittertg.write(trackId + "#" + genre);
						bufferWrittertg.flush();
						bufferWrittertg.newLine();
					}
					System.out.println(splits[0]);
					System.out.println(splits[1]);
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
