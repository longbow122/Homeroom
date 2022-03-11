package me.longbow122.Homeroom;

import me.longbow122.Homeroom.utils.ConfigReader;
import me.longbow122.Homeroom.utils.DBUtils;
import me.longbow122.Homeroom.utils.GUIUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * The main class which will store all called programs, and the main method to be run.
 */
public class Main {

    //TODO
    // HAVE THE MAIN GUI'S BUTTON LISTENERS WORKED ON ONE BY ONE, WORKING ON THE BACKEND AS PROGRESS IS MADE
    // LOGIN BUTTON ALSO NEEDS TO TAKE IN LOGIN INFORMATION UPON PRESSING OF THE ENTER KEY
    // WORK ON CONNECTING TO DB CLUSTER BEFORE WORRYING ABOUT TAKING IN INPUT

    /*
    Main method to be called.
    The called program
     */
    public static void main(String[] args) {
        File config = getConfigFile();
        System.out.println(new ConfigReader(config).getConnectionStringFromConfig());
        openLoginPage();
    }

    private static void openLoginPage() {
        GUIUtils loginGUI = new GUIUtils("Homeroom Login", 300, 600, 700, 300);
        loginGUI.addLabelToFrame("Username: ", 30, 100, 100, 30, false);
        JTextField usernameField = loginGUI.addTextField(100, 100, 400, 25);
        loginGUI.addLabelToFrame("Password: ", 30, 125, 100, 30, false);
        JTextField passwordField = loginGUI.addPasswordField(100, 125, 400, 25);
        loginGUI.addLabelToFrame("Homeroom Login", 225, 65, 1000, 30, true);
        JButton loginButton = loginGUI.addButtonToFrame("Login", 30, 400, 100, 150);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            switch (new DBUtils(username, password).isConnected()){
                case 0:
                    System.out.println("Successful connection!");
                    loginGUI.getFrame().setVisible(false);
                    openMainGUI(username);
                    break;
                case 1: //Timed Out or failed connection.
                    System.out.println("Connection timed out, failed connection!");
                    JOptionPane.showMessageDialog(loginGUI.getFrame(), "Failed to connect due to a timeout! Would you like to reconnect?", "Whoops!", JOptionPane.ERROR_MESSAGE);
                    break; //TODO MAKE A NICER OPTION PANE WITH RECONNECT AND CANCEL OPTIONS
                case 2: //Security exception, bad credentials!
                    System.out.println("Bad credentials passed through, cannot log in!");
                    JOptionPane.showMessageDialog(loginGUI.getFrame(), "Failed to connect due to bad credentials! Please try logging in again.", "Whoops!", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    System.out.println("Failed login!");
                    JOptionPane.showMessageDialog(loginGUI.getFrame(), "Failed login due to a misc error. Please try again!", "Whoops!", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        });
    }

    private static void openMainGUI(String username) {
        GUIUtils mainGUI = new GUIUtils("Homeroom", 1000, 1220, 300, 0);
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
        permissionField.setFont(new Font(mainGUI.getFrame().getFont().getName(), Font.PLAIN, 25));
        //TODO
        // HAVE USER INFORMATION DISPLAY AT THE TOP
        // USERNAME AND PERMISSIONS NEED TO BE SHOWN AT THE TOP.
        // BUTTON LISTENERS NEED TO BE SHOWN
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