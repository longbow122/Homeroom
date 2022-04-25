package me.longbow122.Homeroom;

import com.github.lgooddatepicker.components.DatePicker;
import me.longbow122.Homeroom.utils.ConfigReader;
import me.longbow122.Homeroom.utils.DBUtils;
import me.longbow122.Homeroom.utils.GUIUtils;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class which will store all called programs, and the main method to be run.
 */
public class Main {

    /*
    Main method to be called.
    The called program
     */
    public static void main(String[] args) {
        Main main = new Main();
        File config = main.getConfigFile();
        System.out.println(new ConfigReader(config).getConnectionStringFromConfig());
        main.openLoginPage();
    }

    /*
    Got to be a cleaner way of writing the below code. This just seems messy and outright untidy.
    Open to worded suggestions if anyone had any. (As in, you tell me what you think would work, but try to avoid directly
    spoonfeeding the answer where possible. Thank you!)

    Thinking I could bring the action listener for the login button into the fieldList array if I change its type to Component.
    But then how would I add the action listener without ending up with the same amount of lines as before?
    Trying to keep lines of code to a minimum and clean up this code where possible.
     */
    private void openLoginPage() {
        GUIUtils loginGUI = new GUIUtils("Homeroom Login", 300, 600, 700, 300, true);
        loginGUI.addLabelToFrame("Username: ", 30, 100, 70, 30, false);
        JTextField usernameField = loginGUI.addTextField(100, 100, 400, 25, "Enter the username used to log into Homeroom here!");
        PromptSupport.setPrompt("Enter Username Here", usernameField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, usernameField);
        loginGUI.addLabelToFrame("Password: ", 30, 125, 70, 30, false);
        JTextField passwordField = loginGUI.addPasswordField(100, 125, 400, 25);
        PromptSupport.setPrompt("Enter Password Here", passwordField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, passwordField);
        loginGUI.addLabelToFrame("Homeroom Login", 225, 65, 1000, 30, true);
        JButton loginButton = loginGUI.addButtonToFrame("Login", 30, 400, 100, 150);
        JTextField[] fieldList = new JTextField[]{usernameField, passwordField};
        for (JTextField x : fieldList) {
            x.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String username = usernameField.getText();
                        String password = passwordField.getText();
                        System.out.println("Attempt Connection!");
                        int connection = new DBUtils(username, password).isConnected();
                        while(connection != 0 && connection != 99) {
                            switch (connection) {
                                case 1: //Timed out or failed connection
                                    System.out.println("Connection timed out, failed connection!");
                                    Object[] options = new Object[]{"Reconnect", "Cancel, exit Homeroom"};
                                    int reconnectOption = JOptionPane.showOptionDialog(loginGUI.getFrame(), "Failed to connect due to a timeout! Would you like to reconnect?", "Homeroom failed to connect!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"), options, "Test");
                                    System.out.println(reconnectOption);
                                    switch (reconnectOption) {
                                        case 0:
                                            System.out.println("Reconnect button clicked!");
                                            connection = new DBUtils(username, password).isConnected();
                                            break;
                                        case 1:
                                            System.out.println("Cancel button pressed!");
                                            System.exit(0);
                                            break;
                                        default:
                                            System.out.println("Failed login!");
                                            JOptionPane.showMessageDialog(loginGUI.getFrame(), "Failed login due to a misc error. Please try again!", "Whoops!", JOptionPane.ERROR_MESSAGE);
                                            break;
                                    }
                                    break;
                                case 2: //Bad credentials passed through
                                    System.out.println("Bad credentials passed through, cannot log in!");
                                    JOptionPane.showMessageDialog(loginGUI.getFrame(), "Failed to connect due to bad credentials! Please try logging in again.", "Whoops!", JOptionPane.ERROR_MESSAGE);
                                    connection = 99;
                                    break;
                            }
                        }
                        System.out.println("Wasn't 0, get passed through as valid connection");
                        if(connection != 99) {
                            loginGUI.closeFrame();
                            openMainGUI(username, password);
                            return;
                        }
                        usernameField.setText("");
                        passwordField.setText("");
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                }
            });
        }
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            System.out.println("Attempt Connection!");
            int connection = new DBUtils(username, password).isConnected();
            while(connection != 0 && connection != 99) {
                switch (connection) {
                    case 1: //Timed out or failed connection
                        System.out.println("Connection timed out, failed connection!");
                        Object[] options = new Object[]{"Reconnect", "Cancel, exit Homeroom"};
                        int reconnectOption = JOptionPane.showOptionDialog(loginGUI.getFrame(), "Failed to connect due to a timeout! Would you like to reconnect?", "Homeroom failed to connect!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"), options, "Test");
                        System.out.println(reconnectOption);
                        switch (reconnectOption) {
                            case 0:
                                System.out.println("Reconnect button clicked!");
                                break;
                            case 1:
                                System.out.println("Cancel button pressed!");
                                break;
                            default:
                                System.out.println("Failed login!");
                                JOptionPane.showMessageDialog(loginGUI.getFrame(), "Failed login due to a misc error. Please try again!", "Whoops!", JOptionPane.ERROR_MESSAGE);
                                break;
                        }
                    case 2: //Bad credentials passed through
                        System.out.println("Bad credentials passed through, cannot log in!");
                        JOptionPane.showMessageDialog(loginGUI.getFrame(), "Failed to connect due to bad credentials! Please try logging in again.", "Whoops!", JOptionPane.ERROR_MESSAGE);
                        connection = 99;
                        break;
                }
            }
            System.out.println("Wasn't 0, get passed through as valid connection");
            if(connection != 99) {
                loginGUI.closeFrame();
                openMainGUI(username, password);
                return;
            }
            usernameField.setText("");
            passwordField.setText("");
        });
    }

    /*
    Got to be a cleaner way of writing the below code. This just seems messy and outright untidy.
    Open to worded suggestions if anyone had any. (As in, you tell me what you think would work, but try to avoid directly
    spoonfeeding the answer where possible. Thank you!)
     */
    private void openMainGUI(String username, String password) {
        GUIUtils mainGUI = new GUIUtils("Homeroom", 1000, 1220, 300, 0, true);
        JButton manageStudents = mainGUI.addButtonToFrame("Manage Students", 300, 400, 0, 50);
        manageStudents.setToolTipText("Manage all Students in your school from here.");
        manageStudents.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.BOLD, 40));
        JButton manageClasses = mainGUI.addButtonToFrame("Manage Classes", 300, 400, 400, 50);
        manageClasses.setToolTipText("Manage all classes in your school from here.");
        manageClasses.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.BOLD, 40));
        JButton timetable = mainGUI.addButtonToFrame("Manage Schedule", 300, 400, 800, 50);
        timetable.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.BOLD, 40));
        timetable.setToolTipText("Manage the timetable and scheduling of your school here.");
        JButton admin = mainGUI.addButtonToFrame("Configure Homeroom", 300, 400, 0, 350);
        admin.setText("<html>" + "Configure " + "<br>" + "Homeroom" + "</html>");
        admin.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.BOLD, 40));
        admin.setToolTipText("Configure Homeroom here.");
        JButton forms = mainGUI.addButtonToFrame("Manage Forms", 300, 400, 400, 350);
        forms.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.BOLD, 40));
        forms.setToolTipText("Manage Form Groups here.");
        JButton classes = mainGUI.addButtonToFrame("My Classes", 300, 400, 800, 350);
        classes.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.BOLD, 40));
        classes.setToolTipText("View the classes you teach here.");
        JButton exit = mainGUI.addButtonToFrame("Log Out and Exit", 50, 150, 1050, 0);
        exit.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.PLAIN, 16));
        exit.setToolTipText("Logout and Exit Homeroom.");
        JLabel usernameField = mainGUI.addLabelToFrame("Username: " + username, 0, 0, 300, 30, false);
        usernameField.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.PLAIN, 25));
        JLabel permissionField = mainGUI.addLabelToFrame("Permissions: ", 300, 0, 300, 30, false);
        switch(new DBUtils(username, password).getPermission()) { //Should be a better way of converting permissions. Is it worth just using an Enum here, like I've done with search type?
            case 1: //Using ENUMS would mean I can just directly get the enum and convert it to a string instead of working on a switch statement to handle showing permissions.
                permissionField.setText("Permissions: USER");
                break;
            case 2:
                permissionField.setText("Permissions: ADMIN");
                break;
            default:
                break;
        }
        permissionField.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.PLAIN, 25));
        exit.addActionListener(e -> {
            System.out.println("Logging out of Homeroom and exiting!");
            System.exit(0);
        });
        manageStudents.addActionListener(e -> {
            openManageStudentsGUI(username, password);
            // Open manage students
        });

        //TODO
        // BUTTON LISTENERS FOR EVERY BUTTON.
    }

    private void openManageStudentsGUI(String username, String password) {
        Student searchClass = new Student(username, password);
        GUIUtils gui = new GUIUtils("Manage Students | Homeroom", 1000, 1220, 300, 0, false);
        JButton addStudent = gui.addButtonToFrame("New Student", 60, 200, 0,0);
        addStudent.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        gui.addLabelToFrame("Search:", 200, 10, 100, 25, true, 25);
        JTextField searchField = gui.addTextField(300, 12, 200, 25, "Enter a term to search for here!");
        PromptSupport.setPrompt("Search", searchField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, searchField);
        JButton searchButton = gui.addButtonToFrame("Search", 40, 100, 500, 0);
        searchButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        searchButton.addActionListener(e -> {
            List<Student> searches = searchClass.searchForStudent(searchField.getText());
            System.out.println("Working on searching using: " + searchField.getText());
            if(searches == null) {
                JOptionPane.showMessageDialog(gui.getFrame(), "No results found that fit your search query!", "Homeroom | Student Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
                System.out.println("No results seem to have been found!");
                return;
            }
            System.out.println(searches.size() + " results found!");
            showSearchResults(searches, gui, new DBUtils(username, password).getPermission(), username, password);
        });
        JButton exitButton = gui.addButtonToFrame("Exit Student Management", 60, 300, 920, 0);
        gui.addLabelToFrame("Search Type: ", 630, 10, 170, 30, true, 25);
        JComboBox searchChoice = gui.addComboBox(800, 13, 100, 25, new String[]{"Student ID", "Student Name", "Student DOB", "Student Address", "Student Phone"});
        searchClass.setStudentSearchType(StudentSearchType.UUID); //Ensures that default search type actually applies.
        searchChoice.addActionListener(e -> {
            JComboBox box = (JComboBox) e.getSource();
            int selected = box.getSelectedIndex();
            switch (selected) {
                case 0:
                    searchClass.setStudentSearchType(StudentSearchType.UUID);
                    System.out.println("Search type has been set to UUID!");
                    break;
                case 1:
                    searchClass.setStudentSearchType(StudentSearchType.NAME);
                    System.out.println("Search type has been set to Name!");
                    break;
                case 2:
                    searchClass.setStudentSearchType(StudentSearchType.DOB);
                    System.out.println("Search Type has been set to DOB!");
                    break;
                case 3:
                    searchClass.setStudentSearchType(StudentSearchType.ADDRESS);
                    System.out.println("Search Type has been set to Address!");
                    break;
                case 4:
                    searchClass.setStudentSearchType(StudentSearchType.PHONE);
                    System.out.println("Search Type has been set to Phone Number!");
                    break;
                default:
                    searchClass.setStudentSearchType(StudentSearchType.NAME);
                    System.out.println("Search Type has been set to Student Name!");
                    break;
            }
        });
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                    List<Student> searches = searchClass.searchForStudent(searchField.getText());
                    System.out.println("Working on searching using: " + searchField.getText());
                    if(searches == null) {
                        JOptionPane.showMessageDialog(gui.getFrame(), "No results found that fit your search query!", "Homeroom | Student Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
                        System.out.println("No results seem to have been found!");
                        return;
                    }
                    System.out.println(searches.size() + " results found!");
                    showSearchResults(searches, gui, new DBUtils(username, password).getPermission(), username, password);
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        exitButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        exitButton.addActionListener(e -> {
            gui.closeFrame();
            return;
        });
    }

    private static List<JButton> searchButtons;

    private static void setSearchButtons(List<JButton> buttons) {
        searchButtons = buttons;
    }

    /**
     * A method used to display a certain amount of students within the main Student Management GUI.
     * This method also opens up an option pane which holds a {@link JProgressBar} which is able to display progress of the search.
     * This method, using basic logic and some addition, will also be able to add "pages" to the GUI, which will allow users to cycle
     * through their search results when needed. <p></p>
     * This method is located within the Main class as it will only be used within this one singular instance. I also could not find a class that fit the use-case of this method.
     * @param searchResults The List of student found within the database query.
     * @param gui The {@link JFrame} in the questions
     */
    private void showSearchResults(List<Student> searchResults, GUIUtils gui, int permission, String username, String password) {
        int xLoc = 0;
        int yLoc = 100;
        if(searchButtons != null) {
            for (JButton i : searchButtons) {
                i.setVisible(false);
            }
        }
        List<JButton> buttons = new ArrayList<>();
        for(Student x : searchResults) {
            JButton option = gui.addButtonToFrame(x.getStudentName(), 50, 150, xLoc, yLoc);
            option.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
            option.addActionListener(e -> viewStudentInformation(x, permission, username, password));
            buttons.add(option);
            xLoc = xLoc + 150;
            if(xLoc > 1050) {
                yLoc = yLoc + 50;
                xLoc = 0;
            }
            //TODO IF yLoc is LARGER THAN 1000, THEN NEXT PAGE SHOULD BE CREATED.
            // FIND A WAY OF MAKING PAGINATION WORK
            System.out.println(searchResults.indexOf(x) + " out of " + searchResults.size());
        }
        setSearchButtons(buttons);
    }

    /**
     * Quick method that allows users to view information about a student. This GUI also allows users to view, edit and delete student information depending on the level of permissions the user has.
     * This permissions level will need to be passed through using parameters within the method. <p></p>
     * This method seems far too hard-coded to me, and is definitely not a method that is up to standard. Too many operations, that need to be
     * reduced using loops and switch statements where possible.
     * @param student The student in question, the student for which information should be displayed.
     * @param permission The level of permission that the user is viewing the information from. Dictates whether student information can be edited or deleted.
     */
    private void viewStudentInformation(Student student, int permission, String username, String password) {
        GUIUtils gui = new GUIUtils("Student Management | View Student", 1000, 1700, 0, 0, false);
        gui.addLabelToFrame("Student Name", 100, 0, 150, 25, false, 18);
        JTextField nameField = gui.addTextField(100, 30, 120, 30, "Name of the Student");
        PromptSupport.setPrompt("Student Name", nameField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, nameField);
        nameField.setText(student.getStudentName());
        nameField.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
        gui.addLabelToFrame("Date of Birth", 275, 0, 150, 25, false, 18);
        JTextField dateField = gui.addTextField(275, 30, 100, 30, "Date of Birth of the Student");
        dateField.setEditable(false);
        dateField.setText(student.getStudentDOB());
        dateField.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
        DatePicker dp = gui.addDatePicker(310, 30, 100, 30, "<html>" + "Select " +  "<br>" +  "Date" + "</html>", "Click here to select the date of birth for a student.");
        dp.setDate(LocalDate.parse(student.getStudentDOB(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        dp.addDateChangeListener(event -> {
            String month = String.valueOf(event.getNewDate().getMonthValue());
            String day = String.valueOf(event.getNewDate().getDayOfMonth());
            if(month.length() != 2) { month = "0" + month; }
            if(day.length() != 2) day = "0" + day;
            String convertedDate = month + "/" + day + "/" + event.getNewDate().getYear();
            dateField.setText(convertedDate);
        });
        gui.addLabelToFrame("Student Address", 450, 0, 150, 25, false, 18);
        JTextArea addressField = gui.addTextArea(450, 30, 200, 75, "Enter the address of the student here");
        addressField.setText(student.getStudentAddress());
        PromptSupport.setPrompt("Student Address", addressField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, addressField);
        gui.addLabelToFrame("Student Phone", 675, 0, 150, 25, false, 18);
        JTextField phoneField = gui.addTextField(675, 25, 150, 25, "Enter the students phone number here!");
        PromptSupport.setPrompt("Phone Number", phoneField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, phoneField);
        phoneField.setText(student.getStudentPhone());
        phoneField.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
        gui.addLabelToFrame("Student Medical", 850, 0, 150, 25, false, 18);
        JTextArea medicField = gui.addTextArea(850, 30, 200, 75, "Enter student medical information here!");
        PromptSupport.setPrompt("Student Medical", medicField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, medicField);
        medicField.setText(student.getStudentMedical());
        gui.addLabelToFrame("Guardian Phone", 1075, 0, 150, 25, false, 18);
        JTextField guardianPhone = gui.addTextField(1075, 30, 150, 25, "Enter the student's guardian's phone number.");
        guardianPhone.setText(student.getGuardianPhone());
        guardianPhone.setFont(new Font(gui.getFrame().getName(), Font.PLAIN, 15));
        PromptSupport.setPrompt("Guardian Phone", guardianPhone);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, guardianPhone);
        gui.addLabelToFrame("Guardian Address", 100, 75, 150, 25, false, 18);
        JTextArea guardianAddress = gui.addTextArea(100, 100, 200, 75, "Enter the student's guardian's address.");
        guardianAddress.setText(student.getGuardianAddress());
        gui.addLabelToFrame("Guardian Name", 450, 105, 200, 25, false, 18);
        JTextField guardianName = gui.addTextField(450, 130, 150, 25, "Enter the student's guardian's name.");
        guardianName.setText(student.getGuardianName());
        guardianName.setFont(new Font(gui.getFrame().getName(), Font.PLAIN, 15));
        //TODO "DELETE STUDENT" BUTTON TO BE ADDED
        JTextComponent[] entryFields = {nameField, dateField, addressField, phoneField, guardianPhone, guardianAddress, guardianName, medicField};
        if(permission != 2) {
            dp.setVisible(false);
            //TODO REMOVE DELETE STUDENT BUTTON HERE.
            for(JTextComponent x : entryFields) {
                x.setEditable(false);
            }
        }
        setCallAgain(true);
        for(JTextComponent x : entryFields) {
            x.getDocument().addDocumentListener(new DocumentListener() {
                @Override //When a character is inserted into the field, this event will be fired.
                public void insertUpdate(DocumentEvent e) {
                    if(getCallAgain()) {
                        displayEditedButtons(gui, student, entryFields, username, password);
                    }
                    setCallAgain(false);
                }

                @Override //When a character is removed from the field, this event will be fired.
                public void removeUpdate(DocumentEvent e) {
                    if(getCallAgain()) {
                        displayEditedButtons(gui, student, entryFields, username, password);
                    }
                    setCallAgain(false);
                }

                @Override //When the document is changed, this event will be fired
                public void changedUpdate(DocumentEvent e) {
                    if(getCallAgain()) {
                        displayEditedButtons(gui, student, entryFields, username, password);
                    }
                    setCallAgain(false);
                }
            });
        }
    }

    private static boolean callAgain;
    private static boolean getCallAgain() {
        return callAgain;
    }
    private static void setCallAgain(boolean callAgain1) {
        callAgain = callAgain1;
    }

    private static boolean closeShow;
    private static boolean getCloseShow() { return closeShow; }
    private static void setCloseShow(boolean closeShow1) {
        closeShow = closeShow1;
    }

    /**
     * Method that handles the displaying of special buttons that will only be shown to administrative users to ensure that they have a way of saving, discarding and reverting their changes.
     * @param gui The GUI in question to display these buttons on.
     * @param student The Student that you are viewing and editing the information for.
     * @param fields The input fields of data that you are working with.
     * @param username The username used to log into Homeroom.
     * @param password The password used to log into Homeroom.
     */
    private void displayEditedButtons(GUIUtils gui, Student student, JTextComponent[] fields, String username, String password) {
        setCloseShow(true);
        JFrame frame = (JFrame) gui.getFrame();
        JButton saveEdit = gui.addButtonToFrame("Save Edits", 50, 200, 500, 600);
        JButton revertChanges = gui.addButtonToFrame("Revert Changes", 50, 200, 700, 600);
        JButton discardChangesExit = gui.addButtonToFrame("Discard Changes and Exit", 50, 200, 100, 600);
        JButton saveChangesExit = gui.addButtonToFrame("Save Changes and Exit", 50, 200, 300, 600);
        JButton[] buttons = {saveEdit, revertChanges, discardChangesExit, saveChangesExit};
        Student update = new Student(username, password);
        String updatedName = fields[0].getText();
        String updatedDate = fields[1].getText();
        String updatedAddress = fields[2].getText();
        String updatedPhone = fields[3].getText();
        String updatedGuardianPhone = fields[4].getText();
        String updatedGuardianAddress = fields[5].getText();
        String updatedGuardianName = fields[6].getText();
        String updatedMedic = fields[7].getText(); //TODO, use the #getName() and #setName() methods with the SearchType enums to make up a way of looping through and getting each field.
        //TODO You could use their string values, and then the valueOf method to implement this logic!
        saveEdit.addActionListener(e -> {
            update.updateStudent(student, "StudentName", updatedName);
            update.updateStudent(student, "StudentDOB", updatedDate);
            update.updateStudent(student, "StudentAddress", updatedAddress);
            update.updateStudent(student, "StudentPhone", updatedPhone);
            update.updateStudent(student, "GuardianPhone", updatedGuardianPhone);
            update.updateStudent(student, "GuardianAddress", updatedGuardianAddress);
            update.updateStudent(student, "GuardianName", updatedGuardianName);
            update.updateStudent(student, "StudentMedical", updatedMedic);
            for(JTextComponent x : fields) {
                System.out.println(x.getText());
            }
            System.out.println("Edit saved button clicked!");
            for(JButton x : buttons) {
                x.setVisible(false);
            }
            setCloseShow(false);
        });
        revertChanges.addActionListener(e -> {
            for(JTextComponent x : fields) {
                x.setText("");
            }
            fields[0].setText(student.getStudentName());
            fields[1].setText(student.getStudentDOB());
            fields[2].setText(student.getStudentAddress());
            fields[3].setText(student.getStudentPhone());
            fields[4].setText(student.getGuardianPhone());
            fields[5].setText(student.getGuardianAddress());
            fields[6].setText(student.getGuardianName());
            fields[7].setText(student.getStudentMedical());
            setCloseShow(false);
            for(JButton x : buttons) {
                x.setVisible(false);
            }
        });
        discardChangesExit.addActionListener(e -> {
            setCloseShow(false);
            frame.dispose();
        });
        saveChangesExit.addActionListener(e -> {
            update.updateStudent(student, "StudentName", updatedName);
            update.updateStudent(student, "StudentDOB", updatedDate);
            update.updateStudent(student, "StudentAddress", updatedAddress);
            update.updateStudent(student, "StudentPhone", updatedPhone);
            update.updateStudent(student, "GuardianPhone", updatedGuardianPhone);
            update.updateStudent(student, "GuardianAddress", updatedGuardianAddress);
            update.updateStudent(student, "GuardianName", updatedGuardianName);
            update.updateStudent(student, "StudentMedical", updatedMedic);
            System.out.println("Fields should have successfully been updated. Exiting!");
            setCloseShow(false);
            frame.dispose();
        });
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }
            @Override
            public void windowClosing(WindowEvent e) {
                if(getCloseShow()) {
                    Object[] options = {"Save Edits and Exit", "Discard Changes and Exit"};
                    int saveOption = JOptionPane.showOptionDialog(frame, "Would you like to save your changes and exit?", "Homeroom | Exiting Student Management", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, UIManager.getIcon("OptionPane.warningIcon"), options, "Test");
                    switch (saveOption) {
                        case 0:
                            update.updateStudent(student, "StudentName", updatedName);
                            update.updateStudent(student, "StudentDOB", updatedDate);
                            update.updateStudent(student, "StudentAddress", updatedAddress);
                            update.updateStudent(student, "StudentPhone", updatedPhone);
                            update.updateStudent(student, "GuardianPhone", updatedGuardianPhone);
                            update.updateStudent(student, "GuardianAddress", updatedGuardianAddress);
                            update.updateStudent(student, "GuardianName", updatedGuardianName);
                            update.updateStudent(student, "StudentMedical", updatedMedic);
                            System.out.println("Fields should have successfully been updated. Exiting!");
                            frame.setVisible(false);
                            break;
                        case 1:
                            frame.setVisible(false);
                            System.out.println("Discarding changes and exiting!");
                    }
                }
            }
            @Override
            public void windowClosed(WindowEvent e) {
            }
            @Override
            public void windowIconified(WindowEvent e) {
            }
            @Override
            public void windowDeiconified(WindowEvent e) {
            }
            @Override
            public void windowActivated(WindowEvent e) {
            }
            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    /**
     * This method is used to write the file into the Homeroom folder for use by the program.
     * If file exists in the following path, then there's no need to do anything.
     * If it doesn't exist, create it.
     * Handles {@link javax.print.URIException} catching due to the call made at #createNewFile()
     * @return {@link File} object which should be a valid configuration file.
     */
    public static File getConfigFile() {
        String filePath;
        try {
            filePath = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        File file = new File(filePath + "/config.json");
        if(file.exists()) {
            return file;
        } //Self-explanatory code. Not sure how well this would work
        InputStream fileInput = Main.class.getResourceAsStream("/config.json"); //This should get the contents of the file
        try {
            file.createNewFile();
            FileOutputStream fileOutput = new FileOutputStream(file);
            int i;
            byte[] bytes = new byte[1024];
            while((i = fileInput.read(bytes)) != -1 && fileInput != null) {
                fileOutput.write(bytes, 0, i);
            }
            System.out.println("System should have written the file contents to the new file!");
            return file;
        } catch (IOException e) {
            System.out.println("Please take a look at the following stack trace!");
            e.printStackTrace();
            return null;
        }
    }
}