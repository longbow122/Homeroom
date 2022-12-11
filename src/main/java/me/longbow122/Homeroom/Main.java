package me.longbow122.Homeroom;

import me.longbow122.Homeroom.features.ClassManagement;
import me.longbow122.Homeroom.features.FormManagement;
import me.longbow122.Homeroom.features.StudentManagement;
import me.longbow122.Homeroom.features.TeacherManagement;
import me.longbow122.Homeroom.utils.ConfigReader;
import me.longbow122.Homeroom.utils.DBUtils;
import me.longbow122.Homeroom.utils.GUIUtils;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * The main class which will store all called programs, and the main method to be run.
 */
public class Main {

    /*
    Main method to be called.
    The called program
     */
    public static void main(String[] args) {
        File config = Main.getConfigFile();
        System.out.println(new ConfigReader(config).getConnectionStringFromConfig());
        openLoginPage();
    }

    /*
    Got to be a cleaner way of writing the below code. This just seems messy and outright untidy.
    Open to worded suggestions if anyone had any. (As in, you tell me what you think would work, but try to avoid directly
    spoonfeeding the answer where possible. Thank you!)

    Thinking I could bring the action listener for the login button into the fieldList array if I change its type to Component.
    But then how would I add the action listener without ending up with the same amount of lines as before?
    Trying to keep lines of code to a minimum and clean up this code where possible.
     */
    private static void openLoginPage() {
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
                                case 1 -> { //Timed out or failed connection
                                    System.out.println("Connection timed out, failed connection!");
                                    Object[] options = new Object[]{"Reconnect", "Cancel, Exit Homeroom"};
                                    int reconnectOption = JOptionPane.showOptionDialog(loginGUI.getFrame(), "Failed to connect due to a timeout! Would you like to reconnect?", "Homeroom failed to connect!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"), options, "Test");
                                    System.out.println(reconnectOption);
                                    switch (reconnectOption) {
                                        case 0 -> {
                                            System.out.println("Reconnect button clicked!");
                                            connection = new DBUtils(username, password).isConnected();
                                        }
                                        case 1 -> {
                                            System.out.println("Cancel button pressed!");
                                            System.exit(0);
                                        }
                                        default -> {
                                            System.out.println("Failed login!");
                                            JOptionPane.showMessageDialog(loginGUI.getFrame(), "Failed login due to a misc error. Please try again!", "Whoops!", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                }
                                case 2 -> { //Bad credentials passed through
                                    System.out.println("Bad credentials passed through, cannot log in!");
                                    JOptionPane.showMessageDialog(loginGUI.getFrame(), "Failed to connect due to bad credentials! Please try logging in again.", "Whoops!", JOptionPane.ERROR_MESSAGE);
                                    connection = 99;
                                }
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
                            case 0 -> System.out.println("Reconnect button clicked!");
                            case 1 -> System.out.println("Cancel button pressed!");
                            default -> {
                                System.out.println("Failed login!");
                                JOptionPane.showMessageDialog(loginGUI.getFrame(), "Failed login due to a misc error. Please try again!", "Whoops!", JOptionPane.ERROR_MESSAGE);
                            }
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
     * Got to be a cleaner way of writing the below code. This just seems messy and outright untidy.
     * Open to worded suggestions if anyone had any. (As in, you tell me what you think would work, but try to avoid directly
     * spoonfeeding the answer where possible. Thank you!)
     */
    private static void openMainGUI(String username, String password) {
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
        admin.setText("<html>" + "Configure " + "<br>" + "Homeroom" + "</html>"); // ? <br> is an empty HTML tag, no need to close that one!
        admin.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.BOLD, 40));
        admin.setToolTipText("Configure Homeroom here.");
        JButton manageForms = mainGUI.addButtonToFrame("Manage Forms", 300, 400, 400, 350);
        manageForms.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.BOLD, 40));
        manageForms.setToolTipText("Manage Form Groups here.");
        JButton classes = mainGUI.addButtonToFrame("My Classes", 300, 400, 800, 350);
        classes.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.BOLD, 40));
        classes.setToolTipText("View the classes you teach here.");
        JButton exit = mainGUI.addButtonToFrame("Log Out and Exit", 50, 150, 1050, 0);
        exit.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.PLAIN, 16));
        exit.setToolTipText("Logout and Exit Homeroom.");
        JLabel usernameField = mainGUI.addLabelToFrame("Username: " + username, 0, 0, 300, 30, false);
        usernameField.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.PLAIN, 25));
        JLabel permissionField = mainGUI.addLabelToFrame("Permissions: ", 300, 0, 300, 30, false);
        int permission = new DBUtils(username, password).getPermission();
        //TODO ADD THE FUNCTIONALITY FOR THE BUTTONS THAT HAVE BEEN REMOVED
        // ! Ensure that impossible buttons get added in terms of functionality
        JButton[] hide = {timetable, classes};
        for(JButton x : hide) {
            x.setVisible(false); //TODO GET RID OF THIS ASAP!!! ONCE THE MAIN CLASSES CONNECTION FEATURE IS DONE
            // TODO MAKE A START ON BOTH OF THESE BUTTONS AS SOON AS THE MAIN PROGRAM IS DONE
        }
        switch(permission) { // ! Should be a better way of converting permissions. Is it worth just using an Enum here, like I've done with search type?
            case 1: // ! Using ENUMS would mean I can just directly get the enum and convert it to a string instead of working on a switch statement to handle showing permissions.
                permissionField.setText("Permissions: TEACHER");
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
        manageStudents.addActionListener(e -> new StudentManagement().openManageStudentsGUI(username, password));
        manageForms.addActionListener(e -> new FormManagement().openManageFormsGUI(username, password));
        manageClasses.addActionListener(e -> new ClassManagement().openManageClassGUI(username, password));
        admin.addActionListener(e -> {
            //TODO MAKE GUI THAT OPENS FOR THE CONFIGURE SECTION
            // HAVE A SECTION WITHIN THAT CONFIGURE SECTION WHICH OPENS MANAGE TEACHERS
            if(permission != 2) {
                System.out.println("User cannot access this GUI, since they are not an admin!");
                JOptionPane.showMessageDialog(mainGUI, "You are not allowed to access this section of Homeroom, since you are not an Admin!", "Homeroom | Admin Configuration", JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
                return;
            }
            openConfigureGUI(username, password);
        });
        //TODO
        // BUTTON LISTENERS FOR EVERY BUTTON.
    }

    private static void openConfigureGUI(String username, String password) {
        GUIUtils gui = new GUIUtils("Homeroom | Configure Homeroom", 1000, 1220, 300, 0, false);
        JButton manageTeachers = gui.addButtonToFrame("Manage Teachers", 300, 400, 0, 50);
        manageTeachers.addActionListener(e -> new TeacherManagement().openManageTeacherGUI(username, password));
        manageTeachers.setFont(new Font(gui.getFrame().getFont().getName(), Font.BOLD, 40));
        JButton exit = gui.addButtonToFrame("Exit Teacher Management", 50, 150, 1050, 0);
        exit.addActionListener(e -> {
            System.out.println("Exiting Configure GUI!");
            gui.closeFrame();
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