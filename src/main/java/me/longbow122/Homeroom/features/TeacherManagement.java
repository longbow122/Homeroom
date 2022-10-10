package me.longbow122.Homeroom.features;

import me.longbow122.Homeroom.Class;
import me.longbow122.Homeroom.*;
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
 * The main class that holds the base code for the Teacher management feature. Holds all the fields, methods and logic needed to allow Teacher Management to run as needed.
 *
 * This class should be reworked as much as possibly to ensure that the logic behind it and the general code quality works as good as possible.
 *
 * Needs a great amount of improvement.
 */
public class TeacherManagement {

    //TODO FULLY TEST
    public void openManageTeacherGUI(String username, String password) {
        Teacher searchClass = new Teacher(username, password);
        GUIUtils gui = new GUIUtils("Manage Teachers | Homeroom", 1000, 1220, 300, 0, true);
        JButton addTeacher = gui.addButtonToFrame("New Teacher", 60, 200, 0, 0);
        addTeacher.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        gui.addLabelToFrame("Search:", 200, 10, 100, 25, true, 25);
        JTextField searchField = gui.addTextField(300, 12, 200, 25, "Enter a term to search for here!");
        PromptSupport.setPrompt("Search", searchField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, searchField);
        JButton searchButton = gui.addButtonToFrame("Search", 40, 100, 500, 0);
        searchButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        searchButton.addActionListener(e -> processSearch(searchClass, username, password, searchField.getText(), gui));
        JButton exitButton = gui.addButtonToFrame("Exit Teacher Management", 60, 300, 920, 0);
        gui.addLabelToFrame("Search Type: ", 630, 10, 170, 30, true, 25);
        JComboBox searchChoice = gui.addComboBox(800, 13, 100, 25, new String[]{"Teacher Name", "Homeroom Username"});
        searchClass.setSearchType(TeacherSearchType.TEACHER_NAME); // Ensures that the default is set to the first option.
        searchChoice.addActionListener(e -> {
            JComboBox box = (JComboBox) e.getSource();
            int selected = box.getSelectedIndex();
            switch(selected) {
                case 1:
                    searchClass.setSearchType(TeacherSearchType.MONGO_CONNECTION_NAME);
                    System.out.println("Search Type has been set to the Homeroom username!");
                    break;
                default:
                    searchClass.setSearchType(TeacherSearchType.TEACHER_NAME);
                    System.out.println("Search Type has been set to Teacher Name!");
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
        addTeacher.addActionListener(e -> {
            GUIUtils add = new GUIUtils("Teacher Management | Add Teacher", 1000, 1000, 0, 0, false);
            // Should be no need for any permission checks, since this feature would only be availiable to admins.
            JLabel connectionNameLabel = add.addLabelToFrame("Mongo Connection Name:", 25, 10, 300, 25, true, 20);
            connectionNameLabel.setForeground(Color.RED);
            JTextField connectionName = add.addTextField(25, 50, 200, 25, "Enter the Mongo username of the user here! REQUIRED FIELD!");
            JLabel teacherNameLabel = add.addLabelToFrame("Teacher Name:", 325, 10, 200, 25, true, 20);
            teacherNameLabel.setForeground(Color.RED);
            JTextField teacherName = add.addTextField(325, 50, 200, 25, "Enter the name of this Teacher (REQUIRED FIELD)");
            // ? It is worth noting that Teachers cannot have their Form added to them on creation. This was a choice by design, purely due to a lack of knowledge
            // ? In terms of concurrency and working with Swing in such a way that a method can return a particular object when a button is clicked.
            // ? Any advice on this matter would be appreciated tremendously, otherwise I'll continue to work on this problem until I find a solution in my own time.
            JButton confirm = add.addButtonToFrame("Add Teacher", 25, 150, 650, 30);
            confirm.addActionListener(e1 -> {
                JTextField[] required = {connectionName, teacherName};
                for(JTextField x : required) {
                    if(x.getText().isEmpty()) {
                        Object[] options = {"Continue Adding Data", "Cancel"};
                        int option = JOptionPane.showOptionDialog(add.getFrame(), "There are required fields that have been left empty!", "Homeroom | Teacher Management", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"), options, "Test");
                        System.out.println(option);
                        switch (option) {
                            case 0:
                                System.out.println("Continue adding data was pressed!");
                                return;
                            case 1:
                                System.out.println("Cancel button pressed!");
                                add.closeFrame();
                                return;
                        }
                    }
                }
                Teacher added = new Teacher(username, password).addTeacherToDB(connectionName.getText(), teacherName.getText(), null);
                // ? Passing null through the formID will ensure the Teacher starts off with an empty Form ID in their Document. This is probably
                // ? The best that can be done.
                processSearch(searchClass, username, password, searchField.getText(), gui);
                JOptionPane.showMessageDialog(add.getFrame(), "The Teacher " + added.getTeacherName() + " was successfully added to the database of Teachers", "Teacher successfully added!", JOptionPane.INFORMATION_MESSAGE);
                add.closeFrame();
            });
        });
    }

    /**
     * A basic method used to process the search logic for the main Teachers GUI. Used to display teachers which can be selected and returned.
     * @param searchClass An instance of {@link Teacher} used to handle searching. Passed through to save resources and avoid instantiating a new object.
     * @param username Username used to query the database.
     * @param password Password used to query the database.
     * @param searchText The search parameter for the search text to be processed.
     * @param gui The parent GUI to display the buttons on.
     */
    private void processSearch(Teacher searchClass, String username, String password, String searchText, GUIUtils gui) {
        List<Teacher> searches = searchClass.searchForTeacher(searchText);
        System.out.println("Working on searching using: " + searchText);
        if(searches == null) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No results found that fit your search query!", "Homeroom | Teacher Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
            System.out.println("No results seem to have been found!");
            return;
        }
        System.out.println(searches.size() + " results found!");
        showSearchResults(searches, gui, username, password);
    } //TODO MERGE THIS METHOD WITH SHOWSEARCHRESULTS. NOT SURE WHY THIS IS A SEPERATE METHOD.

    /**
     * A method used to display a certain amount of forms within the main Form Management GUI.
     * This method also opens up an option pane which holds a {@link JProgressBar} which is able to display progress of the search.
     * This method, using basic logic and some addition, will also be able to add "pages" to the GUI, which will allow users to cycle
     * through their search results when needed.
     * @param searchResults The List of {@link Form}s found within the database query.
     * @param gui The {@link JFrame} that is being used as a parent GUI to insert buttons into.
     */
    private void showSearchResults(List<Teacher> searchResults, GUIUtils gui, String username, String password ) {
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
        for(Teacher x : searchResults) {
            JButton option = gui.addButtonToFrame(x.getTeacherName(), 50, 150, xLoc, yLoc);
            option.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
            option.addActionListener(e -> viewTeacherInformation(x, username, password, gui));
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

    private List<List<JButton>> searchButtonsPages;

    private int searchButtonIndex;

    private void setSearchButtons(List<List<JButton>> buttons) {
        searchButtonsPages = buttons;
    }

    /**
     * * Method which emulates the "shifting" of pages, all current buttons are made invisible, and the new pages are made visible.
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

    private boolean isEditMade;

    private void setEditMade(boolean edit) {
        isEditMade = edit;
    }

    private boolean getIsEditMade() {
        return isEditMade;
    }


    /**
     * Method which handles {@link Teacher} removal from a class.
     * It is worth noting that a method of this nature should really be written in the {@link Class}, but due to the higher amount of GUI-based lines it holds, I opted to keep it within this class. <p></p>
     * It is worth noting that this method will allow for selection of all classes that the Teacher in question is currently in, then ask for confirmation before removing them from said class. <b>There is no searching of any kind done within this method.</b>
     * @param username The username used to log into 'Homeroom' and the one used to query the database.
     * @param password The password used to log into 'Homeroom' and the one used to query the database.
     * @param teacher The {@link Teacher} you will be removing from a specific {@link Class}.
     * @param parentGUI The parent GUI to close down to allow for proper refreshing.
     */ //TODO TEST
    protected void classTeacherRemoval(String username, String password, Teacher teacher, GUIUtils parentGUI) {
        GUIUtils gui = new GUIUtils("Search and Select Classes | Homeroom", 1000, 1220, 300,0,false);
        Class c = new Class(username, password);
        List<Class> classesIn = c.getAllClassesTeacherIn(teacher);
        if(classesIn == null || classesIn.isEmpty()) {
            System.out.println("Nothing needs to happen, the student is in no classes!");
            JOptionPane.showMessageDialog(gui.getFrame(), teacher.getTeacherName() + " is in no Classes. As such, no class can be removed from there!", "Homeroom | Teacher Management", JOptionPane.INFORMATION_MESSAGE);
            gui.closeFrame();
            return; // End it here, nothing can be displayed
        }
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
        for(Class x : classesIn) {
            JButton option = gui.addButtonToFrame(x.getClassName(), 50, 150, xLoc, yLoc);
            option.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
            option.addActionListener(e -> {
                Object[] options = {"Yes", "No"};
                // Ask for confirmation before removing the Student from said class
                int option1 = JOptionPane.showOptionDialog(gui.getFrame(), "Are you sure you would like to remove " + teacher.getTeacherName() + " from " + x.getClassName() + "?", "Homeroom | Class Management", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.questionIcon"), options, "TEST");
                System.out.println(option1);
                switch (option1) {
                    case 0:
                        System.out.println("User selected yes, they want the Teacher removed from their class.");
                        c.updateClass(x, "TeacherConnectionName", ""); //Set the teacher connection name to nothing to ensure that they are removed from the class
                        JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully removed " + teacher.getTeacherName() + " from " + x.getClassName() + "!", "Homeroom | Class Management", JOptionPane.INFORMATION_MESSAGE);
                        gui.closeFrame();
                        parentGUI.closeFrame();
                        return;
                    case 1:
                        System.out.println("User does not want their class removed from that student! Nothing needs to happen!");
                        return;
                }
            });
            buttons.add(option);
            System.out.println("The buttons for " + x.getClassName() + " has been implemented, but has not been made visible!");
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
    }

    /**
     * Method that allows users to view information about a {@link Teacher}. This GUI also allows users to edit and view Teacher information.
     * They are able to do so without checking any permissions, since only admins should be allowed to edit information, and since this GUI will be within the "Configure Homeroom" section
     * of the main GUI (which is only available to admins), there is no need to check permissions since you already know where they will be in terms of permissions.<p></p>
     * This method has also taken editing of information into account, and this is also possible through the same GUI. <p></p>
     * This method also seems far too hard-coded for my liking, and is definitely not a method that is up to standard. Too many operations, that need to be reduced using loops and switch statements where possible.
     * @param teacher The {@link Teacher} in question, the Teacher for which information should be displayed.
     * @param username The username of the user accessing the teacher information in question.
     * @param password The password of the user accessing the teacher information in question.
     * @param parentGUI The parent GUI to be refreshed and viewed in terms of information.
     */
    public void viewTeacherInformation(Teacher teacher, String username, String password, GUIUtils parentGUI) {
        GUIUtils gui = new GUIUtils("Teacher Management | View Teacher", 1000, 1700, 0, 0, false);
        gui.addLabelToFrame("Teacher Name:", 100, 0, 150, 25, false, 18);
        JTextField nameField = gui.addTextField(100, 30, 120, 30, "Name of the Teacher");
        PromptSupport.setPrompt("Teacher Name", nameField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, nameField);
        nameField.setText(teacher.getTeacherName());
        nameField.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
        gui.addLabelToFrame("Mongo Connection Username:", 275, 0, 250, 25, false, 18);
        JTextField connectionField = gui.addTextField(275, 30, 100, 30, "The username used by this user to log into Homeroom");
        connectionField.setEditable(false); //The admin should never be able to adjust their connection username from here.
        connectionField.setText(teacher.getConnectionUsername());
        gui.addLabelToFrame("Form Name:", 525, 0, 100, 30, false, 18);
        JTextField formField = gui.addTextField(525, 30, 100, 30, "The name of the Form that the Teacher belongs to");
        Form f = new Form(username, password);
        Teacher t = new Teacher(username, password);
        String formName = "";
        if(f.getFormFromID(teacher.getFormID()) != null) {
            formName = f.getFormFromID(teacher.getFormID()).getFormName();
        }
        formField.setText(formName);
        formField.setEditable(false);
        JButton selectForm = gui.addButtonToFrame("<html>" + "Select" + "<br>" + "Form" + "</html>", 30, 50, 660, 30);
        selectForm.addActionListener(e -> {
            gui.closeFrame();
            teacherFormAddition(username, password, teacher);
        });
        JButton removalForm = gui.addButtonToFrame("Remove From Form", 50, 150, 100, 130);
        if(formName.equals("")) { // ? Effectively check if the teacher has a form or not. If they do, then this string would not be empty.
            removalForm.setVisible(false);
        }
        removalForm.addActionListener(e -> {
            Form teacherForm = f.getFormFromID(teacher.getFormID());
            Object[] options = {"Yes", "No"};
            int option1 = JOptionPane.showOptionDialog(gui.getFrame(), "Are you sure you would like to remove " + teacher.getTeacherName() + " from " + teacherForm.getFormName() + "?", "Homeroom | Teacher Management", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.questionIcon"), options, "TEST");
            System.out.println(option1); // As them for a choice first
            switch (option1) {
                case 0: // User should have selected "yes", meaning that the teacher needs to be removed from the form
                    System.out.println("User has chosen to remove the form from the teacher!");
                    f.updateForm(teacherForm, "TeacherConnectionName", ""); // ? Remove the teacher's connection name from the form to remove the teacher from the form
                    t.updateTeacher(teacher, "FormID", ""); // ? Remove the form ID from the teacher to remove the form from the teacher.
                    JOptionPane.showMessageDialog(gui.getFrame(), teacher.getTeacherName() + " has been successfully removed from " + teacherForm.getFormName() + "!", "Homeroom | Teacher Management", JOptionPane.INFORMATION_MESSAGE);
                    gui.closeFrame();
                    parentGUI.closeFrame();
                    return;
                case 1: // User should have selected "no", meaning that nothing needs to happen
                    System.out.println("User selected no, nothing needs to happen");
                    return;
            }
        });
        JButton removeClass = gui.addButtonToFrame("Remove From Class", 50, 150, 250, 130);
        removeClass.addActionListener(e -> classTeacherRemoval(username, password, teacher, gui)); //TODO TEST!
        JButton deleteTeacher = gui.addButtonToFrame("Delete Teacher", 30, 150, 275, 60);
        deleteTeacher.addActionListener(e -> {
            Object[] options = {"Delete Teacher", "Cancel"};
            int option = JOptionPane.showOptionDialog(gui.getFrame(), "Are you sure you would like to delete " + teacher.getTeacherName() + " form Homeroom?", "Homeroom | Teacher Management", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.questionIcon"), options, "Testy");
            switch (option) {
                case 0: // They have chosen to delete the Teacher
                    t.deleteTeacher(teacher);
                    JOptionPane.showMessageDialog(gui.getFrame(), teacher.getTeacherName() + " has been deleted from Homeroom!", "Homeroom | Teacher Management", JOptionPane.INFORMATION_MESSAGE);
                    gui.closeFrame();
                    parentGUI.closeFrame();
                    return;
                case 1:
                    break;
            }
        });
        // ? When changing Forms within the GUI, the same logic that was behind Students only being in one Form needs to be applied properly, along with the Form Selection methods with context.
        // ? It is important that you take things slowly, and ensure that these methods work before doing anything.
        JTextComponent[] entryFields = {nameField}; //TODO REWORK METHOD TO ONLY REFER TO THE NAME FIELD.
        for(JTextComponent x : entryFields) {
            x.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(!(getIsEditMade())) {
                        displayEditedButtons(gui, teacher, username, password, entryFields);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(!(getIsEditMade())) {
                        displayEditedButtons(gui, teacher, username, password, entryFields);
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });
        }
    }

    /**
     * Method that handles the displaying of special buttons that will only be shown to administrative users to ensure that they have a way of saving, discarding and reverting their changes. <p></p>
     * There are no permissions checking done in this method, since it is assumed that it was done earlier. Even if it was done earlier, it shouldn't really need to be done at all, since the Teacher class is only ever going to be used by Admins.
     * @param gui The GUI in question to display these buttons on.
     * @param teacher The {@link Teacher} that you are viewing and editing the information for.
     * @param username The username used to log into Homeroom.
     * @param password The password used to log into Homeroom.
     * @param fields The fields being used by the Teacher GUI to enter, view and edit data.
     */
    private void displayEditedButtons(GUIUtils gui, Teacher teacher, String username, String password, JTextComponent[] fields) {
        setEditMade(true);
        JFrame frame = (JFrame) gui.getFrame();
        JButton saveEdit = gui.addButtonToFrame("Save Edits", 50, 200, 1100, 30);
        JButton revertChanges = gui.addButtonToFrame("Revert Changes", 50, 200, 1300, 30);
        JButton discardChangesExit = gui.addButtonToFrame("Discard Changes and Exit", 50, 200, 700, 30);
        JButton saveChangesExit = gui.addButtonToFrame("Save Changes and Exit", 50, 200, 900, 30);
        JButton[] buttons = {saveEdit, revertChanges, discardChangesExit, saveChangesExit};
        Teacher update = new Teacher(username, password);
        for(JTextComponent x : fields) {
            System.out.println(x.getText());
        }
        saveEdit.addActionListener(e -> {
            update.updateTeacher(teacher, "TeacherName", fields[0].getText());
            setEditMade(false);
            for(JButton x : buttons) {
                x.setVisible(false);
            }
        });
        revertChanges.addActionListener(e -> {
            for(JTextComponent x : fields) { //TODO INVESTIGATE THIS AT SOME POINT? WHY IS THIS BEING DONE? FOR WHAT REASON?
                x.setText("");
            }
            fields[0].setText(teacher.getTeacherName());
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
            update.updateTeacher(teacher, "TeacherName", fields[0].getText());
            System.out.println("Fields should have successfully been updated. Exiting!");
            setEditMade(false);
            frame.dispose();
        });
    }

    /**
     * Method which handles {@link Form} selection and addition of a {@link Form} to a {@link Teacher}. This method is part of a collection of methods that makes use of method overloading to perform certain tasks that involve the searching and selection of Form Groups, to be added to a Teacher.
     * <p></p>
     * It is worth noting that in this particular context, a Form Group is being added to a particular teacher. This means that this method would be called within Form Management, to ensure that the user can select a Form Group to put the Teacher in.
     * @param username The username used to log into 'Homeroom' and the one used to query the database.
     * @param password The password used to log into 'Homeroom' and the one used to query the database.
     * @param teacher The {@link Teacher} you will be adding to the {@link Form} group.
     */
    public void teacherFormAddition(String username, String password, Teacher teacher) {
        Form searchClass = new Form(username, password);
        GUIUtils gui = new GUIUtils("Search and Select Forms | Homeroom", 1000, 1220, 300, 0, false);
        gui.addLabelToFrame("Search:", 200, 10, 100, 25, true, 25);
        JTextField searchField = gui.addTextField(300, 12, 200, 25, "Enter a term to search for here!");
        PromptSupport.setPrompt("Search", searchField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, searchField);
        JButton searchButton = gui.addButtonToFrame("Search", 40, 100, 500, 0);
        searchButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        searchButton.addActionListener(e -> selectForm(username, password, searchClass, searchField.getText(), gui, teacher));
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
                selectForm(username, password, searchClass, searchField.getText(), gui, teacher);
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
     * A method written to aid in the selection of a {@link Teacher} in particular context. This method is part of a collection of methods that uses method overloading for QOL. <p></p>
     * This method would be used by users to add a {@link Form} to a particular {@link Teacher}.
     * @param username The username used to log into Homeroom.
     * @param password The password used to log into Homeroom.
     * @param searchClass {@link Form} class to handle the searching of a Form with.
     * @param searchText Text to begin querying the database with.
     * @param gui The parent GUI to insert buttons into. This GUI should be under a controlled environment, through a controlled size, etc.
     * @param teacher The {@link Teacher} to add the {@link Form} to.
     */
    protected void selectForm(String username, String password, Form searchClass, String searchText, GUIUtils gui, Teacher teacher) {
        List<Form> searches = searchClass.searchForFormGroup(searchText);
        Teacher t = new Teacher(username, password);
        System.out.println("Working on searching using " + searchText);
        if(searches == null) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No result found that fit your search query!", "Homeroom | Form Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
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
            option.addActionListener(e -> {
                if(x.getTeacherConnectionName().equals(teacher.getConnectionUsername())) { // ? The connection name of the form is the same as the teacher. AKA, the teacher is in the same form.
                    System.out.println("The teacher is currently in the same form and as such, nothing needs to be done!");
                    JOptionPane.showMessageDialog(gui.getFrame(), "The Teacher is already in " + x.getFormName() + "! Nothing needs to happen!");
                    return;
                }
                if(t.isTeacherInForm(teacher)) { // ? The teacher is in A form, but it is not the one that was selected
                    System.out.println("Teacher is in a form, but it is not the same form that has been selected! Give the user a choice!");
                    Object[] options = {"Yes", "No"};
                    int option1 = JOptionPane.showOptionDialog(gui.getFrame(), teacher.getTeacherName() + " is already in the form " + searchClass.getFormFromID(x.getFormID()).getFormName() + "! Would you like to change their current form to " + x.getFormName() + "?", "Homeroom | Form Management", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.questionIcon"), options, "TEST");
                    System.out.println(option1);
                    switch(option1) {
                        case 0:
                            System.out.println("Teacher's form will be switched to the new one!");
                            t.updateTeacher(t.getTeacherFromConnectionUsername(x.getTeacherConnectionName()), "FormID", ""); // ? Remove the old teacher in the old form in the TEACHER DB
                            searchClass.updateForm(searchClass.getFormFromID(teacher.getFormID()), "TeacherConnectionName", ""); // ? Remove the new teacher from the old form in the FORM DB
                            t.updateTeacher(teacher, "FormID", x.getFormID()); // ? Add the FORM to the new TEACHER IN THE TEACHER DB, if they are in one, it will be switched
                            searchClass.updateForm(x, "TeacherConnectionName", teacher.getConnectionUsername()); // ? Add the TEACHER to the FORM in the FORM DB, if they are in one, it will be switched
                            JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully added " + teacher.getTeacherName() + " to the form " + x.getFormName() + "!", "Homeroom | Teacher Management", JOptionPane.INFORMATION_MESSAGE);
                            gui.closeFrame();
                            return;
                            //TODO REFRESHING FOR MANAGE TEACHERS GUI NEEDS TO GO HERE WHERE POSSIBLE
                            //TODO IEW INFORMATION GUI ALSO NEEDS TO BE CLOSED DOWN
                        case 1:
                            System.out.println("Teachers form will not be switched to the new one, instead, nothing will happen!");
                            return;
                    }
                }
                if(t.getTeacherFromConnectionUsername(x.getTeacherConnectionName()) != null) { // ? The form group already has a teacher, but the teacher that is being operated on is a newbie, switch.
                    System.out.println("Selected teacher might not be in a form, but the form currently has a teacher. A switch choice must be made.");
                    Object[] options = {"Yes", "No"};
                    int option1 = JOptionPane.showOptionDialog(gui.getFrame(), x.getFormName() + " already has a teacher assigned to it! Would you like to assign " + teacher.getTeacherName() + " to this form, and remove the old one?", "Homeroom | Form Management", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.questionIcon"), options, "TEST");
                    System.out.println(option1);
                    switch(option1) {
                        case 0: //Should be "yes", which means the user does want the form switches.
                            t.updateTeacher(t.getTeacherFromConnectionUsername(x.getTeacherConnectionName()), "FormID", ""); // ? Teacher in the current form loses their form ID, in FORM DB
                            searchClass.updateForm(x, "TeacherConnectionName", teacher.getConnectionUsername()); // ? Teacher in the current form goes away, to be replaced by the new teacher, in FORM DB
                            t.updateTeacher(teacher, "FormID", x.getFormID()); // ? New teacher gets their form id added to them in TEACHER DB
                            JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully added " + teacher.getTeacherName() + " to the form " + x.getFormName() + "!", "Homeroom | Teacher Management", JOptionPane.INFORMATION_MESSAGE);
                            gui.closeFrame();
                            //TODO REFRESHING FOR MANAGE FORMS GUI NEEDS TO GO HERE WHERE POSSIBLE
                            //TODO VIEWFORMINFORMATION GUI ALSO NEEDS TO BE CLOSED DOWN
                            return;
                        case 1:
                            System.out.println("Teachers form will not be switched to the new one, instead, nothing will happen!");
                            return;
                    }
                }
                System.out.println("About to execute the default branch, where newbies are added");
                searchClass.updateForm(x, "TeacherConnectionName", teacher.getConnectionUsername()); // ? Add the teacher to the form DB
                t.updateTeacher(teacher, "FormID", x.getFormID()); // ? Add the form group to the teacher DB
                JOptionPane.showMessageDialog(gui.getFrame(), "You have successfully added " + teacher.getTeacherName() + " to " + x.getFormName() + "!", "Homeroom | Teacher Management", JOptionPane.INFORMATION_MESSAGE);
                gui.closeFrame();
                //TODO REFRESHING FOR MANAGE FORM GUI NEEDS TO GO HERE WHERE POSSIBLE!!!
            });
            buttons.add(option);
            System.out.println("The button for " + x.getFormName() + " has been implemented, but has not been made visible!");
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
                yLoc = 100;
            }
        }
        pages.add(buttons);
        JButton nextPage = gui.addButtonToFrame(">>", 30, 60, 980, 65);
        JButton backPage = gui.addButtonToFrame("<<", 30, 60, 920, 65);
        nextPage.setToolTipText("Go to the next page of search results.");
        backPage.setToolTipText("go to the previous page of search results.");
        if(!(pages.isEmpty())) {
            List<JButton> firstPage = pages.get(0);
            for(JButton i :firstPage) {
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
                System.out.println("Pages list is empty!");
                return;
            }
            shiftPage(searchButtonIndex, -1, gui); //Go back one page
        });
        System.out.println("Did the button message actually display?");
    }
}
