create database MusicAnalytics;
use MusicAnalytics;
create table Artist
(
ArtistID varchar(50) primary key ,
Name varchar(100) ,
location varchar(100)
);

create table SimilarArtist
(ArtistID varchar(50),
similarArtistID varchar(50),
foreign key(ArtistID) references Artist(ArtistID),
foreign key(similarArtistID) references Artist(ArtistID),
primary key(ArtistID,similarArtistID)
);

#Multivalued attribute
create table Artist_Terms
(ArtistID varchar(50) , 
ArtistTerms varchar(100),
foreign key (artistID) references Artist(artistID),
primary key(ArtistID,ArtistTerms)
);


create table Artist_Genre #multivalued attribute
(ArtistID varchar(50) , 
Genre varchar(100),
foreign key (artistID) references Artist(artistID),
primary key(ArtistID,Genre)
);


create table Song
(
SongID varchar(50) primary key, 
title varchar(200),
year int
);

create table Song_Genre #multivalued attribute
(SongID varchar(50) , 
Genre varchar(100),
foreign key (songID) references Song(songID),
primary key(SongID, Genre)
);

create table album
(
AlbumID varchar(50) primary key,
name varchar(100),
year int 
);


create table track
(
trackID varchar(50) primary key,
title varchar(100),
duration float,
loudness float,
energy float,
songid varchar(50),
AlbumID varchar(50),
foreign key(songid) references song(songid),
foreign key(AlbumID) references Album(AlbumID)
);


create table Track_tags #multivalued attribute
(TrackId varchar(50) , 
tags varchar(100),
foreign key(trackID) references track(trackId),
primary key(trackID,tags)
);

create table Track_Genre #multivalued attribute
(TrackID varchar(50) , 
Genre varchar(100),
foreign key (TrackID) references Track(TrackID),
primary key(TrackID, Genre)
);

create table user
(
UserID varchar(50) primary key,
name varchar(100)
);
create table GenrePreference
(
UserID varchar(50),
GenrePreference varchar(100),
foreign key(userid) references user(userid),
primary key(userid,GenrePreference)

);



create table composes #Relation table
(
SongID varchar(50) ,
ArtistID varchar(50) ,
foreign key(SongID) references song(songID),
foreign key(ArtistID) references Artist(ArtistID),
primary key(SongID,ArtistID)
);

create table ListensTo #Relation table
(ArtistID varchar(50) ,
UserID varchar(50),
foreign key(artistID) references artist(ArtistID),
foreign key(userID) references user(userID),
primary key(ArtistID,UserID)
);

#==========================
create table HasPlayed #Relation table
(
SongID varchar(50),
UserID varchar(50),
playcount int ,
foreign key(songID) references song(songid),
foreign key(UserID) references user(UserID),
primary key(songID,UserID)
);


create table SimilarTrack
(TrackID varchar(50),
similarTrackID varchar(50),
confidence double,
foreign key(TrackID) references track(trackId),
foreign key(similarTrackID) references track(trackId),
primary key(TrackID,similarTrackID)
);

create table recorded #Relation table
(
AlbumID varchar(50) ,
ArtistID varchar(50) ,
foreign key(AlbumID) references Album(AlbumID),
foreign key(ArtistID) references Artist(ArtistID),
primary key(AlbumID,ArtistID)
);