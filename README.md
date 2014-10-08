MusicDataAnalytics-CollaborativeFiltering
=========================================
-----------------------------------------
Requirements
-----------------------------------------
1. Mysql server 5.0 or later
2. Java version 6.0 or higher
3. Libraries required
  1. mysql­java­connector­5.1.26.jar
  2. json­simple­1.1.jar
  3. jdf5.jar, jdf5obj.jar to read hdf5 files

  
------------------------------------------  
Steps for usage and installation
------------------------------------------
1. You need to download and Install mysql
MySQL ­­> http://www.mysql.org

2. Configure database properly and make sure its secured.

3. The database server uses root as default username and password.
Password can be changed by using
UPDATE mysql.user SET Password=PASSWORD('cleartext password')
 WHERE User='username' AND Host='%.host';
Also a new user can be created and should be granted insert, select and update permissions
You need to change the username and password in DBConnect.java according to the DB user
and password.

4. Create database schema musicanalytics and tables by running the MusicAnalyticsDB_Script.

5. Once schema and tables are created the data can be populated by using the java programs
contained in this source code folder. This step have been completed.

6. The Source files consists of reading the raw data in various formats such as json, hdf5 and
converts it to csv files using java.
The csv files are then read, parsed and the data is cleaned and inserted into mysql using java
jdbc connection. This step is also completed.

7. Steps 5 and 6 can be avoided by directly importing the data sql file provided into the database.

8. Database will then be populated and will be ready for data mining.

9. To run the user Interface, class MusicUI inside the analysis package needs to be run. This will
display a user interface with list of users with prev, next and generate buttons. Click on prev or
next buttons to get more users.

10. Select a particular user and click generate recommendation. This will display the listening
history of user and the recommended songs suggested by the algorithm.

11. Select any song and click play button, this will update that song in user listening history in
DB. If the user is listening  that song more than once, the playcount will be incremented.

12. Play any other songs or select any other user and generate recommendation.
