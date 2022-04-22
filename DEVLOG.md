NEA Programming Project Devlog
---
This document will entail the development log for ‘Homeroom’. If any challenges are encountered, the solutions will also be documented. Documentation will be done every day that some form of relevant progress has been made.

### 19/01/2022 - Wednesday
Some progress was made. Main documentation for ‘Homeroom’ was done through the use of a GitHub repository. Main wiki for a user manual will be held there as will the software licensing. GitHub repository will also be used to store source code (obviously!) for ‘Homeroom’. A Trello board will serve as the main form of task tracking and should ensure I see what needs to be done first and soonest. Only thing I need to be aware of is falling behind on that and accidentally missing deadlines. 

Additionally worked on some issue templates for GitHub. Had a minor look at Milestones, Projects and Actions which seem like a good way to automate a few things such as releases. Projects seem like a good way to organise some more stuff. I need to look into that, as right now it sounds similar to a Trello in terms of organisation. Should this be the case, it’s not really best for me.

Also started working on a stakeholder interview form. This will be distributed across to teachers, administrative staff and tutors where I deem fit. I am also considering users on Reddit as a reliable source of potential stakeholder opinion due to the wide range of people their community holds. It will not be hard to find school administrators and teachers willing to fill out a small survey. Teachers within my school will also be able to help with the completion of this survey. Due to privacy issues within the [REDACTED FOR PRIVACY PURPOSES], the forms have had to be hosted on my personal computer. A .csv file holding results will need to be attached detailing any useful results. 

### 18/02/2022 - Friday
Did some extra planning and finally got some form of order in as to what should be worked on first in terms of the writing of code. This is a decent step forward in the right direction, ensuring that the code written behind ‘Homeroom’ is written as fast as possible. 

### 22/02/2022 - Tuesday
Begun testing out the functionality of the integrations my IDE has with the GUI library I plan on using. Once I’ve played around with that for a bit, gotten used to its documentation and methods, I’ll begin working on the first pieces of my frontend for ‘Homeroom’. Once that’s been established, I need to work on the key parts for the backend. 

I plan on working on both the frontend and backend simultaneously. Frontend user-input features will be written before integrating the backend to create the feature as a whole. In this sense, RAD is being used. However, I feel the development methodology I am using follows along the lines of the Agile Model. 

Currently, I’ve had some issues that may seriously hamper the speed at which I’m able to produce frontend-based content for ‘Homeroom’. I’ll need to make support tickets with the maintainers of the IDE and see if there is a temporary fix to the solution I’d like to make use of.
A worst-case scenario would be where I have to manually write each line of code to have the layout of the GUI ready. After that, I’d also need to write all logic related to the GUI. 

### 26/02/2022 - Saturday
Planned out how I plan on using the Swing framework. I’ll have to write a class containing methods which I can access throughout the program to handle my GUIs, as the UI designing tools built into my IDE just don’t cut it.

I plan on beginning work on this as soon as I can.

### 04/03/2022 - Friday
Started work on the program itself. The code analysis workflow I’ve established to run checks every commit seems to be working nicely with the first initial commit, and as I plan on working on both the front end and the back-end simultaneously, I’ll have to start working on the GUI along with the Client-Server Communication components. Should hopefully finish my experiments with Swing today, and then I can start working on reading and writing to a MongoDB database. I may consider making a switch to an SQL server, but I feel the way that it stores data won’t suit my project’s use-case properly.

Started up a basic VPS thanks to Oracle for free today, along with a MongoDB cluster. Only the DB cluster will likely need to be used for now, but if the worst comes to worst, I can make use of the more control and options in trade for processing power by using the free VPS. I’ve also managed to make the first few initialising commits needed to start off ‘Homeroom’, and some decent experimentation and practise work with Swing has been done. I can now start actual work on the project itself.

Login page logic is generally solid and good to go, along with the design of the GUI. Personally, it looks disgusting. But accounting for my absence of artistic talent, it’ll do until I can work on having a nicer one made. As I plan on working on both the frontend and the backend at the same time, I need to fully finish the inner working of the frontend GUI before I can weave in the backend needed for it. Once I feel that the login GUI is as fleshed out as is needed, I can work on establishing a connection with the MongoDB cluster, and I can start experimenting with sending data back and forth between the cluster.

As it’s impossible to predict where “25%” will be in my project, I’ll be satisfied with having some solid code behind the client-server connection, and having the layout for the “Main” GUI fully designed. This is something that I plan on working on after I’ve finished the relevant backend work for the logins.
My only concern right now is that with the speed it takes me to write proper, finalised code, combined with the time needed to do appropriate research and documentation searching, it’ll take too long to have everything finished to an appropriate degree by Monday, which is when I want to have 25% done. I don’t want this to be a rush job.

### 05/03/2022 - Saturday
Had some issues in terms of how the user would go about logging into the database of ‘Homeroom’, as I had realised that MongoDB clusters don’t actually have a host name that you can make use of to log in programmatically. To get around this, I have created a config.json file that is created upon initial start-up of the program. Then, the user is able to edit the file accordingly to ensure that the connection string provided by MongoDB Cloud services to the user can be inserted into the file, and through the login GUI of Homeroom, administrators can login using appropriate users that they will have setup through MongoDB Cloud and can then edit the database as is appropriate.

However, this is not a solution that I deem satisfactory. Should the client choose to make use of a MongoDB cloud cluster, then this solution will have to do for now. However, my intention for the user, as it always has been, is to ensure no configuration needs to be done through the use of editing some form of text file. The solution that I have worked on right now contradicts that. As such, I will also begin planning for ‘Homeroom’, to make use of other NoSQL database solutions, such as a MongoDB server running off of a VPS. This will mean that the user can enter the host name, their usernames and passwords and log in without having to edit a configuration file. 

### 06/03/2022 - Sunday
Going along the lines of my “perfective maintenance” policy, I’ve recently done a minor refactor of the class and any related implementations of that class to ensure that a GUI Window is now opened up upon use of the constructor written. This means that the same object isn’t used to do all the GUI work for multiple Windows and the same time, and multiple instances of the same object are created to do the work using the utility methods written. This will also save me having to pass as many parameters as I did through my methods, which will save some time and memory. I hope.

Also wrote some incredibly minor foundations for my database handling class to supplement the minor amount of research I managed to get done today. Tomorrow, I should be able to finish any login and database work, test it, and then move onto some more frontend work on the main GUI that ‘Homeroom’ users will be working on. I also want to plan a very large GUI refactor that will completely overhaul how the current GUI works. As this may take a very long time and will almost definitely involve a very large refactor of all source code that will have been written, I will likely leave this for last to ensure that I get the core functionality, and maybe even the extra planned features of ‘Homeroom’ developed first.

### 07/03/2022 - Monday
Was unable to figure out how to properly make a connection to the MongoDB cluster. For some reason, there is no way to actually check if a connection between a client and a DB is active or not. I’ve got no idea how I’m going to implement this, but there should be something within the documentation that I’m able to use. Was also unable to properly connect and access information within the database. As I plan on having a username password based login-system to allow certain users to edit and read certain pieces of information, I need to be sure that the login information can actually be passed through the ConnectionString object to ensure that the connection is active for the right user. 

I currently face two issues when working with this. When properly passing the string through, the string seems to be invalid, and the API between the MongoDB cluster and my application will end up throw an InvalidFormatException error. If this does not happen and arguments are passed through the string as needed, then the connection occurs as per-standard. However, what was not shown to me was the fact that, when passing through the wrong credentials, the connection seems to be active anyways. This is something that should not be happening, and as such, I am unsure as to what the cause of this is. I have not been able to solve this issue at this time.

To ensure that I am not wasting any time, I did some minor work on the main GUI that users will mainly be using. Positioned and formatted the buttons and fields of text that will be used, but did not do any backend work on them as of yet. I will work on the backend portion of this GUI once the login system has been perfected. 

Also started some plans for a Homeroom “icon”. As Swing (the GUI library I am making use of) allows for the use of custom icons to be used, I will find a png from the internet, and use it for the logo of my application. Just a nice cosmetic touch I felt I could implement.

### 08/03/2022 - Tuesday
Finally figured out how to properly connect to the database! A rather stupid mistake on my end, but I also blame the bad logging practises and lack of documentation on the part of MongoDB. After asking around, I had realised that the connection string used to connect to the database needed to be URL-encoded (which I had done) and needed to be without the speech marks that generally wrap strings. The stack trace that appeared when running the erroneous code contradicted these findings, hence my confusion. Upon replacing speech marks with an empty character, effectively removing them, connecting had worked as expected. However, the mistake on my end lied with the way the user would be entering in their connection string for the database.This was done through the use of a basic JSON config file, which, when pulling a String from it, will be wrapped in speech marks. These speech marks were not removed when passing it through as the ConnectionString object.

Now that this issue with logging into the cluster has been resolved, more work can be done to perfect the login system. Once this has been done, a trace table of some form will need to be created so I can test this feature and document it.

### 10/03/2022 - Thursday
Having some major issues with actually verifying the connection to the database cluster. Documentation on this is unclear at best, and as such, I’m generally in the dark when it comes to resolving this issue. Right now, when passing through both wrong and right credentials through the connection string, the same exception is thrown, stating that the connection has timed out. (The computer tried connecting using those credentials so many times that it eventually just gave up and is now sure that it won’t work). This could mean a vast majority of things, depending on what the error message is. This would have helped me diagnose the issue, if each error message was actually different. As such, I’m going to have to continue to do research where reasonably possible in an attempt to fix this issue, while making blind code changes. 

As the database is the core part of this project, any code written for it needs to be as tidy and efficient as possible. The time spent making sure the methods within this code work as well as possible is time well spent, provided I can actually do what I need to do.

P.S: Have finally figured out what the issue was. As per usual, it seemed to be an issue with the connection string. I don’t have the time today, but I will need to do some refactoring of code to ensure that I am able to differentiate between a successful and failed login. I need to be able to tell when a connection is valid, given the credentials that the user has entered.

### 11/03/2022 - Friday
Finally managed to work on the database issues and push forward a fix for the databases connection issues. Seemed to be an issue with the WiFi connection with the school. As soon as I switched to another network, connections with the database have gone well.

Also managed to start work on some utility methods to connect to the database, such as a method to verify the current user's connection to the database. Also worked on some basic methods to actually connect to the database.

I should now be able to start work on the main GUI and it's involved backend now. This is something I can now do at great speed now that I have worked my way through the main login system and connection methods. Have also recently gotten some time to perfect how it works, although the code doesn't seem to be very tidy for now. Will have to look into ways of tidying that up.

Also managed to work on some major planning in terms of the functionality of almost everything to do with the main Homeroom GUI. Some features are still to be planned, but I am confident I'll be able to get around to it as soon as possible. 

The next stage in development is testing of the login system developed for Homeroom.

### 12/03/2022 - Saturday
Didn't manage to get much work done, but did manage to do some minor code-cleanups here and there within the GUIUtils class.

Will hopefully make a start on planning some other features of Homeroom, but that is uncertain for now.

### 17/03/2022 - Thursday
Managed to make a little bit more progress, but it wasn't enough work in terms of the code that was written.

I've done some planning as to the fields of data I want to have handled by Homeroom, and I've commissioned third parties to generate said data for me. As the NEA is something you're meant to do entirely without help, this may contradict my "no help at all" policy. However, as this is something as mundane and insignificant as data entry, I don't see why I should have to waste my time entering in each student myself like this. As such, I've simply paid someone to generate 1000 students worth of data, with fields of information I felt would typically be stored by a school.

Using this data (in the form of an Excel file), I can manually upload this to my MongoDB cluster, and then write code accordingly to ensure that 'Homeroom' is able to read from the database, and get the according information from the students.

After this, I can then work on deletion of data (as that will be easier than having to handle the code for data writing), and then the code for actually writing the data. Once all of that has been written, that will essentially be the code for the first major feature (The ability to add, remove and edit the data of students accordingly) complete. The testing process for that will be somewhat long, but hopefully, I should be able to make quick progress once I get over the roadblock of learning how to make use of MongoDB.

I've been trying to convince myself for some time that doing this through MySQL would arguably be quicker and easier to do, but I am still not confident in MySQLs abilities to handle said data at scale and speed, nor am I sure if the data that needs storing is entirely applicable to what I'm handling.

### 29/03/2022 - Tuesday
Did some very minor work on Homeroom, implemented the icon within the GUI,so Homeroom now makes use of the custom icon I found on the internet instead the default Java icon.

### 31/03/2022 - Thursday
Did some thinking about the structure of Homeroom's database, and ensured that it has been designed in Third Normal Form. This will allow for minimal amounts of data to be edited where needed.

Databases also need to ensure that the data within it can be easily searched for and edited if need be. Foreign keys need to be considered carefully to make sure that if they need to be deleted and/or edited, minimal actions are done.

Collections of documents need to be searched through efficiently if need be, primary keys should be the only set of keys that need to be searched through, but it is hard to say what data may need to be used. Secondary keys such as names would also likely need to be searched through for QOL for the user.

My current structure currently holds three entities, with two "link tables" ensuring relations between each object. These can then be used within the rest of the program to build assumptions between each object, and to insert data.

What also needs to be planned is when database entries are made, to ensure that data is inserted at the right time, when the right information is made available to the system. Since I am currently working on the addition, editing and deletion of students, this is fortunately a problem I don't need to handle until later.
However, it is still likely an issue I need to take on some point soon, and it should be tackled when I have the time and space to think through things properly.

### 14/04/2022 - Thursday
Managed to import a pre-made database of students for use within my program. This pre-made database will be a sample set of data I can use within my program as needed for testing, but normally, users will be making their own data. I also need to think of a way to allow users to configure what data they can input into the system, as I feel that would be a good way of going about it. As every school holds potentially different pieces of information, such a feature is important. I feel like having this be possible through a config file would be a good idea, but I'm not 100% on it yet.

For now, I plan on having the fields of information that users can throw in  be hard-coded, as that will make things much easier in the long-run, as I now need to rush somewhat to meet the deadline for 75%. Already added a Trello card for this improvement, but it will need to be done at some point later down the line.

I also plan on implementing compatiability for multiple databases, as now thet I have properly planned my database, MySQL could be very applicable within this program, although having it run across multiple applications is a potential issue.

I've also started writing some base code for the Student Management system, and I'm hoping to have at least that part done within these next few days. I intend on taking the longest with this, as I will be using this to learn how MongoDB queries work, and how I can write them to do what I need to do most efficiently. After that, it should be slow, but steady progress. I've certainly got a lot of testing to do, and I definitely need to work as fast as possible, as I am now handling core features of the program in one chunk. Not sure how my workflow has managed to allow for this, but I've dug my own grave, now I'm going to lie in it.

### 15/04/2022 - Friday
Begun working on some basic database queries, but I need to be able to get the hang of each use-case of each different type of query using MongoDB's Java Driver before I can move on. After that, it should just be a matter of mathematically finding a way to recursively display the information into the GUI, which shouldn't be too hard.

The only issue I have with what I'm currently doing is that there is little to no documentation within the MongoDB Javadocs, meaning I'm essentially left to fend for myself with what's on here.

I hope to at least get somewhere today, with the implementation of at least one method, but progress will be slow until I can figure out what's going wrong with each query.

### 16/04/2022 - Saturday
Started some minor work on the frontend for Student Management. Once I handle the logic behind this, and the database queries involved, I don't see any other issues that may halt major progress for a few days. 

Once I do some minor tests with the frontend, and some more complicated writing of methods to allow for automatic formatting and finding of student search results, I should be able to make use of some of the backend methods already written to work on proper student editing.

Once I work on student data viewing, and editing with permissions, I'll move onto student addition and deletion. 

It is also worth noting that I will only be giving these permissions to ADMIN accounts, although TEACHER accounts will be allowed to view information as needed.

### 17/04/2022 - Sunday
Slightly reworked the searching method for students to allow for all types of fields. This will make searching more exact and much easier to do. I also need to make some minor additions to the GUI handling class and make some further changes to my Student Management GUI to allow for further filtering and exact searching.

Successfully added a drop-down menu and accordingly reworked the GUI to ensure that there is space for the new components. The next part that needs to be done is a listener which is able to accordingly set the type of the search filter. Once I have done this, I need to do some minor testing with a clickable, uneditable text field (or whatever seems to work better/look nicer) and see if I can use that and some basic maths to recursively display student information through the search.

Once I have done that, I need to be able to make use of the same logic to ensure that users are able to open and view the student information. I also need to work with permissions at this point in time.

I hope to at least have the logic for the search filter done today.

### 18/04/2022 - Monday
Managed to make a minor addition to my GUIs thanks to a new library found. SwingX holds new functions which can be used within Swing to improve the UI and UX aspects of my GUI. No form of testing really needs to be done here, as it is just a cosmetic feature.

Had some minor issues with handling the key typed event when working on student searching of any type. Thanks to a more universal method that has been written by me, I've been able to ensure that there is only one universal way of searching for students, no matter what is used to search for them.

Minor improvements to the way searching is done needs to be done to ensure that display of these students can be handled, however, that is another issue that also needs to be handled today.

Once I've ironed through some of the logic errors and improvements which need to be made within student searching, I hope to make use of Microsoft Access to test this searching through their software, and mine. If both programs achieve the same result (albeit, MSA will be faster, then I'll be satisfied in assuming that it is able to search for data reliably.

Once testing is done, I can move onto displays, that I need to figure out listeners for to ensure that they can be clicked.

**P.S:** I've decided to come back to handling the key typing event for "live" searching, and simply add a search button and a listener to listen for ENTER key presses instead. This is a lot simpler to handle, and shouldn't seemingly mess with things as much. I don't know why I'm suddenly having these issues with code that worked fine yesterday, but it's clearly something that needs to be fixed. Immediately.

***P.P.S:*** After finishing the slight rework in Student Management GUIs, I also noticed a minor issue with the logi n system which was also fixed. A more efficient way of searching through the database was also written, but it is still going through some teething issues. It needs to be tested more thoroughly to actually ensure that it is a robust system that would work.
I also had the faint though of having the system manually go through each type of searching until it came up with a result, but this is still something I need to consider, as it would lead to overexcessive database queries, which are computationally intensive on both ends.

### 19/04/2022 - Tuesday
Did some further testing of the searching system within Homeroom. I need to give them some further documentation, but I'm now able to move onto the student name display portion.

Not sure how I'll be able to make use of clickable text fields which need to be of standardised sizing and amounts. To be able to handle searches at scale, I need to be able to make "pages" within the GUI which can then cycle through to the next set of students which can be found. 

As I have a List object of everyone found, I should be able to do this to some degree of ease.

**P.S:** Managed to make use of JButtons to work around this issue with searching by removing all buttons and replacing them with the new search result buttons. I can now get these buttons and edit their looks through other methods to make them look however I like.

However, this now comes with their own set of issues. There are now a few questions (with their respective problems) that I need to ask myself, and solve. *What happens if the buttons are generated so many times to the point where the buttons go out of view? Is there a way of mathematically finding this? Is there a way of finding how many buttons are left on the list of buttons to generate? Is there a way of sorting these out into other pages, which can be displayed with a button?*

If I plan on progressing any further with the student search work, I'll have to handle this issue immediately. Unfortunately, I feel that this issue with rather overly complex, and will definitely take a lot longer to solve, so I feel like I'm going to bench the work I'm doing on the searching functions and work on the ability to add, view, edit and delete records of data with permissions.

### 20/04/2022 - Wednesday
Successfully managed to add permission handling to users, although it's not as clean as I want it to be. At some point, a refactor needs to be done to make sure that the code is as clean as possible.

I also need to work on the general UI and UX of the Student Management GUI when viewing a specific student. Right now, it doesn't look as nice, nor does it look very functional. For now, I plan on finishing this before moving onto the actual backend work for editing students. There are obviously major challenges and issues that need to be handled when it comes to the UI/UX aspect of this program, but those are challenges I expect to overcome with general ease.

Once I have finished, that I can move on to deleting and adding new students.

**P.S:** Had some issues with finding a way to have the date be selected and used within the rest of the program, but after some further testing and the new implementation of the LGoodDatePicker library, I've managed to solve the issue of taking in date input in an easier way for the user.

### 21/04/2022 - Thursday
Have finished work on the Student Management GUI, which has been given a much larger amount of screen space to allow for any additional readjustment or additions should they require any. I feel that the UI and UX is designed somewhat well, but is too minimalistic.

I also need to be able to work on the editing of student information (provided the user logging in has the right permissions). The adding of new students and the deletion of students also needs to be handled. Using the permissions system I have, I'm also able to entirely remove the option for teachers/sub-users, which is likely a good feature to make use of. My only concern for this is that it will make the rest of the GUI look strange.

I'm having some small issues with editing the fields of data, as it is hard to find the right event which is able to do what I need. I need to ensure that an event is fired when there is a change in the text of the field. I either need to find an event which is able to handle this, or I need to find an event that I can use in a hacker, less clean workaround.

I'm still growing increasingly concerned that I'm falling behind the deadline for the project. For my 75% deadline I ***NEED*** to have at least 3 of the main features and buttons to be fully developed. Once I've gotten the first half done, I don't see the rest of it being too hard. 

### 22/04/2022 - Friday
Have managed to get some work done on the internal logic behind Homeroom's Student Management, discarding, saving and editing. It needs to have some touching up purely due to the fact that **it is not clean code.** It does the job, but that's all it seems to do. Operations, where applicable, have been compacted into a loop, but this has not been done enough. I need to be able to find better ways of writing essentially all methods that are within the Main class. These methods have high line-counts, and contain operations that make up the GUI. 

The methods that are being called in question are far too long, and code needs to be compacted to become more readable. As soon as main functionality is achieved within all main buttons, the clean-up of code is something that will become immediately prioritised.



### 23/04/2022 - Saturday