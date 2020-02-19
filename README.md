# Board Game Implementations

Designed three different games:
Tic-Tac-Toe,
Connect Four,
Othello. For each game there is a (name of game)Game.java file that displays the game window.

# Tic-Tac-Toe

Created a TicTacToe game with the following features:

* Players are black and white.
* Background of board is uniform.
* Spots are highlighted when entered only if clicking on them is a legal move (i.e., spot not already selected).
* Start of game has welcome message and indicates that white goes first.
* After a game winning move, message indicates who won and spot highlighting stops.
* After a game drawing move, message indicates that game is a draw.
* After a move that neither wins or draws, message indicates who goes next.

# Connect Four

Created a ConnectFour game. Features include:

* Players are red and black
* Board is 7 columns and 6 rows.
* Background is set up as alternating column stripes.
* All empty spots of a column are highlighted when the cursor enters any spot in the column.
* Clicking on any spot in a column that contains an empty spot toggles the bottommost empty spot and switch turns.
* Clicking on a spot in a column that does not contain an empty spot does nothing.
* Welcome message indicates red to play.
* After a game winning move, message indicates who won and highlights winning spots. Column highlighting stops.
* If a game draws, message indicates that game is a draw.
* After a move that neither wins or draws, message indicates who goes next.

# Othello

Created an Othello game. The rules for Othello can be found here: https://www.ultraboardgames.com/othello/game-rules.php

Features include:
* Players are black and white.
* Board is 8x8 with a checkerboard background pattern.
* The game starts with the middle 2x2 spots set up with alternating white and black pieces already set.
* Welcome message that indicates black to play.
* Spot highlighting only works on spots that are valid moves for player whose turn is next.
* Clicking on a spot that is a valid move sets that spot to the player's color, flip any flanked spots in any direction as appropriate, and set the message to indicate whose turn is next.
* If a player has no valid move, their turn is skipped.
* If there are no more valid moves available to either player, the game is over and the message is set to indicate who won and by what score.


