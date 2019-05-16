# Cryptogame User Manual

**Author**: Team 14

**Team Members**
* Team Member 1: Deepika Sivakumar(dsivakumar6@gatech.edu)
* Team Member 2: Manjina Shrestha(mshrestha6@gatech.edu)
* Team Member 3: Vu Dang(vadang@gatech.edu)

**Version**: 1.1
| 1.1 | 03/08/2019 | Minor corrections for the updates done in the project|
| 1.0 | 03/01/2019 | full doc submitted in D3| 

Cryptogame is android application cryptogram puzzle game that uses simple substitution cipher to encrypt the letters of a short sentence. The player has to decode the encrypted sentence and reveal the original one. Though it sounds easy just to replace the letters, it is a challenging but fun game. You can solve it out guessing the frequency of letters and their patterns in words. (*Hint*: Start with one letter word and look for most repititive letters - they belong to the *ETAOIN* group.) 
So try them out and good luck with the game!!

## System Requirements

- **Minimum SDK Required**: Android 6.0 or higher 

- **CPU**: 1.2 GHz or higher

- **RAM**: 1.0 GB or higher

- **Desktop usage**: Android Studio 3.3 or later versions required.

## Installation 

### Installing the app from source

1. Clone or Download the repository from following URL:

```https://github.gatech.edu/gt-omscs-se-2019spring/6300Spring19Team14/tree/master/GroupProject/```

2. Import Project Crypto6300 in Android Studio.
3. To run you either need to connect your android phone with usb to the computer with developer option enabled or select one of the android emulators in Android Studio
4. Select ***Run app*** from **Run Menu** in Android Studio.
	- To run in your mobile select your phone in the list
	- To run in android studio emulators 
		1. Press ***Create New Virtual Device***
		1. Select one of the phones and then one of the releases and click finish
5. The app is ready for use.

**Install using APK**

1. Copy `Cryptogame.apk` from the `/APK` directory to an Andorid phone.
2. Open the copied `.apk` file in the phone and allow it be installed.
	
## Using the app

- The app has two user types: **Admin** and **Player**. 
- The administrator is able to create new players, crytogram games and view all the player statistics. 
- The players can choose and solve the cryptograms and view the player statistics.

### Features for the Admin

#### Login
1. Start the application.
2. Enter your username.
3. Click on ***Login*** button.

### Add a new player
1. After logging in click on ***Create PLayer*** button.
2. In the form, enter *first name*, *last name* and *unique username* in the provided fields. The maximum number of characters allowed for each field is 30.
3. Select the difficulty level for the player.
4. To add the player, click on ***Add Player*** button and to clear all the fields press ***Reset*** button.

### Add a new cryptogram
1. After logging in, click on ***Create Cryptogram*** button.
2. In the form, enter *unique cryptogram name*, the sentence for which encrypted phrase is generated. The *encrypted phrase* is self generated. The maximum number of characters allowed for cryptogram name is 30 and the cryptogram solution needs to have atlease one alphabet.
3. Enter *the number of incorrect solutions* allowed for solving the cryptogram for *easy* , *normal* and *hard* difficulty levels in the provided fields.
4. Edit any of the fields before saving.
5. To add the cryptogram, click on ***Save*** button and to clear all the fields press ***Reset*** button.

### View Player Statistics
1. After logging in, click on ***View Player Statistics*** button.
2. The list of players with their *first name*, *username*, *difficulty level*, *number of wins* and *number of losses* generated can be viewed.

### Logout
1. After logging in, logout of the app at any time using the ***Logout*** button at the Admin Menu.


### Features for the Player

#### Login
1. Start the application.
2. Enter your username.
3. Click on ***Login*** button.

#### Choose and then solve cryptogram
1. After logging in, click on ***Choose Cryptogram*** button.
2. Select either **not started** or **in-progress** cryptogram from the list displayed. The *encrypted phrase* is self generated when the player clicks a new **not started** cryptogram.
3. The *cryptoname* and *number of allowed incorrect attempts* for player's difiiculty level is available at the top.
4. The enocded phrase can be seen below the remaining attempts and for every non duplicate letter, a blank space is provided to input assumed probable letter. 
4. Enter probable letters in the blanks tallying with the encrypted phrase letters.
5. Clear all the fields using ***Reset*** button at any time.
6. Return back to menu at any time. The app saves the state of the game and marks it as **in-progress**. The game can be resumed later from the list of crytograms.
7. After filling in all the blanks, view the *potential solution* that has been entered before submitting the answer by ***View Potential Solution*** button.
8. Once all the blanks are filled and satisfied with the potential answer, submit with ***Submit Answer*** button.
9. The player is informed by a message either if the solution submitted was correct or incorrect. 
 	- If solution is correct, the game is won then the cryptogram is marked as completed. 
	- If the solution is incorrect, the number of attempts is decreased but still appears in cryptogram list as **in-progress** but with blank fields when the game is started. The unsolved game is also marked as completed and lost game if the number of attempts is equal to zero. 
	- In either completed case, the game is removed from the ***Choose Cryptogram*** to-solve list. 

### View Player Statistics
1. After logging in, click on ***View Player Statistics*** button.
2. The list of players with only their *first name*, *number of wins* and *number of losses* generated can be viewed.


### Logout
1. After logging in, logout of the app at any time using the  ***Logout*** button at the Player Menu.	


