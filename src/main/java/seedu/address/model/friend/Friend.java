package seedu.address.model.friend;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import seedu.address.model.friend.exceptions.GameLinkNotFoundException;
import seedu.address.model.game.Game;
import seedu.address.model.game.GameId;
import seedu.address.model.gamefriendlink.GameFriendLink;
import seedu.address.model.gamefriendlink.SkillValue;

/**
 * Represents a Friend in the gitGud friend's list.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Friend {
    // Identity fields
    // used to uniquely identify each Friend.
    private final FriendId friendId;
    private final FriendName friendName;
    private final Schedule schedule;

    // Data fields
    private final Map<GameId, GameFriendLink> gameFriendLinks = new HashMap<>();

    /**
     * Constructs a {@code Friend}.
     * Every field must be present and not null.
     *
     * @param friendId        a valid friend id.
     * @param friendName      a valid friend name.
     * @param gameFriendLinks a map of game-friend links of this friend.
     * @param schedule        Schedule of friend.
     */
    public Friend(FriendId friendId, FriendName friendName, Map<GameId, GameFriendLink> gameFriendLinks,
                  Schedule schedule) {
        requireAllNonNull(friendId, gameFriendLinks);
        this.friendId = friendId;
        this.friendName = friendName == null ? FriendName.DEFAULT_FRIEND_NAME : friendName;
        this.gameFriendLinks.putAll(gameFriendLinks);
        this.schedule = schedule;
    }

    /**
     * Overloaded constructor using only friendId and friendName.
     *
     * @param friendId   Unique id of friend.
     * @param friendName Name of friend.
     */
    public Friend(FriendId friendId, FriendName friendName) {
        requireAllNonNull(friendId);
        this.friendId = friendId;
        this.friendName = friendName == null ? FriendName.DEFAULT_FRIEND_NAME : friendName;
        this.schedule = new Schedule();
    }

    /**
     * Updates the skill value for the {@code GameFriendLink} with the given {@code gameId} and linked to
     * this friend.
     *
     * @param gameId     valid gameId which is already linked to this friend.
     * @param skillValue value to update friend's skill value to.
     * @throws GameLinkNotFoundException thrown when gameId provided is not linked to this friend.
     */
    public void updateGameFriendLinkSkillValue(GameId gameId, SkillValue skillValue) throws GameLinkNotFoundException {
        if (!gameFriendLinks.containsKey(gameId)) {
            throw new GameLinkNotFoundException();
        }

        gameFriendLinks.get(gameId).setSkillValue(skillValue);
    }

    public FriendId getFriendId() {
        return friendId;
    }

    public FriendName getFriendName() {
        return friendName;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Returns an immutable game-friend-link map, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Map<GameId, GameFriendLink> getGameFriendLinks() {
        return Collections.unmodifiableMap(gameFriendLinks);
    }

    /**
     * Returns true if the friend is currently associated with the game provided.
     * @param game Game to check.
     * @return True if the friend is associated with the game.
     */
    public boolean hasGameAssociation(Game game) {
        return gameFriendLinks.containsKey(game.getGameId());
    }

    /**
     * Returns true if both friends have same friendId.
     *
     * @return boolean result of equals.
     */
    public boolean isSameFriendId(Friend friend) {
        return this.friendId.equals(friend.getFriendId());
    }

    /**
     * Returns true if both friends have the same friendId, name and games.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Friend)) {
            return false;
        }

        Friend otherFriend = (Friend) other;
        return otherFriend.getFriendId().equals(getFriendId())
                && otherFriend.getGameFriendLinks().equals(getGameFriendLinks())
                && otherFriend.getFriendName().equals(getFriendName());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(friendId);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Friend ID: ")
                .append(getFriendId())
                .append("; Name: ")
                .append(getFriendName());

        Collection<GameFriendLink> gameSet = getGameFriendLinks().values();
        if (!gameSet.isEmpty()) {
            builder.append("; Games: ");
            gameSet.forEach(builder::append);
            builder.append(" ");
        }
        builder.append(schedule);
        return builder.toString();
    }
}
