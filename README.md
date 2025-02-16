# Virtual Piano Application

## Project Overview
The Virtual Piano Application is a JavaFX-based interactive piano that allows users to play piano notes using their keyboard. The application provides a visually appealing interface with dynamically resizable keys, realistic sound effects, and recording functionality.

## Features
### 1. Virtual Piano Keyboard
- Interactive piano layout with white and black keys.
- Keyboard bindings for each piano note.
- Responsive key resizing based on window dimensions.

### 2. Sound Playback
- Each key press triggers the corresponding piano sound.
- Uses JavaFX `MediaPlayer` to play sound files.

### 3. Recording and Playback
- Users can start and stop recording their piano sessions.
- Saves recorded sessions as WAV files.

### 4. User Interface & Controls
- Simple and modern UI with a dark theme.
- Styled buttons for recording and stopping playback.
- Menu bar with "About" and "Help" sections.

### 5. Help and Information
- **About Menu:** Provides application details and credits.
- **Help Menu:** Displays key mappings for the piano sounds.

## Technical Details
- **Language:** Java
- **Framework:** JavaFX
- **Sound API:** JavaFX MediaPlayer & Java Sound API
- **UI Components:**
  - `BorderPane` for layout structure.
  - `Pane` for piano key placement.
  - `MenuBar` for application menus.
  - `HBox` for control buttons.

## How It Works
1. The application initializes with a piano layout consisting of white and black keys.
2. Users press designated keyboard keys to play corresponding piano notes.
3. Pressing the "Record" button starts capturing the played sounds.
4. Pressing "Stop" saves the recording as a WAV file.
5. The UI dynamically adjusts when resizing the application window.

## Developers
- **Nazeef ul Hassan Qureshi**
- **Abdullah Wali**


