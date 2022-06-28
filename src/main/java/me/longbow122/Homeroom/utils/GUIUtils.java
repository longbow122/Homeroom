package me.longbow122.Homeroom.utils;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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
     * @param defaultClose Should the window shut down the entire program when closed? True if yes, False if no.
     */
    public GUIUtils(String title, int height, int width, int locationX, int locationY, boolean defaultClose) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        if(defaultClose) {
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        frame.pack();
        frame.setBounds(locationX, locationY, width, height);
        frame.setLayout(null);
        frame.setVisible(true);
        this.frame = frame;
        try {
            frame.setIconImage(ImageIO.read(new File("homeroom.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if(bold) return addLabelToFrame(textField, locationX, locationY, width, height, true, 14);
        return addLabelToFrame(textField, locationX, locationY, width, height, false, 11);
    }

    /**
     * Basic method to add a text field to a frame. This will allow me to add uneditable lines of text to my GUI.
     * <p>
     *     Fills the {@link Window} using the constructor within this class and its attributes.
     * </p>
     * Uses method overloading to allow for an optional size parameter.
     * @param textField The text that is to be displayed within the text field
     * @param locationX The X-coordinate where the label is to be located, relative to the top-left corner of the screen.
     * @param locationY The Y-coordinate where the label is to be located, relative to the top-left corner of the screen.
     * @param bold Should the text displayed be emboldened?
     * @param height How "tall" should the text field be?
     * @param width How "wide" should the text field be?
     * @param size How "large" should the text within the field be?
     * @return {@link JLabel} that the user is able to see, but cannot do anything with.
     */
    public JLabel addLabelToFrame(String textField, int locationX, int locationY, int width, int height, boolean bold, int size) {
        JLabel label = new JLabel(textField);
        frame.add(label);
        label.setBounds(locationX, locationY, width, height);
        label.setVisible(true);
        label.setOpaque(true);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, size));
        if(bold) {
            label.setFont(label.getFont().deriveFont(Font.BOLD, size));
        }
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
     * @param toolTip The text to be displayed to the user which describes the text field to them.
     * @return {@link JTextField} that can be further interacted with and used within the code. The user is able to input data here provided a listener has been written.
     */
    public JTextField addTextField(int locationX, int locationY, int width, int height, String toolTip) {
        JTextField field = new JTextField();
        frame.add(field);
        field.setToolTipText(toolTip);
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

    /**
     * Basic method to add a drop-down menu for selection of particular options or enums. <p></p> Fills the {@link Window} as specified by the constructor for this class.
     * @param locationX The X-Coordinate where the menu is to be located, relative to the top-left corner of the screen.
     * @param locationY The Y-coordinate where the menu is to be located, relative to the top-left corner of the screen.
     * @param width How "wide" the text field should be.
     * @param height How "tall" the text field should be.
     * @param menuChoices String array for the particular choices within the drop-down menu.
     * @return {@link JComboBox} that can be further interacted with and used within the code. The user is able to input data here provided a listener has been written.
     */
    public JComboBox addComboBox(int locationX, int locationY, int width, int height, String[] menuChoices) {
        JComboBox menuBox = new JComboBox(menuChoices);
        frame.add(menuBox);
        menuBox.setToolTipText("Use this menu to select the piece of data to search for students by.");
        menuBox.setBounds(locationX, locationY, width, height);
        menuBox.setVisible(true);
        menuBox.setOpaque(true);
        return menuBox;
    }

    /**
     * Basic method to add a progress bar to any GUI of my choosing. <p></p> Fills the {@link Window} as specified by the constructor for this class.
     * @param locationX The X-Coordinate where the bar is to be located, relative to the top-left corner of the screen.
     * @param locationY The Y-Coordinate where the bar is to be located, relative to the top-left corner of the screen.
     * @param width How "wide" the bar should be.
     * @param height How "tall" the bar should be.
     * @param progress The number in % that the bar should be filled by.
     * @return {@link JProgressBar} that can be further interacted with and used by the code.
     */
    public JProgressBar addProgressBar(int locationX, int locationY, int width, int height, int progress) {
        JProgressBar bar = new JProgressBar();
        bar.setBounds(locationX, locationY, width, height);
        bar.setValue(progress);
        bar.setVisible(true);
        bar.setStringPainted(true);
        bar.setOrientation(SwingConstants.HORIZONTAL);
        frame.add(bar);
        return bar;
    }

    /**
     * Basic method to add a "DatePicker" to any GUI. <p></p> Fills the {@link Window} as specified by the constructor for this method. <p></p>
     * The DatePicker allows users to click on the button that is generated, and a calender GUI will appear which the user is then able to pick a date from. <p></p>
     * A {@link com.github.lgooddatepicker.optionalusertools.DateChangeListener} can then be used with this object to allow me to perform certain actions with the date and on the event this happens.
     * The {@link DatePicker} class from the LGoodDatePicker library was used to handle most of the logic within this method.
     * @param locationX The X-Coordinate where the date picking button is to be located, relative to the top-left corner of the screen.
     * @param locationY The Y-Coordinate where the bar is to be located, relative to the top-left corner of the screen.
     * @param width How "wide" the button should be. Doesn't seem to be working very well, but it's not too big of a problem.
     * @param height How "tall" the button should be. Doesn't seem to be working very well, but it's not too big of a problem.
     * @param text The "title" of the button. What text should the user see on the button to ensure that they know what it does upon clicking it?
     * @param tooltip The tool tip text within the button. What text should be shown to the user when they hover over it?
     * @return {@link DatePicker} object which has been placed within a GUI and can still be further interacted with and used.
     */
    public DatePicker addDatePicker(int locationX, int locationY, int width, int height, String text, String tooltip) {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setAllowEmptyDates(false);
        settings.setEnableMonthMenu(true);
        settings.setAllowKeyboardEditing(false);
        settings.setEnableYearMenu(true);
        settings.setVisibleClearButton(false);
        settings.setVisibleDateTextField(false);
        DatePicker dp = new DatePicker(settings);
        dp.setBounds(locationX, locationY, width, height);
        frame.add(dp);
        dp.setVisible(true);
        dp.getComponentToggleCalendarButton().setText(text);
        dp.getComponentToggleCalendarButton().setToolTipText(tooltip);
        return dp;
    }

    /**
     * Basic method to add a multi-line, multi-column, editable (unless specified otherwise through a method) area of text input. <p></p> Fills the {@link Window} as specified by the constructor.
     * @param locationX The X-Coordinate where the area is to be located, relative to the top-left corner of the screen.
     * @param locationY The Y-Coordinate where the area is to be located, relative to the top-left corner of the screen.
     * @param width How "wide" the area should be.
     * @param height How "tall" the area should be.
     * @param tooltip The tooltip text within the area. What text should be shown to the user when they hover over this?
     * @return {@link JTextArea} object which has been placed within a GUI and can still be further interacted with and used.
     */
    public JTextArea addTextArea(int locationX, int locationY, int width, int height, String tooltip) {
        JTextArea area = new JTextArea();
        //area.setBounds(locationX, locationY, width, height);
        area.setToolTipText(tooltip);
        area.setVisible(true);
        JScrollPane scroll = new JScrollPane(area);
        frame.add(scroll);
        scroll.setBounds(locationX, locationY, width, height);
        scroll.setVisible(true);
        area.setFont(new Font(frame.getFont().getName(), Font.PLAIN, frame.getFont().getSize()));
        return area;
    }
}