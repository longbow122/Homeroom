package me.longbow122.Homeroom.features;

import com.github.lgooddatepicker.components.DatePicker;
import me.longbow122.Homeroom.Student;
import me.longbow122.Homeroom.StudentSearchType;
import me.longbow122.Homeroom.utils.DBUtils;
import me.longbow122.Homeroom.utils.GUIUtils;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class that holds the base code for the Student Management feature. Holds all the fields, methods and logic needed to allow Student Management to run as needed.
 *
 * This class should be reworked as much as possible to ensure that the logic behind it and the general code quality works as good as possible.
 *
 * Needs a great amount of improvement.
 */
public class StudentManagement {

    /**
     * Method which forces the thread to wait for an action to occur before handling anything else. <p></p>
     * It is worth noting that a method of this nature should really be written in the {@link Student} class, but due to the higher amount of GUI-based lines it holds, I opted to keep it within this class.
     * @param username The username used to log into 'Homeroom' and the one used to query the database.
     * @param password The password used to log into 'Homeroom' and the one used to query the database.
     * @return {@link Student} object that represents the selected Student.
     */
    public Student studentSelection(String username, String password) {
        Student searchClass = new Student(username, password);
        GUIUtils gui = new GUIUtils("Search and Select Students | Homeroom", 1000, 1220 ,300, 0, false);
        gui.addLabelToFrame("Search:", 200, 10, 100, 25, true, 25);
        JTextField searchField = gui.addTextField(300, 12, 200, 25, "Enter a term to search for here!");
        PromptSupport.setPrompt("Search", searchField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, searchField);
        JButton searchButton = gui.addButtonToFrame("Search", 40, 100, 500, 0);
        searchButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        searchButton.addActionListener(e -> {
            handleStudentReturnSearch(searchClass, searchField.getText(), gui);
        });
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
                    handleStudentReturnSearch(searchClass, searchField.getText(), gui);
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        return studentSelected;
        //TODO TEST
    }

    /**
     * Convenience method to handle the searching and returning of a {@link Student}.
     * @param searchClass Student class to handle searching of a Student
     * @param searchText Text to begin querying the database with.
     * @param gui The parent GUI of which said buttons should be displayed on.
     * @return {@link Student} object represented the selected Student.
     */
    private Student handleStudentReturnSearch(Student searchClass, String searchText, GUIUtils gui) {
        List<Student> searches = searchClass.searchForStudent(searchText);
        System.out.println("Working on searching using: " + searchText);
        if(searches == null) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No results found that fit your search query!", "Homeroom | Student Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
            System.out.println("No results seem to have been found!");
            return null;
        }
        System.out.println(searches.size() + " results found!");
        return selectStudent(searches, gui);
    }

    /**
     * A basic method used to process the search logic for the main student GUI. Used to display students which can be selected and returned.
     * @param searchClass An instance of {@link Student} used to handle searching. Passed through to save resources and avoid instantiating a new object.
     * @param username Username used to query the database
     * @param password Password used to query the database
     * @param searchText The search parameter for the search text to be processed
     * @param gui The parent GUI to display the buttons
     */
    private void processSearches(Student searchClass, String username, String password, String searchText, GUIUtils gui) {
        List<Student> searches = searchClass.searchForStudent(searchText);
        System.out.println("Working on searching using: " + searchText);
        if(searches == null) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No results found that fit your search query!", "Homeroom | Student Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
            System.out.println("No results seem to have been found!");
            return;
        }
        System.out.println(searches.size() + " results found!");
        showSearchResults(searches, gui, new DBUtils(username, password).getPermission(), username, password);
    }

    public void openManageStudentsGUI(String username, String password) {
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
        searchButton.addActionListener(e -> processSearches(searchClass, username, password, searchField.getText(), gui));
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
                    processSearches(searchClass, username, password, searchField.getText(), gui);
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
        });
        addStudent.addActionListener(e -> {
            GUIUtils add = new GUIUtils("Student Management | Add Student", 1000, 1000, 0, 0, false);
            if(new DBUtils(username, password).getPermission() != 2) {
                JOptionPane.showMessageDialog(gui.getFrame(),"You are not able to make use of this feature as you are not an administrator!", "Insufficient Permissions!", JOptionPane.ERROR_MESSAGE);
                add.closeFrame();
                return;
            } //Hide the add student button to ensure that only admins can make use of it.
            JLabel nameLabel = add.addLabelToFrame("Student Name:", 25, 30, 150, 20, false);
            nameLabel.setFont(new Font(add.getFrame().getFont().getName(), Font.BOLD, 20));
            nameLabel.setForeground(Color.RED);
            JTextField studentName = add.addTextField(25, 70, 200, 25, "Enter the name of the Student here! REQUIRED FIELD");
            JLabel phoneLabel = add.addLabelToFrame("Student Phone:", 250, 30, 200, 20, false);
            phoneLabel.setFont(new Font(add.getFrame().getFont().getName(), Font.BOLD, 20));
            phoneLabel.setForeground(Color.RED);
            JTextField studentPhone = add.addTextField(250, 70, 200, 25, "Enter the phone number of the Student here! REQUIRED FIELD");
            JLabel dobLabel = add.addLabelToFrame("Date of Birth:", 475, 30, 150, 20, false);
            dobLabel.setFont(new Font(add.getFrame().getFont().getName(), Font.BOLD, 20));
            dobLabel.setForeground(Color.RED);
            JTextField DOB = add.addTextField(475, 70, 150, 25, "Enter the DOB of the student here!");
            DOB.setEditable(false);
            DatePicker dp = add.addDatePicker(560, 70, 100, 30, "<html>" + "Select " +  "<br>" +  "Date" + "</html>", "Click here to select the date of birth for a student.");
            dp.addDateChangeListener(event -> {
                String month = String.valueOf(event.getNewDate().getMonthValue());
                String day = String.valueOf(event.getNewDate().getDayOfMonth());
                if(month.length() != 2) { month = "0" + month; }
                if(day.length() != 2) day = "0" + day;
                String convertedDate = month + "/" + day + "/" + event.getNewDate().getYear();
                DOB.setText(convertedDate);
            });
            JLabel studentAddressLabel = add.addLabelToFrame("Student Address:", 25, 100, 200, 20,false);
            studentAddressLabel.setFont(new Font(add.getFrame().getFont().getName(), Font.BOLD, 20));
            studentAddressLabel.setForeground(Color.RED);
            JTextArea studentAddress = add.addTextArea(25, 130, 200, 75, "Enter the address of the Student here!");
            JLabel studentMedicalLabel = add.addLabelToFrame("Student Medical:", 250, 100, 200, 20, false);
            studentMedicalLabel.setFont(new Font(add.getFrame().getFont().getName(), Font.BOLD, 20));
            JTextArea studentMedical = add.addTextArea(250, 130, 200, 75,"Enter the medical information of the Student here!");
            JLabel guardianNameLabel = add.addLabelToFrame("Guardian Name:", 475, 100,200, 20,false);
            guardianNameLabel.setFont(new Font(add.getFrame().getFont().getName(), Font.BOLD, 20));
            guardianNameLabel.setForeground(Color.RED);
            JTextField guardianName = add.addTextField(475, 130, 150, 25, "Enter the student's guardians name here! REQUIRED FIELD");
            JLabel guardianPhoneLabel = add.addLabelToFrame("Guardian Phone:", 475, 160, 200, 20, false);
            guardianPhoneLabel.setFont(new Font(add.getFrame().getFont().getName(), Font.BOLD, 20));
            guardianPhoneLabel.setForeground(Color.RED);
            JTextField guardianPhone = add.addTextField(475, 180, 150, 25, "Enter the guardian's phone number here! REQUIRED FIELD!");
            JLabel guardianAddressLabel = add.addLabelToFrame("Guardian Address:", 25, 210, 200, 20, false);
            guardianAddressLabel.setFont(new Font(add.getFrame().getFont().getName(), Font.BOLD, 20));
            guardianAddressLabel.setForeground(Color.RED);
            JTextArea guardianAddress = add.addTextArea(25, 240, 150, 75, "Enter the student's guardians address here!");
            JTextComponent[] requiredFields = {studentName, studentPhone, DOB, studentAddress, guardianName, guardianPhone, guardianAddress};
            JButton submitData = add.addButtonToFrame("Add Student", 40, 100, 200, 240);
            JButton cancelData = add.addButtonToFrame("Cancel", 40, 100, 300, 240);
            cancelData.addActionListener(e1 -> {
                add.closeFrame();
                System.out.println("Adding student process cancelled!");
            });
            submitData.addActionListener(e12 -> {
                for(JTextComponent x : requiredFields) {
                    if(x.getText().equals("")) {
                        Object[] options = {"Cancel", "Continue Adding Data"};
                        int selected = JOptionPane.showOptionDialog(add.getFrame(), "One of the required fields is missing data! Please enter data for the red-marked fields!", "Issue Entering Data!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"), options, "Test");
                        switch(selected) {
                            case 0:
                                add.closeFrame();
                                return;
                            case 1:
                                return;
                        }
                    }
                }
                String[] phoneNumbers = {studentPhone.getText(), guardianPhone.getText()};
                for(String x : phoneNumbers) {
                    for(Character ch : x.toCharArray()) {
                        int ascii = ch;
                        if((isWithinRangeOf(65, 90, ascii) || isWithinRangeOf(97, 122, ascii)) || (ascii == 33 && ascii == 32)) {
                            Object[] options = {"Cancel", "Continue Editing Data"};
                            int selected = JOptionPane.showOptionDialog(add.getFrame(), "A phone number does not seem to have the right input! Please enter a valid phone number using country codes or numerical characters!", "Issue Entering Data!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"), options, "Test");
                            switch(selected) {
                                case 0:
                                    add.closeFrame();
                                    return;
                                case 1:
                                    return;
                            }
                        }
                    }
                }
                Student added = new Student(username, password).addStudentToDB(studentName.getText(), DOB.getText(), studentAddress.getText(), studentPhone.getText(), studentMedical.getText(), guardianName.getText(), guardianAddress.getText(), guardianPhone.getText());
                processSearches(searchClass, username, password, searchField.getText(), gui);
                JOptionPane.showMessageDialog(add.getFrame(), "The student " + added.getStudentName() + " was successfully added to the database of Students!", "Student Successfully Added!", JOptionPane.INFORMATION_MESSAGE);
                add.closeFrame();
                return;
            });
        });
    }

    /**
     * A basic method to check whether a number is within the range of two number.
     * @param one The lower bound of the two numbers to check.
     * @param two The upper bound of the two numbers to check.
     * @param check The number to check for whether it lies in the range.
     * @return Boolean representing the validity of the claim. Does the checking number lie within the range?
     */
    private boolean isWithinRangeOf(int one, int two, int check) {
        return check >= one && check <= two;
    }

    private List<List<JButton>> searchButtonsPages;

    private int searchButtonIndex;

    private void setSearchButtons(List<List<JButton>> buttons) {
        searchButtonsPages = buttons;
    }

    /**
     * Method which emulates the "shifting" of pages, all current buttons are made invisible, and the new pages are made visible.
     * <p></p>
     * Hard-coded numerical values are used for the shift amount as there is a specified amount of shifts that can be made per button press.
     * @param currentIndex The current numbered index that the page is currently on.
     * @param shiftAmount The amount to shift the index by. The index number should be positive if you want to move up a page and index number should be negative if you want to move down a page.
     */
    private void shiftPage(int currentIndex, int shiftAmount, GUIUtils gui) {
        if(shiftAmount < 0) {
            System.out.println("The shift amount is negative! Subtraction should occur!");
            if(currentIndex - 1 < 0) {
                System.out.println("After subtraction, index would be negative and as such, no more pages!");
                JOptionPane.showMessageDialog(gui.getFrame(),"There are no more pages for you to move through!");
                return;
            }
            searchButtonIndex = searchButtonIndex - 1;
        } else {
            System.out.println("The shift amount is positive? Should be incrementing!");
            if(currentIndex + shiftAmount >= searchButtonsPages.size()) {
                System.out.println("Out of index for that addition!");
                JOptionPane.showMessageDialog(gui.getFrame(), "There are no more pages for you to move through!");
                return;
            }
            searchButtonIndex++;
        }
        for(JButton i : searchButtonsPages.get(currentIndex)) {
            i.setVisible(false);
            System.out.println(i.getText());
        }
        System.out.println("Old pages should be invisible.");
        System.out.println("Current index: " + currentIndex);
        System.out.println("New index: " + searchButtonIndex);
        for(JButton x : searchButtonsPages.get(searchButtonIndex)) {
            x.setVisible(true);
            System.out.println(x.getText());
        }
        System.out.println("New page should be visible.");
    }



    private Student studentSelected;

    private void setStudentSelected(Student x) {
        studentSelected = x;
    }

    private Student getStudentSelected() {
        return studentSelected;
    }
    private final Object lock = new Object();

    /**
     * Method used to return a {@link Student} object which the rest of the program is able to use. This method provides the same functionality as the Student Management GUI does, except this method should, in theory provide a returned object upon clicking of the button. <p></p>
     * WARNING: THIS METHOD MAKES USE OF THE SYNCHRONIZE KEYWORD, AND WILL WAIT ON THE THREAD FOR A RESPONSE FROM THE USER BEFORE CONTINUING. THIS WILL LIKELY HOLD UP THE ENTIRE PROGRAM UNTIL INPUT IS RECIEVED. <P></P>
     * Care should be taken to ensure that this does not affect the rest of the program.
     * @param searchResults The List of Students returned from the search provided by the user.
     * @param gui The parent GUI to insert buttons into. This GUI should be under a controlled environment, through a controlled size, etc.
     * @return {@link Student} object representing the student that was selected by the user.
     */
    private Student selectStudent(List<Student> searchResults, GUIUtils gui) {
        int xLoc = 0;
        int yLoc = 100;
        if(searchButtonsPages != null) {
            for(List<JButton> x : searchButtonsPages) {
                for(JButton i : x) {
                    i.setVisible(false);
                }
            }
        }
        List<List<JButton>> pages = new ArrayList<>();
        List<JButton> buttons = new ArrayList<>();
        for(Student x : searchResults) {
            JButton option = gui.addButtonToFrame(x.getStudentName(), 50, 150, xLoc, yLoc);
            option.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
            option.addActionListener(e -> {
                setStudentSelected(x);//TODO THIS NEEDS TO RETURN THE STUDENT OBJECT
                gui.closeFrame(); //Close the window afterwards, selection is all done now
                synchronized (lock) {
                    lock.notifyAll();
                }
            });
            buttons.add(option);
            System.out.println("A button was added to the list of buttons!");
            option.setVisible(false);
            xLoc = xLoc + 150;
            if(xLoc > 1050) {
                yLoc = yLoc + 50;
                xLoc = 0;
            }
            if(yLoc > 930) {
                pages.add(buttons); //Add it to the list of lists to ensure you know what goes within each page
                System.out.println("A list of buttons has been added to the page list!");
                buttons = new ArrayList<JButton>();
                yLoc = 100; //Reset to y= 100
            }
            System.out.println(searchResults.indexOf(x) + " out of " + searchResults.size());
        }
        pages.add(buttons); //Add it to the list of lists to ensure you know what goes within each page
        JButton nextPage = gui.addButtonToFrame(">>", 30, 60, 980, 65);
        JButton backPage = gui.addButtonToFrame("<<", 30, 60, 920, 65);
        nextPage.setToolTipText("Go to the next page of search results.");
        backPage.setToolTipText("Go to the previous page of search results.");
        if(!pages.isEmpty()) {
            List<JButton> firstPage = pages.get(0);
            for(JButton i : firstPage) {
                i.setVisible(true);
            }
        } else {
            for(JButton x : buttons) {
                x.setVisible(true);
            }
        }
        searchButtonIndex = 0;
        setSearchButtons(pages);
        nextPage.addActionListener(e -> {
            System.out.println("Current index: " + searchButtonIndex);
            if(pages.isEmpty()) {
                JOptionPane.showMessageDialog(gui.getFrame(), "There are no further pages for you to move between!");
                System.out.println("Pages list is empty!");
                return;
            }
            shiftPage(searchButtonIndex, 1, gui); //Go forward one page
        });
        backPage.addActionListener(e -> {
            System.out.println("Current index: " + searchButtonIndex);
            if(pages.isEmpty()) {
                JOptionPane.showMessageDialog(gui.getFrame(), "There are no further pages for you to move between!");
                System.out.println("Page is empty!");
                return;
            }
            shiftPage(searchButtonIndex, -1, gui); //Go back one page
        });
        synchronized (lock) {
            try {
                System.out.println("Thread is about to be locked!");
                lock.wait(); //TODO BLOCKS EVERYTHING, INCLUDING THE BUTTONS BEING CREATED. THIS IS OKAY TO HAVE, PROVIDED ACTIONS CAN BE PERFORMED THROUGH THE BUTTON
                System.out.println("Thread should have now been locked!");
            } catch (InterruptedException e) {
                System.out.println("An exception was caught! Look at the below stack trace!");
                e.printStackTrace();
            }
        }
        return getStudentSelected();
    }

    /**
     * A method used to display a certain amount of students within the main Student Management GUI.
     * This method also opens up an option pane which holds a {@link JProgressBar} which is able to display progress of the search.
     * This method, using basic logic and some addition, will also be able to add "pages" to the GUI, which will allow users to cycle
     * through their search results when needed.
     * @param searchResults The List of student found within the database query.
     * @param gui The {@link JFrame} in the questions
     * @param permission The level of permissions that the user holds. Passed through the login system.
     * @param username The username used to log into the 'Homeroom' database.
     * @param password The password used to log into the 'Homeroom' database.
     */
    private void showSearchResults(List<Student> searchResults, GUIUtils gui, int permission, String username, String password) {
        int xLoc = 0;
        int yLoc = 100;
        if(searchButtonsPages != null) {
            for(List<JButton> x : searchButtonsPages) {
                for(JButton i : x) {
                    i.setVisible(false);
                }
            }
        }
        List<List<JButton>> pages = new ArrayList<>();
        List<JButton> buttons = new ArrayList<>();
        for(Student x : searchResults) {
            JButton option = gui.addButtonToFrame(x.getStudentName(), 50, 150, xLoc, yLoc);
            option.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
            option.addActionListener(e -> viewStudentInformation(x, permission, username, password, gui, 0));
            buttons.add(option);
            System.out.println("A button was added to the list of buttons!");
            option.setVisible(false);
            xLoc = xLoc + 150;
            if(xLoc > 1050) {
                yLoc = yLoc + 50;
                xLoc = 0;
            }
            if(yLoc > 930) {
                pages.add(buttons); //Add it to the list of lists to ensure you know what goes within each page
                System.out.println("A list of buttons has been added to the page list!");
                buttons = new ArrayList<JButton>();
                yLoc = 100; //Reset to y= 100
            }
            System.out.println(searchResults.indexOf(x) + " out of " + searchResults.size());
        }
        pages.add(buttons); //Add it to the list of lists to ensure you know what goes within each page
        JButton nextPage = gui.addButtonToFrame(">>", 30, 60, 980, 65);
        JButton backPage = gui.addButtonToFrame("<<", 30, 60, 920, 65);
        nextPage.setToolTipText("Go to the next page of search results.");
        backPage.setToolTipText("Go to the previous page of search results.");
        if(!pages.isEmpty()) {
            List<JButton> firstPage = pages.get(0);
            for(JButton i : firstPage) {
                i.setVisible(true);
            }
        } else {
            for(JButton x : buttons) {
                x.setVisible(true);
            }
        }
        searchButtonIndex = 0;
        setSearchButtons(pages);
        nextPage.addActionListener(e -> {
            System.out.println("Current index: " + searchButtonIndex);
            if(pages.isEmpty()) {
                JOptionPane.showMessageDialog(gui.getFrame(), "There are no further pages for you to move between!");
                System.out.println("Pages list is empty!");
                return;
            }
            shiftPage(searchButtonIndex, 1, gui); //Go forward one page
        });
        backPage.addActionListener(e -> {
            System.out.println("Current index: " + searchButtonIndex);
            if(pages.isEmpty()) {
                JOptionPane.showMessageDialog(gui.getFrame(), "There are no further pages for you to move between!");
                System.out.println("Page is empty!");
                return;
            }
            shiftPage(searchButtonIndex, -1, gui); //Go back one page
        });
    }

    /**
     * Quick method that allows users to view information about a student. This GUI also allows users to view, edit and delete student information depending on the level of permissions the user has.
     * This permissions level will need to be passed through using parameters within the method. <p></p>
     * This method seems far too hard-coded to me, and is definitely not a method that is up to standard. Too many operations, that need to be
     * reduced using loops and switch statements where possible.
     * @param student The student in question, the student for which information should be displayed.
     * @param permission The level of permission that the user is viewing the information from. Dictates whether student information can be edited or deleted.
     * @param parentGUI The parent GUI to be refreshed and viewed in terms of information.
     * @param option The option to make use of in terms of the GUI to stem from. As multiple GUIs can make use of this same method, there needs to be leeway for this. 0 for Student Management, 1 for Form Management.
     */
    public void viewStudentInformation(Student student, int permission, String username, String password, GUIUtils parentGUI, int option) {
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
        JButton deleteStudent = gui.addButtonToFrame("Delete Student", 50, 100, 100, 200);
        deleteStudent.addActionListener(e -> {
            Object[] options = {"Delete Student", "Cancel"};
            int select = JOptionPane.showOptionDialog(gui.getFrame(), "Please confirm that you wish to delete the information for " + student.getStudentName(), "About to delete " + student.getStudentName() + "!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, UIManager.getIcon("OptionPane.warningIcon"), options, "Test");
            switch (select) {
                case 0:
                    new Student(username, password).deleteStudent(student);
                    JOptionPane.showMessageDialog(gui.getFrame(), student.getStudentName() + " has been removed from the Homeroom database!");
                    gui.closeFrame();
                    parentGUI.closeFrame();
                    switch(option) {
                        case 0:
                            openManageStudentsGUI(username, password);
                            break;
                        case 1:
                            new FormManagement().openManageFormsGUI(username, password);
                            break;
                    }
                    return;
                case 1:
                    break;
            }
        });
        JTextComponent[] entryFields = {nameField, dateField, addressField, phoneField, guardianPhone, guardianAddress, guardianName, medicField};
        if(permission != 2) {
            dp.setVisible(false);
            deleteStudent.setVisible(false);
            for(JTextComponent x : entryFields) {
                x.setEditable(false);
            }
        }
        for(JTextComponent x : entryFields) {
            x.getDocument().addDocumentListener(new DocumentListener() {
                @Override //When a character is inserted into the field, this event will be fired.
                public void insertUpdate(DocumentEvent e) {
                    setEntryFields(new JTextComponent[]{nameField, dateField, addressField, phoneField, guardianPhone, guardianAddress, guardianName, medicField});
                    if(!(getIsEditMade())) {
                        displayEditedButtons(gui, student, username, password);
                    }
                }

                @Override //When a character is removed from the field, this event will be fired.
                public void removeUpdate(DocumentEvent e) {
                    setEntryFields(new JTextComponent[]{nameField, dateField, addressField, phoneField, guardianPhone, guardianAddress, guardianName, medicField});
                    if(!(getIsEditMade())) {
                        displayEditedButtons(gui, student, username, password);
                    }
                }

                @Override //When the document is changed, this event will be fired
                public void changedUpdate(DocumentEvent e) {
                }
            });
        }
    }

    private boolean isEditMade;

    private void setEditMade(boolean edit) {
        isEditMade = edit;
    }

    private boolean getIsEditMade() {
        return isEditMade;
    }

    private JTextComponent[] entryFields;

    private void setEntryFields(JTextComponent[] fields) {
        entryFields = fields;
    }

    private JTextComponent[] getFields() {
        return entryFields;
    }


    /**
     * Method that handles the displaying of special buttons that will only be shown to administrative users to ensure that they have a way of saving, discarding and reverting their changes.
     * @param gui The GUI in question to display these buttons on.
     * @param student The Student that you are viewing and editing the information for.
     * @param username The username used to log into Homeroom.
     * @param password The password used to log into Homeroom.
     */
    private void displayEditedButtons(GUIUtils gui, Student student, String username, String password) {
        setEditMade(true);
        JFrame frame = (JFrame) gui.getFrame();
        JButton saveEdit = gui.addButtonToFrame("Save Edits", 50, 200, 500, 600);
        JButton revertChanges = gui.addButtonToFrame("Revert Changes", 50, 200, 700, 600);
        JButton discardChangesExit = gui.addButtonToFrame("Discard Changes and Exit", 50, 200, 100, 600);
        JButton saveChangesExit = gui.addButtonToFrame("Save Changes and Exit", 50, 200, 300, 600);
        JButton[] buttons = {saveEdit, revertChanges, discardChangesExit, saveChangesExit};
        Student update = new Student(username, password);
        for(JTextComponent x : getFields()) {
            System.out.println(x.getText());
        }
        saveEdit.addActionListener(e -> {
            update.updateStudent(student, "StudentName", getFields()[0].getText());
            update.updateStudent(student, "StudentDOB", getFields()[1].getText());
            update.updateStudent(student, "StudentAddress", getFields()[2].getText());
            update.updateStudent(student, "StudentPhone", getFields()[3].getText());
            update.updateStudent(student, "GuardianPhone", getFields()[4].getText());
            update.updateStudent(student, "GuardianAddress", getFields()[5].getText());
            update.updateStudent(student, "GuardianName", getFields()[6].getText());
            update.updateStudent(student, "StudentMedical", getFields()[7].getText());
            setEditMade(false);
            for(JButton x : buttons) {
                x.setVisible(false);
            }
        });
        revertChanges.addActionListener(e -> {
            for(JTextComponent x : getFields()) {
                x.setText("");
            }
            getFields()[0].setText(student.getStudentName());
            getFields()[1].setText(student.getStudentDOB());
            getFields()[2].setText(student.getStudentAddress());
            getFields()[3].setText(student.getStudentPhone());
            getFields()[4].setText(student.getGuardianPhone());
            getFields()[5].setText(student.getGuardianAddress());
            getFields()[6].setText(student.getGuardianName());
            getFields()[7].setText(student.getStudentMedical());
            setEditMade(false);
            for(JButton x : buttons) {
                x.setVisible(false);
            }
        });
        discardChangesExit.addActionListener(e -> {
            frame.dispose();
            setEditMade(false);
        });
        saveChangesExit.addActionListener(e -> {
            update.updateStudent(student, "StudentName", getFields()[0].getText());
            update.updateStudent(student, "StudentDOB", getFields()[1].getText());
            update.updateStudent(student, "StudentAddress", getFields()[2].getText());
            update.updateStudent(student, "StudentPhone", getFields()[3].getText());
            update.updateStudent(student, "GuardianPhone", getFields()[4].getText());
            update.updateStudent(student, "GuardianAddress", getFields()[5].getText());
            update.updateStudent(student, "GuardianName", getFields()[6].getText());
            update.updateStudent(student, "StudentMedical", getFields()[7].getText());
            System.out.println("Fields should have successfully been updated. Exiting!");
            setEditMade(false);
            frame.dispose();
        });
    }
}
