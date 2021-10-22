package seedu.address.logic.commands.friends;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.FLAG_FRIEND_NAME;

import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.CommandType;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.friend.Friend;
import seedu.address.model.friend.FriendId;
import seedu.address.model.friend.FriendName;
import seedu.address.model.gamefriendlink.GameFriendLink;

/**
 * Edits the details of an existing friend in the address book.
 */
public class EditFriendCommand extends Command {

    public static final String COMMAND_WORD = "--edit";
    public static final String USAGE_EXAMPLE = "Example: friend " + COMMAND_WORD + " Draco "
            + FLAG_FRIEND_NAME + "Marcus Tang";
    public static final String MESSAGE_EDIT_FRIEND_SUCCESS = "Friend edited - \nId: %1$s \nNew name: %2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided. \n" + USAGE_EXAMPLE;
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the friend identified "
            + "by friend's FRIEND_ID. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: FRIEND_ID "
            + FLAG_FRIEND_NAME + "NEW_FRIEND_NAME \n"
            + USAGE_EXAMPLE;

    private final FriendId friendIdToEdit;
    private final EditFriendDescriptor editFriendDescriptor;

    /**
     * @param friendIdToEdit       of the person in the filtered person list to edit
     * @param editFriendDescriptor details to edit the person with
     */
    public EditFriendCommand(FriendId friendIdToEdit, EditFriendDescriptor editFriendDescriptor) {
        requireNonNull(friendIdToEdit);
        requireNonNull(editFriendDescriptor);

        this.friendIdToEdit = friendIdToEdit;
        this.editFriendDescriptor = new EditFriendDescriptor(editFriendDescriptor);
    }

    /**
     * Creates and returns a {@code Friend} with the details of {@code friendToEdit}
     * edited with {@code EditFriendDescriptor}.
     */
    private static Friend createEditedPerson(Friend friendToEdit, EditFriendDescriptor editFriendDescriptor) {
        assert friendToEdit != null;
        assert editFriendDescriptor != null;

        FriendId friendId = friendToEdit.getFriendId();
        FriendName updatedFriendName = editFriendDescriptor.getFriendName().orElse(friendToEdit.getName());
        Set<GameFriendLink> gameFriendLinks = friendToEdit.getGameFriendLinks();

        return new Friend(friendId, updatedFriendName, gameFriendLinks);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasFriendWithId(this.friendIdToEdit)) {
            throw new CommandException(String.format(Messages.MESSAGE_FRIEND_ID_NOT_FOUND,
                    this.friendIdToEdit));
        }

        Friend friendToEdit = model.getFriend(friendIdToEdit);
        Friend editedFriend = createEditedPerson(friendToEdit, editFriendDescriptor);

        model.setFriend(friendToEdit, editedFriend);

        return new CommandResult(String.format(MESSAGE_EDIT_FRIEND_SUCCESS,
                editedFriend.getFriendId(),
                editedFriend.getName()),
                CommandType.FRIEND_EDIT);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditFriendCommand)) {
            return false;
        }

        // state check
        EditFriendCommand e = (EditFriendCommand) other;
        return friendIdToEdit.equals(e.friendIdToEdit)
                && editFriendDescriptor.equals(e.editFriendDescriptor);
    }

    /**
     * Stores the details to edit the friend with. Each non-empty field value will replace the
     * corresponding field value of the friend.
     */
    public static class EditFriendDescriptor {
        private FriendName friendName;

        public EditFriendDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditFriendDescriptor(EditFriendDescriptor toCopy) {
            setFriendName(toCopy.friendName);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(friendName);
        }

        public Optional<FriendName> getFriendName() {
            return Optional.ofNullable(friendName);
        }

        public void setFriendName(FriendName friendName) {
            this.friendName = friendName;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditFriendDescriptor)) {
                return false;
            }

            // state check
            EditFriendDescriptor e = (EditFriendDescriptor) other;

            return getFriendName().equals(e.getFriendName());
        }
    }
}
