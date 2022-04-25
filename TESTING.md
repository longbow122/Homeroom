Testing Document
---
This document will entail details of any testing that has been done for any particular feature. This documentation will speed up the production of any reports that may need to be written.

Testing will be shown through the use of this document, with a title, minimal explanation as to what the feature is, and a testing table for any potential output. Any issues debugged and experienced will be documented through the use of *italics*.

[Markdown Table Generator was used to quickly handle the creation of tables.](https://www.tablesgenerator.com/markdown_tables)


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
| Phone Number   | Erroneous       | +73                                  | No Results Found!  | Yes!             |

## Student Data Editing
Each Student will hold certain sets of information that can be edited through the use of Homeroom's GUI. 

Each field of information within the document can be edited and updated. This needs to be tested thoroughly to ensure that the system is robust.



