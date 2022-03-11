NEA Programming Project Devlog
---
This document will entail the development log for ‘Homeroom’. If any challenges are encountered, the solutions will also be documented. Documentation will be done every day that some form of relevant progress has been made.

### 19/01/2022 - Wednesday
Some progress was made. Main documentation for ‘Homeroom’ was done through the use of a GitHub repository. Main wiki for a user manual will be held there as will the software licensing. GitHub repository will also be used to store source code (obviously!) for ‘Homeroom’. A Trello board will serve as the main form of task tracking and should ensure I see what needs to be done first and soonest. Only thing I need to be aware of is falling behind on that and accidentally missing deadlines. 

Additionally worked on some issue templates for GitHub. Had a minor look at Milestones, Projects and Actions which seem like a good way to automate a few things such as releases. Projects seem like a good way to organise some more stuff. I need to look into that, as right now it sounds similar to a Trello in terms of organisation. Should this be the case, it’s not really best for me.

Also started working on a stakeholder interview form. This will be distributed across to teachers, administrative staff and tutors where I deem fit. I am also considering users on Reddit as a reliable source of potential stakeholder opinion due to the wide range of people their community holds. It will not be hard to find school administrators and teachers willing to fill out a small survey. Teachers within my school will also be able to help with the completion of this survey. Due to privacy issues within the Avanti Schools Trust, the forms have had to be hosted on my personal computer. A .csv file holding results will need to be attached detailing any useful results. 

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