package seedu.address.model.friend;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FRIEND_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GAME_APEX_LEGENDS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GAME_CSGO;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalFriends.ALICE;
import static seedu.address.testutil.TypicalFriends.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.FriendBuilder;

public class FriendTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Friend friend = new FriendBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> friend.getGames().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // same name, all other attributes different -> returns true
        Friend editedAlice = new FriendBuilder(ALICE).withGames(VALID_GAME_CSGO).build();
        assertTrue(ALICE.equals(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new FriendBuilder(ALICE).withFriendName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Friend editedBob = new FriendBuilder(BOB).withFriendName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.equals(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new FriendBuilder(BOB).withFriendName(nameWithTrailingSpaces).build();
        assertFalse(BOB.equals(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Friend aliceCopy = new FriendBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different friendId -> returns false
        Friend editedAlice = new FriendBuilder(ALICE).withFriendId(VALID_FRIEND_ID_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different name -> returns false
        editedAlice = new FriendBuilder(ALICE).withFriendName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different games -> returns false
        editedAlice = new FriendBuilder(ALICE).withGames(VALID_GAME_APEX_LEGENDS).build();
        assertFalse(ALICE.equals(editedAlice));
    }
}
