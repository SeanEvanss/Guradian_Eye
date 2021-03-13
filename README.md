# Fall-detection-system

A project created for advanced software enginnerring module in NTU. The main layout of the project involves mobile application (created on Android Studio) along with a python script running the program on a local machine. 

The python script is light enought to be deployable on smaller scale machines such as a Raspberry Pi.


Currently the Flask server is set to be on a localhost mode (this is a POC demo), so to activate it, you'll need to:

1) Run app.py. This should set up the local hosting at port 5000 with debug mode on.
2) In the Android studio code, you will need to change the URL call address (IPv4) to whatever network you are currently running the Flask server on.
3) There are several different calls which will/can be implemented but most are self-explanatory based on the app.py code, including the headers to pass
