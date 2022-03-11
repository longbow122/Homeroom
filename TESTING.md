Testing Document
---
This document will entail details of any testing that has been done for any particular feature. This documentation will speed up the production of any reports that may need to be written.

Testing will be shown through the use of this document, with a title, minimal explanation as to what the feature is, and a testing table for any potential output. Any issues debugged and experienced will be documented through the use of *italics*.

[Markdown Table Generator was used to quickly handle the creation of tables.](https://www.tablesgenerator.com/markdown_tables)


## Logins
The login system involves passing two strings through a Swing GUI, where these arguments are then passed into a connection string which is used to facilitate the connection to an external NoSQL database cluster. There are several different events that should occur upon different inputs.

*Experienced some issues regarding the connection string itself, and the way that failed logins are handled. A silly mistake in boolean logic and while loops led to this, but it was quickly rectified.*

*Issues were also seen when connecting under certain networks. When making use of my home network/mobile hotspot to connect to the DB, it seemed to work just fine.*

| Type of Data    | Username Input | Password Input | Expected Output                       | Achieved Output? | Real Output |
|-----------------|----------------|----------------|---------------------------------------|------------------|-------------|
| Erroneous       | asdadas        | dasdadads      | Error Message stating bad credentials | Yes!             | N/A         |
| Normal/Expected | dhruvil_patel  | pp             | Successful login                      | Yes!             | N/A         |
| Erroneous       | null/no input  | null/no input  | Error Message stating bad credentials | Yes!             | N/A         |


