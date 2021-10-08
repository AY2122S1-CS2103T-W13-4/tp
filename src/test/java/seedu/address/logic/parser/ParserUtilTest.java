package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.friend.FriendName;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_GAME_NAME = "Counter Strike";
    private static final String VALID_IN_GAME_USERNAME = "Ferrari_peek";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseFriendName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseFriendName((String) null));
    }

    @Test
    public void parseFriendName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseFriendName(INVALID_NAME));
    }

    @Test
    public void parseFriendName_validValueWithoutWhitespace_returnsName() throws Exception {
        FriendName expectedFriendName = new FriendName(VALID_NAME);
        assertEquals(expectedFriendName, ParserUtil.parseFriendName(VALID_NAME));
    }

    @Test
    public void parseFriendName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        FriendName expectedFriendName = new FriendName(VALID_NAME);
        assertEquals(expectedFriendName, ParserUtil.parseFriendName(nameWithWhitespace));
    }

    @Test
    public void parseGamesAndUsernames_validInput_success() throws ParseException {
        List<String> games = new ArrayList<String>();
        games.add(VALID_GAME_NAME + ":" + VALID_IN_GAME_USERNAME);
        HashMap<String, String> expectedGamesHashMap = new HashMap<>();
        expectedGamesHashMap.put(VALID_GAME_NAME, VALID_IN_GAME_USERNAME);
        assertEquals(expectedGamesHashMap, ParserUtil.parseGamesAndUsernames(games));
    }

    // TODO: Add tests for parseFriendId and parseGame
}
