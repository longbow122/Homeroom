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
 * The class representing the Form object. This class makes use of the {@link me.longbow122.Homeroom.utils.DBUtils} class to make database queries, and edit information about particular forms.
 * This class holds several constructors which can be used to get instances of the form, depending on the case of what needs to be used where.
 */
public class Form {

    /*
    FORM DATABASE STRUCTURE:
    String FormID
    String TeacherName
    String FormName
    Array students (Array of UUIDs denoting StudentID)
     */

    private String formID;

    private String teacherName;

    private String formName;

    private List<Student> students;

    private String connectionUsername;

    private String connectionPassword;

    private DBUtils db;

    private FormSearchType searchType;

    public void setFormSearchType(FormSearchType type) {
        searchType = type;
    }

    //TODO TEST ENTIRE CLASS LOGIC IN THE CONTEXT OF THE PROGRAM AND ITS GUIs

    /*
    Forms should hold the following information:

    The name of the teacher, as a string.
    The name of the form, as a String
    The ID of the form, as a String.
    A list of all students in that form. (Use the List<Student> object)
     */

    /*TODO How to add a list of student to the database:

    As students will each have a unique ID, get each and every single one of those from your list of Students.
    Using that list of UUIDs, convert them all into a string, put them into an array and insert that Array into your document.
     */

    //TODO
    // IMPLEMENT A SEARCH AND SELECT FOR FORMS

    /**
     * Constructor for the Form class. Does not make use of any attributes, as you should be using the methods within this clas to either find Forms or to make new Forms. <p></p>
     * Methods within this class will return {@link Form} objects for you to use within the rest of the program.
     * @param connectionUsername The username used to connect to the database with.
     * @param connectionPassword The password used to connect to the database with.
     */
    public Form(String connectionUsername, String connectionPassword) {
        this.connectionUsername = connectionUsername;
        this.connectionPassword = connectionPassword;
        connect(connectionUsername, connectionPassword);
    }

    /**
     * Private constructor for use within this class. As there will be multiple methods which need to return Student objects. <p></p>
     * WARNING: DOES NOT MAKE A CONNECTION TO THE DATABASE. YOU SHOULD USE A "PLAIN CONSTRUCTOR" to connect to the database if neded. The {@link #connect(String, String)} method can also be used to do this too.
     * @param formID The ID of the Form. Should be a version 4 UUID in a String. Should be entirely unique.
     * @param teacherName The name of the Teacher. Could, in theory be used as a secondary key, but isn't really worth the risk.
     * @param formName The name of the Form. Could also, in theory, but used as a secondary key, but is not the best thing to do as the formID will be entirely reliable and can be used as a primary key just fine.
     * @param students A list of {@link Student}s within the form as a Student object. Each Student object will have their respective ID which can uniquely point to their respective database records.
     */
    private Form(String formID, String teacherName, String formName, List<Student> students) {
        this.formID = formID;
        this.teacherName = teacherName;
        this.formName = formName;
        this.students = students;
    }

    public String getFormID() {
        return formID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public List<Student> getStudents() {
        return students;
    }

    public String getFormName() {
        return formName;
    }

    /**
     * Basic getter method to get the StudentID as a Version 4 UUID of every single Student within the formm. Can be used to verify or modify information.
     * Other convenience methods to make the insertion of students into forms easier will also be handled, but this will likely have to come at a later date.
     * @return A list of the Student ID of every single student as Strings.
     */
    public List<String> getStudentsInFormID() {
        List<String> IDs = new ArrayList<>();
        for(Student x : students) {
            IDs.add(x.getStudentID());
        }
        return IDs;
    }

    public DBUtils connect(String connectionUsername, String connectionPassword) {
        db = new DBUtils(connectionUsername, connectionPassword);
        return db;
    }

    /**
     * Method used to get a {@link Form} object from the database using the primary key within the Forms table. As the formID is the only entirely unique key, it should be the only one used to get and find specific forms.
     * @param formID The UUID of the form in question.
     * @return The {@link Form} in question.
     */
    public Form getFormFromID(String formID) {
        if(!(db.isConnected() == 0)) { //Connection failed for some reason, fail handling is triggered!
            return null;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection<Document> forms = homeroom.getCollection("Forms");
        BasicDBObject query = new BasicDBObject();
        query.put("FormID", formID);
        try (MongoCursor<Document> found = forms.find(query).iterator()) {
            while (found.hasNext()) {
                Document x = found.next();
                return new Form(x.get("FormID").toString(), x.get("TeacherName").toString(), x.get("FormName").toString(), fromIDArrayToStudents((String[]) x.get("Students")));
            }
            return null;
        }
    }

    /**
     * Basic method written to add students to the database of Homeroom. This method also generates a version 4 UUID for the user to make of when working within the program and when handling data.
     * Checks are already made to ensure that the UUID will be valid within the database and will not be wrong. <br>
     * This method should generally be used with an instance of {@link Form} class that is able to make connections. If this method is not possible, then the developer should make use of the {@link #connect(String, String)} method.
     * @param teacherName The name of the teacher who is under control of the form class.
     * @param formName The name of the form in question. Forms can be given an identifying name which should be unique where posssible.
     * @param studentIDs The IDs of the {@link Student}s in the form group. This should be stored as an array to ensure that MongoDB can take the data.
     * @return An Object representing the Form group, which can be used within the rest of the program.
     */
    public Form addFormToDB(String teacherName, String formName, String[] studentIDs) {
        UUID uuid = UUID.randomUUID();
        while(getFormFromID(uuid.toString()) != null) {
            uuid = UUID.randomUUID();
        }
        MongoCollection<Document> formsDB = db.getHomeroomDB().getCollection("Forms");
        HashMap<String, Object> dataValues = new HashMap<>();
        dataValues.put("FormID", uuid);
        dataValues.put("TeacherName", teacherName);
        dataValues.put("FormName", formName);
        dataValues.put("Students", studentIDs);
        Document insertInfoDocument = new Document(dataValues);
        formsDB.insertOne(insertInfoDocument);
        return new Form(uuid.toString(), teacherName, formName, fromIDArrayToStudents(studentIDs));
    }

    private List<Student> fromIDArrayToStudents(String[] studentIDs) {
        Student util = new Student(connectionUsername, connectionPassword);
        List<Student> studentList = new ArrayList<>();
        for(String x : studentIDs) {
            if(util.isStudentValid(util.getStudentFromID(x))) {
                studentList.add(util.getStudentFromID(x));
            } // This if statement, in a controlled scenario should never fail and should always be TRUE.
              // Continue along the for loop if false, which shouldn't really happen.
            continue;
        }
        return studentList;
    }

    /**
     * Method that returns a list of all students depending on the {@link FormSearchType}. The parameter string will look for an exact match or a similar match.
     * This is used within the user interface for "searching" of students. Users will then be able to click on a Form group and access their information from there. <p></p>
     * WARNING: THIS METHOD DOES NOT HAVE ANY SCOPE FOR PROPER NULL HANDLING!!! THIS NEEDS TO BE HANDLED SEPARATELY.
     * @param searchString The string to search for. Depends on what your search criteria is, but that can be applied with the search filter in the GUI.
     * @return List of Forms representing all students that match the search.
     */
    public List<Form> searchForFormGroup(String searchString) {
        if(db.isConnected() != 0) {
            return null;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection forms = homeroom.getCollection("Forms");
        List<Document> matches;
        if(searchType == null) setFormSearchType(FormSearchType.FORM_NAME); //Ensures that default form search types actually go through
        switch(searchType) {
            case UUID:
                System.out.println("Searching for a form using their UUID!");
                matches = (List<Document>) forms.find(Filters.regex("FormID", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
            case FORM_NAME:
                System.out.println("Searching for a form using their form name!");
                matches = (List<Document>) forms.find(Filters.regex("FormName", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
            case TEACHER_NAME:
                System.out.println("Searching for a form using their teacher name!");
                matches = (List<Document>) forms.find(Filters.regex("TeacherName", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
            default:
                System.out.println("Searching for a form using their name!");
                matches = (List<Document>) forms.find(Filters.regex("FormName", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
        }
        List<Form> found = new ArrayList<>();
        if(matches.isEmpty()) {
            return null;
        }
        GUIUtils progress = new GUIUtils("Search Progress | Homeroom", 200, 400, 400, 400, false);
        progress.addLabelToFrame("Searching...", 170, 20, 70, 30, true, 10);
        JProgressBar bar = progress.addProgressBar(50, 60, 300, 20, 0);
        for(Document x : matches) {
            int roundedPercent = Math.round((matches.indexOf(x) / matches.size() - 1) * 100);
            bar.setString("Searching for Forms: " + roundedPercent + "%");
            bar.setValue(roundedPercent);
            System.out.println(x.get("FormName"));
            found.add(new Form(x.get("FormID").toString(), x.get("TeacherName").toString(), x.get("FormName").toString(), fromIDArrayToStudents((String[]) x.get("Students"))));
        }
        progress.closeFrame();
        return found;
    }

    /**
     * Method that deletes a specific record of a {@link Form} that has been specified. This is done by getting the Student's ID.
     * @param form The {@link Form} to delete.
     * @return {@link Boolean} that represents the success or failure of this operation.
     */
    public boolean deleteForm(Form form) {
        if(db.isConnected() != 0) {
            return false;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection<Document> forms = homeroom.getCollection("Forms");
        Document queryDoc = new Document("FormID", form.getFormID());
        forms.deleteOne(queryDoc);
        return true;
    }

    /**
     * Method that updates a specified filed of a {@link Form} that has been specified. Any filed can be updated provided the the right field name has been given.
     * The specified string is the string that the field will be updated to. There is no input validation done on the string.
     * @param form The {@link Form} to update.
     * @param fieldToUpdate The field to be updated. Should be a valid field name.
     * @param updateObj The object that the field will be updated to. Should be a valid object, storing valid information that an admin user should use. There is no input validation done on this object. In the context of Forms, {@link Object}s were use to ensure that the array field used within the program can be updated properly.
     * @return {@link Boolean} that represents the success or failure of this operation.
     */
    public boolean updateForm(Form form, String fieldToUpdate, Object updateObj) {
        if(db.isConnected() != 0) {
            return false;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection<Document> forms = homeroom.getCollection("Forms");
        Document queryDoc = new Document("FormID", form.getFormID());
        Bson update = Updates.set(fieldToUpdate, updateObj);
        UpdateOptions options = new UpdateOptions().upsert(false);
        forms.updateOne(queryDoc, update, options);
        return true;
    }

    /**
     * Quick convienince method used to handle modifcation of any students in a Form Group.
     * @param form The form in question that is to be modified.
     * @param option The option to choose from. 1 if you wish to ADD a student to the form group, 2 if you wish to REMOVE a student from the form group.
     * @param student The {@link Student} in question.
     * @return Boolean representing the success or failure of the operation.
     */
    public boolean modifyFormStudents(Form form, int option, Student student) {
        if(db.isConnected() != 0) {
            return false;
        }
        List<Student> studentsInForm = form.getStudents();
        switch(option) {
            case 1: //1 should be passed into the method to ADD a student to the form.
                if(studentsInForm.contains(student)) {
                    return true; //Already added to the form, nothing needs to be done.
                }
                studentsInForm.add(student);
                String[] studentIDs = new String[studentsInForm.size()];
                for(Student x : studentsInForm) {
                    studentIDs[studentsInForm.indexOf(x)] = x.getStudentID();
                }
                return updateForm(form, "students", studentIDs);
            case 2: //2 should be passed into the method to REMOVE a student from the form
                if(!(studentsInForm.contains(student))) {
                    return true; //Does not exist within the form already, no need to remove anything
                }
                studentsInForm.remove(student);
                String[] studentRemove = new String[studentsInForm.size()];
                for(Student x : studentsInForm) {
                    studentRemove[studentsInForm.indexOf(x)] = x.getStudentID();
                }
                return updateForm(form, "students", studentRemove);
        }
        return false;
    }


}
