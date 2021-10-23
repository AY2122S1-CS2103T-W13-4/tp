package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_FRIEND_ID_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FRIEND_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.testutil.TypicalGameFriendLinks.ALICE_CSGO_SKILL_VALUE_EIGHT_LINK;
import static seedu.address.testutil.TypicalGameFriendLinks.BOB_CSGO_SKILL_VALUE_NULL_LINK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.FriendsList;
import seedu.address.model.friend.Friend;
import seedu.address.model.friend.Schedule;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalFriends {
    public static final String ALICE_FRIEND_ID = "94351253";
    public static final String BENSON_FRIEND_ID = "98765432";
    public static final Friend ALICE = new FriendBuilder().withFriendName("Alice Pauline")
            .withFriendId(ALICE_FRIEND_ID)
            .withGameFriendLinks(ALICE_CSGO_SKILL_VALUE_EIGHT_LINK).withSchedule(new Schedule()).build();
    public static final Friend BENSON = new FriendBuilder().withFriendName("Benson Meier")
            .withFriendId(BENSON_FRIEND_ID)
            .withGameFriendLinks(BOB_CSGO_SKILL_VALUE_NULL_LINK).withSchedule(new Schedule()).build();
    public static final Friend CARL = new FriendBuilder().withFriendName("Carl Kurz").withFriendId("95352563")
            .build();
    public static final Friend DANIEL = new FriendBuilder().withFriendName("Daniel Meier").withFriendId("87652533")
            .build();
    public static final Friend ELLE = new FriendBuilder().withFriendName("Elle Meyer").withFriendId("9482224")
            .build();
    public static final Friend FIONA = new FriendBuilder().withFriendName("Fiona Kunz").withFriendId("9482427")
            .build();
    public static final Friend GEORGE = new FriendBuilder().withFriendName("George Best").withFriendId("9482442")
            .build();

    // Manually added
    public static final Friend HOON = new FriendBuilder().withFriendName("Hoon Meier").withFriendId("8482424")
            .build();
    public static final Friend IDA = new FriendBuilder().withFriendName("Ida Mueller").withFriendId("8482131")
            .build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Friend AMY = new FriendBuilder().withFriendName(VALID_NAME_AMY)
            .withFriendId(VALID_FRIEND_ID_AMY).build();
    public static final Friend BOB = new FriendBuilder().withFriendName(VALID_NAME_BOB)
            .withFriendId(VALID_FRIEND_ID_BOB).build();

    private TypicalFriends() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static FriendsList getTypicalFriendsList() {
        FriendsList ab = new FriendsList();
        for (Friend friend : getTypicalFriends()) {
            ab.addFriend(friend);
        }
        return ab;
    }

    public static List<Friend> getTypicalFriends() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
