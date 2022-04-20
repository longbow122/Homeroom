package me.longbow122.Homeroom;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.longbow122.Homeroom.utils.DBUtils;
import me.longbow122.Homeroom.utils.GUIUtils;
import org.bson.Document;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The class representing the Student object. This class makes use of the {@link me.longbow122.Homeroom.utils.DBUtils} class to make database queries, and edit information about particular students.
 * This class holds several constructors which can be used to get instances of the class, depending on the case of what needs to be used where.
 */
public class Student {
    private String studentID;
    private String studentName;
    private String studentDOB;
    private String studentAddress;
    private String studentPhone;
    private String studentMedical;
    private String guardianName;
    private String guardianPhone;
    private String guardianAddress;
    private String connectionUsername;
    private String connectionPassword;
    private DBUtils db;
    private StudentSearchType searchType;

    private StudentSearchType getStudentSearchType() {
        return searchType;
    }

    public void setStudentSearchType(StudentSearchType type) {
        searchType = type;
    }

    /**
     * Constructor for the Student class. Does not make use of any attributes, as you should be using the methods within this class to either find students, or make new Students within the database.
     * <p></p>
     * Methods within this class will return {@link Student} objects for you to use within the program, but this plain constructor SHOULD NOT be used without another method suffixing it.
     * @
     */
    public Student(String connectionUsername, String connectionPassword) {
        this.connectionUsername = connectionUsername;
        this.connectionPassword = connectionPassword;
        db = new DBUtils(connectionUsername, connectionPassword);
    }

    /**
     * Private constructor for use within this class. As there will be multiple methods which are to be used within the class, this constructor will help with methods that return any Student objects.
     * @param studentID - The ID of the student. Should be a version 4 UUID in a String. Should be entirely unique.
     * @param studentName - The name of the student. Could, in theory be used as a secondary key, but not worth the risk.
     * @param studentDOB - The date of birth of the student. Stored as a {@link String} since you cannot store the slashes for the date format in an Integer. Seemed easier to use this too.
     * @param studentAddress - The address of the student. Stored as a {@link String} for obvious reasons.
     * @param studentPhone - The phone number of the student. Stored as a {@link String} to allow for international country codes.
     * @param studentMedical - The medical information of the Student.
     * @param guardianName - The name of the guardian of the Student.
     * @param guardianAddress - The address of the guardian. Will normally be the same as the Student's address, but just in case, it is stored seperately.
     * @param guardianPhone - The phone number of the guardian. Stored as a {@link String} to allow for international country codes.
     */
    private Student(String connectionUsername, String connectionPassword, String studentID, String studentName, String studentDOB, String studentAddress, String studentPhone, String studentMedical, String guardianName, String guardianAddress, String guardianPhone) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentDOB = studentDOB;
        this.studentAddress = studentAddress;
        this.studentPhone = studentPhone;
        this.studentMedical = studentMedical;
        this.guardianName = guardianName;
        this.guardianPhone = guardianPhone;
        this.guardianAddress = guardianAddress;
        this.connectionPassword = connectionPassword;
        this.connectionUsername = connectionUsername;
        db = new DBUtils(connectionUsername, connectionPassword);
    }

    public String getStudentName() {
        return studentName;
    }
    public String getStudentID() { return studentID; }
    public String getStudentAddress() { return studentAddress; }
    public String getStudentPhone() { return studentPhone; }
    public String getStudentMedical() { return studentMedical; }
    public String getStudentDOB() { return studentDOB; }
    public String getGuardianName() { return guardianName; }
    public String getGuardianPhone() { return guardianPhone; }
    public String getGuardianAddress() { return guardianAddress; }


    /**
     * Convienience method to check whether the student object you have gotten is valid. This method is normally called everywhere, and should be to ensure safety.
     * @param student - The Student object you are checking for, to ensure that it is valid.
     * @return Boolean representing whether the Student is present within the database or not using the exact attributes provided.
     */
    public boolean isStudentValid(Student student) {
        if(student == null) return false;
        String checkID = student.getStudentID();
        Student found = getStudentFromID(checkID);
        System.out.println(checkID);
        System.out.println(found.getStudentID());
        return found != null;
    }

    /**
     *
     * Method used to get a Student object from the database using the primary key within the Students table. As the studentID is the only entirely unique key, it should be the only one used to get and find specific students.
     * @param studentID - The UUID of the Student as a String representation.
     * @return Student object representing the Student in question. Null if none found.
     */
    public Student getStudentFromID(String studentID) {
        if(!(db.isConnected() == 0)) { //Connection failed for some reason, fail handling needs to go here.
            return null;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection<Document> students = homeroom.getCollection("Students");
        BasicDBObject query = new BasicDBObject();
        query.put("StudentID", studentID);
        try (MongoCursor<Document> found = students.find(query).iterator()) {
            while (found.hasNext()) {
                Document x = found.next();
                return new Student(connectionUsername, connectionPassword, x.get("StudentID").toString(), x.get("StudentName").toString(), x.get("StudentDOB").toString(), x.get("StudentAddress").toString(), x.get("StudentPhone").toString(), x.get("StudentMedical").toString(), x.get("GuardianName").toString(), x.get("GuardianAddress").toString(), x.get("GuardianPhone").toString());
            }
            return null;
        } // Finally block used to execute this statement regardless of an exception being thrown. Used when statements MUST be run no matter what.
        // In this case, closing the cursor connection must be done, regardless of an operation success or failure. As such, it belongs in a finally block.
    }

    /** TODO ALLOW SEARCHING BY FORM GROUP WHEN FORM GROUPS HAVE BEEN IMPLEMENTED.
     * Method that returns a list of all students depending on the {@link StudentSearchType}. The parameter string will look for an exact match or a similar match.
     * This is used within the user interface for "searching" of students. Users will then be able to click on a student and access their information through there. <p></p>
     * WARNING: THIS METHOD DOES NOT HAVE ANY SCOPE FOR PROPER NULL HANDLING!!! THIS NEEDS TO BE HANDLED SEPARATELY.
     * @param searchString  The string to search for. Depends on what your search criteria is, but that can be applied with the search filter in the GUI.
     * @return List of Students representing all students that match the search.
     */
    public List<Student> searchForStudent(String searchString) {
        if(db.isConnected() != 0) { //Connection has failed for some reason, fail handling needs to go here.
            return null;
        }
        MongoDatabase homeroom = db.getHomeroomDB();
        MongoCollection students = homeroom.getCollection("Students");
        List<Document> matches;
        if(searchType == null) setStudentSearchType(StudentSearchType.NAME); // Ensures that it defaults to NAME searching if there is no default value.
        switch(searchType) {
            case UUID: //TODO UUID SEARCHING WORKS GOOD, SORTED
                System.out.println("Searching for a student using their UUID!");
                matches = (List<Document>) students.find(Filters.regex("StudentID", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
            case DOB: //TODO DOB SEARCHING WORKS GOOD, SORTED.
                System.out.println("Searching for a student using their date of birth!");
                matches = (List<Document>) students.find(Filters.regex("StudentDOB", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
            case PHONE: //TODO PHONE SEARCHING WORKS GOOD, SORTED.
                System.out.println("Searching for a student using their phone number!");
                matches = (List<Document>) students.find(Filters.regex("StudentPhone", Pattern.compile("(?i)^(\\+" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
            case ADDRESS: //TODO ADDRESS SEARCHING WORKS GOOD, SORTED.
                System.out.println("Searching for a student using their address!");
                matches = (List<Document>) students.find(Filters.regex("StudentAddress", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
            default: //TODO NAMES SEARCHING WORKS GOOD, SORTED
                System.out.println("Searching for a student using their name!");
                matches = (List<Document>) students.find(Filters.regex("StudentName", Pattern.compile("(?i)^(" + searchString + ")", Pattern.CASE_INSENSITIVE))).into(new ArrayList<Document>());
                break;
        }
        List<Student> found = new ArrayList<>();
        if(matches.isEmpty()) {
            return null;
        }
        GUIUtils progress = new GUIUtils("Search Progress | Homeroom", 200, 400, 400, 400, false);
        progress.addLabelToFrame("Searching...", 170, 20, 70, 30, true, 10);
        JProgressBar bar = progress.addProgressBar(50, 60, 300, 20, 0);
        for(Document x : matches) {
            int roundedPercent = Math.round((matches.indexOf(x) / matches.size() - 1) * 100);
            bar.setString("Searching for Students: " + roundedPercent + "%");
            bar.setValue(roundedPercent);
            String medicalInfo;
            if(x.containsKey("StudentMedical") == false || x.get("StudentMedical") == null) { //Problem line, needs to have it be null somewhere if there is no medical value
                medicalInfo = "N/A"; //Doesn't seem to contain a key
            } else medicalInfo = x.get("StudentMedical").toString();
            System.out.println(x.get("StudentName"));
            found.add(new Student(connectionUsername, connectionPassword, x.get("StudentID").toString(), x.get("StudentName").toString(), x.get("StudentDOB").toString(), x.get("StudentAddress").toString(), x.get("StudentPhone").toString(), medicalInfo, x.get("GuardianName").toString(), x.get("GuardianAddress").toString(), x.get("GuardianPhone").toString()));
        }
        progress.closeFrame();
        return found;
    }
}
