package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.friend.Friend;
import seedu.address.model.game.Game;


/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;
    private ObservableList<Friend> friendList;
    private boolean isFriendTable;

    // Independent Ui parts residing in this Ui container
    private FriendListPanel friendListPanel;
    private GameListPanel gameListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private Friend currentFriendToGet;
    private FriendMainCardTable friendMainCardTable;
    private GameMainCardTable gameMainCardTable;
    private Game currentGameToGet;


    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane friendsPlaceholder;

    @FXML
    private VBox friendBox;

    @FXML
    private VBox gameBox;

    @FXML
    private StackPane gamesPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private HBox mainCard;

    @FXML
    private VBox leftMainCard;

    @FXML
    private VBox rightMainCard;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.friendList = logic.getFilteredFriendsList();

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        friendListPanel = new FriendListPanel(logic.getFilteredFriendsList());
        showFriendList();

        gameListPanel = new GameListPanel(logic.getFilteredGamesList());
        showGameList();

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
        showFriendBox();
        showGameBox();
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    private void hideFriendBox() {
        friendBox.setVisible(false);
        friendBox.setManaged(false);
    }

    private void showFriendBox() {
        friendBox.setVisible(true);
        friendBox.setManaged(true);
    }

    private void showGameBox() {
        gameBox.setVisible(true);
        gameBox.setManaged(true);
    }

    private void addFriendListPanelToFriendsPlaceholder() {
        // only shows friend list if not already being shown
        if (!friendsPlaceholder.getChildren().contains(friendListPanel.getRoot())) {
            friendsPlaceholder.getChildren().add(friendListPanel.getRoot());
        }
    }
    private void addGameListPanelToGamesPlaceholder() {
        if (!gamesPlaceholder.getChildren().contains(gameListPanel.getRoot())) {
            gamesPlaceholder.getChildren().add(gameListPanel.getRoot());
        }
    }

    /**
     * Shows the {@Code friendListPanel} when the {@Code friend --list} command is run.
     */
    private void showFriendList() {
        // only shows friend list if not already being shown
        addFriendListPanelToFriendsPlaceholder();
    }

    private void showGameList() {
        addGameListPanelToGamesPlaceholder();
    }

    private void handleFriendGet(Friend friendToGet) {
        currentFriendToGet = friendToGet;
        if (currentFriendToGet == null) {
            rightMainCard.getChildren().clear();
            return;
        }
        if (this.friendList.stream().filter(x -> x.isSameFriendId(currentFriendToGet)).count() != 0) {
            friendToGet = this.friendList
                    .stream()
                    .filter(x -> x.isSameFriendId(currentFriendToGet))
                    .findFirst()
                    .get();

            friendMainCardTable = new FriendMainCardTable(friendToGet);
            rightMainCard.getChildren().clear();
            rightMainCard.getChildren().add(friendMainCardTable.getRoot());
        } else {
            rightMainCard.getChildren().clear();
            friendMainCardTable = new FriendMainCardTable();
            rightMainCard.getChildren().add(friendMainCardTable.getRoot());
        }
    }

    private void handleGameGet(Game gameToGet) {
        currentGameToGet = gameToGet;
        if (currentGameToGet == null) {
            rightMainCard.getChildren().clear();
            return;
        }
        if (friendList.stream().filter(friend -> friend.hasGameAssociation(gameToGet)).count() != 0) {
            List<Friend> friendsWithGame = friendList.stream()
                    .filter(friend -> friend.hasGameAssociation(gameToGet))
                    .collect(Collectors.toList());

            gameMainCardTable = new GameMainCardTable(FXCollections.observableList(friendsWithGame), gameToGet);
            rightMainCard.getChildren().clear();
            rightMainCard.getChildren().add(gameMainCardTable.getRoot());
        } else {
            rightMainCard.getChildren().clear();
            gameMainCardTable = new GameMainCardTable();
            rightMainCard.getChildren().add(gameMainCardTable.getRoot());
        }
    }


    /**
     * Handles the mounting and dismounting of UI Regions when a {@Code friend}
     * command is run.
     * @param commandResult The {@Code commandResult} from the {@Code friend} command.
     */
    private void handleFriendCommand(CommandResult commandResult) {
    }



    /**
     * Handles the mounting and dismounting of UI Regions when a {@Code game}
     * command is run.
     * @param commandResult The {@Code commandResult} from the {@Code game} command.
     */
    private void handleGameCommand(CommandResult commandResult) {
    }

    /**
     * Executes the command based on the {@Code CommandType}
     * enumeration.
     *
     * @see Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            switch (commandResult.getCommandType()) {
            case FRIEND_GET:
                isFriendTable = true;
                handleFriendGet(commandResult.getFriendToGet());
                break;
            case FRIEND_ADD_GAME_SKILL:
            case FRIEND_ADD:
            case FRIEND_EDIT:
            case FRIEND_LIST:
            case CLEAR:
            case FRIEND_LINK:
            case FRIEND_UNLINK:
            case FRIEND_DELETE:
                if (isFriendTable) {
                    handleFriendGet(this.currentFriendToGet);
                } else {
                    handleGameGet(this.currentGameToGet);
                }
                break;
            case GAME_GET:
                isFriendTable = false;
                handleGameGet(commandResult.getGameToGet());
                break;
            case GAME_ADD: case GAME_DELETE: case GAME_LIST:
                if (isFriendTable) {
                    handleFriendGet(this.currentFriendToGet);
                } else {
                    handleGameGet(this.currentGameToGet);
                }
                break;
            case HELP:
                handleHelp();
                break;
            case EXIT:
                handleExit();
                break;
            default:
                break;
            }
            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
