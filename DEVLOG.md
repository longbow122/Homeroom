NEA Programming Project Devlog
---

# First Iteration - Initial Development
This document will entail the development log for ‘Homeroom’. If any challenges are encountered, the solutions will also be documented. Documentation will be done every day that some form of relevant progress has been made.

This main section of the devlog details the main development process for 'Homeroom'.

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
Tested every single button that is made within the Student Management GUI that will allow for just the editing of student information. Buttons and events that trigger the reversion of changed fields into their original pieces of data seem to work well, but I'm having some inital issues with the saving of new fields into the database. 

When a field is edited, the updated data does not seem to properly save into the new field where it is meant to be.I'd be lying if I said I didn't expect some issues from a method that was written once and not tested again, and as such, I need to ensure I fix it today. Once I get that done, I need to move onto the addition and deletion of students. That shouldn't be too hard, and I can then mark the Student Management feature "done" for now.

Compatibility with classes and form groups still need to be worked on within the Student Management GUI, but until those are actually done, I'll need to wait until I get those features developed.

### 25/04/2022 - Monday
Still struggling with the method I've written to update documents. With some testing, I've decided to deduce that the problem lies with the way I'm making use of MongoDB's methods within the Java Driver. As such, I need to refactor code accordingly to ensure that my update statements work. Will need to do some reading into possible solutions by browsing through the JavaDoc.

### 26/04/2022 - Tuesday
Made some minor progress on the program by doing some minor class refactoring. Ensured that uneeded connections within the program are not made where possible. One such example was within my "search" method, where Students would be searched for, and upin being found, a new connection would be made. This happened as when a new Student object was instantiated, a new connection would be made for EOU. At scale, this would likely not work in my favour.

### 04/05/2022 - Wednesday
Failed to fix the issue at hand. Found the cause of it, but need to do some major refactoring to ensure that full and intended functionality is there.

**P.S:** Somehow managed to more or less fix it. However, it seems I'm "caching" the results of the search within the student management menu, so I need to find a way to be able to refresh the menu after a saving operation of editing of data has been called. This refresh operation will need to be done across the board. This refresh operation also needs to be automatic, as relying on users to refresh things manually is stretching too far.

### 06/05/2022 - Friday
Did some minor testing of deletion and editing of student information. It is becoming increasingly clear that the "refreshing" of search results after a particular action on a Student is done needs to be implemented. When a Student is deleted, as the refresh does not occur, the Student's information can be still viewed and edited.

It is likely that this will cause a major conflict with data entry if a sub-user should choose to edit said deleted student, then the student will likely be re-added to the database, which is not an intended feature.

Further testing needs to be done, and the search GUI needs to be refreshed no matter what.

Additionally, there is an increasingly worrying amount of private, static variables being used within my code. While this may be the only potential solution that can be used, it is not always best practise. My Main class needs to be refactored accoridngly to ensure that this does not continue.

**P.S:** Ensuring that the GUI can be refreshed through the call of a method or through some other hacky workaround is going to be a challenge. Finding a clean and reliable way will be incredibly hard. This needs to be done as soon as possible, and preferably before I handle pagination.

Once pagination is handled, that will be an entire feature developed, and I can begin to move onto another aspect of the program.

To ensure I have a little more time to finish working on this issue, I've decided to temporarily move onto the addition of Students before working on GUI refreshing and pagination.

### 17/05/2022 - Tuesday
Did some planning of functionality for the addition of students. Also implemented a few lines of code which will entirely handle this feature. This requires a lot more testing, as I have had trouble with MongoDB's API due to the large clash in documentation.

Once I have finished and properly tested the addition of Student's data, the deletion of students also needs to be tested. Once that has been tested, I need to find a good way of paginating results that are larger than what the GUI can hold. The way I see it, I have two options to explore when it comes to this, scroll panes or simple page buttons. I'll decide once I start working on it, but I need to be able to pick what seems easier to work on.

## 18/05/2022 - Wednesday
Did some minor work on student addition and also worked on major pieces of the documentation for it. Testing needs to be done for it, but the main components of the code have been written. Some final database methods need to be written, but once that's been written that should be the first piece of the program done.

## 20/05/2022 - Friday
Wrote some basic code that will handle the final bits and pieces of Student Management. All that now needs to be done is the handling work for the nicer details within the program. Once final finishing touches have been put in, I'll work on the rest of the program.

The newer set of features also requires intensive testing, and this needs to be done before I can move on. I don't want to have to swamp myself with testing once the entire program is developed and I also don't want to have to handle ferocious bugs in one large go.

## 27/05/2022 - Friday
Code for adding and removing students seems to work fine, but as the data is stored cached at first, the data for deleted students can still be viewed somewhat quickly through the window. **It is now of vital importance that the GUI is refreshed with a new set of buttons to ensure that deleted students can't be viewed.

Once I have handled that, Student Management is done and I can now move onto the next feature for Homeroom.

I've also had a quick read through my main class and have done some minor work towards trying to clean up my code, but it is still not up to standard. I've definitely got to go over multiple refactors once functionality is achieved.

## 15/06/2022 - Wednesday
Spent a long time refactoring a few methods, implementing further page refreshing upon addition and deletion of Students and also added large section to the testing document to ensure that every feature of Homeroom is appropriately documented.

I've more or less finished one entire piece of my program (Student Management), and can now begin to start planning another section of the project. Before I do that, I need to work on pagination or scrolling of the Student Management menu to ensure that if buttons are created that end up going out of bounds of the original window, the user is still able to view those buttons by scrolling down.

I hope to have a general plan for this feature fully worked out and finished by today at the least, but implementation might take a little longer to do.

## 16/06/2022 - Thursday
I've made some minor work on the method for pagination, which will likely be finished today. Once I finish this, I'll move straight onto Form Group work, which will end up being much quicker to implement, due to it having an extremely similar, if not smaller structure to Students.

Once I finish work on Form Groups, I can start moving onto Lessons, which will also work the same, (if not similar to Form Groups), only added with some extra functionality to add Students to the Lesson and Form Group.

Once that has been finished, scheduling (likely the harder feature to implement) will need to be done. Due to the way that comparisons between days and times need to be done, I predict it will take me most if not the entire summer to ensure that it is as efficient as possible.

In my personal opinion, (although I haven't started it), I predict that the write up will be generally easy. The supplementary documents I am writing (such as this development log and the testing document) will let me get things documented within the write-up quickly and easily.

**P.S:** The way I'm making use of method to handle the creation and display of my GUIs is definitely not smart, nor is it efficient. This needs to be worked on immediately after functionality is achieved. Due to University applications and personal statement work ramping up, I fear I may not have much time to do everything I need to do, but we will have to see.

## 20/06/2022 - Monday
First attempt to handle pagination smoothly through the use of a JScrollPane seemed to have failed, but I hope to have rectified that by making two JButtons which will then move through a List of Lists of Buttons. These lists will be "pages" of buttons containing student information, which can then be flicked between by users, provided that the search results are large enough to require this.

Once I have sorted through this, I will begin work on the Forms Management, which will hopefully become must easier, as they will work incredibly similarly to Student Management.

The quality and overall readability of my code is a major concern, and is something I need to keep a tab on if I want to save myself a large amount of time in the future.

Currently, my code focuses purely on functionality. The programs works and nothing else. The code is by all means not clean and easy to read, and is definitely not efficient.

I feel my reliance on the ArrayList data structure is far too much. This, technically would be okay, but if it is beginning to affect my code with the excessive use of private fields, excessive use of if statements and general bad code practise, then it needs to stop. I will likely be spending an entire iteration's worth of work trying to figure out how to clean up each class.

While the code may not consume many resources computationally, it is able to take significantly less, which is the best solution that is needed. Once I finish testing the new pagination solution, I will be ready to move on, and begin merging the student branch.

**P.S:** Encounters some issues when handling pagination logic through the JButtons. I'm still in progress of diagnosing as to exactly why the issue I have is occuring, but I believe I'm slowly getting to the root of it.

It seems to be a combination of problems only revealing themselves now due to the new system with pagination and handling Lists.

## 21/06/2022 - Tuesday
I still seem to be having issues with pagination logic, of which I'm not completely sure how to fix. I managed to trace the method's logic issues down to the List of buttons that it's meant to be iterating through.

Not sure what the issue is exactly, but I'll work on figuring it out. I'm sure that the list of lists contains the right buttons, meaning that it might be an issue with making the buttons visible and invisible. Not sure why this is causing an issue, but I'll need to look into it.

**P.S:** Found the issue at hand. The index doesn't seem to be shifting properly in terms of the new paged index, meaning that the same JButton is then being made invisible and visible again. This would obviously cause issues, as it does not give the effect to the user that the page is actually "shifting". I'll need to find a way to properly fix this before moving on.

**P.P.S:** Issue found. The list of lists for the buttons does not seem to be properly shifting across due to the fact that it is not properly adding buttons. The logic behind this needs to be debugged and reworked to ensure that all 300 elements in my sample data set are successfully added and viewable.

***P.P.P.S:*** Found the issue at hand and managed to fix it. Seemed to be an issue with the List of buttons I was recycling in order to save resources not properly wiping every object within the list. Resolved this by creating a new ArrayList and replacing it in memory.

*I will now begin to make a move on the Form Management section of Homeroom, allowing me to temporarily finish Student Management until form group functionality needs to be implemented within Student Management.*

## 22/06/2022 - Wednesday
Made a major start on Form Management, but have not managed to come anywhere near a stage where I can actually begin testing the feature and see how it's shaping up in terms of the GUI. As the feature (fortunately!) works incredibly similarly to Student Management, I currently plan on being able to implement this very quickly, and with general ease. Currently the process of implementing Forms seems to be a glorified copy and paste job, handling variable names and method signatures as the only real change in the code.

Due to this point, I have come to the realisation that a better way of implementing this would likely be through handing a basic "Entities" object, which can hold all of the template information that may be needed. This might even be applicable to become an abstract class, as all Homeroom objects will hold some form of information. This, I feel would likely be a better way of making use of Object-Oriented Programming. This is something I have managed to plan, but I have not managed to implement.

I am not sure what I plan on implementing after this, but once I finish working on a barebones implementation of Form Management, a large testing process will likely be underway, and as a result of that, I will likely need to make a large addition to the testing document to ensure that every feature has been appropriately documented and tested. I am also yet to document the uses of Homeroom and every feature that Homeroom has to offer. This has been planned, but will likely be a rush job that is done straight after implementation.

I am also growing increasingly worried that I will not be able to make multiple iterations over my program to ensure improvement and updates (as is standard with the Agile model of development). This is something I need to plan carefully and think about.

## 23/06/2022 - Thursday
Still continuing to make major progress, but due to the linked nature of all the classes (in as, every method tends to call code from another method, which still needs to be written), it seems that the easiest way of going about writing the Forms feature is to write the logic for the entire feature (bar a few minor touches) and then test in one go.

My only concern in doing this is that I may end up with a large amount of bugs which will just be harder to fix due to the larger amount of code to trace through. This makes things much harder to handle. I have also written a few extra convenience methods that are being called throughout the program, but I am yet to test them. As they involve stopping the thread of logic and waiting for a response, I do have my concerns as to how that might affect the rest of the program, but we will likely just have to wait and see.

## 27/06/2022 - Tuesday
Made major progress, and managed to implement a fair majority of the Form Management logic. The finishing touches will be a large amount of document addition and testing, which needs to be done at speed.

My concerns have also been justified, and I have begun to experience issues with the way I am freezing the thread to allow for the program to wait for input from the user for selecting a Student to be added to the form. This issue simply lies in where I make my method calls and how I go about freezing the thread, which needs to be fixed somehow. I'm not sure how to go about doing this, but I'll need to find a way quickly.

My ability to test, debug and write code has also seriously been harmed by the fact that I have run out of mobile data. This means I cannot test my product on the go, and will need to do so at home, if I am able to find the time.

## 08/07/2022 - Friday
Failed to make any relevant progress in the time that has elapsed. The issue seems to lie where the thread freezes over due to the call on the wait method. I'm entirely unsure as to how to resolve the issue, but I'm sure there'll be a solution.

I believe the issue lies in where the method call actually is, but I'll have to see.

## 31/07/2022 - Sunday
I have essentially 3 weeks to finish the entire project, which is a deadline I may just about hit if I manage to fix this issue within a day and am lucky enough to encounter no issues.

After some research, it appears that I have taken the wrong approach to concurrency then I think I have, and as such, will need to work on refactoring the entire selection method to ensure that a user can search for and select a student to add to a form, and to perform other general tasks.

It is worth noting that the only reason I am choosing to pursue the development of this feature is because it will bring a great amount of utility to Homeroom upon completion.

**P.S:** I've managed to find myself options, but I can't be certain if any of them work, nor am I certain if any of them will actually be a valid choice for me. I've decided to make an entirely separate branch of code, and work on testing it there. It's become evident to me that there is such a large amount of refactoring and code editing that needs to be done that I no longer feel that the work I am doing belongs on the forms-branch.

## 01/08/2022 - Monday
Unfortunately, I have still failed to find a solution to my original problem. However, I did realise that my original problem was not a good problem to begin what. Due to the way Swing behaves, my entire issue of having a multi-threaded program to work on this task on the side, while freezing everything else was very bad.

Originally, I had done this to ensure that the user did not mess anything up when potentially making use of a bad input, and therefore freezing the main thread until receiving input from what I wanted seemed like a good idea.

I have since realised that this was a very bad idea, since handling the threads in the way I wanted was either not possible, or far too hard for someone who is still trying to learn how to put multi-threaded code into practise.

I realised that I can then resolve my issue by merging a few methods I had written to handle student and form selection, and then, using an additional integer parameter, I could use that to indicate "selection" of how the method would apply itself to the rest of the program. The integer option would indicate the context as to how the method was being used, and it could perform different actions dependent on that.
I would also need to ensure that any extra GUI that Homeroom opens closes the previous GUI before opening the new one. This way, the user is not able to provide input that could potentially be malicious. although the user did not intend it to be malicious, if Homeroom is incapable of handling multiple forms of input from different GUIs at the same time, then it is clear only one GUI at a time should be used to avoid this problem.

In my second iteration of Homeroom, this extra functionality can be something I work on, but for now, only the core functionality of Homeroom needs to be worked on, and all planned features of my program need to be handled. Anything else can be worked on at a later date.

Since this refactor will be so large, I doubt I will be able to get it finished tomorrow, but I can still try to do so.

## 02/08/2022 - Tuesday
In theory, the refactoring of code seems to be going well enough. Provided I sort my way through some minor issues, I should be able to finish the entire code rework tonight. I have managed to remove all multi-threaded code from my work, and am working on trying to refactor all my methods to become more event-based. In this way, the methods can achieve the intended functionality in the right context.

The only issue I currently have with this is that I have had to use method overloading to ensure that each task that the selection task needs to do is done correctly and in the right context. This means that large chunks of code have been repeated, and this is obviously not good for many reasons. I need to find a way to rework this once functionality is achieved. It's untidy and hacky, and it should be removed.

I've also found that the rework for this does not contain itself to just one class, since I need to rework other methods within other classes, to ensure that my design now fits the implementation of these particular methods. I have chosen to make it so that all Forms start off with no students, and you can then progressively add Students from the Student Management menu, or through the Form Management menu when editing a specific Form. It is worth noting that a Student can only be a part of one form, so this is something I need to account for in case a user messes up somewhere.

This rework is beginning to look like it will take longer than just this one day, but I'll have to see how fast I can get things done.

## 03/08/2022 - Wednesday
I believe that I have refactored an adequate amount of code to consider the issue solved, at least for now. As per usual, I still hold the opinion that my code is not up to standard, and can definitely be improved.

In order to ensure that Form Groups becomes seamlessly integrated with the rest of the program, I will need to do some further work on reworking both the Form and Student data structures, ensuring that they are able to store both members of a Form, and what Form the Students are in. This can be done by creating a link between the two features in my databases, and making further code additions to ensure that both GUIs hold information related to the Forms in the correct, robust manner.

Once I am finished with integrating Forms and Students together, to ensure they both work in unison, I will move onto Classes, which will store the actual lessons themselves. These data structures are where admins would create lessons which hold classrooms, teachers and Students.

Unfortunately, I am unable to focus on the quality of my code, and put towards more focus on working further on getting features working to a point where it can be considered "good enough", and start getting documentation out. Once this has been completed, I can start to consider working on a second iteration, which aims to refactor and overhaul most of the program for efficiency and seamlessness.

Since I have considered the concurrency and student selection issue fixed, I will be merging the concurrency-test-branch with the forms-branch, and continuing my work on Form Groups there.

It is likely that I will be working further on Student classes, since Forms need to be completely integrated with Students and the rest of the program before I can consider this safe to move on.

**P.S:** Finished testing the last and final pieces of FormManagement that does not directly integrate Students with Forms. Tomorrow, I will likely be spending a good part of the day working on forming a relation within my entire program between Students and Forms, ensuring that Students know which form they're in directly from their database object, and ensuring that Forms know which Students they have directly from their database object. I will also need to ensure that Students can only be in one form, to avoid any collisions.

Provided I can pass an empty String object through the MongoDB documents to indicate that a Student is not currently associated with a form, I should be okay. I'm not exactly sure how this would be implemented within the "Add a Student" GUI, but I'm sure this is a problem I can solve for later, considering it is a design issue, not an implementation issue.

I don't foresee the association between Forms and Students to be too much of an issue in terms of difficulty, just might take a good amount of time given the strenuous testing involved.

## 04/08/2022 - Thursday
I have finished all work related to the association between Students and Forms. Each Form now holds an array of version 4 UUIDs, which will indicate which Students are in what Form and each Student now holds the form's ID as a version 4 UUID. All of these measures also mean that checks can be made while adding or changing the form of the Student, to ensure that the Student cannot be in more than one form.

This now means that Students and Forms are entirely linked together, and can be used to find and interact with each other. All that is left to do is test this feature and its functionality. Due to the time in which it took me to fully refactor methods accordingly and the time it took me to write any additional code for these methods, I will likely have to test these changes tomorrow, but I will simply postscript them onto this day's devlog, since it is work that should have ideally been done yesterday.

**P.S:** I've encountered an issue where MongoDB will end up either closing the socket or timing out due to the excessive amount of database-editing calls I'm making, which is opening an excessive amount of connections. This will need to be resolved before I continue. I've also noticed a minor mistake in the order in which I've entered my parameters for a method which creates button, which led to the dimensions being distorted. I have also encountered an issue where a method call is made within the Student class that shouldn't be called in this context. This is because the method makes use of database operations, but cannot access said database since there are no credentials being passed through the object it is calling in, within this context. Also experienced an issue where if a Student was already in a form, and they were to be moved to a new one, they would still be added to the new one just fine, but the old form would still retain their data. This will need to be fixed.

**P.P.S:** Due to the sheer volume of issues I've encountered, I will need to approach these issues tomorrow, and work on them one-by-one, until the feature is solid. After that, I will move onto the finishing touches of viewing Form Information, and modifying Students in relation to Forms. Once that is done, I should be able to move onto handling Classes.

It is worth noting that Classes might have a more complex structure in terms of storing information, since not only do they need to hold Students, and the person managing the Class, but they will also need to somehow store the times at which the lessons will be held, and then a system will need to be put in place to ensure that 'Homeroom' is able to accordingly sort the Classes out in order of time, and in relation to each user that is managing the class.

To ensure that the feature is robust, I may have to implement teacher accounts before this, which will definitely be more complex, since it directly interacts with the permissions of the MongoDB cluster.

## 05/08/2022 - Friday
Resolved the following issues, using the following techniques:

#### Excessive Database Calls
Methods within my Database-handling class would make a connection to my database when needed, but this also included constructors that were being used. A fix for this was simply refactoring code to avoid the excessive use of connection methods.
This would ensure I don't reach Mongo Atlas's upper limit of 500 connections active at any one time. Some code was also refactored to rely on the data held within the Objects, and less on the databsse itself. This would also make things easier on the bandwidth consumption for the user.

#### Contextual Method Calls within the Student Class
Some constructors within the Student object can be used to interact with the database, since they hold the username and password used to connect to it, but others do not. The constructors which do not have any access to the database (since they hold only data about the Student), were calling methods that interacted with the database. This led to errors claiming that particular objects were null, because they were. Database-handling methods regularly access the username and password, which the constructors which work with data-only do not have.
This caused issues, since in this case, the username and password would be null. The fix for this was relatively simple, and did not require much refactoring. All that needed to be done was change out classes that did not interact with the database for classes that were, and ensure that the right methods were being called.
In cases where such constructors were not seen as the best option to use, a "resource object" was instantiated, for use within the method only, and the objects that held data passed into the methods that were called from the resource object, allowing any database modifications to happen.

#### Duplicates in the Students Array of the Form DB
Previously, a universal method was being used to modify the Forms database. This means that all data within all fields was being modified using the same method, and some logic. When it came to removing an instance of an element from an array, the universal modification method would not have been able to do this. 

As such, all that needed to be done was a basic refactor of the method which targeted the modification of the Students within a Form. This allowed for Students to be removed from the FormDB successfully, using MongoDBs pull method. This method would pull all instances of a particular element out of the array.

Now that I have managed to find and fix the above issues with changing the Student's form through Student Management, I need to ensure that the same can be done through Form Management, and selection of Forms through the Student Management menu.
I am assuming that it should be the same either way, since any refactor that I made to one method would apply across the whole program (as is an attribute of OOP and procedural programming), but it never hurts to actually test the feature, and find any issues

Additionally, due to the extensive amount of time it took me to resolve the last issue, I will have to leave the testing and fixing of any issues with the Student Management method for tomorrow.

**P.S:** It is worth noting that I still need to work on properly "refreshing" the GUI once the changes have been made to the Form to ensure that changes apply in a seamless manner, but due to the complexity of that issue, I am going to leave that until after I test the changing of Forms through the Student Management GUI since that is probably easier.

## 06/08/2022 - Saturday
It seems I failed to copy over the fixes from yesterday over to Student Management, since a large number of these mistakes are within the code for Form Management. These will need to be fixed before I can finish testing. Considering the fact that these are all issues I've fixed before, I should be able to finish that in no time.

**P.S:** It was much quicker to fix the issues than expected. Will now move onto handling the refreshing of the GUIs before moving onto another feature. I believe I also need to weave permissions into everything before I can start work on Classes and Lessons. That feature may get slightly complex, since times and days are brought into the mix, since it is to be assumed that the same lessons will be scheduled multiple times in a week. I also need to plan on how I could have multiple week schedules.

***P.P.S:*** It seems that handling refreshing was much harder than expected. Since I don't have much time until this project needs to be completed, this is a challenge I will likely be leaving for a later time, since the automatic refreshing of the GUI, while is technically needed, is fundamentally a design issue in my work. This can be, and should have been prevented by simply using a single Window to handle the entire program where possible. This is something that is feasible, but does not need to be done right now, since I should be focusing on the priority features.

As a result of that, I have decided to leave a todo comment where the code should be added onto, in case I ever find the time to rework it, and I will move onto something that is more important, and will likely be done regardless. These would be the finishing touches of the Forms feature, such as allowing for Teacher accounts (which will likely go in a separate branch) and allowing for permissions when adding students to the form, and further modifying the Form's students.

As a side note, permissions have been implemented, and I am now going to begin the merging of code process for Forms, where I can then get started on Teacher accounts, which will likely take a lot more effort, since they need to directly interact with the database, and the authenticated users.

## 07/08/2022 - Sunday
### Pre-Script
Forgot to implement deletion of Forms, so that is something I will be doing today. I don't expect it to be too hard, since it is a simple extension on previous functionality, that literally works the same.

I am now able to declare the Forms and Forms Management feature of 'Homeroom' entirely finished. There are some minor refactors and feature reworks that need to be done regarding Teachers and Teacher accounts, but since these reworks will be behind a myriad of other features, I deemed it appropriate to do it that way. As such, I will now be merging forms-branch into the main branch to update the project, and will now make a start on teachers-branch, which will bring these additions, and more to 'Homeroom'. It is worth noting that Teacher accounts are a needed addition to the Classes feature, and as such, need to be developed before anything else.

## 08/08/2022 - Monday
