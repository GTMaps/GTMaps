# GTMaps  
Indoor navigational app for GT buildings.

======
## Install Guide
**Pre-Requisites:**

1. Must have Android Device with Android version 21 (5.0 Lollipop) or later
2. Android Device must have “Allow unknown sources” enabled under Developer Settings


**Dependencies:**

- None required for installation. All necessary dependencies are bundled in the .apk download.


**Download:**

- [Download from our Github Repository!](github.com/GTMaps/GTMaps "GT Maps Github Repository")
- Navigate to our repository and select 'download'
- Clone the repository yourself, ~~or download the zip folder~~ (in progress)


**Build:**

- ~~No compilation or building necessary.  The .apk file is already executable.~~
1. Create a new project in Android Studio
  - Select existing project files, and choose the GT Maps Android folder from the cloned repository
2. Allow gradle sync to run
3. Select Run from the Android Studio menu


**Installation:**  
*METHOD 1-* (in progress)

1. ~~Connect your Android device to your computer using a USB cable~~
2. ~~Open a file browser on the computer and locate the GTMaps.apk in the downloaded zip folder~~
3. ~~Copy this .apk onto the Android device~~
4. ~~Open a file browser on the Android Device and locate the .apk file~~
5. ~~Tap on this file to install it~~
  1. ~~(Optional) Add the GT Maps app to your device’s home screen~~

*METHOD 2 (Requires Android Studio and source code)-*

1. Pull the source code from our github into your local android development environment.
2. Connect your phone to your development environment over USB.
 - *Android Studio should automatically download any dependencies to your machine after a “Gradle Sync” is performed.*
3. Press the “Play” symbol at the top of your workspace and choose your phone as the target device.  Android Studio will build and compile the application into an apk, then automatically download it to your phone and automatically open it.
  1. (Optional) Add the GT Maps app to your device’s home screen

**Running Application:**
- Locate the app on the android device either through the All Apps button or on the home screen (if you chose to add it to the screen) and tap the application to open.

======
## Release Notes (v 1.0)
**New features:**

- Support for first floor College of Computing Building
- Separate Room List activity provided to select from all possible rooms
- Separate Gallery View to present supported campus buildings. (Beta)
- Building and Room list autocomplete in textviews.
- Routing between known entrances and rooms
- Routing is now shortest path.
- Hallway vector projection to calculate relative room and hallway positions (left and right).
- Route Translation Algorithm to convert calculated routes to human-readable directions.
- Native backend SqliteDB transactions for safe and semantic data handling.
- Coordinate Mapping Software (w/docs) for rapidly building-coordinate data for new floors and buildings.
- Compiles on Android version 21 and higher


**Bug Fixes:**

- App no longer crashes when viewing gallery


**Known Bugs:**

- Clicking a building in gallery view does not forward the building id to the next activity.
- Gallery navigation menu includes option for “choose another room”, even when not functional.
- Rare crashes when going to the directions activity from the room search activity
- Minor logic errors present for specific rooms
