package analysis.impl;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * Business object containing the required information for a track
 *
 */
public class TrackInformation {
	String trackId;
	double duration;
	double loudness;
	int year;
	Set<String> genres;
	int playcount;
	String title;
	
	public Set<String> getGenres() {
		return genres;
	}
	public void setGenres(Set<String> genres) {
		this.genres = genres;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public TrackInformation() {
		genres = new HashSet<String>();
	}
	public String getTrackId() {
		return trackId;
	}
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getPlaycount() {
		return playcount;
	}
	public void setPlaycount(int playcount) {
		this.playcount = playcount;
	}
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public double getLoudness() {
		return loudness;
	}
	public void setLoudness(double loudness) {
		this.loudness = loudness;
	}
}