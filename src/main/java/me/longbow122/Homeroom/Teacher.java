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
import java.util.regex.Pattern;

/**
 * The class representing the Teacher object. This class makes use of the {@link me.longbow122.Homeroom.utils.DBUtils} class to make database queries and edit information about particular Teachers.
 * This class holds several constructors and methods which can be used to get instances of the class, depending on the case of what needs to be used where.
 *
 * @author Dhruvil Patel
 */
public class Teacher {

    /*
    TODO
     TEACHER ACCOUNTS CANNOT BE AUTOMATICALLY DELETED, OR REMOVED IN THE SAME WAY THROUGH THE HOMEROOM GUI. WITH THE CURRENT DESIGN, THEY NEED TO BE DELETED
     THROUGH BOTH MONGODB COMPASS AND ATLAS. THIS IS NOT CLEAN, SINCE IT REQUIRES DIRECT ADMIN ACTION THROUGH STUFF THAT IS NOT HOMEROOM, WHICH DOES NOT ALIGN
     WITH THE EASE OF USE IDEOLOGY THAT HOMEROOM TRIES TO HIT.
     THIS PROBLEM WILL NEED TO BE SOLVED SOMEHOW, BUT AS OF 07/08/2022, I HAVE NO IDEAS.
     */

    //TODO TEACHER DATABASE SCHEME:
    // CONNECTION USERNAME
    // TEACHER NAME
    // FORM ID

    //TODO
    // IMPLEMENT TEACHER DELETIONS THROUGH THE GUI AND DB, AND THE REST OF THE ACCOUNT CAN BE REMOVED THROUGH ATLAS. WOULD NEED TO BE DOCUMENTED.

    private String teacherName;
    private DBUtils db;
    private String connectionUsername;
    private String connectionPassword;
    private TeacherSearchType searchType;
    public void setSearchType(TeacherSearchType s) {
        searchType = s;
    }
    private String formID;
    public String getTeacherName() { return teacherName; }
    public String getConnectionUsername() { return connectionUsername; }
    public String getFormID() { return formID; }

    /**
     * Constructor for the Teacher class. Does not make use of any attributes, as you should be using the methods within this class to either find Teachers or to make and delete Forms. <p></p>
     * Methods within this class will return {@link Teacher} objects for you to use within the rest of the program. <p></p>
     * It is worth noting that this constructor will force a connection to be made. Avoid using this if you have the name of the teacher available.
     * @param connectionUsername The username used to connect to the database with. Also the username used to check for particular teachers and whether they currently exist or not.
     * @param connectionPassword The password used to connect to the database with.
     */
    public Teacher(String connectionUsername, String connectionPassword) {
        this.connectionUsername = connectionUsername;
        this.connectionPassword = connectionPassword;
        connect(connectionUsername, connectionPassword);
    }

    /**
     * Constructor for the Teacher class which stores all possible information that the class might use, along with an option to connect to the database or not. <p></p>
     * Methods within this class will return {@link Teacher} objects for you to use within the rest of the program.
     * @param connectionUsername The username used to connect to the database with. Also the username used to check for particular teachers and whether they current exist or not.
     * @param connectionPassword The password used to connect to the database with.
     * @param teacherName The real name of the teacher. Used in GUI displays.
     * @param connect Boolean asking whether a connection to the database should be established.
     */
    public Teacher(String connectionUsername, String connectionPassword, String teacherName, String formID, boolean connect) {
        this.connectionUsername = connectionUsername;
        this.connectionPassword = connectionPassword;
        if(formID == null) {
            this.formID = "";
        } else this.formID = formID;
        if(connect) {
            connect(connectionUsername, connectionPassword);
        }
        this.teacherName = teacherName;
    }

    /**
     * Private constructor to be used within the class to retrieve Teachers from the DB, and make use of the data. There is no connection to the database involved with this.
     * @param connectionUsername The username that the user uses to log into the database.
     * @param teacherName The name of the teacher that they have chosen.
     * @param formID The UUID of the {@link Form} group they are associated with as a String. Allows null to be passed through to allow association with no forms.
     */
    private Teacher(String connectionUsername, String teacherName, String formID) {
        this.connectionUsername = connectionUsername;
        this.teacherName = teacherName;
        if(formID == null) {
            this.formID = "";
        } else this.formID = formID;
    }

    private DBUtils connect(String connectionUsername, String connectionPassword) {
        this.db = new DBUtils(connectionUsername, connectionPassword);
        return db;
    }

    /**
     * Method used to find a {@link Teacher} within the Homeroom database using the connection username (which is being used as a primary key within that {@link MongoCollection}). <p></p>
     * This method makes use of the {@link #connectionUsername} attribute instead of any parameters since there is nothing else to realistically use, since this method is used to get a specific teacher, not search for one. <p></p>
     * It is worth noting that the null result from this can also be used to check the validity of the Teacher's presence in the database, since a null result can be used to assume that they do not exist.
     * A bad connection, might return null, so something needs to be prepared for this eventuality.
     * @return {@link Teacher} object representing the Teacher account itself.
     */
    public Teacher getTeacherFromConnectionUsername(String connectionUsername) {
        if(db.isConnected() != 0) { //Connection failed for some reason.
            return null;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection<Document> teachers = homeroom.getCollection("Teachers");
        BasicDBObject query = new BasicDBObject();
        query.put("ConnectionUsername", connectionUsername);
        try (MongoCursor<Document> found = teachers.find(query).iterator()) {
            while(found.hasNext()) {
                Document x = found.next();
                return new Teacher(connectionUsername, x.get("TeacherName").toString(), x.get("FormID").toString());
            }
            return null;
        }
    }

    /**
     * Method that returns as list of all {@link Teacher}s depending on the {@link TeacherSearchType}. The parameter string will look for an exact match or a similar match.
     * This is used within the user interface for "searching" of students. Users will then be able to click on a teacher and access their information through there. <p></p>
     * WARNING: THIS METHOD DOES NOT HAVE ANY SCOPE FOR PROPER NULL HANDLING!!! THIS NEEDS TO BE HANDLED SEPERATELY.
     * @param searchString The string to search for. Depends on what your search criteria is, but that can be applied with the search filter in the GUI.
     * @return {@link List} of {@link Teacher}s representing all Teachers that match the search.
     */
    public List<Teacher> searchForTeacher(String searchString) {
        if(db.isConnected() != 0) { //Connection has failed for some reason, begin handling null results.
            return null;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection teachers = homeroom.getCollection("Teachers");
        List<Document> matches;
        if(searchType == null) setSearchType(TeacherSearchType.TEACHER_NAME); //Ensures that it defaults to NAME searching if there is no default value preset.
        switch (searchType) {
            case MONGO_CONNECTION_NAME:
                System.out.println("Searching for teachers using their Homeroom usernames!");
                matches = (List<Document>) teachers.find(Filters.regex("ConnectionUsername", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
            default:
                System.out.println("Searching for teachers using their names!");
                matches = (List<Document>) teachers.find(Filters.regex("TeacherName", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
        }
        List<Teacher> found = new ArrayList<>();
        if(matches.isEmpty()) {
            return null;
        }
        GUIUtils progress = new GUIUtils("Search Progress | Homeroom", 200, 400, 400, 400, false);
        progress.addLabelToFrame("Searching...", 170, 20, 70, 30, true, 10);
        JProgressBar bar = progress.addProgressBar(50, 60, 300, 20, 0);
        for(Document x : matches) {
            int roundedPercent = Math.round((matches.indexOf(x) / matches.size() - 1) * 100);
            bar.setString("Searching for Teachers: " + roundedPercent + "%");
            bar.setValue(roundedPercent);
            String formID;
            if(!x.containsKey("FormID") || x.get("FormID") == null) {
                formID = "";
            } else formID = x.get("FormID").toString();
            found.add(new Teacher(x.get("ConnectionUsername").toString(), x.get("TeacherName").toString(), formID));
            
        }
        progress.closeFrame();
        return found;
    }

    /**
     * Basic method written to add {@link Teacher}s to the database of Homeroom. There is no need for them to make use of a UUID in this case, since the connection username will be entirely unique.
     * Checks are down on the {@link Form} group referenced by the Form ID already, to ensure that it is a valid form. <p></p>
     * This method should generally be used within an instance of the {@link Teacher} class that is able to make connections. If this method is not possible, then the developer should make use of the {@link #connect(String, String)} method.
     * @param connectionUsername The username used by the user to log into 'Homeroom'.
     * @param teacherName The real name of the teacher, chosen by the user upon creation.
     * @param formID The {@link Form} that the {@link Teacher} belongs to represented as a version 4 UUID. It is worth noting that the form may be null in the case that the Teacher is not part of a form. In this case, null-handling has been used to ensure that nothing malicious is passed through.
     * @return A representation of the {@link Teacher} in Object form. Can be used to pull and handle information throughout the rest of the program.
     */
    public Teacher addTeacherToDB(String connectionUsername, String teacherName, String formID) {
        MongoCollection<Document> teachersDB = db.getHomeroomDB().getCollection("Teachers");
        HashMap<String, Object> dataValues = new HashMap<>();
        dataValues.put("ConnectionUsername", connectionUsername);
        dataValues.put("TeacherName", teacherName);
        if(new Form(connectionUsername, connectionPassword).getFormFromID(formID) != null) { //Make sure that the form is valid
            dataValues.put("FormID", formID);
        } else dataValues.put("FormID", ""); // Form was not valid, not safe to pass that ID in!
        Document insertInfoDocument = new Document(dataValues);
        teachersDB.insertOne(insertInfoDocument);
        return new Teacher(connectionUsername, teacherName, formID);
    }

    /**
     * Method that updated a specified field of a {@link Teacher} that has been specified. Any field can be updated provided the right field name has been given.
     * The specified string is the string that the field will be updated to. There is no input validation being done on the string.
     * @param teacher The {@link Teacher} to update.
     * @param fieldToUpdate The field to be updated. Should be a valid field name.
     * @param updateObj The object that the field will be updated to. Should be a valid object, storing valid information that an admin user should use. There is no input validation done on this object.
     * @return {@link Boolean} that represents the success or failure of this operation.
     */
    public boolean updateTeacher(Teacher teacher, String fieldToUpdate, Object updateObj) {
        if(db.isConnected() != 0) { //Something has gone wrong with the database connection!
            return false;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection<Document> teachers = homeroom.getCollection("Teachers");
        Document queryDoc = new Document("ConnectionUsername", teacher.getConnectionUsername());
        Bson update = Updates.set(fieldToUpdate, updateObj);
        UpdateOptions options = new UpdateOptions().upsert(false);
        teachers.updateOne(queryDoc, update, options);
        return true;
    }

    /**
     * Basic method used to check whether a {@link Teacher} is <i>in</i> a {@link Form}. Used within the program to ensure that the Teacher does not join multiple forms, and if they do so, they can be removed from one and put in the other.
     * The result of this method can be used to determine that.
     * @param teacher The {@link Teacher} object you are checking for, to determine whether they are in a {@link Form} group or not.
     * @return {@link Boolean} representing whether the Teacher is in a form or not.
     */
    public boolean isTeacherInForm(Teacher teacher) {
        Form form = new Form(connectionUsername, connectionPassword);
        return form.getFormFromID(teacher.getFormID()) != null;
    }

    /**
     * Method that deleted a specific record of a {@link Teacher} that has been specified. This is done by getting the Teacher's connection name, and associated form where applicable. <p></p>
     * It is also worth noting that this method handles the deletion of information from relevant Form Groups where needed. This means that the "Forms" collection is also accessed, and the connection name is removed from there.
     * @param teacher The {@link Teacher} you wish to delete.
     * @return {@link Boolean} that represents the success or failure of this operation.
     */
    public boolean deleteTeacher(Teacher teacher) {
        Form f = new Form(connectionUsername, connectionPassword);
        if(isTeacherInForm(teacher)) { // Teacher is in a form, they need to be deleted from that form as well
            Form form = f.getFormFromID(teacher.getFormID());
            f.updateForm(form, "TeacherConnectionName", ""); // Remove the teacher from the form as well
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection<Document> forms = homeroom.getCollection("Teachers");
        Document queryDoc = new Document("ConnectionUsername", teacher.getConnectionUsername());
        forms.deleteOne(queryDoc); // Delete the teacher from the TEACHER DB
        return true;
    }
}
