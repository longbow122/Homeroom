package me.longbow122.Homeroom;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import me.longbow122.Homeroom.utils.DBUtils;
import me.longbow122.Homeroom.utils.GUIUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * The class representing the Class object. This class makes use of the {@link me.longbow122.Homeroom.utils.DBUtils} class to make database queries and edit information about particular Classes.
 * This class holds several constructors and methods which can be used to get instances of the class, depending on the case of what needs to be used where.
 *
 * @author Dhruvil Patel
 */
public class Class {

    //TODO
    // THIS CLASS (AND OTHER DATA OBJECTS LIKE IT) NEED TO BE SEPARATED FROM DATA OBJECT-RELATED METHODS, AND DATABASE METHODS.
    // ONCE YOU HAVE BEEN ABLE TO SPLIT UP THE METHODS, MOVE THE DATABASE METHODS TO A "HANDLER" CLASS, WHICH WILL HOLD ALL OF THESE METHODS.
    // HAVE THE NEEDED CONNECTION INFO STATIC WITHIN THIS HANDLER CLASS, AND DECLARE THESE OBJECTS WITH THEIR RESPECTIVE CONNECTIONS UPON LOGIN.
    // THIS WAY YOU CAN JUST MAKE A STATIC CALL TO A METHOD WHICH RETURNS A FORM, USING A DATABASE OPERATION THAT DOES WHATEVER YOU NEED IT TO DO.

    /*
    ? CLASS DATABASE STRUCTURE: (Classes)
    ? String ClassID (primary key)
    ? String ClassName (secondary key)
    ? String TeacherConnectionName (foreign key)
    ? Array Students (Array of UUIDs denoting StudentID)
     */
    //TODO HOW WOULD YOU IMPLEMENT THE TIMES AT WHICH CLASSES ARE HELD???
    // Should this be handled within the Class object itself, or within the Schedule (TBD) object???

    private String classID;
    private String className;
    private String teacherConnectionName;
    private List<Student> students;
    private String connectionUsername;
    private String connectionPassword;
    private DBUtils db;
    private ClassSearchType searchType;

    public void setClassSearchType(ClassSearchType searchType) {
        this.searchType = searchType;
    }

    /**
     * Constructor for the Class class. Does not make use of any attributes, as you should be suing the methods within this class to either find Classes or to make new Classes. <p></p>
     * Methods within this class will return {@link Class} objects for you to use within the rest of the program.
     *
     * @param connectionUsername The username used to connect to the database with.
     * @param connectionPassword The password used to connect to the database with.
     */
    public Class(String connectionUsername, String connectionPassword) {
        this.connectionUsername = connectionUsername;
        this.connectionPassword = connectionPassword;
        connect(connectionUsername, connectionPassword);
    }

    /**
     * Private constructor for use within this class. As there will be multiple methods which need to return {@link Class} objects. <p></p>
     * WARNING: DOES NOT MAKE A CONNECTION TO THE DATABASE. YOU SHOULD USE A "PLAIN CONSTRUCTOR" TO CONNECT TO THE DATABASE IF NEEDED. The {@link #connect(String, String)} method can be used to do this too.
     *
     * @param classID               The ID of the Class. Should be a version 4 UUID in a String. Should be entirely unique.
     * @param className             The name of the Class. Could also, in theory, be used as a secondary key, but is not the best thing to do as the classID will be entirely reliable and can be used as a primary key just fine.
     * @param teacherConnectionName The Mongo Connection name of the {@link Teacher}. Used as a foreign key within this DB.
     * @param students              A list of {@link Student}s within the Class as a Student object. Each Student object will have their respective ID which can uniquely point to their respective database records.
     */
    private Class(String classID, String className, String teacherConnectionName, List<Student> students) {
        this.classID = classID;
        this.className = className;
        this.teacherConnectionName = teacherConnectionName;
        this.students = students;
    }

    public DBUtils connect(String connectionUsername, String connectionPassword) {
        db = new DBUtils(connectionUsername, connectionPassword);
        return db;
    }

    public String getClassID() {
        return classID;
    }

    public String getTeacherConnectionName() {
        return teacherConnectionName;
    }

    public List<Student> getStudents() {
        return students;
    }

    public String getClassName() {
        return className;
    }

    /**
     * Basic getter method to get the StudentID as a Version 4 UUID of every single Student within the Class. Can be used to verify or modify information.
     * Other convieneience methods to make the insertion of Students into Classes easier will also be handled, but this will likely have to come at a later date.
     *
     * @return {@link List} of {@link java.util.UUID}s as a {@link String} representing every Student within the Class.
     */
    public List<String> getStudentsInClassID() {
        List<String> IDs = new ArrayList<>();
        for (Student x : students) {
            IDs.add(x.getStudentID());
        }
        return IDs;
    }

    /**
     * Basic method to convert a List of Version 4 UUIDs representing the IDs of Students to a List of Students for easier interaction with the object.
     * @param studentIDs The Student IDs you have available as a {@link List} of Strings.
     * @return {@link List} of {@link Student}s.
     */
    private List<Student> fromIDListToStudents(List<String> studentIDs) {
        Student util = new Student(connectionUsername, connectionPassword);
        List<Student> studentList = new ArrayList<>();
        for (String x : studentIDs) {
            if (util.isStudentValid(util.getStudentFromID(x))) {
                studentList.add(util.getStudentFromID(x));
            } // This if statement, in a controlled scenario should never fail and should always be TRUE.
            // Continue along the for loop if false, which shouldn't really happen.
        }
        return studentList;
    }

    /**
     * Method used to get a {@link Class} object from the database using the primary key within the Class collection. As ClassID is the only entirely unique key, it should be the only one used to find and get specific forms.
     * @param classID The UUID of the Class in question.
     * @return The {@link Class} in question.
     */
    public Class getClassFromID(String classID) {
        if(db.isConnected() != 0) { //Connection failed for some reason, fail handling is triggered!
            return null;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection<Document> classes = homeroom.getCollection("Classes");
        BasicDBObject query = new BasicDBObject();
        query.put("ClassID", classID);
        try(MongoCursor<Document> found = classes.find(query).iterator()) {
            while(found.hasNext()) {
                Document x = found.next();
                return new Class(x.get("ClassID").toString(), x.get("ClassName").toString(), x.get("TeacherConnectionName").toString(), fromIDListToStudents((List<String>) x.get("Students")));
            }
            return null;
        }
    }

    /**
     * Basic method written to add Classes to the database of Homeroom. This method also generates a version 4 UUID for the user to make use of when working within the program and handling data.
     * Checks are already made to ensure that the UUID will be valid within the database and will not be wrong. <p></p>
     * This method should generally be used with an instance of {@link Class} class that is able to make connections.
     * @param className The name of the Class in question. Classes can be given an identifying name that should ideally be unique where possible.
     * @param teacherConnectionName The mongo connection name of the teacher. The username of the teacher account.
     * @param studentIDs The IDs of the {@link Student}s in the form group. This should be stored as an array to ensure that MongoDB can take the data.
     * @return An Object representing the Class, which can be used within the rest of the program.
     */
    public Class addClassToDB(String className, String teacherConnectionName, List<String> studentIDs) {
        UUID uuid = UUID.randomUUID();
        while(getClassFromID(uuid.toString()) != null) {
            uuid = UUID.randomUUID();
        }
        MongoCollection<Document> classDB = db.getHomeroomDB().getCollection("Classes");
        //TODO THE ABOVE CAN BE STATIC, AND NOTED DOWN AS A FIELD WITHIN THE CLASS. YOU CAN THEN CALL THIS ON LOGIN TO ENSURE THAT EVERYTHING IS UP AND RUNNING AND IT SHOULD WORK JUST FINE.
        // The same can be sent for db#getHomeroomDB(), this can be called on login, along with every other Object,
        HashMap<String, Object> dataValues = new HashMap<>();
        dataValues.put("ClassID", uuid.toString());
        dataValues.put("TeacherConnectionName", teacherConnectionName);
        dataValues.put("ClassName", className);
        dataValues.put("Students", studentIDs);
        Document insertInfoDocument = new Document(dataValues);
        classDB.insertOne(insertInfoDocument);
        return new Class(uuid.toString(), className, teacherConnectionName, fromIDListToStudents(studentIDs));
    }

    /**
     * Method that returns a list of all {@link Class}es depending on the {@link ClassSearchType}. The parameter string will look for an exact match or a similar match.
     * This is used within the user interface for "searching" of Classes. Users will then be able to click on a Class and access its information from there. <br>
     * WARNING: THIS METHOD DOES NOT HAVE ANY SCOPE FOR PROPER NULL HANDLING!!! THIS NEEDS TO BE HANDLED SEPARATELY.
     * @param searchString The string to search for. Depends on what your search criteria is, but that can be applied with the search filter in the GUI.
     * @return {@link List} of all {@link Class}es representing all Classes that match the search.
     */
    public List<Class> searchForClass(String searchString) {
        if(db.isConnected() != 0) {
            return null;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection forms = homeroom.getCollection("Classes");
        List<Document> matches;
        if(searchType == null) setClassSearchType(ClassSearchType.CLASS_NAME); //Ensures that default form search types actually go through
        switch(searchType) {
            case UUID:
                System.out.println("Searching for a class using their UUID!");
                matches = (List<Document>) forms.find(Filters.regex("ClassID", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
            case CLASS_NAME:
                System.out.println("Searching for a class using their class name!");
                matches = (List<Document>) forms.find(Filters.regex("ClassName", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
            default:
                System.out.println("Searching for a class using their name!");
                matches = (List<Document>) forms.find(Filters.regex("ClassName", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
        }
        List<Class> found = new ArrayList<>();
        if(matches.isEmpty()) {
            return null;
        }
        GUIUtils progress = new GUIUtils("Search Progress | Homeroom", 200, 400, 400, 400, false);
        progress.addLabelToFrame("Searching...", 170, 20, 70, 30, true, 10);
        JProgressBar bar = progress.addProgressBar(50, 60, 300, 20, 0);
        for(Document x : matches) {
            int roundedPercent = Math.round((matches.indexOf(x) / matches.size() - 1) * 100);
            bar.setString("Searching for Classes: " + roundedPercent + "%");
            bar.setValue(roundedPercent);
            found.add(new Class(x.get("ClassID").toString(), x.get("ClassName").toString(), x.get("TeacherConnectionName").toString(), fromIDListToStudents((List<String>) x.get("Students"))));
        }
        progress.closeFrame();
        return found;
    }

    /**
     * Method that deletes a specific record of a {@link Class} that has been specified. This is done by getting the Student's ID.
     * @param clazz The {@link Class} to delete.
     * @return {@link Boolean} that represents the success or failure of this operation.
     */
    public boolean deleteClass(Class clazz) {
        if(db.isConnected() != 0) {
            return false;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection<Document> classes = homeroom.getCollection("Classes");
        Document queryDoc = new Document("ClassID", clazz.getClassID());
        classes.deleteOne(queryDoc);
        return true;
    }

    /**
     * Method that updates a specified field of a {@link Class} that has been specified. Any field can be updated provided that the right field name has been given.
     * The specified string is the string that the field will be updated to. There is no input validation done on the String.
     * @param clazz The {@link Class} to update. Is not spelled correctly to allow for compilation to pass through successfully, ensuring that semantic and syntax analysis pass.
     * @param fieldToUpdate The field to be updated. Should be a valid field name.
     * @param updateObj The object that the field will be updated to. Should be a valid object, storing valid information that an admin user should use. There is no input validation done on this object. In the context of Classes, {@link Object}s were used to ensure that the array field used within the program can be updated properly.
     * @return {@link Boolean} that represents the success or failure of this operation.
     */
    public boolean updateClass(Class clazz, String fieldToUpdate, Object updateObj) {
        if(db.isConnected() != 0) {
            return false;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection<Document> classes = homeroom.getCollection("Classes");
        Document queryDoc = new Document("ClassID", clazz.getClassID());
        Bson update = Updates.set(fieldToUpdate, updateObj);
        UpdateOptions options = new UpdateOptions().upsert(false);
        classes.updateOne(queryDoc, update, options);
        return true;
    }


    /**
     * Quick convenience method used to handle modification of any Students in a Class.
     * @param clazz The class in question that is to be modified.
     * @param option The option to choose from. 1 if you with to ADD a Student to the Class, 2 if you wish to REMOVE a Student from the Class.
     * @param student The {@link Student} in question.
     * @return {@link Boolean} representing the success or failure of this operation.
     */
    public boolean modifyClassStudents(Class clazz, int option, Student student) {
        if(db.isConnected() != 0) {
            return false;
        }
        List<Student> studentsInClass = clazz.getStudents();
        List<String> allStudentIDs = new ArrayList<>();
        for(Student x : studentsInClass) {
            System.out.println(x.getStudentName());
        }
        System.out.println("This is who is currently in the class!");
        switch (option) {
            case 1: //1 should be passed into the method to ADD a student to the Class.
                if(clazz.getStudentsInClassID().contains(student.getStudentID())) {
                    System.out.println("CASE 1 FOR MODIFY IS CLAIMING THAT THE LIST OF STUDENTS ALREADY CONTAINS THE STUDENT IN QUESTION, SO NOTHING NEEDS TO BE DONE!");
                    return true; // Already added to the form, nothing needs to be done.
                }
                studentsInClass.add(student);
                for(Student x : studentsInClass) {
                    allStudentIDs.add(x.getStudentID());
                    System.out.println(x.getStudentName());
                }
                System.out.println("This is who is NOW in the class after ADDING a student, supposedly.");
                return updateClass(clazz, "Students", allStudentIDs);
            case 2: //2 should be passed into the method to REMOVE a student from the form.
                if(!(clazz.getStudentsInClassID().contains(student.getStudentID()))) {
                    System.out.println("CASE 2 FOR MODIFY IS CLAIMING THAT THE LIST OF STUDENTS DOES NOT CONTAIN STUDENT IN QUESTION");
                    return true;
                }
                System.out.println("This is who is NOW in the class after removing a student, supposedly");
                MongoDatabase homeroom = db.getHomeroomDB();
                MongoCollection<Document> classes = homeroom.getCollection("Classes");
                Document queryDoc = new Document("ClassID", clazz.getClassID());
                Bson update = Updates.pull("Students", student.getStudentID());
                UpdateOptions options = new UpdateOptions().upsert(false);
                classes.updateOne(queryDoc, update, options);
                return true; // What would happen is anything were to fail along this line?
        }
        return false;
    }

    /**
     * A quick, simple method to check whether a specific {@link Student} is in a class. This method should be called only using a resource constructor.
     * @param student The {@link Student} to check for, to ensure that they are in the specified {@link Class}.
     * @param clazz The {@link Class} to check for, to ensure that the specified Student is in said Class.
     * @return {@link Boolean} representing whether the specified Student is in the specified Class or not.
     */
    public boolean isStudentInClass(Student student, Class clazz) {
        List<Student> students = clazz.getStudents();
        return students.contains(student);
    }

    /**
     * Method used to get a list of Classes that the {@link Student} is currently in. This is more often used within StudentManagement classes to ensure that Student and Class removal works easier. <p></p>
     * It is worth noting that this method should be used in combination with a search class constructor to ensure that a connection with the database is actually established. <p></p>
     * WARNING: THIS METHOD DOES NOT HAVE ANY SCOPE FOR PROPER NULL HANDLING!!! THIS NEEDS TO BE HANDLED SEPARATELY.
     * @param student The {@link Student} you wish to get a list for all the classes they are in for.
     * @return A {@link List} containing all the {@link Class}es they are in.
     */
    public List<Class> getAllClassesStudentIn(Student student) {
        if(db.isConnected() != 0) {
            return null;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection classes = homeroom.getCollection("Classes");
        List<Document> matches = (List<Document>) classes.find(Filters.eq("Students", student.getStudentID())).into(new ArrayList<Document>()); // ? Find all classes with the Student array containing the ID of the student.
        List<Class> found = new ArrayList<>();
        if(matches.isEmpty()) {
            return null;
        }
        GUIUtils progress = new GUIUtils("Search Progress | Homeroom", 200, 400, 400, 400, false);
        progress.addLabelToFrame("Searching...", 170, 20, 70, 30, true, 10);
        JProgressBar bar = progress.addProgressBar(50, 60, 300, 20, 0);
        for(Document x : matches) {
            int roundedPercent = Math.round((matches.indexOf(x) / matches.size() - 1) * 100);
            bar.setString("Searching for Classes: " + roundedPercent + "%");
            bar.setValue(roundedPercent);
            found.add(new Class(x.get("ClassID").toString(), x.get("ClassName").toString(), x.get("TeacherConnectionName").toString(), fromIDListToStudents((List<String>) x.get("Students"))));
        }
        progress.closeFrame();
        return found;
    }

    /**
     * Method used to get a list of Classes that the {@link Teacher} is currently teaching. This is often used within TeacherManagement classes to ensure that Student and Class removal works easier. <p></p>
     * It is worth nothing that this method should be used in combination with a search class constructor to ensure that a connection with the database is actually established. <p></p>
     * WARNING: THIS METHOD DOES NOT HAVE ANY SCOPE FOR PROPER NULL HANDLING!!! THIS NEEDS TO BE HANDLED SEPARATELY.
     * @param teacher The {@link Teacher} you wish to get a list for all the classes they are in for.
     * @return A {@link List} containing all the {@link Class}es they are in.
     */
    public List<Class> getAllClassesTeacherIn(Teacher teacher) {
        if(db.isConnected() != 0) {
            return null;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection classes = homeroom.getCollection("Classes");
        List<Document> matches = (List<Document>) classes.find(Filters.eq("TeacherConnectionName", teacher.getConnectionUsername())).into(new ArrayList<Document>());
        List<Class> found = new ArrayList<>();
        if(matches.isEmpty()) {
            return null;
        }
        GUIUtils progress = new GUIUtils("Search Progress | Homeroom", 200, 400, 400, 400, false);
        progress.addLabelToFrame("Searching...", 170, 20, 70, 30, true, 10);
        JProgressBar bar = progress.addProgressBar(50, 60, 300, 20, 0);
        for(Document x : matches) {
            int roundedPercent = Math.round((matches.indexOf(x) / matches.size() - 1) * 100);
            bar.setString("Searching for Classes: " + roundedPercent + "%");
            bar.setValue(roundedPercent);
            found.add(new Class(x.get("ClassID").toString(), x.get("ClassName").toString(), x.get("TeacherConnectionName").toString(), fromIDListToStudents((List<String>) x.get("Students"))));
        }
        progress.closeFrame();
        return found;
    }
}