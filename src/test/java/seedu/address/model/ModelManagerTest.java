package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalFriends.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.friend.FriendNameContainsKeywordsPredicate;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.FriendBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new FriendsList(), new FriendsList(modelManager.getFriendsList()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasFriend_nullFriend_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasFriend(null));
    }

    @Test
    public void hasFriendId_nullFriendId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasFriendId(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasFriend(ALICE));
    }

    @Test
    public void hasFriendId_friendIdNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasFriendId(ALICE.getFriendId()));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addFriend(ALICE);
        assertTrue(modelManager.hasFriend(ALICE));
    }

    @Test
    public void hasFriendId_FriendIdInAddressBook_returnsTrue() {
        modelManager.addFriend(ALICE);
        assertTrue(modelManager.hasFriend(ALICE));
    }

    @Test
    public void hasFriendId_sameIdDifferentName_returnsTrue() {
        FriendBuilder amyFriendBuilder = new FriendBuilder();
        modelManager.addFriend(amyFriendBuilder.build());
        FriendBuilder amyNewNameFriendBuilder = new FriendBuilder().withFriendName("Bob");
        assertTrue(modelManager.hasFriendId(amyNewNameFriendBuilder.build().getFriendId()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredFriendsList().remove(0));
    }

    @Test
    public void equals() {
        FriendsList friendsList = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        FriendsList differentFriendsList = new FriendsList();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(friendsList, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(friendsList, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentFriendsList, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredFriendsList(new FriendNameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(friendsList, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredFriendsList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(friendsList, differentUserPrefs)));
    }
}
