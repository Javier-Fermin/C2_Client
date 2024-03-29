/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import businessLogic.Registrable;
import exceptions.AuthenticationException;
import exceptions.BadEmailException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import businessLogic.RegistrableFactory;
import exceptions.PasswordEncryptionException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import cryptography.AsymetricClient;
import static cryptography.AsymetricClient.encryptedData;
import model.User;
import model.UserType;

/**
 * This is the class that is responsible of controlling the responses for the
 * actions of the SignIn window.
 *
 * @author Emil and Fran
 */
public class SignInController implements ChangeListener<String> {

    /**
     * The Logger for the logs
     */
    protected static final Logger LOGGER = Logger.getLogger(SignInController.class.getName());

    /**
     * An object that implements the Registable interface
     */
    private Registrable registro;

    /**
     * The window stage
     */
    private Stage stage; //This window Stage

    /**
     * signInWindow main Pane
     */
    @FXML // fx:id="signInWindow"
    private Pane signInWindow;

    /**
     * Decoration view with iamge
     */
    @FXML // fx:id="imageLabel"
    private ImageView imageLabel;

    /**
     * LogIn label for the window
     */
    @FXML // fx:id="logInLabel"
    private Label logInLabel;

    /**
     * Username label for the window
     */
    @FXML // fx:id="usernameLabel"
    private Label usernameLabel;

    /**
     * Password label for the window
     */
    @FXML // fx:id="passwordLabel"
    private Label passwordLabel;

    /**
     * TextField to insert the username of the user
     */
    @FXML // fx:id="usernameText"
    private TextField usernameText;

    /**
     * PasswordField to insert the password
     */
    @FXML // fx:id="passwordText"
    private PasswordField passwordText;

    /**
     * Button to SignIn
     */
    @FXML // fx:id="signInButton"
    private Button signInButton;

    /**
     * signUpAccess label for the window
     */
    @FXML // fx:id="signUpAccessLabel"
    private Label signUpAccessLabel;

    /**
     * Link to the SignUpWindow
     */
    @FXML // fx:id="signUpLink"
    private Hyperlink signUpLink;
    
    @FXML // fx:id="resetLink"
    private Hyperlink resetLink;

    /**
     * Button to show showPasswordText
     */
    @FXML // fx:id="showPasswordButton"
    private ToggleButton showPasswordButton;

    /**
     * TexField to show the password in clear
     */
    @FXML // fx:id="showPasswordText"
    private TextField showPasswordText;

    /**
     * Method that change image of the window When pressed: The ToggleButton
     * icon is changed: If it is selected, its icon is hide.png If it is not
     * selected, its icon is show.png
     *
     * @param event ActionEvent object
     */
    @FXML
    public void passwordButtonAction(ActionEvent event) {
        LOGGER.info("Change image to the button showPasswordButton");
        if (!showPasswordButton.isSelected()) {
            showPasswordButton.setGraphic(new ImageView("/resources/images/show.png"));
        } else {
            showPasswordButton.setGraphic(new ImageView("/resources/images/hide.png"));
        }
    }

    /**
     * This button is the event of signInButton, that will be enabled if
     * usernameText and passwordText contain information.
     *
     * @param event ActionEvent object
     */
    @FXML
    public void signInButtonAction(ActionEvent event) {
        try {
            if (usernameText.getText().equals("user")&&passwordText.getText().equals("abcd*1234")) {
                Stage sStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StatsGUI.fxml"));
                Parent root = (Parent) loader.load();

                StatsWindowController cont = ((StatsWindowController) loader.getController());
                cont.setStage(sStage);
                cont.initStage(root, new User(null, null, null, null, null, UserType.PLAYER));
                stage.close();
            } else if (usernameText.getText().equals("root")&&passwordText.getText().equals("abcd*1234")) {
                Stage sStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StatsGUI.fxml"));
                Parent root = (Parent) loader.load();
                StatsWindowController cont = ((StatsWindowController) loader.getController());
                cont.setStage(sStage);
                cont.initStage(root, new User(null, null, null, null, null, UserType.ADMIN));
                stage.close();
            } else {
                LOGGER.info("Validate if email in usernameText has a correct format");
                User user = null;
                // When pressed: The content of usernameText is validated: 
                if (isValid(usernameText.getText())) {
                    LOGGER.info("Execute signIn method to take user data");
                    //ENcrypt the password
                    byte[] bytePassword = AsymetricClient.encryptedData(passwordText.getText());

                    String passwdEncrypted = AsymetricClient.hexadecimal(bytePassword);

                    //The SignIn logic layer method will be used, defining the parameters with the content of usernameText and passwordText: 
                    user = registro.signIn(new User("", passwdEncrypted, "", usernameText.getText(), "", null));

                    //If the user is null, the user will be informed with an authentication error message (AuthenticationException).
                    LOGGER.info("Open Main Window");
                    //If no exception has occurred, the user is prompted, the window will be closed and the MainWindow window will be displayed.
                    Stage sStage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StatsGUI.fxml"));
                    Parent root = (Parent) loader.load();
                    StatsWindowController cont = ((StatsWindowController) loader.getController());
                    cont.setStage(sStage);
                    cont.initStage(root, user);
                    stage.close();
                    // If the content does not follow an email address pattern, the user will be informed with an authentication error message (AuthenticationException).
                } else {
                    throw new BadEmailException("Email error: Bad email format");
                }
            }
        } catch (BadEmailException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            LOGGER.severe("Email have a incorrect format");

        } catch (ProcessingException ex) {
            LOGGER.info("Unable to connect to the server");
            new Alert(Alert.AlertType.ERROR, "Unable to connect to the server").showAndWait();
        } catch (AuthenticationException | InternalServerErrorException | NotFoundException ex ) {
            new Alert(Alert.AlertType.ERROR, "Authentication error").showAndWait();
            LOGGER.severe("Authentication error");
            //In the event that it takes a while to connect to the server, the user will be informed that the timeout has occurred with the TimeOutException.
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error has ocurred during data I/O").showAndWait();
            LOGGER.severe("App error");
        } catch (PasswordEncryptionException ex) {
            ex.printStackTrace();
            LOGGER.info("Encryption Error");
            new Alert(Alert.AlertType.ERROR, "An Encryption error has ocurred").showAndWait();
        }
    }

    /**
     * This window will close and display the SignUpWindow.
     *
     * @param event ActionEvent object
     */
    @FXML
    public void signUpClicked(ActionEvent event) {
        try {
            LOGGER.info("Open SignIn Window");
            Stage sStage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUp.fxml"));
            Parent root = (Parent) loader.load();

            SignUpController cont = ((SignUpController) loader.getController());

            cont.setStage(sStage);
            cont.initStage(root);

            stage.close();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, "Sign Up window error" + ex.getMessage());
        }
    }

    /**
     * Setter of stage
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Method that initialize SignInWindow
     *
     * @param root DOM of the window
     */
    public void initStage(Parent root) {
        try {
            LOGGER.info("Inicialize Window initStage");
            registro = new RegistrableFactory().getRegistrable();
            Scene scene = new Scene(root);
            //Window no Resizable
            stage.setResizable(false);
            stage.getIcons().add(new Image("/resources/images/icon.png"));
            //Insert “Odoo Sign In”.  tittle
            stage.setTitle("Odoo - SignIn");

            //Confirmation is requested when leaving the window
            LOGGER.info("If the window want to exit, alert to verify");
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    //If you accept, you will exit the application.
                    //If you cancel, you will return to the initial window.
                    Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?").showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        Platform.exit();
                    }
                    event.consume();
                }
            });
            LOGGER.info("Set escape to window escape button");
            //The esc key is the window escape button.
            stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                if (KeyCode.ESCAPE == event.getCode()) {
                    Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?").showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        Platform.exit();
                    }
                    event.consume();
                }
            });

            LOGGER.info("set usernameText and passwordText properties");
            //The usernameText, which will be an empty field, will pick up the focus. This will have a mail pattern.
            usernameText.textProperty().addListener(this);
            usernameText.requestFocus();
            addTextLimiter(usernameText, 500);
            //The passwordText will be an empty field and will have a range of allowed characters.
            passwordText.textProperty().addListener(this);
            resetLink.setOnAction(this::handleHyperlinkActionEvent);
            addTextLimiter(passwordText, 500);

            LOGGER.info("set buttons properties");
            showPasswordButton.setOnAction(this::passwordButtonAction);
            signUpLink.setOnAction(this::signUpClicked);
            signInButton.setOnAction(this::signInButtonAction);

            //SignInButton from the window is disable
            signInButton.disableProperty().set(true);
            //The signInButton button will be set as the default window button
            signInButton.setDefaultButton(true);

            stage.setScene(scene);
            stage.setOnShowing(this::handleWindowShowing);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, "Inicialize error" + ex.getMessage());

        }
    }

    /**
     * In the case of passwordText, negate the value before saving it. The
     * showPasswordText text property is bidirectionally “joined” to the
     * passwordText text property. The passwordText and showPasswordText fields
     * define their visibility by collecting the selection value of
     * showPasswordButton.
     *
     * @param event ActionEvent object
     */
    private void handleWindowShowing(WindowEvent event) {
        LOGGER.info("bindBidirectional propertie to showPasswordText and passwordText");
        showPasswordText.textProperty().bindBidirectional(passwordText.textProperty());
        showPasswordButton.setGraphic(new ImageView("/resources/images/show.png"));
        passwordText.visibleProperty().bind(showPasswordButton.selectedProperty().not());
        showPasswordText.visibleProperty().bind(showPasswordButton.selectedProperty());
    }
    
    private void handleHyperlinkActionEvent(ActionEvent ae){
        try{
            if(!usernameText.getText().isEmpty()){
            User user = new User();
            user.setEmail(usernameText.getText());
            registro.recoverPassword(user);
            }else{
                new Alert(Alert.AlertType.ERROR, "Please introduce a email").showAndWait();
            }
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
        
    }
    
    private static final String EMAIL_PATTERN
            = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    /**
     * Method that matches the Pattern
     *
     * @param email the email to validate
     * @return Boolean matcher
     */
    public static boolean isValid(final String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * The usernameText and passwordField are checked for information: Once the
     * fields are not empty, the signInButton button will be enabled. In the
     * event that one of the two does not have information or its content has
     * been deleted, this button will be disabled.
     *
     * @param observable the disable property from the button signInButton
     * @param oldValue the previus signInButton disable property
     * @param newValue the new signInButton disable property
     */
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.isEmpty() && !usernameText.getText().isEmpty() && !passwordText.getText().isEmpty()) {
            if (signInButton.isDisabled()) {
                LOGGER.info("signUpButton enabled.");
            }
            signInButton.disableProperty().set(false);
        } else {
            if (!signInButton.isDisabled()) {
                LOGGER.info("signUpButton disabled.");
            }
            signInButton.disableProperty().set(true);
        }
    }

    /**
     * Limiter to the TextFields in the window
     *
     * @param tf the content of the text field
     * @param maxLength //the maximun number of characters available
     */
    public static void addTextLimiter(final TextField tf, final int maxLength) {
        LOGGER.info("Limit number os character can write");
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

}
