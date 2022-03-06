package me.longbow122.Homeroom;

import me.longbow122.Homeroom.utils.ConfigReader;
import me.longbow122.Homeroom.utils.GUIUtils;

import javax.swing.*;
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
    // LOGIN PAGE WORKS DECENTLY, JUST NEEDS TO HAVE IT LOG INTO THE DB CLUSTER WITH USER INPUT
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

        GUIUtils frame = new GUIUtils("Homeroom Login", 300, 600, 700, 300);
        frame.addLabelToFrame("Username: ", 30, 100, 100, 30, false);
        JTextField usernameField = frame.addTextField(100, 100, 400, 25);
        frame.addLabelToFrame("Password: ", 30, 125, 100, 30, false);
        JTextField passwordField = frame.addPasswordField(100, 125, 400, 25);
        frame.addLabelToFrame("Homeroom Login", 225, 65, 1000, 30, true);
        JButton loginButton = frame.addButtonToFrame("Login", 30, 400, 100, 150);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if(username.equals("longbow") && password.equals("ahaha")) {
                JOptionPane.showConfirmDialog(frame.getFrame(), "Success?");
                return;
            } //TODO LOGIN LOGIC WOULD GO HERE
            JOptionPane.showMessageDialog(frame.getFrame(), "Failure to login!");
        });
    }

    /**
     * This method is used to write the file into the Homeroom folder for use by the program.
     * If file exists in the following path, then there's no need to do anything.
     * If it doesn't exist, create it.
     * Handles {@link javax.print.URIException} catching due to the call made at #createNewFile()
     * @return {@link File} object which should be a valid configuration file.
     */
    private static File getConfigFile() {
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
