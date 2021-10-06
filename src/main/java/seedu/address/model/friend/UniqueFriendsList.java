package seedu.address.model.friend;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.friend.exceptions.DuplicateFriendException;
import seedu.address.model.friend.exceptions.FriendNotFoundException;

/**
 * A list of friends that enforces uniqueness between its elements and does not allow nulls.
 *
 * A friends is considered unique by comparing using {@code Friend#equals(Object)}. As such, adding and updating
 * of friends uses Friend#equals(Object) for equality so as to ensure that the friend being added or updated
 * is unique in terms of identity in the UniqueFriendsList. The removal of a friend also uses
 * Friend#equals(Object) so as to ensure that the friend with exactly the same friendId will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Friend#equals(Object)
 */
public class UniqueFriendsList implements Iterable<Friend> {

    private final ObservableList<Friend> internalList = FXCollections.observableArrayList();
    private final ObservableList<Friend> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent friend as the given argument.
     */
    public boolean contains(Friend toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::equals);
    }

    /**
     * Adds a friend to the list.
     * The friend must not already exist in the list.
     */
    public void add(Friend friendToAdd) {
        requireNonNull(friendToAdd);
        if (contains(friendToAdd)) {
            throw new DuplicateFriendException();
        }
        internalList.add(friendToAdd);
    }

    /**
     * Replaces the friend {@code friendToEdit} in the list with {@code editedFriend}.
     * {@code friendToEdit} must exist in the list.
     * The friend identity of {@code editedFriend} must not be the same as another existing friend in the list.
     */
    public void setFriend(Friend friendToEdit, Friend editedFriend) {
        requireAllNonNull(friendToEdit, editedFriend);

        int index = internalList.indexOf(friendToEdit);
        if (index == -1) {
            throw new FriendNotFoundException();
        }

        // guard against if editedFriend already exists in the UniqueFriendsList.
        if (!friendToEdit.equals(editedFriend) && contains(editedFriend)) {
            throw new DuplicateFriendException();
        }

        internalList.set(index, editedFriend);
    }

    /**
     * Removes the equivalent friend from the list.
     * The friend must exist in the list.
     */
    public void remove(Friend toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new FriendNotFoundException();
        }
    }

    public void setFriends(UniqueFriendsList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code friends}.
     * {@code friends} must not contain duplicate friends.
     */
    public void setFriends(List<Friend> friends) {
        requireAllNonNull(friends);
        if (!friendsAreUnique(friends)) {
            throw new DuplicateFriendException();
        }

        internalList.setAll(friends);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Friend> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Friend> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueFriendsList // instanceof handles nulls
                        && internalList.equals(((UniqueFriendsList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code friends} contains only unique friends.
     */
    private boolean friendsAreUnique(List<Friend> friends) {
        for (int i = 0; i < friends.size() - 1; i++) {
            for (int j = i + 1; j < friends.size(); j++) {
                if (friends.get(i).equals(friends.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
