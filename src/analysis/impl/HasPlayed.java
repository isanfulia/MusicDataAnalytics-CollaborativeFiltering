package analysis.impl;

public class HasPlayed {
		public String userID,trackID;
		public int playcount;
		public HasPlayed(String uid,String tid,int pcount){
			this.playcount = pcount;
			this.trackID = tid;
			this.userID = uid;
		}
}
