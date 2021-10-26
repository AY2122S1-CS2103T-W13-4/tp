package seedu.address.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import seedu.address.model.friend.Friend;
import seedu.address.model.gamefriendlink.GameFriendLink;

import java.util.ArrayList;

/**
 * Table showing Friends/Games and their corresponding usernames and skills levels.
 */
public class FriendMainCardTable extends UiPart<Region> {

    private static final String FXML = "FriendMainCardTable.fxml";
    private ObservableList<Friend> friendList;

    @FXML
    private TableView<GameFriendLink> tableView;

    @FXML
    private TableColumn<GameFriendLink, String> gameCol;

    @FXML
    private TableColumn<GameFriendLink, String> usernameCol;

    @FXML
    private TableColumn<GameFriendLink, String> skillCol;

    public FriendMainCardTable(Friend friend) {
        super(FXML);

            tableView.setItems(FXCollections.observableList(new ArrayList<>(friend.getGameFriendLinks())));

            gameCol.setCellValueFactory(link -> new SimpleStringProperty(link.getValue().getGameId().toString()));
            usernameCol.setCellValueFactory(link -> new SimpleStringProperty(link.getValue().getUserName().toString()));
            skillCol.setCellValueFactory(link -> new SimpleStringProperty(link.getValue().getSkillValue().toString()));

    }

    public FriendMainCardTable() {
        super(FXML);
    }
}
