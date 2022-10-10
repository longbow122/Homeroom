package me.longbow122.Homeroom.features;

import me.longbow122.Homeroom.Class;
import me.longbow122.Homeroom.*;
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
import java.util.ArrayList;
import java.util.List;

/**
 * The main class that holds the base code for the Class Management feature. Holds all the fields, methods and logic needed to allow Class Management to run as needed.
 *
 * This class should be reworked as much as possible to ensure that the logic behind it and the general code quality works as good as possible.
 */
public class ClassManagement {

    public void openManageClassGUI(String username, String password) {
        Class searchClass = new Class(username, password);
        GUIUtils gui = new GUIUtils("Manage Classes | Homeroom", 1000, 1220, 300, 0, true);
        JButton addClass = gui.addButtonToFrame("New Class", 60, 200, 0, 0);
        addClass.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        gui.addLabelToFrame("Search:", 200, 10, 100, 25, true, 25);
        JTextField searchField = gui.addTextField(300, 12, 200, 25, "Enter a term to search for here!");
        PromptSupport.setPrompt("Search", searchField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, searchField);
        JButton searchButton = gui.addButtonToFrame("Search", 40, 100, 500, 0);
        searchButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        searchButton.addActionListener(e -> processSearch(searchClass, username, password, searchField.getText(), gui));
        JButton exitButton = gui.addButtonToFrame("Exit Class Management", 60, 300, 920, 0);
        gui.addLabelToFrame("Search Type: ", 630, 10, 170, 30, true, 25);
        JComboBox searchChoice = gui.addComboBox(800, 13, 100, 25, new String[]{"Class ID", "Class Name"});
        searchClass.setClassSearchType(ClassSearchType.UUID); //Ensures that default search type actually applies
        searchChoice.addActionListener(e -> {
            JComboBox box = (JComboBox) e.getSource();
            int selected = box.getSelectedIndex();
            switch (selected) {
                case 0:
                    searchClass.setClassSearchType(ClassSearchType.UUID);
                    System.out.println("Search type has been set to UUID!");
                    break;
                case 1:
                    searchClass.setClassSearchType(ClassSearchType.CLASS_NAME);
                    System.out.println("Search type has been set to Class Name!");
                    break;
            }
        });
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                    processSearch(searchClass, username, password, searchField.getText(), gui);
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
        exitButton.addActionListener(e -> gui.closeFrame());
        addClass.addActionListener(e -> {
            GUIUtils add = new GUIUtils("Class Management | Add Class", 1000, 1000, 0, 0, false);
            if(new DBUtils(username, password).getPermission() != 2) {
                JOptionPane.showMessageDialog(gui.getFrame(), "you are not able to make use of this features as you are not an administrator!");
                add.closeFrame();
                return;
            } //Hide the add class button to ensure that only admins can make use of it.
            JLabel nameLabel = add.addLabelToFrame("Class Name:", 25, 30, 150, 20, false);
            nameLabel.setFont(new Font(add.getFrame().getFont().getName(), Font.BOLD, 20));
            nameLabel.setForeground(Color.RED);
            JTextField className = add.addTextField(25, 70, 200, 25, "Enter the name of the Class here! REQUIRED FIELD!");
            // ! it is worth noting that everything else should be added on-edit to allow for the selection methods to work properly. This should be fixed at some point.
            JButton submitData = add.addButtonToFrame("Add Class", 40, 100, 200, 240);
            JButton cancelData = add.addButtonToFrame("Cancel", 40, 100, 300, 240);
            cancelData.addActionListener(e1 -> {
                add.closeFrame();
                System.out.println("Added class process cancelled!");
            });
            submitData.addActionListener(e12 -> {
                if(className.getText().equals("")) {
                    Object[] options = {"Cancel", "Continue Adding Data"};
                    int selected = JOptionPane.showOptionDialog(add.getFrame(), "You did not fill in the class name correctly! Please fill in all data in the red-marked fields!", "Issue Entering Data!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"), options, "Test");
                    switch(selected) {
                        case 0:
                            add.closeFrame();
                            return;
                        case 1:
                            return;
                    }
                }
                List<String> studentIDs = new ArrayList<>(); // * Pass an empty list into the database first when making the Class. When editing the class, you can add the Students accordingly later.
                Class added = new Class(username, password).addClassToDB(className.getText(), "", studentIDs);
                JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully added the class " + added.getClassName() + " to Homeroom!", "homeroom | Class Management", JOptionPane.INFORMATION_MESSAGE);
                add.closeFrame(); //Close the main addition after adding a student
                // ? An empty string is passed into the database for Teachers to ensure that no Teacher is assigned at the start. Better database practises need to be in place before this system can be reworked in terms of design.
                // ? As such, a teacher needs to be assigned to the form on edit.
            });
        });
    }

    private List<List<JButton>> searchButtonsPages;

    private int searchButtonIndex;

    private void setSearchButtons(List<List<JButton>> buttons) {
        searchButtonsPages = buttons;
    }

    /**
     * A basic method used to process the search logic for the main classes GUI. Used to display forms which can be selected and returned.
     * @param searchClass An instance of {@link Class} used to handle searching. Passed through to save resources and avoid instantiating a new object.
     * @param username Username used to query the database
     * @param password Password used to query the database
     * @param searchText The search parameter for the search text to be processed
     * @param gui The parent GUI to display the buttons in.
     */
    private void processSearch(Class searchClass, String username, String password, String searchText, GUIUtils gui) {
        List<Class> searches = searchClass.searchForClass(searchText);
        System.out.println("Working on searching using: " + searchText);
        if(searches == null) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No results found that fit your search query!", "Homeroom | Class Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
            System.out.println("No results seem to have been found!");
            return;
        }
        System.out.println(searches.size() + " results found!");
        showSearchResults(searches, gui, username, password);
    }

    /**
     * A method used to display a certain amount of classes within the main Class Management GUI.
     * This method also opens up an option pane which holds a {@link JProgressBar} which is able to display progress of the search.
     * This method, using basic logic and some addition, will also be able to add "pages" to the GUI, which will allow users to cycle
     * through their search results when needed.
     * @param searchResults The List of {@link Class}es found within the database query.
     * @param gui The parent GUI to add buttons to.
     * @param username Username used to query the database
     * @param password Password used to query the database
     */
    private void showSearchResults(List<Class> searchResults, GUIUtils gui, String username, String password) {
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
        for(Class x : searchResults) {
            JButton option = gui.addButtonToFrame(x.getClassName(), 50, 150, xLoc, yLoc);
            option.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
            option.addActionListener(e -> viewClassInformation(x, new DBUtils(username, password).getPermission(), username, password, gui));
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
                buttons = new ArrayList<>();
                yLoc = 100; // Reset to y=100 to ensure that the coordinates stay proper
            }
            System.out.println(searchResults.indexOf(x) + " out of " + searchResults.size());
        }
        pages.add(buttons);
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
     * Method which emulates the "shifting" of pages, all current buttons are made invisible, and the new pages are made visible. <p></p>
     * Hard-coded numerical values are used for the shift amount as there is a specified amount of shifts that can be made per button press.
     * @param currentIndex The current numbered index that the page is currently on.
     * @param shiftAmount The amount to shift the index by. The index number should be positive if you want to move up a page and index number should be negative if you want to move down a page.
     * @param gui The parent GUI to make use of within the page menu.
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

    private boolean isEditMade;

    private void setEditMade(boolean edit) {
        isEditMade = edit;
    }

    private boolean getIsEditMade() {
        return isEditMade;
    }

    private List<List<JButton>> studentButtonsPages;

    private void setStudentButtonsPages(List<List<JButton>> x) {
        studentButtonsPages = x;
    }

    private int studentButtonIndex;

    //TODO THE BELOW METHOD NEEDS TO BE REWORKED OR REMOVED TO BECOME MORE UNIVERSAL AND/OR OO.

    /**
     * Method made to display the information of students within a class information GUI.
     * @param students The students to display within the GUI.
     * @param parentGUI The parent GUI to display the student information buttons inside
     * @param username The username used to log into 'Homeroom' and the one used to query the database
     * @param password The password used to log into 'Homeroom' and the one used to query the database
     */
    private void displayAllStudentInInfoGUI(List<Student> students, GUIUtils parentGUI, String username, String password) {
        int xLoc = 0;
        int yLoc = 180;
        if(studentButtonsPages != null) {
            for(List<JButton> x : studentButtonsPages) {
                for(JButton i : x) {
                    i.setVisible(false);
                }
            }
        }
        List<List<JButton>> pages = new ArrayList<>();
        List<JButton> buttons = new ArrayList<>();
        for(Student x : students) {
            JButton option = parentGUI.addButtonToFrame(x.getStudentName(), 50, 150, xLoc, yLoc);
            option.setFont(new Font(parentGUI.getFrame().getFont().getName(), Font.PLAIN, 15));
            option.addActionListener(e -> new StudentManagement().viewStudentInformation(x, new DBUtils(username, password).getPermission(), username, password, parentGUI, 1));
            buttons.add(option);
            System.out.println("A button was added to the list of buttons!");
            option.setVisible(false);
            xLoc = xLoc + 150;
            if(xLoc > 1050) {
                yLoc = yLoc + 50;
                xLoc = 0;
            }
            if(yLoc > 930) {
                pages.add(buttons); //add it to the list of lits to ensure you know what's being added.
                System.out.println("A list of buttons has been added to the page list!");
                buttons = new ArrayList<JButton>();
                yLoc = 100;
            }
            System.out.println(students.indexOf(x) + "out of " + students.size());
        }
        pages.add(buttons); //MOVE OUT OF YLOC BLOCK
        JButton nextPage = parentGUI.addButtonToFrame(">>", 30, 60, 460, 30);
        JButton backPage = parentGUI.addButtonToFrame("<<", 30, 60, 400, 30);
        nextPage.setToolTipText("Go to the next page of search results.");
        backPage.setToolTipText("Go to the previous page of search results.");
        if(!pages.isEmpty()) {
            List<JButton> firstPage = pages.get(0);
            for(JButton i : firstPage) {
                i.setVisible(true);
            }
        } else {
            for(JButton y : buttons) {
                y.setVisible(true);
            }
        }
        studentButtonIndex = 0;
        setStudentButtonsPages(pages);
        nextPage.addActionListener(e -> {
            System.out.println("Current index: " + studentButtonIndex);
            if(pages.isEmpty()) {
                JOptionPane.showMessageDialog(parentGUI.getFrame(), "There are no further pages for you to move through!");
                System.out.println("Pages list is empty!");
                return;
            }
            shiftPageForClasses(studentButtonIndex, 1, parentGUI); //Go forward one page
        });
        backPage.addActionListener(e -> {
            System.out.println("Current index: " + studentButtonIndex);
            if(pages.isEmpty()) {
                JOptionPane.showMessageDialog(parentGUI.getFrame(), "There are no further pages for you to move through!");
                System.out.println("Page is empty!");
                return;
            }
            shiftPageForClasses(studentButtonIndex, -1, parentGUI); //Go back one page
        });
        System.out.println("Method has finished calling!");
        return;
    }

    /** TODO METHOD NEEDS TO BE REWORKED TO BE MORE OO
     * <p></p>
     * * Method which emulates the "shifting" of pages, all current buttons are made invisible, and the new pages are made visible.
     * @param currentIndex The current numbered index that the page is currently on.
     * @param shiftAmount The amount to shift the index by. The index number should be positive if you want to move up a page and index number should be negative if you want to move down a page
     * @param gui The parent GUI to make use of within page menu.
     */
    private void shiftPageForClasses(int currentIndex, int shiftAmount, GUIUtils gui) {
        if(shiftAmount < 0) {
            System.out.println("The shift amount is negative! Subtraction should occur!");
            if(currentIndex - 1 < 0) {
                System.out.println("After subtraction, index would be negative and as such, no more pages!");
                JOptionPane.showMessageDialog(gui.getFrame(),"There are no more pages for you to move through!");
                return;
            }
            studentButtonIndex = studentButtonIndex - 1;
        } else {
            System.out.println("The shift amount is positive? Should be incrementing!");
            if(currentIndex + shiftAmount >= studentButtonsPages.size()) {
                System.out.println("Out of index for that addition!");
                JOptionPane.showMessageDialog(gui.getFrame(), "There are no more pages for you to move through!");
                return;
            }
            studentButtonIndex++;
        }
        for(JButton i : studentButtonsPages.get(currentIndex)) {
            i.setVisible(false);
            System.out.println(i.getText());
        }
        System.out.println("Old pages should be invisible.");
        System.out.println("Current index: " + currentIndex);
        System.out.println("New index: " + studentButtonIndex);
        for(JButton x : studentButtonsPages.get(studentButtonIndex)) {
            x.setVisible(true);
            System.out.println(x.getText());
        }
        System.out.println("New page should be visible.");
    }

    /**
     * Method that allows users to view information about a {@link Class}. This GUI also allows users to edit, view and delete Class information depending on the level of permissions the user has.
     * This permission level will need to be passed through using parameters within the method. <p></p>
     * This method seems far too hard-coded for me, and is definitely not a method that is up to standard. Too many operations, that need to be reduced using loops and switch statements where possible. <p></p>
     * This method has also taken editing of information into account, and this is also possible through the same GUI. Editing what {@link me.longbow122.Homeroom.Student}s are in the {@link Class} is not as dynamic as basic text editing, and is done through the GUI as a form of Class Management.
     * What is meant by this is that you need to make use of thr buttons and add and remove {@link me.longbow122.Homeroom.Student}s individually.
     * @param clazz      The {@link Class} in question, the Class for which information should be displayed.
     * @param permission The permission level of the user accessing the Class information in question.
     * @param username The username of the user accessing the class information in question.
     * @param password The password of the user accessing the class information in question.
     * @param parentGUI The parent GUI to be refreshed and viewed in terms of information.
     */
    // * Worth noting that, unlike other methods, this one does not hold a context option since there is currently no other context other than Class Management where a refresh might occur.
    public void viewClassInformation(Class clazz, int permission, String username, String password, GUIUtils parentGUI) {
        GUIUtils gui = new GUIUtils("Class Management | View Class", 1000, 1700, 0, 0, false);
        displayAllStudentInInfoGUI(clazz.getStudents(), gui, username, password);
        Class c = new Class(username, password);
        Teacher t = new Teacher(username, password);
        gui.addLabelToFrame("Class Name", 100, 0, 150, 25, false, 18);
        JTextField nameField = gui.addTextField(100, 30, 120, 30, "Name of the Class");
        PromptSupport.setPrompt("Form Name", nameField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, nameField);
        nameField.setText(clazz.getClassName());
        nameField.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
        gui.addLabelToFrame("Teacher Name", 275, 0, 150, 25, false, 18);
        JTextField teacherField = gui.addTextField(275, 30, 100, 30, "Name of the teacher of the form");
        teacherField.setEditable(false);
        String teacherName = "";
        if(t.getTeacherFromConnectionUsername(clazz.getTeacherConnectionName()) != null) {
            teacherName = t.getTeacherFromConnectionUsername(clazz.getTeacherConnectionName()).getTeacherName();
        } // ? The way this is done to avoid NPEs is temporary. When the database rework fully goes through, the entire system will be a lot cleaner and will allow for further functionality.
        teacherField.setText(teacherName);
        teacherField.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
        JButton selectTeacher = gui.addButtonToFrame("Select Teacher", 30, 150, 275, 60);
        selectTeacher.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 15));
        selectTeacher.addActionListener(e -> {
            gui.closeFrame();
            classTeacherAddition(username, password, clazz);
        });
        List<Student> studentsInClass = new ArrayList<>();
        Student s = new Student(username, password);
        for(String x : clazz.getStudentsInClassID()) {
            studentsInClass.add(s.getStudentFromID(x));
        }
        JButton addStudent = gui.addButtonToFrame("Add Student", 30, 150, 530, 30);
        addStudent.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 15));
        addStudent.addActionListener(e -> {
            gui.closeFrame();
            classStudentAddition(username, password, clazz);
        });
        JButton addForm = gui.addButtonToFrame("Add Form", 30, 150, 530, 60);
        addForm.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 15));
        addForm.addActionListener(e -> {
            gui.closeFrame();
            classFormAddition(username, password, clazz);
        });
        JButton deleteClass = gui.addButtonToFrame("Delete Class", 30, 150, 690, 30);
        deleteClass.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 15));
        deleteClass.addActionListener(e -> {
            Object[] options = {"Delete Class", "Cancel"};
            int option = JOptionPane.showOptionDialog(gui.getFrame(), "Are you sure you would like to delete " + clazz.getClassName() + " from Homeroom?", "Homeroom | Class Management", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("informationIcon"), options, "Test");
            switch (option) {
                case 0: //They have chosen to delete the class!
                    System.out.println("Class will now be deleted!");
                    for(Student x : studentsInClass) {
                        c.modifyClassStudents(clazz, 2, x);
                        // * Only Classes hold Student information, since we want a one-to-many relationship where possible.
                        // * There is no need to have Students hold Class information, since we can pull this through other means
                        // * If anything, Students should be holding the Schedule object, once implemented
                    }
                    c.deleteClass(clazz);
                    JOptionPane.showMessageDialog(gui.getFrame(), clazz.getClassName() + " has successfully been deleted from Homeroom!", "Homeroom | Class Management", JOptionPane.INFORMATION_MESSAGE);
                    gui.closeFrame();
                    parentGUI.closeFrame();
                    break;
                case 1: //They have chosen to cancel, nothing needs to happen!
                    System.out.println("Cancelling!");
                    break;
            }
        });
        if(permission != 2) {
            addStudent.setVisible(false); // Normal users should not be able to add students to a class
            deleteClass.setVisible(false); // Normal users should not be able to delete forms within the database.
            nameField.setEditable(false); // Normal users should not be able to edit the name of a class
            selectTeacher.setVisible(false); // Normal users should not be able to change the teacher of a class
        }
        JTextComponent[] entryFields = {nameField, teacherField};
        for(JTextComponent x : entryFields) {
            x.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(!(getIsEditMade())) {
                        displayEditedButtons(gui, clazz, username, password, entryFields);
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(!(getIsEditMade())) {
                        displayEditedButtons(gui, clazz, username, password, entryFields);
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });
        }
    }

    /**
     * Method which handles {@link Student} addition to a {@link Class}. This method is part of a collection of methods that make use of method overloading to perform certain tasks that involve the searching and selection of Students, to be added to a Class.
     * It is worth noting that a method of this nature should really be written in the {@link Class} class, but due to the higher amount of GUI-based lines it holds, I opted to keep it within this class. <p></p>
     * It is worth noting that in some cases, a {@link Student} may already be in a {@link Class}. This is okay, since they can be in multiple classes.
     * <p></p>
     * It is worth noting that in this context, a {@link Student} is being added to a particular {@link Class}.
     * @param username The username used to log into Homeroom
     * @param password The password used to log into Homeroom
     * @param clazz The {@link Class} you will be adding to the {@link Student}
     */
    protected void classStudentAddition(String username, String password, Class clazz) {
        Student searchClass = new Student(username, password);
        GUIUtils gui = new GUIUtils("Search and Select Student | Homeroom", 1000, 1220, 300, 0, false);
        gui.addLabelToFrame("Search:", 200, 10, 100, 25, true, 25);
        JTextField searchField = gui.addTextField(300, 12, 200, 25, "Enter a term to search for here!");
        PromptSupport.setPrompt("Search", searchField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, searchField);
        JButton searchButton = gui.addButtonToFrame("Search", 40, 100, 500, 0);
        searchButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        searchButton.addActionListener(e -> selectStudentForClassAddition(username, password, searchClass, searchField.getText(), gui, clazz));
        gui.addLabelToFrame("Search Type:", 600, 10, 170, 30, true, 25);
        JComboBox searchChoice = gui.addComboBox(800, 13, 100, 25, new String[]{"Student ID", "Student Name", "Student DOB", "Student Address", "Student Phone"});
        searchClass.setStudentSearchType(StudentSearchType.NAME); //Ensures that default search type actually applies.
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
                    selectStudentForClassAddition(username, password, searchClass, searchField.getText(), gui, clazz);
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /**
     * A method written to aid in the selection of a {@link Student} in particular context. <p></p>
     * This method would be used to add a {@link Student} to a particular {@link Class}.
     * @param username The username used to log into Homeroom.
     * @param password The password used to log into Homeroom.
     * @param searchClass {@link Student} class to handle the searching of a Student with.
     * @param searchText Text to begin querying the database with.
     * @param gui The parent GUI to insert buttons into. This GUI should be under a controlled environment, through a controlled size, etc.
     * @param clazz The {@link Class} to add the {@link Student} to.
     */
    protected void selectStudentForClassAddition(String username, String password, Student searchClass, String searchText, GUIUtils gui, Class clazz) {
        List<Student> searches = searchClass.searchForStudent(searchText);
        System.out.println("Working on searching using: " + searchText);
        if(searches == null) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No results have been found that fit your search query!", "Homeroom | Student Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
            System.out.println("No results seem to have been found!");
            return;
        }
        System.out.println(searches.size() + " results found!");
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
        for(Student x : searches) {
            JButton option = gui.addButtonToFrame(x.getStudentName(), 50, 150, xLoc, yLoc);
            option.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
            Class c = new Class(username, password);
            option.addActionListener(e -> {
                // ? Just add the Student to the Class, since they can be in multiple Classes. Clashes can be checked for elsewhere.
                // ? The only check that needs to be done is to ensure that the Student is already in the class.
                if(c.isStudentInClass(x, clazz)) {
                    System.out.println("Student seems to already be in the same class!");
                    JOptionPane.showMessageDialog(gui.getFrame(), x.getStudentName() + " is already in " + clazz.getClassName() + "! Nothing needs to happen!");
                    return;
                }
                // all needed checks run through, add them straight in
                System.out.println("About to add the Student to the Class!");
                c.modifyClassStudents(clazz, 1, x);
                JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully added " + x.getStudentName() + " to " + clazz.getClassName() + "!", "Homeroom | Class Management", JOptionPane.INFORMATION_MESSAGE);
                gui.closeFrame();
                //TODO REFRESHING FOR MANAGE CLASSES GUI NEEDS TO GO HERE!
            });
            buttons.add(option);
            System.out.println("The buttons for " + x.getStudentName() + " has been implemented, but has not been made visible!");
            option.setVisible(false);
            xLoc = xLoc + 150;
            if(xLoc > 1050) {
                yLoc = yLoc + 50;
                xLoc = 0;
            }
            if(yLoc > 930) {
                pages.add(buttons); //Add it to the list of lists to ensure that you know what goes within each page
                System.out.println("A list of buttons has been added to the page list!");
                buttons = new ArrayList<>();
                yLoc = 100;

            }
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
        System.out.println("Did the button message actually display?");
    }

    /**
     * Method that handles the displaying of special buttons that will only be shown to administrative users to ensure that they have a way of saving, discarding and reverting their changes. <p></p>
     * It is worth noting that adding Students and Teachers to the class is done entirely through the GUI. There is no saving, discarding or reverting for that. Adding {@link Student}s is done entirely separately through the GUI.
     * No particular design reasons behind this and it does need to be reworked.
     * @param gui The GUI in question to display these buttons on.
     * @param clazz The {@link Class} that you are viewing and editing the information for.
     * @param username The username used to log into Homeroom.
     * @param password The password used to log into Homeroom.
     * @param fields The fields being used by the Class GUI to enter, view and edit data.
     */
    private void displayEditedButtons(GUIUtils gui, Class clazz, String username, String password, JTextComponent[] fields) {
        Teacher t = new Teacher(username, password);
        setEditMade(true);
        JFrame frame = (JFrame) gui.getFrame();
        JButton saveEdit = gui.addButtonToFrame("Save Edits", 50, 200, 1100, 80);
        JButton revertChanges = gui.addButtonToFrame("Revert Changes", 50, 200, 1300, 80);
        JButton discardChangesExit = gui.addButtonToFrame("Discard Changes and Exit", 50, 200, 700, 80);
        JButton saveChangesExit = gui.addButtonToFrame("Save Changes and Exit", 50, 200, 900, 80);
        Class update = new Class(username, password);
        JButton[] buttons = {saveEdit, revertChanges, discardChangesExit, saveChangesExit};
        for(JTextComponent x : fields) {
            System.out.println(x.getText());
        }
        saveEdit.addActionListener(e -> {
            update.updateClass(clazz, "ClassName", fields[0].getText());
            setEditMade(false);
            for(JButton x : buttons) {
                x.setVisible(false);
            }
        });
        revertChanges.addActionListener(e -> {
            for(JTextComponent x : fields) {
                x.setText("");
            }
            fields[0].setText(clazz.getClassName());
            String teacherName = "";
            if(t.getTeacherFromConnectionUsername(clazz.getTeacherConnectionName()) != null) {
                teacherName = t.getTeacherFromConnectionUsername(clazz.getTeacherConnectionName()).getTeacherName();
            }
            fields[1].setText(teacherName);
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
            update.updateClass(clazz, "ClassName", fields[0].getText());
            System.out.println("Fields should have successfully been updated. Exiting!");
            setEditMade(false);
            frame.dispose();
        });
    }

    /**
     * Method which handles {@link Teacher} addition to a {@link Class}. This method is part of a collection of methods that make use of method overloading to perform certain tasks that involve the searching and selection of Teachers.
     * It is worth ntoing that a method of this nature should really be written in the {@link Class} class, but due to the higher amount of GUI-based lines it holds, I opted to keep it within this class. <p></p>
     * It is worth noting that in some cases, a {@link Teacher} may already be in a {@link Class}. Logic should be written to overcome this. <p></p>
     * It is worth noting that in this context, a {@link Teacher} is being added to a particular {@link Class}.
     * @param username The username used to log into Homeroom
     * @param password The password used to log into Homeroom
     * @param clazz The {@link Class} you will be adding to the {@link Teacher}.
     */
    protected void classTeacherAddition(String username, String password, Class clazz) {
        Teacher searchClass = new Teacher(username, password);
        GUIUtils gui = new GUIUtils("Search and Select Teachers | Homeroom", 1000, 1220, 300, 0, false);
        gui.addLabelToFrame("Search:", 200, 10, 100, 25, true, 25);
        JTextField searchField = gui.addTextField(300, 12, 200, 25, "Enter a term to search for here!");
        PromptSupport.setPrompt("Search", searchField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, searchField);
        JButton searchButton = gui.addButtonToFrame("Search", 40, 100, 500, 0);
        searchButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        searchButton.addActionListener(e -> selectTeacherForClassAddition(username, password, searchClass, searchField.getText(), gui, clazz));
        gui.addLabelToFrame("Search Type:", 600, 10, 170, 30, true, 25);
        JComboBox searchChoice = gui.addComboBox(800, 13, 100, 25, new String[]{"Teacher Name", "Mongo Connection Name"});
        searchClass.setSearchType(TeacherSearchType.TEACHER_NAME); //Ensures that default search type actually applies.
        searchChoice.addActionListener(e -> {
            JComboBox box = (JComboBox) e.getSource();
            int selected = box.getSelectedIndex();
            switch (selected) {
                case 0:
                    searchClass.setSearchType(TeacherSearchType.TEACHER_NAME);
                    System.out.println("Search type has been set to Teacher Name!");
                    break;
                case 1:
                    searchClass.setSearchType(TeacherSearchType.MONGO_CONNECTION_NAME);
                    System.out.println("Search Type has been set to Mongo Connection Name!");
                    break;
            }
        });
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                    selectTeacherForClassAddition(username, password, searchClass, searchField.getText(), gui, clazz);
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /**
     * A method written to aid in the selection of a {@link Teacher} in particular context. <p></p>
     * This method would be used to add a {@link Teacher} to a particular {@link Class}.
     * @param username The username used to log into Homeroom.
     * @param password The password used to log into Homeroom.
     * @param searchClass {@link Teacher} class to handle the searching of a Teacher with.
     * @param searchText Text to begin querying the database with.
     * @param gui The parent GUI to insert buttons into. This GUI should be under a controlled environment, through a controlled size, etc.
     * @param clazz The {@link Class} to add the {@link Teacher}.
     */
    protected void selectTeacherForClassAddition(String username, String password, Teacher searchClass, String searchText, GUIUtils gui, Class clazz) {
        List<Teacher> searches = searchClass.searchForTeacher(searchText);
        System.out.println("Working on searching using: " + searchText);
        if(searches == null) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No results have been found that fit your search query!", "Homeroom | Teacher Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
            System.out.println("No results seem to have been found!");
            return;
        }
        System.out.println(searches.size() + " results found!");
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
        for(Teacher x : searches) {
            JButton option = gui.addButtonToFrame(x.getTeacherName(), 50, 150, xLoc, yLoc);
            option.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
            Class c = new Class(username, password);
            option.addActionListener(e -> {
                if(clazz.getTeacherConnectionName().equals(x.getConnectionUsername())) {
                    // ? The teacher is already in the same class, nothing needs to happen.
                    JOptionPane.showMessageDialog(gui.getFrame(), "The Teacher is already in " + clazz.getClassName() + "! Nothing needs to happen!", "Homeroom | Class Management", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("The Teacher was already in the form and as such, nothing needs to happen!");
                    return;
                }
                if(!clazz.getTeacherConnectionName().equals("")) {
                    // ? The Class already has a Teacher, but it is not the same Teacher that has been selected! The old one needs to be removed from the class, and the new one put in.
                    System.out.println("Selected Teacher might have a class, but the class currently has a teacher. Switch choice needs to be made.");
                    Object[] options = {"Yes", "No"};
                    int option1 = JOptionPane.showOptionDialog(gui.getFrame(), searchClass.getTeacherFromConnectionUsername(clazz.getTeacherConnectionName()).getTeacherName() + " is already in " + clazz.getClassName() + "! Would you like to have them replaced from this Class by " + x.getTeacherName() + "?", "Homeroom | Class Management", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.questionIcon"), options, "TEST");
                    System.out.println(option1);
                    switch (option1) {
                        case 0: //Should be "yes", which means the user does want the teacher replaced.
                            c.updateClass(clazz, "TeacherConnectionName", x.getConnectionUsername());
                            // ? Replace the old Teacher with the new one by updating them within the DB.
                            JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully added " + x.getTeacherName() + " to the class " + clazz.getClassName() + "!", "Homeroom | Class Management", JOptionPane.INFORMATION_MESSAGE);
                            gui.closeFrame();
                            //TODO REFRESHING FOR MANAGE CLASSES GUI NEEDS TO GO HERE WHERE POSSIBLE
                            // TODO VIEWCLASSINFORMATION GUI ALSO NEEDS TO BE CLOSED DOWN
                            return;
                        case 1: //Should be "no", which means the user does NOT want anything done
                            System.out.println("Teachers form will not be switched to the new one, instead, nothing will happen!");
                            return;
                    }
                }
                // All checks run through, assumed that the Class has no Teacher assigned to it, so they can be assigned straight in
                c.updateClass(clazz, "TeacherConnectionName", x.getConnectionUsername());
                JOptionPane.showMessageDialog(gui.getFrame(), "you have successfully added " + x.getTeacherName() + " to the class " + clazz.getClassName() + "!", "Homeroom | Class Management", JOptionPane.INFORMATION_MESSAGE);
                gui.closeFrame();
                //TODO REFRESHING FOR MANAGE CLASSES GUI NEEDS TO GO HERE WHERE POSSIBLE
            });
            buttons.add(option);
            System.out.println("The buttons for " + x.getTeacherName() + " has been implemented, but has not been made visible!");
            option.setVisible(false);
            xLoc = xLoc + 150;
            if(xLoc > 1050) {
                yLoc = yLoc + 50;
                xLoc = 0;
            }
            if(yLoc > 930) {
                pages.add(buttons); //Add it to the list of lists to ensure that you know what goes within each page
                System.out.println("A list of buttons has been added to the page list!");
                buttons = new ArrayList<>();
                yLoc = 100;

            }
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
        System.out.println("Did the button message actually display?");
        return;
    }

    /**
     * Method which handles {@link Form} addition to a {@link Class}. It is worth noting that a method of this nature should really be written in the {@link Class} class, but due to the higher amount of GUI-based lines it holds, I opted to keep it within this class. <p></p>
     * It is also worth noting that in some cases, a {@link Student} within the {@link Form} in question may already be in the {@link Class} in question. Logic should be written to overcome this. <p></p>
     * It is worth noting that in this context, a {@link Form} is being added to a particular {@link Class}.
     * @param username The username used to log into Homeroom.
     * @param password The password used to log into Homeroom.
     * @param clazz The {@link Class} you will be adding to the {@link Form}
     */
    protected void classFormAddition(String username, String password, Class clazz) {
        Form searchClass = new Form(username, password);
        GUIUtils gui = new GUIUtils("Search and Select Forms | Homeroom", 1000, 1220, 300, 0, false);
        gui.addLabelToFrame("Search:", 200, 10, 100, 25, true, 25);
        JTextField searchField = gui.addTextField(300, 12, 200, 25, "Enter a term to search for here!");
        PromptSupport.setPrompt("Search", searchField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, searchField);
        JButton searchButton = gui.addButtonToFrame("Search", 40, 100, 500, 0);
        searchButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        searchButton.addActionListener(e -> selectFormForClassAddition(username, password, searchClass, searchField.getText(), gui, clazz));
        gui.addLabelToFrame("Search Type:", 600, 10, 170, 30, true, 25);
        JComboBox searchChoice = gui.addComboBox(800, 13, 100, 25, new String[]{"Teacher Name", "Mongo Connection Name"});
        searchClass.setFormSearchType(FormSearchType.FORM_NAME); //Ensures that the default search type actually applies.
        searchChoice.addActionListener(e -> {
            JComboBox box = (JComboBox) e.getSource();
            int selected = box.getSelectedIndex();
            switch (selected) {
                case 0:
                    searchClass.setFormSearchType(FormSearchType.FORM_NAME);
                    System.out.print("Search type has been set to Form Name!");
                    break;
                case 1:
                    searchClass.setFormSearchType(FormSearchType.UUID);
                    System.out.println("Search type has been set to Form UUID!");
                    break;
            }
        });
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                    selectFormForClassAddition(username, password, searchClass, searchField.getText(), gui, clazz);
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /**
     * A method written to aid in the selection of a {@link Form} in particular context. <p></p>
     * This method would be used to add a {@link Form} to a particular {@link Class}.
     * @param username The username used to log into Homeroom.
     * @param password The password used to log into Homeroom.
     * @param searchClass {@link Form} class to handle the searching of a Form with.
     * @param searchText Text to begin querying the database with.
     * @param gui The parent GUI to insert buttons into. This GUI should be under a controlled environment, through a controlled size, etc.
     * @param clazz The {@link Class} to add the {@link Form}s {@link Student}s to.
     */
    protected void selectFormForClassAddition(String username, String password, Form searchClass, String searchText, GUIUtils gui, Class clazz) {
        List<Form> searches = searchClass.searchForFormGroup(searchText);
        System.out.println("Working on searching using: " + searchText);
        if(searches == null) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No Results have been found that fit your search query!", "Homeroom | Form Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
            System.out.println("No results seem to have been found!");
            return;
        }
        System.out.println(searches.size() + " results found!");
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
        for(Form x : searches) {
            JButton option = gui.addButtonToFrame(x.getFormName(), 50, 150, xLoc, yLoc);
            option.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
            Class c = new Class(username, password);
            option.addActionListener(e -> {
                System.out.println("Ask for confirmation before adding an entire form to a class");
                Object[] options =  {"Yes", "No"};
                int option1 = JOptionPane.showOptionDialog(gui.getFrame(), "Are you sure you would like to add all students in the form " + x.getFormName() + " to the class " + clazz.getClassName() + "?", "Homeroom | Form Management", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.questionIcon"), options, "TEST");
                System.out.println(option1); //Ask for confirmation beforehand
                switch(option1) {
                    case 0 : //Should be "yes", meaning they want the entire form added to the class.
                        List<String> studentIDs = x.getStudentsInFormID();
                        List<String> studentsInClass = clazz.getStudentsInClassID();
                        for(String x1 : studentIDs) {
                            if(studentsInClass.contains(x1)) {
                                // ? The student in question is already in the class, no need to do anything.
                                continue;
                            }
                            c.modifyClassStudents(clazz, 1, new Student(username, password).getStudentFromID(x1)); //Add them to the class.
                        }
                        JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully added " + x.getFormName() + "'s students to " + clazz.getClassName() + "!", "Homeroom | Form Management", JOptionPane.INFORMATION_MESSAGE);
                        gui.closeFrame();
                        //TODO REFRESHING FOR MANAGE CLASSES GUI NEEDS TO GO HERE WHERE POSSIBLE
                        //TODO VIEW CLASS INFORMATION ALSO NEEDS TO BE CLOSED DOWN
                        return;
                    case 1: //Should be "no", meaning they don't want the entire form added to the class.
                        System.out.println("User does not want to add that form to the class!");
                        return;
                }
            });
            buttons.add(option);
            System.out.println("The buttons for " + x.getFormName() + " has been implemented, but has not been made visible!");
            option.setVisible(false);
            xLoc = xLoc + 150;
            if(xLoc > 1050) {
                yLoc = yLoc + 50;
                xLoc = 0;
            }
            if(yLoc > 930) {
                pages.add(buttons); //Add it to the list of lists to ensure that you know what goes within each page
                System.out.println("A list of buttons has been added to the page list!");
                buttons = new ArrayList<>();
                yLoc = 100;

            }
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
        System.out.println("Did the button message actually display?");
        return;
    }
}
