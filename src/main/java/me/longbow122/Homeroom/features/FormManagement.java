package me.longbow122.Homeroom.features;

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
 * The main class that holds the base code for the Form Management feature. Holds all the fields, methods and logic needed to allow Form Management to run as needed.
 *
 * This class should be reworked as much as possible to ensure that the logic behind it and the general code quality works as good as possible.
 *
 * Needs a great amount of improvement.
 */
public class FormManagement {

    //TODO ENSURE THAT, AT SOME POINT, YOU CAN ADD STUDENTS TO THE FORM WHEN ORIGINALLY MAKING THE FORM.
    public void openManageFormsGUI(String username, String password) {
        Form searchClass = new Form(username, password);
        GUIUtils gui = new GUIUtils("Manage Forms | Homeroom", 1000, 1220, 300, 0, true);
        JButton addForm = gui.addButtonToFrame("New Form", 60, 200, 0, 0);
        addForm.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        gui.addLabelToFrame("Search:", 200, 10, 100, 25, true, 25);
        JTextField searchField = gui.addTextField(300, 12, 200, 25, "Enter a term to search for here!");
        PromptSupport.setPrompt("Search", searchField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, searchField);
        JButton searchButton = gui.addButtonToFrame("Search", 40, 100, 500, 0);
        searchButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        searchButton.addActionListener(e -> processSearch(searchClass, username, password, searchField.getText(), gui));
        JButton exitButton = gui.addButtonToFrame("Exit Form Management", 60, 300, 920, 0);
        gui.addLabelToFrame("Search Type: ", 630, 10, 170, 30, true, 25);
        JComboBox searchChoice = gui.addComboBox(800, 13, 100, 25, new String[]{"Form ID", "Form Name"});
        searchClass.setFormSearchType(FormSearchType.UUID); // Ensures that the default search type is set to form ID.
        searchChoice.addActionListener(e -> {
            JComboBox box = (JComboBox) e.getSource();
            int selected = box.getSelectedIndex();
            switch(selected) {
                case 0:
                    searchClass.setFormSearchType(FormSearchType.UUID);
                    System.out.println("Search Type has been set to UUID!");
                    break;
                default:
                    searchClass.setFormSearchType(FormSearchType.FORM_NAME);
                    System.out.println("Search Type has been set to Form Name!");
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
        addForm.addActionListener(e -> {
            GUIUtils add = new GUIUtils("Form Management | Add Form", 1000, 1000, 0, 0, true);
            if(new DBUtils(username, password).getPermission() != 2) {
                JOptionPane.showMessageDialog(gui.getFrame(), "You are not able to make use of this feature as you are not an administrator!");
                add.closeFrame();
                return;
            } //Disables use of the add form GUI to standard users.
            JLabel formNameLabel = add.addLabelToFrame("Form Name:", 25, 30, 150, 20, true, 20);
            formNameLabel.setForeground(Color.RED);
            JTextField formName = add.addTextField(25, 70, 200, 25, "Enter the name of the Form here! REQUIRED FIELD!");
            //TODO THE ABOVE LOGIC FOR TEACHERS NEEDS TO BE RE-WORKED. ACCOMMODATE FOR THIS AT SOME POINT WHEN HANDLING CONFIGURATION. IT NEEDS TO BE DONE IN THE SAME WAY THAT ADDING A STUDENT TO A FORM IS DONE.
            JButton confirm = add.addButtonToFrame("Add Form", 25, 150, 500, 30);
            confirm.addActionListener(e12 -> {
                JTextField[] required = new JTextField[]{formName};
                for(JTextField x : required) {
                    if(x.getText().isEmpty()) {
                        Object[] options = new Object[]{"Continue Adding Data", "Cancel"};
                        int option = JOptionPane.showOptionDialog(add.getFrame(), "There are required fields that have been left empty!", "Oops!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"), options, "Test");
                        System.out.println(option);
                        switch (option) {
                            case 0:
                                System.out.println("Continue adding data pressed!");
                                return;
                            case 1:
                                System.out.println("Cancel button pressed!");
                                add.closeFrame();
                                break;
                        }
                    }
                }
                List<String> studentIDs = new ArrayList<>(); //Pass an empty list into the database first when making the Form. When editing the form, you can add Students accordingly later.
                Form added = new Form(username, password).addFormToDB("", formName.getText(), studentIDs);
                // ? An empty String is passed into the database to ensure that no Teacher is assigned at the start. Better database practises (to be added) need to be in place before this system can be reworked in terms of design.
                // ? As such, a teacher needs to be assigned to the form on edit.
                processSearch(searchClass, username, password, searchField.getText(), gui);
                JOptionPane.showMessageDialog(add.getFrame(), "The Form " + added.getFormName() + " was successfully added to the database of Forms!", "Form Successfully Added!", JOptionPane.INFORMATION_MESSAGE);
                add.closeFrame();
            });
        });
    }

    private List<List<JButton>> studentButtonsPages;

    private void setStudentButtonsPages(List<List<JButton>> x) {
        studentButtonsPages = x;
    }

    private int studentButtonIndex;
    //TODO THE BELOW METHOD NEEDS TO BE REWORKED OR REMOVED TO BECOME MORE UNIVERSAL AND/OR OO.

    /**
     * Method made to display the information of students within a form information GUI.
     * @param students The students to display within the GUI.
     * @param parentGUI The parent GUI to display the student information buttons inside
     * @param username The username used to log into 'Homeroom' and the one used to query the database.
     * @param password The password used to log into 'Homeroom' and the one used to query the database.
     */
    private void displayAllStudentsInInfoGUI(List<Student> students, GUIUtils parentGUI, String username, String password) {
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
            System.out.println("A button was added to the lit of buttons!");
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
            shiftPageForForms(studentButtonIndex, 1, parentGUI); //Go forward one page
        });
        backPage.addActionListener(e -> {
            System.out.println("Current index: " + studentButtonIndex);
            if(pages.isEmpty()) {
                JOptionPane.showMessageDialog(parentGUI.getFrame(), "There are no further pages for you to move through!");
                System.out.println("Page is empty!");
                return;
            }
            shiftPageForForms(studentButtonIndex, -1, parentGUI); //Go back one page
        });
        System.out.println("Method has finished calling!");
        return;
    }

    /**
     * A basic method used to process the search logic for the main forms GUI. Used to display forms which can be selected and returned.
     * @param searchClass An instance of {@link Form} used to handle searching. Passed through to save resources and avoid instantiating a new object.
     * @param username Username used to query the database
     * @param password Password used to query the database
     * @param searchText The search parameter for the search text to be processed
     * @param gui The parent GUI to display the buttons
     */
    private void processSearch(Form searchClass, String username, String password, String searchText, GUIUtils gui) {
        List<Form> searches = searchClass.searchForFormGroup(searchText);
        System.out.println("Working on searching using: " + searchText);
        if(searches == null) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No results found that fit your search query!", "Homeroom | Form Management", JOptionPane.ERROR_MESSAGE,UIManager.getIcon("OptionPane.errorIcon"));
            System.out.println("No results seem to have been found!");
            return;
        }
        System.out.println(searches.size() + " results found!");
        showSearchResults(searches, gui, username, password);
    }

    /**
     * A method used to display a certain amount of forms within the main Form Management GUI.
     * This method also opens up an option pane which holds a {@link JProgressBar} which is able to display progress of the search.
     * This method, using basic logic and some addition, will also be able to add "pages" to the GUI, which will allow users to cycle
     * through their search results when needed.
     * @param searchResults The List of {@link Form}s found within the database query.
     * @param gui The {@link JFrame} in the questions
     */
    private void showSearchResults(List<Form> searchResults, GUIUtils gui, String username, String password ) {
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
        for(Form x : searchResults) {
            JButton option = gui.addButtonToFrame(x.getFormName(), 50, 150, xLoc, yLoc);
            option.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
            option.addActionListener(e -> viewFormInformation(x, new DBUtils(username, password).getPermission(), username, password, gui, 0));
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

    private boolean isEditMade;

    private void setEditMade(boolean edit) {
        isEditMade = edit;
    }

    private boolean getIsEditMade() {
        return isEditMade;
    }

    /**
     * Method that handles the displaying of special buttons that will only be shown to administrative users to ensure that they have a way of saving, discarding and reverting their changes. <p></p>
     * It is worth noting that adding Students is done entirely through the GUI. There is no saving, discarding or reverting for that. Adding {@link Student}s is done entirely separately through the GUI.
     * No particular design reasons behind this and it does need to be reworked.
     * @param gui The GUI in question to display these buttons on.
     * @param form The {@link Form} that you are viewing and editing the information for.
     * @param username The username used to log into Homeroom.
     * @param password The password used to log into Homeroom.
     * @param fields The fields being used by the Form GUI to enter, view and edit data.
     */
    private void displayEditedButtons(GUIUtils gui, Form form, String username, String password, JTextComponent[] fields) {
        Teacher t = new Teacher(username, password);
        setEditMade(true);
        JFrame frame = (JFrame) gui.getFrame();
        JButton saveEdit = gui.addButtonToFrame("Save Edits", 50, 200, 1100, 80);
        JButton revertChanges = gui.addButtonToFrame("Revert Changes", 50, 200, 1300, 80);
        JButton discardChangesExit = gui.addButtonToFrame("Discard Changes and Exit", 50, 200, 700, 80);
        JButton saveChangesExit = gui.addButtonToFrame("Save Changes and Exit", 50, 200, 900, 80);
        Form update = new Form(username, password);
        JButton[] buttons = {saveEdit, revertChanges, discardChangesExit, saveChangesExit};
        for(JTextComponent x : fields) {
            System.out.println(x.getText());
        }
        saveEdit.addActionListener(e -> {
            update.updateForm(form, "FormName", fields[0].getText());
            setEditMade(false);
            for(JButton x : buttons) {
                x.setVisible(false);
            }
        });
        revertChanges.addActionListener(e -> {
            for(JTextComponent x : fields) {
                x.setText("");
            }
            fields[0].setText(form.getFormName());
            String teacherName = "";
            if(t.getTeacherFromConnectionUsername(form.getTeacherConnectionName()) != null) {
                teacherName = t.getTeacherFromConnectionUsername(form.getTeacherConnectionName()).getTeacherName();
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
            update.updateForm(form, "FormName", fields[0].getText());
            System.out.println("Fields should have successfully been updated. Exiting!");
            setEditMade(false);
            frame.dispose();
        });
    }


    /**
     * Method that allows users to view information about a {@link Form}. This GUI also allows users to edit, view and delete Form information depending on the level of permissions the user has.
     * This permission level will need to be passed through using parameters within the method. <p></p>
     * This method seems far too hard-coded for me, and is definitely not a method that is up to standard. Too many operations, that need to be reduced using loops and switch statements where possible. <p></p>
     * This method has also taken editing of information into account, and this is also possible through the same GUI. Editing what {@link Student}s are in the {@link Form} is not as dynamic as basic text editing, and is done through the GUI as a form of Form Management and Student Management.
     * What is meant by this is that you need to make use of the buttons and add and remove {@link Student}s individually.
     * @param form       The {@link Form} in question, the Form for which information should be displayed.
     * @param permission The permission level of the user accessing the form information in question.
     * @param username   The username of the user accessing the form information in question.
     * @param password   The password of the user accessing the form information in question.
     * @param parentGUI  The parent GUI to be refreshed and viewed in terms of information.
     * @param contextOption The option to make use of in terms of the GUI to stem from. As multiple GUIs can make use of this same method, there needs to be leeway for this. 0 to refresh Forms Management, 1 to refresh Student Management
     */
    public void viewFormInformation(Form form, int permission, String username, String password, GUIUtils parentGUI, int contextOption) {
        GUIUtils gui = new GUIUtils("Form Management | View Form", 1000, 1700, 0, 0, false);
        Form f = new Form(username, password);
        Teacher t = new Teacher(username, password);
        gui.addLabelToFrame("Form Name", 100, 0, 150, 25, false, 18);
        JTextField nameField = gui.addTextField(100, 30, 120, 30, "Name of the Form");
        PromptSupport.setPrompt("Form Name", nameField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, nameField);
        nameField.setText(form.getFormName());
        nameField.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
        gui.addLabelToFrame("Teacher Name", 275, 0, 150, 25, false, 18);
        JTextField teacherField = gui.addTextField(275, 30, 100, 30, "Name of the teacher of the form");
        teacherField.setEditable(false);
        String teacherName = "";
        if(t.getTeacherFromConnectionUsername(form.getTeacherConnectionName()) != null) {
            teacherName = t.getTeacherFromConnectionUsername(form.getTeacherConnectionName()).getTeacherName();
        } // ? The way this is done to avoid NPEs is temporary. When the database rework fully goes through, the entire system will be a lot cleaner and will allow for further functionality.
        teacherField.setText(teacherName);
        teacherField.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
        JButton selectTeacher = gui.addButtonToFrame("Select Teacher", 30, 150, 275, 60);
        selectTeacher.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 15));
        selectTeacher.addActionListener(e -> formTeacherAddition(username, password, form));//TODO TEST
        List<Student> studentsInForm = new ArrayList<>();
        Student s = new Student(username, password);
        for(String x : form.getStudentsInFormID()) {
            studentsInForm.add(s.getStudentFromID(x));
        }
        displayAllStudentsInInfoGUI(studentsInForm, gui, username, password);
        JButton addStudent = gui.addButtonToFrame("Add Student", 30, 150, 530, 30);
        addStudent.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 15));
        addStudent.addActionListener(e -> {
            gui.closeFrame();
            new StudentManagement().studentFormAddition(username, password, form);
        });
        JButton deleteForm = gui.addButtonToFrame("Delete Form", 30, 150, 690, 30);
        deleteForm.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 15));
        deleteForm.addActionListener(e -> {
            Object[] options = {"Delete Form", "Cancel"};
            int option = JOptionPane.showOptionDialog(gui.getFrame(), "Are you sure you would like to delete " + form.getFormName() + " from Homeroom?", "Homeroom | Form Management", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("informationIcon"), options, "Test");
            switch (option) {
                case 0: //They have chosen to delete the form!
                    System.out.println("Form will now be deleted!");
                    for(Student x : studentsInForm) {
                        s.updateStudent(x, "FormID", "");
                        f.modifyFormStudents(form, 2, x);
                    }
                    f.deleteForm(form);
                    JOptionPane.showMessageDialog(gui.getFrame(), form.getFormName() + " has successfully been deleted from Homeroom!", "Homeroom | Form Management", JOptionPane.INFORMATION_MESSAGE);
                    gui.closeFrame();
                    parentGUI.closeFrame();
                    switch(contextOption) { //Closes the GUI and re-opens the according GUI to give off the impression that the GUI was "refreshed"
                        case 0:
                            openManageFormsGUI(username, password);
                            break;
                        case 1:
                            new StudentManagement().openManageStudentsGUI(username, password);
                            break;
                    }
                    return;
                case 1:
                    break;
            }
        });
        JTextComponent[] entryFields = {nameField, teacherField};
        if(permission != 2) {
            addStudent.setVisible(false); // Normal users should not be able to add to the database.
            deleteForm.setVisible(false); //Normal uses should also not be able to delete forms from the database.
            for(JTextComponent x : entryFields) {
                x.setEditable(false);
            }
        }
        for(JTextComponent x : entryFields) {
            x.getDocument().addDocumentListener(new DocumentListener() {
                @Override // When a character is inserted into the field, this event will be fired.
                public void insertUpdate(DocumentEvent e) {
                    if(!(getIsEditMade())) {
                        displayEditedButtons(gui, form, username, password, entryFields);
                    }
                }

                @Override // When a character is removed from the field, this event will be fired.
                public void removeUpdate(DocumentEvent e) {
                    if(!(getIsEditMade())) {
                        displayEditedButtons(gui, form, username, password, entryFields);
                    }
                }
                @Override // When the document is changed, this event will be fired.
                public void changedUpdate(DocumentEvent e) {
                }
            });
        }
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

    /** TODO METHOD NEEDS TO BE REWORKED TO BE MORE OO
     * <p></p>
     * * Method which emulates the "shifting" of pages, all current buttons are made invisible, and the new pages are made visible.
     * <p></p>
     * Hard-coded numerical values are used for the shift amount as there is a specified amount of shifts that can be made per button press.From what I remembe
     * @param currentIndex The current numbered index that the page is currently on.
     * @param shiftAmount The amount to shift the index by. The index number should be positive if you want to move up a page and index number should be negative if you want to move down a page.
     * @param gui The parent GUI to make use of within the page menu.
     */
    private void shiftPageForForms(int currentIndex, int shiftAmount, GUIUtils gui) {
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
     * Method which handles {@link Form} selection and addition of {@link Teacher} to a {@link Form}. This method is part of a collection of methods that makes use of method overloading to perform certain tasks that involve the searching and selection of Teachers, to be added to a Form group.
     * It is worth noting that a method of this nature should really be written in the {@link Form} class, but due to the higher amount of GUI-based lines it holds, I opted to keep it within this class. <p></p>
     * It is also worth nothing that in some cases, a {@link Teacher} may already be in a {@link Form}. In this case, logic will be in place to ask for user confirmation, before moving the Teacher to the newer form, and removing them from the older form.
     * <p></p>
     * It is worth noting that in this context, a teacher is being added to a particular form. This means that this method would be called within Form Management, to ensure that the user can select a teacher to run the form.
     * @param username The username used to log into 'Homeroom' and the one used to query the database.
     * @param password The password used to log into 'Homeroom' and the one used to query the database
     * @param formGroup The {@link Form} you will be adding to the {@link Teacher}.
     */
    protected void formTeacherAddition(String username, String password, Form formGroup) {
        Teacher searchClass = new Teacher(username, password);
        GUIUtils gui = new GUIUtils("Search and Select Teachers | Homeroom", 1000, 1220, 300, 0, false);
        gui.addLabelToFrame("Search:", 200, 10, 100, 25, true, 25);
        JTextField searchField = gui.addTextField(300, 12, 200, 25, "Enter a term to search for here!");
        PromptSupport.setPrompt("Search", searchField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, searchField);
        JButton searchButton = gui.addButtonToFrame("Search", 40, 100, 500, 0);
        searchButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        searchButton.addActionListener(e -> selectTeacher(username, password, searchClass, searchField.getText(), gui, formGroup));
        gui.addLabelToFrame("Search Type:", 630, 10, 170, 30, true, 25);
        JComboBox searchChoice = gui.addComboBox(800, 13, 100, 25, new String[]{"Connection Name", "Teacher Name"});
         searchClass.setSearchType(TeacherSearchType.TEACHER_NAME); //Ensures that default search type actually applies.
        searchChoice.addActionListener(e -> {
            JComboBox box = (JComboBox) e.getSource();
            int selected = box.getSelectedIndex();
            switch (selected) {
                case 0:
                    searchClass.setSearchType(TeacherSearchType.MONGO_CONNECTION_NAME);
                    System.out.println("Search Type has been set to Mongo Name!!");
                    break;
                case 1:
                    searchClass.setSearchType(TeacherSearchType.TEACHER_NAME);
                    System.out.println("Search Type has been set to Name!");
            }
        });
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                selectTeacher(username, password, searchClass, searchField.getText(), gui, formGroup);
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
     * Method which handles {@link Form} selection and addition to a {@link Form}. This method is part of a collection of methods that makes use of method overloading to perform certain tasks that involve the searching and selection of Form Groups.
     * It is worth noting that a method of this nature should really be written in the {@link Form} class, but due to the higher amount of GUI-based lines it holds, I opted to keep it within this class. <p></p>
     * It is also worth noting that in some cases, a Student may already be in a Form. In this case, logic will be in place to ask for user confirmation, before moving the Student to the newer form, and removing them from the older one.
     * <p></p>
     * It is worth noting that in this context, a Form is being added to a Student. This meamns that this method would be called within Student Management, to ensure that the user can select a Form for the Student to go in.
     * @param username The username used to log into 'Homeroom' and the one used to query the database.
     * @param password The password used to log into 'Homeroom' and the one used to query the database.
     * @param student The {@link Student} you will be adding to the Form.
     */
    protected void formStudentAddition(String username, String password, Student student) {
        Form searchClass = new Form(username, password);
        GUIUtils gui = new GUIUtils("Search and Select Forms | Homeroom", 1000, 1220, 300, 0, false);
        gui.addLabelToFrame("Search:", 200, 10, 100, 25, true, 25);
        JTextField searchField = gui.addTextField(300, 12, 200, 25, "Enter a term to search for here!");
        PromptSupport.setPrompt("Search", searchField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, searchField);
        JButton searchButton = gui.addButtonToFrame("Search", 40, 100, 500, 0);
        searchButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        searchButton.addActionListener(e -> new StudentManagement().selectForm(username, password, searchClass, searchField.getText(), gui, student));
        gui.addLabelToFrame("Search Type:", 630, 10, 170, 30, true, 25);
        JComboBox searchChoice = gui.addComboBox(800, 13, 100, 25, new String[]{"Form ID", "Form Name"});
        searchClass.setFormSearchType(FormSearchType.UUID); //Ensures that default search type actually applies.
        searchChoice.addActionListener(e -> {
            JComboBox box = (JComboBox) e.getSource();
            int selected = box.getSelectedIndex();
            switch (selected) {
                case 0:
                    searchClass.setFormSearchType(FormSearchType.UUID);
                    System.out.println("Search Type has been set to UUID!");
                    break;
                case 1:
                    searchClass.setFormSearchType(FormSearchType.FORM_NAME);
                    System.out.println("Search Type has been set to Name!");
            }
        });
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                new StudentManagement().selectForm(username, password, searchClass, searchField.getText(), gui, student);
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /** TODO THIS METHOD HAS MORE CONTEXTS, IT JUST NEEDS TO BE ADDED AND IMPLEMENTED. COME BACK TO THIS.
     * A method written to aid in the selection of {@link Student}s in particular context. This method is part of a collection of methods that uses method overloading for QOL.
     * <p></p>
     * This method would be used by users to add a {@link Student} to a particular {@link Form}. It would be useful, when editing the information of a Form Group, to ensure that you add the ID of the Student to it's data
     * @param searchClass {@link Student} class to handle the searching of a Student with.
     * @param searchText Text to begin querying the database with.
     * @param gui The parent GUI to insert buttons into. This GUI should be under a controlled environment, through a controlled size, etc.
     * @param formGroup The {@link Form} to add the {@link Student} to.
     * @param username The username used to log into Homeroom.
     * @param password The password used to log into Homeroom.
     */
    protected void selectStudent(String username, String password, Student searchClass, String searchText, GUIUtils gui, Form formGroup) {
        List<Student> searches = searchClass.searchForStudent(searchText);
        System.out.println("Working on searching using: " + searchText);
        if(searches == null) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No results found that fit your search query!", "Homeroom | Student Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
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
            Form form = new Form(username, password);
            option.addActionListener(e -> {
                if(formGroup.getStudentsInFormID().contains(x.getStudentID())) {
                    System.out.println("The student was already in the form and as such, no database operations need to happen!");
                    JOptionPane.showMessageDialog(gui.getFrame(), "The Student is already in " + formGroup.getFormName() + "! Nothing needs to happen!");
                    return;
                }
                if(searchClass.isStudentInForm(x)) {
                    System.out.println("Student is in a form, but it is not the same form that has been selected! Give the user a choice!");
                    Object[] options = {"Yes", "No"};
                    int option1 = JOptionPane.showOptionDialog(gui.getFrame(), x.getStudentName() + " is already in the form " + form.getFormFromID(x.getFormID()).getFormName() + "! Would you like to change their current form to " + formGroup.getFormName() + "?", "Homeroom | Form Management", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.questionIcon"), options, "Test");
                    System.out.println(option1);
                    switch(option1) {
                        case 0: //Should be "yes", which means the user does want the form switched
                            System.out.println("Student's form will be switched to the new one!");
                            System.out.println(form.getFormFromID(x.getFormID()).getFormName() + " is losing a student");
                            form.modifyFormStudents(form.getFormFromID(x.getFormID()), 2, x); //Remove from old form first in terms of form
                            searchClass.updateStudent(x, "FormID", formGroup.getFormID()); //Both remove old form and add new form in terms of StudentDB
                            form.modifyFormStudents(formGroup, 1, x); //Add to new form in terms of form
                            JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully added " + x.getStudentName() + " to the form " + formGroup.getFormName() + "!", "Homeroom | Form Management", JOptionPane.INFORMATION_MESSAGE);
                            gui.closeFrame(); //Close down the selection window, we're done with it
                            //TODO REFRESHING FOR MANAGE FORMS GUI NEEDS TO GO HERE WHERE POSSIBLE.
                            //TODO VIEW FORM INFORMATION GUI NEEDS TO BE CLOSED HERE
                            return;
                        case 1: //Should be "no", meaning the user does not want the form to be switched to the old one,
                            System.out.println("Student's form will not be switched to the new one, instead, nothing will happen");
                            return;
                    }
                }
                // All checks run through, the Student in this case, should not be in any forms, so just add them straight
                System.out.println("About to execute the default branch, where newbies are added!");
                form.modifyFormStudents(formGroup, 1, x); //Add the student to the form in the form DB.
                searchClass.updateStudent(x, "FormID", formGroup.getFormID()); // Add the student to the form in the student DB, so the student has the ID of their form group
                JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully added " + x.getStudentName() + " to " + formGroup.getFormName() + "!", "Homeroom | Form Management", JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.informationIcon"));
                gui.closeFrame(); //Close the window afterwards, selection is all done now
                //TODO REFRESHING FOR MANAGE FORMS GUI NEEDS TO GO HERE WHERE POSSIBLE
                //TODO VIEW FORM INFORMATION GUI NEEDS TO BE CLOSED HERE
            });
            buttons.add(option);
            System.out.println("The button for " + x.getStudentName() + " has been implemented, but has not been made visible!");
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
     * A method written to aid in the selection of a {@link Teacher} in particulat context. This method is part of a collection of methods that uses method overloading for QOL. <p></p>
     * This method would be used by users to add a {@link Teacher} to a particular {@link Form}.
     * @param username The username used to log into Homeroom.
     * @param password The password used to log into Homeroom.
     * @param searchClass  {@link Teacher} class to handle the searching of a Teacher with.
     * @param searchText Text to begin querying the database with.
     * @param gui The parent GUI to insert buttons into. This GUI should be under a controlled environment, through a controlled size, etc.
     * @param formGroup The {@link Form} to add the {@link Teacher} to.
     */
    protected void selectTeacher(String username, String password, Teacher searchClass, String searchText, GUIUtils gui, Form formGroup) {
        List<Teacher> searches = searchClass.searchForTeacher(searchText);
        System.out.println("Working on searching using: " + searchText);
        if(searches == null) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No results found that fit your search query!", "Homeroom | Teacher Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
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
            option.setFont(new Font((gui.getFrame().getFont().getName()), Font.PLAIN, 15));
            Form form = new Form(username, password);
            option.addActionListener(e -> {
                if(formGroup.getTeacherConnectionName().equals(x.getConnectionUsername())) { // ? Should check for if the form group currently assigned is equal to the
                    // ? teacher that is currently being selected. AKA, is the teacher in the same form?
                    System.out.println("The teacher was already in the form and as such, no database operations need to happen!");
                    JOptionPane.showMessageDialog(gui.getFrame(), "The Teacher is already in " + formGroup.getFormName() + "! Nothing needs to happen!");
                    return;
                }
                if(searchClass.isTeacherInForm(x)) {
                    System.out.println("Teacher is in a form, but it is not the same form that has been selected! Give the user a choice!");
                    Object[] options = {"Yes", "No"};
                    int option1 = JOptionPane.showOptionDialog(gui.getFrame(), x.getTeacherName() + " is already in the form " + form.getFormFromID(x.getFormID()).getFormName() + "! Would you like to change their current form to " + formGroup.getFormName() + "?", "Homeroom | Form Management", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.questionIcon"), options, "TEST");
                    System.out.println(option1);
                    switch(option1) {
                        case 0: //Should be "yes", which means the user does want the form switches.
                            System.out.println("Teacher's form will be switched to the new one!");
                            System.out.println(form.getFormFromID(x.getFormID()).getFormName() + " is losing their teacher!");
                            searchClass.updateTeacher(searchClass.getTeacherFromConnectionUsername(formGroup.getTeacherConnectionName()), "FormID", ""); // ? Remove the old teacher from the old form in the TEACHER DB, since they no longer teach that form
                            form.updateForm(form.getFormFromID(x.getFormID()), "TeacherConnectionName", ""); // ? Remove the new teacher from the old form, in the FORM DB
                            searchClass.updateTeacher(x, "FormID", formGroup.getFormID()); // ? Add the FORM to the TEACHER in the TEACHER DB, if they are in one, it will be switched
                            form.updateForm(formGroup, "TeacherConnectionName", x.getConnectionUsername()); // ? Add the TEACHER to the FORM in the FORMDB, if they are in one, it will be switched.
                            JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully added " + x.getTeacherName() + " to the form " + formGroup.getFormName() + "!", "Homeroom | Form Management", JOptionPane.INFORMATION_MESSAGE);
                            gui.closeFrame();
                            //TODO REFRESHING FOR MANAGE FORMS GUI NEEDS TO GO HERE WHERE POSSIBLE
                            //TODO VIEWFORMINFORMATION GUI ALSO NEEDS TO BE CLOSED DOWN
                            return;
                        case 1:
                            System.out.println("Teachers form will not be switched to the new one, instead, nothing will happen!");
                            return;
                    }
                }
                if(searchClass.getTeacherFromConnectionUsername(formGroup.getTeacherConnectionName()) != null) { // ? The form group already has a teacher, but the teacher selected is a newbie, switch needs to be made.
                    System.out.println("Selected teacher might not be in a form, but the form currently has a teacher. A switch choice must be made.");
                    Object[] options = {"Yes", "No"};
                    int option1 = JOptionPane.showOptionDialog(gui.getFrame(), formGroup.getFormName() + " already has a teacher assigned to it! Would you like to assign " + x.getTeacherName() + " to this form, and remove the old one?", "Homeroom | Form Management", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.questionIcon"), options, "TEST");
                    System.out.println(option1);
                    switch(option1) {
                        case 0: //Should be "yes", which means the user does want the form switches.
                            searchClass.updateTeacher(searchClass.getTeacherFromConnectionUsername(formGroup.getTeacherConnectionName()), "FormID", ""); // ? Teacher in the current form loses their form ID, in FORM DB
                            form.updateForm(formGroup, "TeacherConnectionName", x.getConnectionUsername()); // ? Teacher in the current form goes away, to be replaced by the new teacher, in FORM DB
                            searchClass.updateTeacher(x, "FormID", formGroup.getFormID()); // ? New teacher gets their form id added to them in TEACHER DB
                            JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully added " + x.getTeacherName() + " to the form " + formGroup.getFormName() + "!", "Homeroom | Form Management", JOptionPane.INFORMATION_MESSAGE);
                            gui.closeFrame();
                            //TODO REFRESHING FOR MANAGE FORMS GUI NEEDS TO GO HERE WHERE POSSIBLE
                            //TODO VIEWFORMINFORMATION GUI ALSO NEEDS TO BE CLOSED DOWN
                            return;
                        case 1:
                            System.out.println("Teachers form will not be switched to the new one, instead, nothing will happen!");
                            return;
                    }
                }
                // All checks run through, the Teacher in this case, should not be in any forms, so can just add them straight
                System.out.println("About to execute the default branch, where newbies are added");
                form.updateForm(formGroup, "TeacherConnectionName", x.getConnectionUsername()); // ? Add the teacher to the form DB
                searchClass.updateTeacher(x, "FormID", formGroup.getFormID()); // ? Add the form group to the teacher DB
                JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully added " + x.getTeacherName() + " to " + formGroup.getFormName() + "!", "Homeroom | Form Management", JOptionPane.INFORMATION_MESSAGE);
                gui.closeFrame();
                //TODO REFRESHING FOR MANAGE FORMS GUI NEEDS TO GO HERE WHERE POSSIBLE!!!
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
                buttons = new ArrayList<JButton>();
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