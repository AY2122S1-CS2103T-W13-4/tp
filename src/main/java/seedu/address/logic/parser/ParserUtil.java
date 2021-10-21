package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.friend.FriendId;
import seedu.address.model.friend.FriendName;
import seedu.address.model.game.GameId;
import seedu.address.model.gamefriendlink.GameFriendLink;
import seedu.address.model.gamefriendlink.SkillValue;
import seedu.address.model.gamefriendlink.UserName;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Private constructor to hide implicit public constructor since
     * {@code ParserUtil} is a utility class.
     */
    private ParserUtil() {
    }

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String friendId} into a {@code FriendId}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code friendId} is invalid.
     */
    public static FriendId parseFriendId(String friendId) throws ParseException {
        requireNonNull(friendId);
        String trimmedName = friendId.trim();
        if (friendId.isBlank()) {
            throw new ParseException(FriendId.MESSAGE_EMPTY_FRIEND_ID);
        } else if (!FriendId.isValidFriendId(trimmedName)) {
            throw new ParseException(FriendId.MESSAGE_INVALID_CHARACTERS);
        }
        return new FriendId(trimmedName);
    }

    /**
     * Parses a {@code String gameId} into a {@code GameId}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code gameId} is invalid.
     */
    public static GameId parseGameId(String gameId) throws ParseException {
        requireNonNull(gameId);
        String trimmedName = gameId.trim();
        if (!GameId.isValidGameId(trimmedName)) {
            throw new ParseException(GameId.MESSAGE_INVALID_CHARACTERS_IN_GAME_ID);
        }
        return new GameId(trimmedName);
    }

    /**
     * Parses a {@code String friendName} into a {@code FriendName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code friendName} is invalid.
     */
    public static FriendName parseFriendName(String friendName) throws ParseException {
        requireNonNull(friendName);
        String trimmedName = friendName.trim();
        if (!FriendName.isValidName(trimmedName)) {
            throw new ParseException(FriendName.MESSAGE_CONSTRAINTS);
        }
        return new FriendName(trimmedName);
    }

    /**
     * Parses {@code Collection<String> games} into a {@code Set<Game>}.
     */
    public static Set<GameFriendLink> parseGameFriendLinks(Collection<String> games) throws ParseException {
        requireNonNull(games);
        final Set<GameFriendLink> gameSet = new HashSet<>();
        for (String gameName : games) {
            // TODO - Edit command

            // gameSet.add(new Game(parseGameId(gameName)));
        }
        return gameSet;
    }

    /**
     * Takes in {@code userName} and returns the corresponding {@code UserName} if its format is valid.
     */
    public static UserName parseUserName(String userName) throws ParseException {
        if (!UserName.isValidUserName(userName)) {
            throw new ParseException(UserName.MESSAGE_CONSTRAINTS);
        }
        return new UserName(userName);
    }

    /**
     * Takes in {@code skillVal} and returns the corresponding {@code SkillValue} if its range is valid.
     */
    public static SkillValue parseSkillValue(String skillVal) throws ParseException {
        try {
            Integer skillValue = Integer.parseInt(skillVal);
            if (!SkillValue.validateSkillValue(skillValue)) {
                throw new ParseException(SkillValue.MESSAGE_CONSTRAINTS);
            }
            return new SkillValue(skillValue);
        } catch (NumberFormatException nfe) {
            throw new ParseException(SkillValue.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean areFlagsPresent(ArgumentMultimap argumentMultimap, Flag... flags) {
        return Stream.of(flags).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
