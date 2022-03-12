package me.longbow122.Homeroom.utils;
import javax.swing.*;
import java.awt.*;

/**
 * Utility class for the core GUI work that needs to be done within Homeroom.
 */
public class GUIUtils {

    private final Window frame;
    /**
     * New constructor to work with this class. Will return an instance of the class that can be accessed throughout the program.
     * <p>
     * </p>
     * An empty constructor should be used if you want to access any other method within the program.
     * This constructor will also open an empty {@link JFrame} which can be filled with components by using methods implemented within this class
     * as seen fit.
     * <p>
     * If more than one {@link Window} is to be opened, then it is recommended that a new object be instantiated.
     * </p>
     * @param title The title of the {@link JFrame} to be shown when opened.
     * @param height The "height" of the {@link JFrame}. How much vertical space the window should take up.
     * @param width The "width" of the {@link JFrame}. How much horizontal space the window should take up.
     * @param locationX How deep to the left into the screen the {@link JFrame} should be. Relative to the top-left corner of the screen.
     * @param locationY How deep downwards into the screen the {@link JFrame} should be. Relative to the top-left corner of the screen.
     */
    public GUIUtils(String title, int height, int width, int locationX, int locationY) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        frame.pack();
        if (height == 0) height = 1000; //TODO L36 AND 37 need reworking. Look into tenary operators.
        if (width == 0) width = 1000;
        frame.setBounds(locationX, locationY, width, height);
        frame.setLayout(null);
        frame.setVisible(true);
        this.frame = frame;
    }

    /**
     * Another constructor which can be used to instantiate the GUIUtils class. Makes use of method overloading to allow for default values within the height and width of the {@link Window}
     * @param title The title of the {@link JFrame} to be shown when opened.
     * @param locationX How deep to the left of the screen the {@link JFrame} should be. Relative to the top-left corner of the screen.
     * @param locationY How deep downwards into the screen the {@link JFrame} should be. Relative to the top-left corner of the screen.
     */
    public GUIUtils(String title, int locationX, int locationY) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        frame.pack();
        frame.setBounds(locationX, locationY, 1000, 1000);
        frame.setLayout(null);
        frame.setVisible(true);
        this.frame = frame;
    }

    /**
     * Basic method written to easily close down {@link JFrame}s.
     */
    public void closeFrame() {
        frame.setVisible(false);
    }

    /**
     * Basic getter for the {@link Window} that all of these methods refer to.
     * <p>
     *     Is useful to have when needing to do things such as {@link JOptionPane#createDialog(Component, String)}, where you do not have a
     *     parent component to actually pass through.
     * </p>
     * Other than using it for the above use-case, the use of this getter is generally not recommended unless the window is to be closed.
     * @return {@link Window} representing the JFrame that is currently open and associated with this object.
     */
    public Window getFrame() {
        return frame;
    }

    /**
     * Basic method to add buttons to a frame.
     * <p>
     *     Fills the {@link Window} that is attributed with this class.
     * </p>
     * @param buttonTitle  Name of the button in the middle.
     * @param buttonHeight How "tall" the button should be.
     * @param buttonWidth  How "wide" the button should be.
     * @param locationX    The X-coordinate where the button is to be located, relative to the top-left corner of the screen.
     * @param locationY    The Y-coordinate where the button is to be located, relative to the top-left corner of the screen.
     * @return {@link JButton} that the user is able to interact with.
     */
    public JButton addButtonToFrame(String buttonTitle, int buttonHeight, int buttonWidth, int locationX, int locationY) {
        JButton button = new JButton(buttonTitle);
        frame.add(button);
        button.setSize(buttonWidth, buttonHeight);
        button.setVisible(true);
        button.setLocation(locationX, locationY);
        return button;
    }

    /**
     * Basic method to add a text field to a frame. This will allow me to add uneditable lines of text to my GUI.
     * <p>
     *     Fills the {@link Window} using the constructor within this class and its attributes.
     * </p>
     * @param textField The text that is to be displayed within the text field
     * @param locationX The X-coordinate where the label is to be located, relative to the top-left corner of the screen.
     * @param locationY The Y-coordinate where the label is to be located, relative to the top-left corner of the screen.
     * @param bold Should the text displayed be emboldened?
     * @param height How "tall" should the text field be?
     * @param width How "wide" should the text field be?
     * @return {@link JLabel} that the user is able to see, but cannot do anything with.
     */
    public JLabel addLabelToFrame(String textField, int locationX, int locationY, int width, int height, boolean bold) {
        JLabel label = new JLabel(textField);
        frame.add(label);
        label.setBounds(locationX, locationY, width, height);
        label.setVisible(true);
        label.setOpaque(true);
        if(bold) label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        return label;
    }

    /**
     * Basic method to add a text field for one-lined input. Will come in handy when entering in student information.
     * Is used to generate a text field intended for one-lined input of any size.
     * <p>
     * Fills the {@link Window} as specified by the constructor for this class.
     * </p>
     * @param locationX The X-coordinate where the fields is to be located, relative to the top-left corner of the screen.
     * @param locationY The Y-coordinate where the field is to be located, relative to the top-left corner of the screen.
     * @param width How "wide" the text field should be.
     * @param height How "tall" the text field should be.
     * @return {@link JTextField} that can be further interacted with and used within the code. The user is able to input data here provided a listener has been written.
     */
    public JTextField addTextField(int locationX, int locationY, int width, int height) {
        JTextField field = new JTextField();
        frame.add(field);
        field.setToolTipText("Enter the username used to log into Homeroom here!");
        field.setBounds(locationX, locationY, width, height);
        field.setVisible(true);
        field.setOpaque(true);
        return field;
    }

    /**
     * Basic method to add a password field for one-lined, obfuscated input. Is only generally used to enter passwords.
     * <p>
     * Fills the {@link Window} as specified by the constructor for this class.
     * </p>
     * @param locationX The X-coordinate where the fields is to be located, relative to the top-left corner of the screen.
     * @param locationY The Y-coordinate where the field is to be located, relative to the top-left corner the screen.
     * @param width How "wide" the text field should be.
     * @param height How "tall" the text field should be.
     * @return {@link JPasswordField} that can be further interacted with and used within the code. The user is able to input data here provided a listener has been written.
     */
    public JPasswordField addPasswordField(int locationX, int locationY, int width, int height) {
        JPasswordField field = new JPasswordField();
        frame.add(field);
        field.setToolTipText("Enter the password used to log into Homeroom here!");
        field.setBounds(locationX, locationY, width, height);
        field.setVisible(true);
        field.setOpaque(true);
        return field;
    }
}