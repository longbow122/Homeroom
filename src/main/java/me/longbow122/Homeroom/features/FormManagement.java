package me.longbow122.Homeroom.features;

import me.longbow122.Homeroom.Form;
import me.longbow122.Homeroom.FormSearchType;
import me.longbow122.Homeroom.Student;
import me.longbow122.Homeroom.utils.DBUtils;
import me.longbow122.Homeroom.utils.GUIUtils;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
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

    public void openManageFormsGUI(String username, String password) {
        Form searchClass = new Form(username, password);
        GUIUtils gui = new GUIUtils("Manage Forms | Homeroom", 1000, 1220, 300, 0, false);
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
        JComboBox searchChoice = gui.addComboBox(800, 13, 100, 25, new String[]{"Form ID", "Teacher Name", "Form Name"});
        searchClass.setFormSearchType(FormSearchType.UUID); // Ensures that the default search type is set to form ID.
        searchChoice.addActionListener(e -> {
            JComboBox box = (JComboBox) e.getSource();
            int selected = box.getSelectedIndex();
            switch(selected) {
                case 0:
                    searchClass.setFormSearchType(FormSearchType.UUID);
                    System.out.println("Search Type has been set to UUID!");
                    break;
                case 1:
                    searchClass.setFormSearchType(FormSearchType.TEACHER_NAME);
                    System.out.println("Search Type has been set to Teacher Name!");
                    break;
                case 2:
                    searchClass.setFormSearchType(FormSearchType.FORM_NAME);
                    System.out.println("Search Type has been set to Form Name!");
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
            GUIUtils add = new GUIUtils("Form Management | Add Form", 1000, 1000, 0, 0, false);
            if(new DBUtils(username, password).getPermission() != 2) {
                JOptionPane.showMessageDialog(gui.getFrame(), "You are not able to make use of this feature as you are not an administrator!");
                add.closeFrame();
                return;
            } //Disables use of the add form GUI to standard users.
            JLabel formNameLabel = add.addLabelToFrame("Form Name:", 25, 30, 150, 20, true, 20);
            formNameLabel.setForeground(Color.RED);
            JTextField formName = add.addTextField(25, 70, 200, 25, "Enter the name of the Form here! REQUIRED FIELD!");
            JLabel teacherNameLabel = add.addLabelToFrame("Teacher Name:", 250, 30, 200, 20, true, 20);
            teacherNameLabel.setForeground(Color.RED);
            JTextField teacherName = add.addTextField(250, 70, 200, 25, "Enter the name of the teacher that will watch over the form.");
            //TODO THE ABOVE LOGIC FOR TEACHERS NEEDS TO BE RE-WORKED. ACCOMMODATE FOR THIS AT SOME POINT WHEN HANDLING CONFIGURATION.
            JLabel studentLabel = add.addLabelToFrame("Students:", 25, 130, 200, 25, true, 20);
            JButton selectStudent = add.addButtonToFrame("Select Student", 50, 200, 25, 175);
            List<Student> studentsToAdd = new ArrayList<>();
            selectStudent.addActionListener(e1 -> {
                Student selected = new StudentManagement().studentSelection(username, password);
                System.out.println("Method should have finished calling?");
                //studentsToAdd.add(selected); TODO GET RID OF DEBUG COMMENTS EVENTUALLY
                //System.out.println(selected.getStudentName() + " was added to the list!");
                //displayAllStudentsInInfoGUI(studentsToAdd, add, username, password);
            });
            //TODO ADD CONFIRM ADDITION BUTTONS
            JButton confirm = add.addButtonToFrame("Add Form", 25, 150, 500, 30);
            confirm.addActionListener(e12 -> {
                JTextField[] required = new JTextField[]{formName, teacherName};
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
                if(studentsToAdd.isEmpty()) {
                    JOptionPane.showMessageDialog(add.getFrame(), "There must be at least one student within a form! ", "Oops!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String[] studentIDs =  new String[studentsToAdd.size()];
                for(Student x : studentsToAdd) {
                    studentIDs[studentsToAdd.indexOf(x)]  = x.getStudentID();
                }
                Form added = new Form(username, password).addFormToDB(teacherName.getText(), formName.getText(), studentIDs);
                processSearch(searchClass, username, password, searchField.getText(), gui);
                JOptionPane.showMessageDialog(add.getFrame(), "The Form " + added.getFormName() + " was successfully added to the database of Forms!", "Form Successfully Added!", JOptionPane.INFORMATION_MESSAGE);
                add.closeFrame();
            });
            //TODO TEST FORM ADDITION SYSTEM WITH THE DATABASE
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
        int yLoc = 280;
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
                JButton nextPage = parentGUI.addButtonToFrame(">>", 30, 60, 980, 65);
                JButton backPage = parentGUI.addButtonToFrame("<<", 30, 60, 920, 65);
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
            }
        }
    }

    /**
     * A basic method used to process the search logic for the main forms GUI. Used to display forms which can be selected and returned.
     * @param searchClass An instance of {@link Form} used to handle searching. Passed through to save resources and avoid instantiated a new object.
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
            option.addActionListener(e -> viewFormInformation(x, new DBUtils(username, password).getPermission(), username, password, gui));
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



    private void viewFormInformation(Form form, int permission, String username, String password, GUIUtils parentGUI) {
        GUIUtils gui = new GUIUtils("Form Management | View Form", 1000, 1700, 0, 0, false);
        gui.addLabelToFrame("Form Name", 100, 0, 150, 25, false, 18);
        JTextField nameField = gui.addTextField(100, 30, 120, 30, "Name of the Form");
        PromptSupport.setPrompt("Form Name", nameField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, nameField);
        nameField.setText(form.getFormName());
        nameField.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
        gui.addLabelToFrame("Teacher Name", 275, 0, 150, 25, false, 18);
        JTextField teacherField = gui.addTextField(275, 30, 100, 30, "Name of the teacher of the form");
        PromptSupport.setPrompt("Teacher Name", teacherField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT,teacherField);
        teacherField.setText(form.getTeacherName());
        teacherField.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
        displayAllStudentsInInfoGUI(form.getStudents(), gui, username, password);
        //TODO HANDLE DISPLAY OF STUDENTS THROUGH BUTTONS AS IS STANDARD, BUT IN THE CONTEXT OF FORMS.
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

    /** TODO METHOD NEEDS TO BE REWORKED TO BE MORE OO
     * <p></p>
     * * Method which emulates the "shifting" of pages, all current buttons are made invisible, and the new pages are made visible.
     * <p></p>
     * Hard-coded numerical values are used for the shift amount as there is a specified amount of shifts that can be made per button press.
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
     * Method used to return a {@link Form} object which the rest of the program is able to use. This method provides the same functionality as the Form Management GUI does, except this method should, in theory provide a returned object upon clicking if the button. <p></p>
     * WARNING: THIS METHOD MAKES USE OF THE SYNCHRONIZE KEYWORD, AND WILL WAIT ON THE THREAD FOR A RESPONSE FROM THE USER BEFORE CONTINUING. THIS WILL LIKELY HOLD UP THE ENTIRE PROGRAM UNTIL INPUT IS REACHIEVED. <P></P>
     * Care should be taken to ensure that this does not affect the rest of the program.
     * @param username The username used to log into 'Homeroom' and the one used to query the database.
     * @param password The password used to log into 'Homeroom' and the one used to query the database.
     * @return {@link Form} object that represents the selected Form.
     */
    public Form formSelection(String username, String password) {
        Form searchClass = new Form(username, password);
        GUIUtils gui = new GUIUtils("Search and Select Forms | Homeroom", 1000, 1220, 300, 0, false);
        gui.addLabelToFrame("Search:", 200, 10, 100, 25, true, 25);
        JTextField searchField = gui.addTextField(300, 12, 200, 25, "Enter a term to search for here!");
        PromptSupport.setPrompt("Search", searchField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, searchField);
        JButton searchButton = gui.addButtonToFrame("Search", 40, 100, 500, 0);
        searchButton.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 20));
        final Object lock = new Object();
        searchButton.addActionListener(e -> {
            handleReturnFormSearch(searchClass, searchField.getText(), gui);
            synchronized (lock) {
                lock.notifyAll();
            }
        });
        gui.addLabelToFrame("Search Type: ", 630, 10, 170, 30, true, 25);
        JComboBox searchChoice = gui.addComboBox(800, 13, 100, 25, new String[]{"Form ID", "Teacher Name", "Form Name"});
        searchClass.setFormSearchType(FormSearchType.UUID); //Ensures that the default search type is set to form ID.
        searchChoice.addActionListener(e -> {
            JComboBox box = (JComboBox) e.getSource();
            int selected = box.getSelectedIndex();
            switch(selected) {
                case 0:
                    searchClass.setFormSearchType(FormSearchType.UUID);
                    break;
                case 1:
                    searchClass.setFormSearchType(FormSearchType.TEACHER_NAME);
                    break;
                case 2:
                    searchClass.setFormSearchType(FormSearchType.FORM_NAME);
                    break;
                default:
                    searchClass.setFormSearchType(FormSearchType.FORM_NAME);
                    break;
            }
        });
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                    handleReturnFormSearch(searchClass, searchField.getText(), gui);
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                System.out.println("A stack trace has occurred! Please investigate!");
                e.printStackTrace();
            }
        }
        return formSelected; //TODO TEST WHERE AND WHEN POSSIBLE
    }

    /**
     * Convenience method to handle the searching and returning of a {@link Form}.
     * @param searchClass {@link Form} class to handle searching of a Form
     * @param searchText Text to begin querying the database with
     * @param gui The parent GUI of which said buttons should be displaying on.
     * @return {@link Form} object represented the selected Form.
     */
    private Form handleReturnFormSearch(Form searchClass, String searchText, GUIUtils gui) {
        List<Form> searches = searchClass.searchForFormGroup(searchText);
        System.out.println("Working on searching using: " + searchText);
        if(searches == null) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No results were found that fit your search query!", "Homeroom | Form Management", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
            System.out.println("No results seem to have been found!");
            return null;
        }
        System.out.println(searches.size() + " results found!");
        return selectForm(searches, gui);
    }

    private Form formSelected;
    private void setFormSelected(Form x) {
        formSelected = x;
    }

    /**
     * Method used to return {@link Form} object which the rest of the program is able to use. The method provide the same functionality as the Form Management GUI does, except this method should, in theory provide a returned object upon clicking of the button. <p></p>
     * WARNING: THIS METHOD MAKES USE OF THE SYNCHRONISE KEYWORD, AND WILL WAIT ON THE THREAD FOR A RESPONSE FROM THE USER BEFORE CONTINUING. THIS WILL LIKELY HOLD UP THE ENTIRE PROGRAM UNTIL INPUT IS RECIEVED. <P></P>
     * Care should be taken to ensure that this does not affect the rest of the program.
     * @param searchResults  The List of {@link Form} returned from the search provide by the user.
     * @param gui The parent GUI to insert buttons into. This GUI should be under a controlled environment, through a controlled size, etc.
     * @return {@link Form} object representing the form that was selected by the user.
     */
    private Form selectForm(List<Form> searchResults, GUIUtils gui) {
        int xLoc = 0;
        int yLoc = 100;
        if (searchButtonsPages != null) {
            for (List<JButton> x : searchButtonsPages) {
                for (JButton i : x) {
                    i.setVisible(false);
                }
            }
        }
        List<List<JButton>> pages = new ArrayList<>();
        List<JButton> buttons = new ArrayList<>();
        final Object lock = new Object();
        for (Form x : searchResults) {
            JButton option = gui.addButtonToFrame(x.getFormName(), 50, 150, xLoc, yLoc);
            option.setFont(new Font(gui.getFrame().getFont().getName(), Font.PLAIN, 15));
            option.addActionListener(e -> {
                setFormSelected(x);
                gui.closeFrame();
                synchronized (lock) {
                    lock.notifyAll();
                }
            });
            buttons.add(option);
            System.out.println("A buttons was added to the list of buttons!");
            option.setVisible(false);
            xLoc = xLoc + 150;
            if (xLoc > 1050) {
                yLoc = yLoc + 50;
                xLoc = 0;
            }
            if (yLoc > 930) {
                pages.add(buttons);
                System.out.println("A list of buttons has been added to the page list!");
                buttons = new ArrayList<>();
                yLoc = 100;
            }
            System.out.println(searchResults.indexOf(x) + " out of " + searchResults.size());
        }
        pages.add(buttons);
        JButton nextPage = gui.addButtonToFrame(">>", 30, 60, 980, 65);
        JButton backPage = gui.addButtonToFrame("<<", 30, 60, 920, 65);
        nextPage.setToolTipText("Go to the next page of search results");
        backPage.setToolTipText("Go to the previous page of search results");
        if (!(pages.isEmpty())) {
            List<JButton> firstPage = pages.get(0);
            for (JButton i : firstPage) {
                i.setVisible(true);
            }
        } else {
            for (JButton x : buttons) {
                x.setVisible(true);
            }
        }
        searchButtonIndex = 0;
        setSearchButtons(pages);
        nextPage.addActionListener(e -> {
            System.out.println("Current index: " + searchButtonIndex);
            if (pages.isEmpty()) {
                JOptionPane.showMessageDialog(gui.getFrame(), "There are no further pages for you to move between!");
                System.out.println("Pages list is empty!");
                return;
            }
            shiftPage(searchButtonIndex, 1, gui); //Go forward one page
        });
        backPage.addActionListener(e -> {
            System.out.println("Current index: " + searchButtonIndex);
            if(pages.isEmpty()) {
                JOptionPane.showMessageDialog(gui.getFrame(), "There are no further pages for you to move through!");
                System.out.println("Page is empty!");
                return;
            }
            shiftPage(searchButtonIndex, -1, gui); //Go back one page
        });
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                System.out.println("An exception was caught! Look at the below stack trace!");
                e.printStackTrace();
            }
        }
        return formSelected;
    }
}