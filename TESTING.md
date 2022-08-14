Testing Document
---
This document will entail details of any testing that has been done for any particular feature. This documentation will speed up the production of any reports that may need to be written.

Testing will be shown through the use of this document, with a title, minimal explanation as to what the feature is, and a testing table for any potential output. Any issues debugged and experienced will be documented through the use of *italics*.

[Markdown Table Generator was used to quickly handle the creation of tables.](https://www.tablesgenerator.com/markdown_tables)

# First Iteration

The below pieces of text were written during the first iteration of development for this program. Further iterations may occur, but this depends on whether a deadline is met.


## Logins
The login system involves passing two strings through a Swing GUI, where these arguments are then passed into a connection string which is used to facilitate the connection to an external NoSQL database cluster. There are several different events that should occur upon different inputs.

*Experienced some issues regarding the connection string itself, and the way that failed logins are handled. A silly mistake in boolean logic and while loops led to this, but it was quickly rectified.*

*Issues were also seen when connecting under certain networks. When making use of my home network/mobile hotspot to connect to the DB, it seemed to work just fine.*

| Type of Data    | Username Input                 | Password Input | Expected Output                       | Achieved Output? | Real Output |
|-----------------|--------------------------------|----------------|---------------------------------------|------------------|-------------|
| Erroneous       | asdadas                        | dasdadads      | Error Message stating bad credentials | Yes!             | N/A         |
| Normal/Expected | [REDACTED FOR PRIVACY REASONS] | pp             | Successful login                      | Yes!             | N/A         |
| Erroneous       | null/no input                  | null/no input  | Error Message stating bad credentials | Yes!             | N/A         |

## Bulk Student Searching
This system was implemented to allow for searching for students by names. This is where a string is passed through a text entry field within a GUI, which can then be passed through a method written to search through the Collection of Documents for names containing any instance of the string to be searched for anywhere in the name.

For example, if the user were to search for "D", then any Student with the letter "D" in their name will be returned. Various different types of inputs can be used here, and as such, certain types of error handling needs to be done.

As a sample dataset was used, I am able to use a pre-existing search function through Excel to quickly get the expected result for any form of searching. This can then be tested within my program.

| Type of Data    | Name Search Input | Expected Result | Achieved Result? | Real Result         |
|-----------------|-------------------|-----------------|------------------|---------------------|
| Expected/Normal | D                 | 267 Found!      | Yes!             | 267 Found!          |
| Expected/Normal | Dave              | 2 Found!        | Yes!             | 267 Found!          |
| Expected/Normal | 234234            | 0 Found!        | No!              | No message printed! |

The final testing string did not give me what I wanted. I suspect that this is due to the query returning null, and as such, the list within the method returns empty. The method will have to be written to account for this accordingly. I am currently unsure as to how I am going to implement such a fix, but when a solution is found, it will be documented here. 

**Found Fix:** Was simply able to check if the list was empty with one method, and used that to return an empty list which can then be checked for to return something else anywhere for easier error handling. Not an amazingly smart fix, but it seems to do the job.

For now, I intend in delivering a working program, before I bother making my code more efficient, and finally, more clean. It's clear that delivering a working product is what needs to be done first.

## Exact Student Searching through Primary Key
Each Student will be assigned a version 4 UUID when being added to the table. Homeroom will automatically account for this and assign a UUID accordingly.

In this section, a method was written to test for searching of specific students using their unique user ID. This can be passed through a method and can be used to retrieve information of any student, no matter what field is identical.

Data that was passed through in this test were UUIDs, and random strings. In the case of a random string being passed through, a message should be returned. Later down the line, some other form of error handling will have to be written to account for the rest of it.

| Type of Data    | ID Search Input                      | Expected Result    | Achieved Result? |
|-----------------|--------------------------------------|--------------------|------------------|
| Expected/Normal | 4580edae-291e-4b97-89ca-cf3c785daa0b | Student Found!     | Yes!             |
| Expected/Normal | 0ce5f91a-2768-40e3-958d-11c81efd7c4b | Student Found!     | Yes!             |
| Erroneous       | 12312312312                          | Student Not Found! | Yes!             |
| Erroneous       | adasdadasd                           | Student Not Found! | Yes!             |

## Student Searching using Search Parameters
Each Student will obviously hold certain sets of information that they will have and use throughout their education. Some of these pieces of information are personally identifiable. This means that Homeroom is able to make use of these fields of information to identify students.

These fields of information can now be used to search for Students within Homeroom's database, using many different types of parameters. Users would enter in a search string, and the system would check each field to see whether the search term started with that field. If this is a match, then it will identify it as a found search result. This is something that needs to be tested for each field, and each potential type of input.

Null-Handling has already been accounted for, and it should ensure that when a null list (AKA, nothing has been found that matches the search) is returned, the program is able to cope with this sort of data being fed through, and can adjust to it while providing a different output.

| Type of Search | Type of Data    | Inputted Data                        | Expected Result    | Achieved Result? |
|----------------|-----------------|--------------------------------------|--------------------|------------------|
| UUID           | Expected/Normal | 810bc697-f3e1-4d2f-83ba-73d453937e76 | One Result Found!  | Yes!             |
| UUID           | Edge-Case       | u                                    | No Results Found!  | Yes!             |
| UUID           | Erroneous       | 34233456363tggssvsQ                  | No Results Found!  | Yes!             |
| Name           | Expected/Normal | Thiago                               | One Result Found!  | Yes!             |
| Name           | Edge-Case       | A                                    | 43 Results Found!  | Yes!             |
| Name           | Erroneous       | Zssadw4                              | No Results Found!  | Yes!             |
| Date of Birth  | Expected/Normal | 01/27/2007                           | One Result Found!  | Yes!             |
| Date of Birth  | Edge-Case       | 03                                   | 29 Results Found!  | Yes!             |
| Date of Birth  | Erroneous       | dddddadadtsdaadada                   | No Results Found!  | Yes!             |
| Address        | Expected/Normal | 16 The Meadows                       | One Result Found!  | Yes!             |
| Address        | Edge-Case       | 2                                    | 30 Results Found!  | Yes!             |
| Address        | Erroneous       | Qqanfnfhdysts                        | No Results Found!  | Yes!             |
| Phone Number   | Expected/Normal | +44 7911 867535                      | One Result Found!  | Yes!             |
| Phone Number   | Edge-Case       | +44                                  | 300 Results Found! | Yes!             |
| Phone Number   | Edge-Case       | +73                                  | No Results Found!  | Yes!             |

## Student Data Editing
Each Student will hold certain sets of information that can be edited through the use of Homeroom's GUI. 

Each field of information within the document can be edited and updated. This needs to be tested thoroughly to ensure that the system is robust.

Permissions are also involved in this section of the program and as such, only users granted administrative permissions will be able to edit Student Information. This aspect of testing was **successful**

| Type of Edit     | Original Data Value                                                    | New Data Value                  | Expected Result                 | Achieved Result? |
|------------------|------------------------------------------------------------------------|---------------------------------|---------------------------------|------------------|
| Student Name     | Dave Gordon                                                            | Bill Gordon                     | Bill Gordon                     | Yes!             |
| Student DOB      | 09/08/2002                                                             | 05/12/1998                      | 05/12/1998                      | Yes!             |
| Student Address  | 9 Farman Street, Hove, HN3 1AL                                         | 55 Farman Street, Hove, HN3 1AL | 55 Farman Street, Hove, HN3 1AL | Yes!             |
| Student Phone    | +44 7911 242246                                                        | +44 7911 242243                 | +44 7911 242243                 | Yes!             |
| Student Medical  | Student has ADHD, student gets restless and loses concentration easily | N/A                             | N/A                             | Yes!             |
| Guardian Phone   | +44 7457 331582                                                        | +44 7457 331599                 | +44 7457 331599                 | Yes!             |
| Guardian Address | 9 Farman Street, Hove, HN3 1AL                                         | 99 Farman Street, Hove, HN3 1AL | 99 Farman Street, Hove, HN3 1AL | Yes!             |
| Guardian Name    | Jerry Gordon                                                           | Juan Gordon                     | Juan Gordon                     | Yes!             |

## Student Deletion
When Students leave the institution, the school (as per data protection laws, and as is good practise) are required to delete all relevant data on their alumni after a certain amount of time.

This is something that 'Homeroom' is able to handle through the clicking of a button. This does not need to be tested as much, but should be tested somewhat extensively.

Permissions are also involved in this section of the program and as such, only users granted administrative permissions should be able to delete permissions. This aspect of testing was **successful.**

The following students to be deleted are as follows:

| Student Name  | Student DOB  | Student Address                                       | Student Phone   | Student Medical      | Guardian Phone  | Guardian Address                                        | Guardian Name  | Deletion Successful?  |
|---------------|--------------|-------------------------------------------------------|-----------------|----------------------|-----------------|---------------------------------------------------------|----------------|-----------------------|
| Allana Dyer   | 31/08/2005   | 49 Evan St, Stonehaven, AB39 2ET                      | +44 7700 167364 | Thyroid Problems,    | +44 7948 82482  | 49 Evan St, Stonehaven, AB39 2ET                        | Brenden Stones | Yes!                  |
| Abby Donnelly | 02/16/2004   | Flat 10, Bevan House, 1 Rookery Lane, Barnet, EN4 0BW | +44 7911 29759  | Student has dyslexia | +44 7457 918162 | Flat 10, 1 Bevan House, 1 Rookery Lane, Barnet, EN4 0BW | Reggie Fisher  | Yes!                  |
| Bill Gordon   | 05/12/1998   | 55 Farman Street, Hove, BN3 1AL                       | +44 7911 242243 | N/A                  | +44 7457 331599 | 99 Farman Street, Hove, BN3 1AL                         | Juan Gordon    | Yes!                  |

## Student Data Addition
When a Student joins the institution, the school must add their data to the database to ensure that they can make use of this data within the school as and when is needed.

This is a feature that 'Homeroom' is able to handle through a basic GUI-based form. The user can enter data through this form, then either close the form or press a button within the form to send the data and add the data for the Student to the database.

Certain fields of information also MUST not be empty, and certain fields of text within the form are forced to follow a certain format due to being uneditable, and only editable through a GUI.

| Student Name  | Student DOB  | Student Address                                       | Student Phone   | Student Medical      | Guardian Phone  | Guardian Address                                        | Guardian Name  | Addition Successful? |
|---------------|--------------|-------------------------------------------------------|-----------------|----------------------|-----------------|---------------------------------------------------------|----------------|----------------------|
| Allana Dyer   | 31/08/2005   | 49 Evan St, Stonehaven, AB39 2ET                      | +44 7700 167364 | Thyroid Problems,    | +44 7948 82482  | 49 Evan St, Stonehaven, AB39 2ET                        | Brenden Stones | Yes!                 |
| Abby Donnelly | 02/16/2004   | Flat 10, Bevan House, 1 Rookery Lane, Barnet, EN4 0BW | +44 7911 29759  | Student has dyslexia | +44 7457 918162 | Flat 10, 1 Bevan House, 1 Rookery Lane, Barnet, EN4 0BW | Reggie Fisher  | Yes!                 |
| Bill Gordon   | 05/12/1998   | 55 Farman Street, Hove, BN3 1AL                       | +44 7911 242243 | N/A                  | +44 7457 331599 | 99 Farman Street, Hove, BN3 1AL                         | Juan Gordon    | Yes!                 |

**P.S:** An issue was found, where the field for inputs of phone numbers accepted alphanumerical values. Characters within the alphabet should not be accepted, and input validation should be worked on to ensure that bad input is caught.

This was implemented by doing checks on the denary ASCII value of each string. If a character that is not a number is within the value of 65-90, or 97-122, or it is not 33, then it is a character that is not part of a valid phone number and as such, should be flagged.
The checks I have made on each ASCII value check whether the character is a letter, or not a "+" character. These are the only characters that make up a part of a valid phone number.

**P.P.S:** Forgot to account for the space character, which can be used within phone numbers with international country codes. All I needed to do was add an extra check for the space character into the phone number check. This has since been fixed.


## Bulk Form Searching
This system was implemented to allow for searching for forms by names. This is where a string is passed through a text entry field within a GUI, which can then be passed through a method written to search through the Collection of Documents for names containing any instance of the string to be searched for anywhere in the name.

For example, if the user were to search for "A", then any Form with the letter "A" in their name will be returned. Various different types of inputs can be used here, and as such, certain types of error handling needs to be done.

No such sample dataset was made availiable for this, so I have had to make one myself, and work with it to ensure that everything works as I need to.

| Type of Data    | Name Search Input | Expected Result | Achieved Result? |
|-----------------|-------------------|-----------------|------------------|
| Expected/Normal | 9                 | 6 Found!        | Yes!             |
| Expected/Normal | 9 A               | 1 Found!        | Yes!             |
| Expected/Normal | 234242442         | 0 Found!        | Yes!             |


## Exact Form Searching through Primary Key
Each Form will be assigned a version 4 UUID when being added to the table. Homeroom will automatically account for this and assign a UUID accordingly.

In this section, a method was written to test for searching of specific forms using their unique user ID. This can be passed through a method and can be used to retrieve information of any form, no matter what field is identical.

Data that was passed through in this test were UUIDs, and random strings. In the case of a random string being passed through, a message should be returned. Later down the line, some other form of error handling will have to be written to account for the rest of it.

| Type of Data    | Name Search Input                    | Expected Result | Achieved Result? |
|-----------------|--------------------------------------|-----------------|------------------|
| Expected/Normal | ea38ce95-d889-47ed-9e0f-10d41b5fd0f9 | Form Found!     | Yes!             |
| Expected/Normal | 4d401626-38f3-4763-b6d2-3c4a8616715d | Form Found!     | Yes!             |
| Errorneous      | 234242442                            | Form Not Found! | Yes!             |
| Errorneous      | adasdadawdasdadasda                  | Form Not Found! | Yes!             |

## Form Searching using Search Parameters
Each Form will obviously hold certain sets of information that they can also be identified with. This means that Homeroom can make use of these pieces of information to identify forms.

These fields of information can now be used to search for Forms within Homeroom's database, using many different types of parameters. Users would enter in a search string, and the system would check each field to see whether the search term started with that field. If this is a match, then it will identify it as a found search result. This is something that needs to be tested for each field, and each potential type of input.

Null-Handling has already been accounted for, and it should ensure that when a null list (AKA, nothing has been found that matches the search) is returned, the program is able to cope with this sort of data being fed through, and can adjust to it while providing a different output.

| Type of Search | Type of Data    | Inputted Data                        | Expected Result | Achieved Result? |
|----------------|-----------------|--------------------------------------|-----------------|------------------|
| UUID           | Expected/Normal | ea38ce95-d889-47ed-9e0f-10d41b5fd0f9 | Form Found!     | Yes!             |
| UUID           | Edge-Case       | d                                    | No Forms Found! | Yes!             |
| UUID           | Erroneous       | dwdwadawdawdawd                      | No Forms Found! | Yes!             |
| Teacher Name   | Expected/Normal | Mr Gilligan                          | Form Found!     | Yes!             |
| Teacher Name   | Edge-Case       | M                                    | 6 Forms Found!  | Yes!             |
| Teacher Name   | Erroneous       | e2342423425235                       | No Forms Found! | Yes!             |
| Form Name      | Expected/Normal | 7 Fire                               | Form Found!     | Yes!             |
| Form Name      | Edge-Case       | 7                                    | 6 Forms Found!  | Yes!             |
| Form Name      | Erroneous       | adawdawdwa                           | No Forms Found! | Yes!             |

## Form Data Editing
Each Form will hold certain sets of information that can be edited through the use of Homeroom's GUI.

Each field of information which the document can be edited and updated. This needs to be tested thoroughly to ensure that the system is robust.

Permissions are also involved in this section of the program and as such, only users granted administrative permissions will be able to edit Form information. This aspect of testing was **successful**.

| Type of Edit | Original Data Value | New Data Value  | Expected Result | Achieved Result? |
|--------------|---------------------|-----------------|-----------------|------------------|
| Teacher Name | Mr Abby Dawson      | Mrs Abby Dawson | Mrs Abby Dawson | Yes!             |
| Form Name    | 7 Air               | 8 Air           | 8 Air           | Yes!             |

## Form Deletion
When a Form no longer needs to be in the system, presumably because the Form has left school as a whole, the school (as per data protection law, and as is good practise) are required to delete all relevant data on their alumni after a certain amount of time.

This is something that 'Homeroom' is able to handle through the clicking of a button. This does not need to tested as much, but should be tested somewhat extensively.

Permissions are also involved in this section of the program and as such, only users granted administrative permissions should be able to delete permissions. This aspect of testing was **successful**.

| Form Name | Teacher Name | Students                                       | Deletion Successful? |
|-----------|--------------|------------------------------------------------|----------------------|
| 11 Water  | Mr Raniga    | Angelika Novak, Akaash Clements, Arnav Emerson | Yes!                 |
| 12 Air    | Mr Patel     | Zander Tucker, Zunaira Gough                   | Yes!                 |
| 9 Planet  | Mr Gilligan  | Zayyan Avila                                   | Yes!                 |

## Form Data Addition
When a Form needs to be created, the school must add their data to the database to ensure that they can make use of this data within the school as and when needed.

This is a feature that 'Homeroom' is able to handle through a basic GUI-based form. The user can enter data through this form, then either close the form or press a button within the form to send the data and add the data for the Form to the database.

Certain fields of information also MUST not be empty, and certain fields of text within the form are forced to follow a certain format due to being uneditable, and only editable through a GUI.

It is worth noting that due to a later design change, Students could no longer be added to a Form. This is now done as a part of data editing, and will be treated as such in terms of any testing that may need to be done.

| Form Name | Teacher Name | Expected Output          | Test Passed? |
|-----------|--------------|--------------------------|--------------|
| 7 Fire    | Mr Gilligan  | Successfully added Form! | Yes!         |
| 7 Water   | Mr Weyers    | Successfully added Form! | Yes!         |
| 7 Air     | Mr Arnell    | Successfully added Form! | Yes!         |
| 7 Earth   | Mr Yates     | Successfully added Form! | Yes!         |
| 7 Aether  | Mr Griffin   | Successfully added Form! | Yes!         |
| 7 Cosmos  | Mr West      | Successfully added Form! | Yes!         |

**P.S:** An issue was encountered where the addition of forms would fail due to MongoDB not being able to accept Arrays of String into their Arrays data type within their Collections. This issue was rectified with a little bit of research, allowing me to realise that an ArrayList would be needed to insert an Array to the database.
Once the ArrayList was used, the problem was solved.

## Student Data Editing through Form Management GUI
As Students can be found and associated through their Forms, this is something that Homeroom needs to account for in the form of Student Management. Users should be able to view the information of a Student in a Form, and edit their information through that same GUI.

It is worth noting that permissions were also involved in this section of the program, but they will not be tested here, since they should have been tested already in previous blocks of code and previous tests. Additionally, due to the behaviour of this feature and the code behind it, there is no such table required for testing. If one instance of testing this feature works, then it is more than safe to assume that this feature if robust enough under most cases.

This aspect of testing was **successful**.

| Type of Edit     | Original Data Value                                                    | New Data Value                  | Expected Result                 | Achieved Result? |
|------------------|------------------------------------------------------------------------|---------------------------------|---------------------------------|------------------|
| Student Name     | Dave Gordon                                                            | Bill Gordon                     | Bill Gordon                     | Yes!             |
| Student DOB      | 09/08/2002                                                             | 05/12/1998                      | 05/12/1998                      | Yes!             |
| Student Address  | 9 Farman Street, Hove, HN3 1AL                                         | 55 Farman Street, Hove, HN3 1AL | 55 Farman Street, Hove, HN3 1AL | Yes!             |
| Student Phone    | +44 7911 242246                                                        | +44 7911 242243                 | +44 7911 242243                 | Yes!             |
| Student Medical  | Student has ADHD, student gets restless and loses concentration easily | N/A                             | N/A                             | Yes!             |
| Guardian Phone   | +44 7457 331582                                                        | +44 7457 331599                 | +44 7457 331599                 | Yes!             |
| Guardian Address | 9 Farman Street, Hove, HN3 1AL                                         | 99 Farman Street, Hove, HN3 1AL | 99 Farman Street, Hove, HN3 1AL | Yes!             |
| Guardian Name    | Jerry Gordon                                                           | Juan Gordon                     | Juan Gordon                     | Yes!             |

## Teacher Addition
When a Teacher joins the institution, the school must add their data to the database to ensure that they can make use of this data within the school as and when is needed.

This is a feature that Homeroom is able to handle through a basic GUI-based form. The user can enter data through this form, then either close the form or press a button within the form to send the data and add the data for the Teacher to the database.

Certain fields of information also MUST not be empty, and certain fields of text within the form are forced to follow a certain format due to being uneditable, and only editable through a GUI.

It is also worth noting that some fields of information (such as the Teacher's form and form name) cannot be added to the database, since this must be done on edit, as part of a seperate process. It is worth noting that all logic behind that feature was found to be successful in testing.

| Mongo Connection Name | Teacher Name | Addition Successful? |
|-----------------------|--------------|----------------------|
| arnell                | Mr Arnell    | Yes!                 |
| thunder               | Mr Thunder   | Yes!                 |
| rasool                | Mr Rasool    | Yes!                 |

## Teacher Deletion
When a Teacher leaves the institution, the school (as per data protection laws, and as is good practise) are required to delete all relevant data on their staff after a certain amount of time. It is also worth noting that deletion of data, in this case, also handles some Form Group data, since the connection name of the Teacher is stored in that Collection. To ensure that deletion within the local Homeroom system and the database cluster goes well, this information needs to be removed.

This is something that 'Homeroom' is able to handle through the clicking of a button. This does not need to be tested as much, but should be tested somewhat extensively.

There were no permissions involved in this entire category, since any relevant permissions work was already handled in another section of the program.

It is also worth noting that the deletion system I currently have in place is not as clean as it could be, due to API limitations. Previously, the Java driver for MongoDB allowed you to directly interact with the authorised users of the database. Using these methods, you could both create, edit and delete information regarding the authorised users of the database within the application itself, meaning that there would be no need to access MongoDB Atlas at all.
However, the latest version of the API saw these methods removed, making possible only through Atlas. This means that the account information needs to be deleted on Atlas first, to ensure that it becomes inaccessible, and it needs to be removed from the Homeroom system, to ensure that the data is removed properly, and any relevant records are sanitised.

| Mongo Connection Name | Teacher Name | Deletion Successful? |
|-----------------------|--------------|----------------------|
| arnell                | Mr Arnell    | Yes!                 |
| thunder               | Mr Thunder   | Yes!                 |
| rasool                | Mr Rasool    | Yes!                 |

## Teacher Data Editing
Each Teacher will hold fields of information that can be edited through the use of Homeroom's GUI.

Each field of information within the document can be edited and updated. This needs to be tested thoroughly to ensure that the system is robust.

Permissions are also involved in this section of the program and as such, only users granted administrative permissions will be able to edit Teacher Information. This aspect of testing was **successful.**

**P.S:** An issue occurred where getting the teacher name from the connection name of the teacher from the form group would result in an NPE when attempting to display this to the user. This was because the Form Group would not have a connection name associated with it to start, since all Forms start off with having no Teachers.

| Type of Edit | Original Data Value | New Data Value | Addition Successful? |
|--------------|---------------------|----------------|----------------------|
| Teacher Name | Mr Arnell           | Mrs Arnell     | Yes!                 |
| Teacher Name | Mr Thunder          | Mrs Thunder    | Yes!                 |
| Teacher Name | Mr Rasool           | Mrs Rasool     | Yes!                 |
| Form Name    | 8 Air               | 9 Air          | Yes!                 |
| Form Name    | 12 Planet           | 11 Planet      | Yes!                 |
| Form Name    | 10 Fire             | 7 Fire         | Yes!                 |

## Bulk Teacher Searching
This system was implemented to allow for searching for Teachers by names. This is where a string is passed through a text entry field within a GUI, which can then be passed through a method written to search through the Collection of Documents for names containing any instance of the String to be searched for anywhere in the name.

| Type of Search        | Type of Data    | Inputted Data | Expected Result     | Achieved Result? |
|-----------------------|-----------------|---------------|---------------------|------------------|
| Mongo Connection Name | Expected/Normal | arnell        | One Teacher Found!  | Yes!             |
| Mongo Connection Name | Edge-Case       | a             | One Teacher Found!  | Yes!             |
| Mongo Connection Name | Erroneous       | 2231          | No Teachers Found!  | Yes!             |
| Teacher Name          | Expected/Normal | Mr Gill       | One Teacher Found!  | Yes!             |
| Teacher Name          | Edge-Case       | M             | Two Teachers Found! | Yes!             |
| Teacher Name          | Erroneous       | 234422323     | No Teachers Found!  | Yes!             |

## Exact Teacher Searching through Primary Key
Each Teacher is automatically assigned a primary key (thanks to MongoDB Atlas not allowing for identical usernames) within Homeroom.

In this section, a method was written to test for searching of specific forms using their unique user ID. This can be passed through a method and can be used to retrieve information of any Teacher, no matter what field is identical.

Data that was passed through in this test were connection names and random strings. In the case of a random string being passed through, a message should be returned. Later down the line, some other form of error handling will have to be written to account for the rest of it.

| Type of Search        | Type of Data    | Inputted Data | Expected Result     | Achieved Result? |
|-----------------------|-----------------|---------------|---------------------|------------------|
| Mongo Connection Name | Expected/Normal | arnell        | One Teacher Found!  | Yes!             |
| Mongo Connection Name | Edge-Case       | a             | One Teacher Found!  | Yes!             |
| Mongo Connection Name | Erroneous       | 2231          | No Teachers Found!  | Yes!             |

## Teacher Searching through Search Parameters
Each Teacher will obviously hold certain sets of information that they can also be identified with. This means that Homeroom can make use of these pieces of information to identify Teachers.

These fields of information can now be used to search for Teachers within Homeroom's database, using many different types of parameters. Users would enter in a search string, and the system would check each field to see whether the search term started with that field. If this is a match, then it will identify it as a found search result. This is something that needs to be tested for each field, and each potential type of input.

Null-Handling has already been accounted for, and it should ensure that when a null list (AKA, nothing has been found that matches the search) is returned, the program is able to cope with this data being fed through and can adjust to it while providing a different output.

| Type of Search        | Type of Data    | Inputted Data | Expected Result     | Achieved Result? |
|-----------------------|-----------------|---------------|---------------------|------------------|
| Mongo Connection Name | Expected/Normal | arnell        | One Teacher Found!  | Yes!             |
| Mongo Connection Name | Edge-Case       | a             | One Teacher Found!  | Yes!             |
| Mongo Connection Name | Erroneous       | 2231          | No Teachers Found!  | Yes!             |
| Teacher Name          | Expected/Normal | Mr Gill       | One Teacher Found!  | Yes!             |
| Teacher Name          | Edge-Case       | M             | Two Teachers Found! | Yes!             |
| Teacher Name          | Erroneous       | 234422323     | No Teachers Found!  | Yes!             |

 