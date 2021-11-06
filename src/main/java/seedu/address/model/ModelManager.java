package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.friend.Friend;
import seedu.address.model.friend.FriendId;
import seedu.address.model.game.Game;
import seedu.address.model.game.GameId;
import seedu.address.model.gamefriendlink.GameFriendLink;

/**
 * Represents the in-memory model of the gitGud friends and games list data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final FriendsList friendsList;
    private final FilteredList<Friend> filteredFriends;
    private final SortedList<Friend> filteredAndSortedFriends;
    private final GamesList gamesList;
    private final FilteredList<Game> filteredGames;
    private final UserPrefs userPrefs;

    /**
     * Initializes a ModelManager with the given friends list and userPrefs.
     */
    public ModelManager(ReadOnlyFriendsList readOnlyFriendsList, ReadOnlyGamesList readOnlyGamesList,
                        ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(readOnlyFriendsList, readOnlyGamesList, userPrefs);

        logger.fine("Initializing with friends book: " + readOnlyFriendsList
                + " games book: " + readOnlyGamesList
                + " and user prefs " + userPrefs);

        this.friendsList = new FriendsList(readOnlyFriendsList);
        this.filteredFriends = new FilteredList<>(this.friendsList.getFriendsList());
        this.filteredAndSortedFriends = new SortedList<>(filteredFriends);
        this.gamesList = new GamesList(readOnlyGamesList);
        filteredGames = new FilteredList<>(this.gamesList.getGamesList());
        this.userPrefs = new UserPrefs(userPrefs);
    }

    public ModelManager() {
        this(new FriendsList(), new GamesList(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getFriendsListFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setFriendsListFilePath(addressBookFilePath);
    }

    //=========== FriendsBook ================================================================================

    @Override
    public void setFriendsList(ReadOnlyFriendsList readOnlyFriendsList) {
        this.friendsList.resetData(readOnlyFriendsList);
    }

    @Override
    public ReadOnlyFriendsList getFriendsList() {
        return friendsList;
    }

    @Override
    public boolean hasFriend(Friend friend) {
        requireNonNull(friend);
        return friendsList.hasFriend(friend);
    }

    @Override
    public boolean hasFriendWithId(FriendId friendId) {
        requireNonNull(friendId);
        return friendsList.hasFriendWithId(friendId);
    }

    @Override
    public Friend getFriend(FriendId friendId) {
        return friendsList.getFriend(friendId);
    }

    @Override
    public void deleteFriend(FriendId targetId) {
        Friend friendToDelete =
                this.getFriendsList().getFriendsList()
                        .stream()
                        .filter(friend -> friend.getFriendId().equals(targetId))
                        .findFirst()
                        .get();
        friendsList.removeFriend(friendToDelete);
    }

    @Override
    public void addFriend(Friend friend) {
        friendsList.addFriend(friend);
        updateFilteredAndSortedFriendsList(PREDICATE_SHOW_ALL_FRIENDS);
    }

    @Override
    public void setFriend(Friend target, Friend editedFriend) {
        requireAllNonNull(target, editedFriend);
        // For debugging purposes while UI does not support displaying all fields yet.
        logger.info("ModelManager:setFriend - " + editedFriend);
        friendsList.setFriend(target, editedFriend);
    }

    @Override
    public void linkFriend(Friend toLink, GameFriendLink gameFriendLink) {
        friendsList.linkFriend(toLink, gameFriendLink);
    }

    @Override
    public void removeLinkAllFriends(Game game) {
        friendsList.removeLinkAllFriends(game);
    }

    @Override
    public void unlinkFriend(Friend toUnlink, Game game) {
        friendsList.unlinkFriend(toUnlink, game);
    }

    //=========== Filtered Friend List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Friend}s filtered by the provided predicate and sorted
     * by the {@code friendComparator} if not null.
     */
    @Override
    public ObservableList<Friend> getFilteredAndSortedFriendsList() {
        return filteredAndSortedFriends;
    }

    @Override
    public void updateFilteredAndSortedFriendsList(Predicate<Friend> predicate) {
        requireNonNull(predicate);
        // important to set the comparator to null first.
        filteredAndSortedFriends.setComparator(null);
        filteredFriends.setPredicate(predicate);
    }

    @Override
    public void updateFilteredAndSortedFriendsList(Predicate<Friend> predicate, Comparator<Friend> comparator) {
        requireAllNonNull(predicate, comparator);
        filteredFriends.setPredicate(predicate);
        filteredAndSortedFriends.setComparator(comparator);
    }

    //=========== Games List ==================================================================================

    @Override
    public void setGamesList(ReadOnlyGamesList readOnlyGamesList) {
        this.gamesList.resetData(readOnlyGamesList);
    }

    @Override
    public ReadOnlyGamesList getGamesList() {
        return gamesList;
    }

    @Override
    public boolean hasGame(Game game) {
        requireNonNull(game);
        return gamesList.hasGame(game);
    }

    @Override
    public boolean hasGameWithId(GameId gameId) {
        requireNonNull(gameId);
        return gamesList.hasGameWithId(gameId);
    }

    @Override
    public void deleteGame(GameId targetId) {
        requireNonNull(targetId);
        gamesList.removeGame(getGame(targetId));
    }

    @Override
    public void addGame(Game game) {
        gamesList.addGame(game);
        updateFilteredGamesList(PREDICATE_SHOW_ALL_GAMES);
    }

    @Override
    public Game getGame(GameId gameId) {
        return gamesList.getGame(gameId);
    }

    //=========== Filtered Game List Accessors =============================================================

    @Override
    public ObservableList<Game> getFilteredGamesList() {
        return filteredGames;
    }

    @Override
    public void updateFilteredGamesList(Predicate<Game> predicate) {
        requireNonNull(predicate);
        filteredGames.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
            // instanceof handles other == null
        } else if (!(obj instanceof ModelManager)) {
            return false;
        } else {
            // state check
            ModelManager other = (ModelManager) obj;
            return friendsList.equals(other.friendsList)
                    && filteredFriends.equals(other.filteredFriends)
                    && filteredAndSortedFriends.equals(other.filteredAndSortedFriends)
                    && gamesList.equals(other.gamesList)
                    && filteredGames.equals(other.filteredGames)
                    && userPrefs.equals(other.userPrefs);
        }
    }

}
