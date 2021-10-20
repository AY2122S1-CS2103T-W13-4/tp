---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/AY2122S1-CS2103T-W13-4/tp/tree/master/docs/diagrams) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of gitGud.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/AY2122S1-CS2103T-W13-4/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2122S1-CS2103T-W13-4/tp/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of gitGud consists of four components.

* [**`UI`**](#ui-component): The UI of the gitGud.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds application data for gitGud in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user 
issues the command `friend --delete Draco`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2122S1-CS2103T-W13-4/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)
             
**Description** :                                                                                    

The `Storage` component is responsible for persisting application data to be stored in json file format and reading stored data
back into the application. 

**Functionality** :

The storage component saves the following application data in json format and reads them back into corresponding objects:
* friends list
* games list
* user preferences

**Component Structure** : 

<img src="images/StorageClassDiagram.png" width="550" />

There are 3 main model objects stored by the Storage component in json format, namely `Friend`, `Game`,
and `UserPrefs`, each stored in separate json files.

The Storage component inherits from `FriendsListStorage`, `GamesListStorage` and 
`UserPrefStorage`, which means it can be treated as any one of the three.

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:
* Gamer who plays competitive games
* Computer Science university student
* Busy (can only play at specific times based on a weekly schedule)
* Plays multiple online competitive multiplayer games (e.g., Apex legends, Minecraft, Valorant, etc)
* Has a group of similar persona gamer friends who like him are busy with their computer science university degrees

Computer Science university student gamer who is busy but wants to keep track of equally busy friends who want to play 
online competitive multiplayer games together.


**Value proposition**:
#### Problem:
For busy university student gamers who love playing multiplayer competitive games with their friends, it can
often be challenging scheduling times to play games with friends out of a packed schedule.
Additionally, it can be challenging to make the most out of the limited time available by choosing
the right friends to play with - those you have the most fun playing with or have great performance at the game.

#### How gitGud solves the problem and make users' lives easier:
gitGud allows users to store your friends' available timeslots and find matching times to play multiplayer
competitive games together - allowing users to identify and coordinate gaming sessions with friends.
It also allows users to keep track of each friend's competitive gaming performance and rate how much fun they had gaming with a friend
after each gaming session. These factors are used for gitGud to recommend and help users to decide which friends to play with
for future multiplayer competitive gaming sessions.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a (describes user) | I want to (functionality)                                          | So that I can (rationale)                                 |
|----------|-----------------------|--------------------------------------------------------------------|-----------------------------------------------------------|
| ***      | user                  | be able to easily add my friends personal info/data (name, userid) | store a list of friends who I can possibly play with      |
| ***      | user                  | be able to link my friends to the games they play                  | associate my friends with a particular game and store their usernames for each game               |
| ***      | user                  | be able to view a list of my friends information                   | see who my friends are                                    |
| ***      | user                  | be able to delete a friend from the contact list                   | remove friends that were mistakenly added                 |
| ***      | user                  | be able to see full information of a friend from the contact list  | get any information I want about the friend               |
| ***      | user                  | be able to easily add games that I want to play with my friends    | store the games that I plan to play with my friends       |
| ***      | user                  | be able to view a list of my game information                      | see which of my friends play certain games                |
| ***      | user                  | be able to delete a game from the contact list                     | remove games that were mistakenly added or due to typos   |
| ***      | user                  | be able to see full information of a game from the games list      | see information about which friends play the game and their in-game usernames   |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `gitGud` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - Add a friend** 

**MSS**

1. User requests to add a friend and specifies a `FRIEND_ID`.
2. gitGud adds a friend into the friends list and informs user of successful addition of friend.

    Use case ends.

**Extensions** 

* 1a. gitGud detects that the `FRIEND_ID` provided already exists and is not unique.
    * 1a1. gitGud informs user that it has failed to add a friend.<br>
      1a2. User makes another request to add a friend with a different `FRIEND_ID`.<br>
      Use case continues from step 2.

**Use case: UC02 - Delete a friend**

**MSS**

1. User requests to delete a friend using the friend's `FRIEND_ID`
2. gitGud deletes the person

    Use case ends.

**Extensions**

* 2a. The given `FRIEND_ID` is invalid.

    * 2a1. gitGud shows an error message.

      Use case resumes at step 1.


**Use case: UC03 - List friends whose `FRIEND_ID` contains a keyword**

**MSS**

1. User requests to list friends in gitGud using a keyword filter.
2. gitGud shows a filtered list of friends whose `FRIEND_ID` contains the keyword.

   Use case ends.

**Extensions**

* 2a. The user does not provide a keyword.

    * 2a1. All friends are listed.

      Use case ends.


**Use case: UC04 - List games whose `GAME_NAME` contains a keyword**

**MSS**

1. User requests to list games in gitGud using a keyword filter.
2. gitGud shows a filtered list of games whose `GAME_NAME` contains the keyword.

   Use case ends.

**Extensions**

* 2a. The user does not provide a keyword.

    * 2a1. All games are listed.

      Use case ends.


**Use case: UC05 - Link a friend to a particular game**

**MSS**

1. User links a friend (using `FRIEND_ID`) with a particular game (using `GAME_NAME`) and the username for that game 
   (using `USERNAME`).
2. gitGud associates the friend with the game provided.

    Use case ends.

**Extensions**

* 2a. The given `FRIEND_ID` is invalid.

    * 2a1. gitGud shows an error message.
    * 2a2. User can list friends currently in gitGud or add new friends.

      Use case resumes at step 1.


* 2b. The given `GAME_NAME` is invalid.

    * 2b1. gitGud shows an error message.
    * 2b2. User can list games currently in gitGud or add new games.

      Use case resumes at step 1.


**Use case: UC06 - Get a friend's complete information**

**MSS**

1. User <u>list friends (UC03)</u> and chooses a `FRIEND_ID`.
2. User requests for complete information about `FRIEND_ID`.
3. gitGud displays the complete information related to  `FRIEND_ID`.

    Use case ends.

**Extensions**

* 3a. The given `FRIEND_ID` is invalid.
    * 3a1. gitGud shows an error message.

      Use case resumes at step 1.

### Non-Functional Requirements

1. Should work on any mainstream OS as long as it has Java 11 or above installed.
2. Should be able to hold up to 100 friends or games without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Does not require internet connection.
5. The code should be open source.

### Glossary

* **Competitive Game/s**: Multiplayer games with a ranking system
* **Busy**: Maximum of 7hrs/week to play games
* **Mainstream OS**: Windows, Linux, Unix, OS-X

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample friends. The window size may not be 
      optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Adding a friend
1. Adding a friend to gitGud

    1. Prerequisites: List all friends using the `friend --list` command. There should not be a friend with 
       `FRIEND_ID` Draco as well as friend with `FRIEND_ID` MrFeely already stored in gitGud. 

    2. Test case: `friend --add Draco --name Marcus`<br>
       Expected: Friend with `FRIEND_ID` Draco is added. gitGud states that `FRIEND_ID` is added.
       
    3. Test case: `friend --add MrFeely`<br>
       Expected: Friend with `FRIEND_ID` MrFeely is added. gitGud states that `FRIEND_ID` is added.

    4. Other incorrect add commands to try: `friend --name Marcus`, `friend --add`, `friend --name`, ...`
       Expected: No friend is added. Error details shown in the status message. Status bar remains the same.

2. _{ more test cases …​ }_

### Adding a game
1. Adding a game to gitGud

    1. Prerequisites: List all games using the `game --list` command. There should not be a game with `GAME_NAME` 
       Valorant already stored in gitGud.

    2. Test case: `game --add Valorant`<br>
       Expected: Game with `GAME_NAME` Valorant is added. gitGud states that `GAME_NAME` is added.

    3. Test case: `game --add`<br>
       Expected: No game is added. Error details shown in the status message. Status bar remains the same.

2. _{ more test cases …​ }_

### Linking a friend to a game
1. Linking a friend to a game in gitGud.

    1. Prerequisites: There exists a friend with `FRIEND_ID` Draco, and there exists a game with `GAME_NAME` Valorant.

    2. Test case: `link --friend Draco --game Valorant --user Draconian`<br>
       Expected: A link between Draco and Valorant is created and Draco's username for Valorant, Draconian, is 
       stored in the link.

    3. Test case: `link --game Valorant --name Draco --user Draconian`<br>
       Expected: No link is added. Error details shown in the status message. Status bar remains the same.

    3. Test case: `link --friend Draco --game Valorant`<br>
       Expected: No link is added. Error details shown in the status message. Status bar remains the same.

    4. Other incorrect link commands to try: `link --friend Draco --user Draconian`, `link --game Valorant --user 
       Draconian`, `link --friend`, ...
       Expected: Similar to previous.

2. _{ more test cases …​ }_

### Deleting a friend

1. Deleting a friend from gitGud

   1. Prerequisites: Only one friend with `FRIEND_ID` Draco exists in gitGud.

   1. Test case: `friend --delete Draco`<br>
      Expected: Friend with `FRIEND_ID` Draco is deleted. gitGud states that `FRIEND_ID` is deleted.

   1. Test case: `friend --delete MrFeely`<br>
      Expected: No friend is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `friend --delete`, `...` 
      Expected: Similar to previous.

2. _{ more test cases …​ }_

### Deleting a game

1. Deleting a game from gitGud

    1. Prerequisites: Only one game with `GAME_NAME` Valorant exists.

    1. Test case: `game --delete Valorant`<br>
       Expected: Game with `GAME_NAME` Valorant is deleted. gitGud states that `GAME_NAME` is deleted.

    1. Test case: `game --delete CSGO`<br>
       Expected: No game is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `game --delete`, `...`
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Filtering friends in friends' list using a keyword

1. Filtering friends' list using a keyword

    1. Prerequisites: There exists more than one friend in gitGud, one of which has the `FRIEND_ID` Draco.

    2. Test case: `friend --list`<br>
       Expected: All friends are listed.

    3. Test case: `friend --list Draco`<br>
       Expected: The friend Draco is listed, as his `FRIEND_ID` contains the keyword Draco.

    4. Test case: `friend --list Dra`<br>
       Expected: The friend Draco is listed, as his `FRIEND_ID` contains the keyword `Dra`.

    5. Test case: `friend --list co`<br>
       Expected: The friend Draco is listed, as his `FRIEND_ID` contains the keyword `co`.

### Filtering friends in games' list using a keyword

1. Filtering games' list using a keyword

    1. Prerequisites: There exists more than one game in gitGud, one of which has the `GAME_NAME` Valorant.

    2. Test case: `game --list`<br>
       Expected: All games are listed.

    3. Test case: `game --list Valorant`<br>
       Expected: The game Valorant is listed, as its `GAME_ID` contains the keyword `Valorant`.

    4. Test case: `game --list Valo`<br>
       Expected: The game Valorant is listed, as its `GAME_ID` contains the keyword `Valo`.

    4. Test case: `game --list ant`<br>
       Expected: The game Valorant is listed, as its `GAME_ID` contains the keyword `ant`.

### Viewing a friend's full data

1. View a friend's full game information, including their in-game usernames

    1. Prerequisites: A friend with `FRIEND_ID` Draco exists in gitGud.

    2. Test case: `friend --get Draco`<br>
       Expected: The full information about friend with `FRIEND_ID` Draco is listed.

    3. Test case: `friend --get Dra`<br>
        Expected: No friend is found. Error details shown in the status message. Status bar remains the same.

    4. Test case: `friend --get co`<br>
       Expected: No friend is found. Error details shown in the status message. Status bar remains the same.

    5. \Other incorrect delete commands to try: `friend --get`, `...`
       Expected: Similar to previous.
   
2. ### Viewing a game's full data

1. View a game's full information, including the friends which play that game.

    1. Prerequisites: A game with `GAME_NAME` Valorant exists in gitGud.

    2. Test case: `game --get Valorant`<br>
       Expected: The full information about game with `GAME_NAME` Valorant is listed.

    3. Test case: `game --get Valo`<br>
       Expected: No game is found. Error details shown in the status message. Status bar remains the same.

    4. Test case: `game --get rant`<br>
       Expected: No game is found. Error details shown in the status message. Status bar remains the same.

    5. \Other incorrect delete commands to try: `game --get`, `...`
       Expected: Similar to previous.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
