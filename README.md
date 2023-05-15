# Online Whiteboard
Online Whiteboard is a collaborative drawing application that allows multiple users to draw and communicate with each other in real time. It is designed to be used in a remote environment where users are not physically present in the same location.

## Features
Create, join and manage multiple rooms
Draw using various shapes, colors and line thicknesses
Add text and images to the board
Real-time collaboration and communication between users
Save and load drawings
Kick out users from the room
## Technologies Used
Java
Java RMI (Remote Method Invocation)
Swing GUI toolkit
## How to Run
1. Clone the repository
2. Navigate to the out/artifacts
3. Start the server using `java -jar OnlineWhiteBoard_Server.jar [portNumber]` the server will be running on your local IP address. Without portNumber Input, the server will run on default 1099.
4. Create a room using `java -jar CreateWhiteboard.jar -u [username]`
5. Join the room using `java -jar JoinWhiteboard.jar -u [username] -r[roomNumber]`
## Usage
1. Start the application by following the above steps
2. Create a room or join an existing one
3. Draw on the whiteboard by selecting a shape, color and line thickness
4. Add text or images to the board using the toolbar
5. Collaborate with other users in real-time by chatting or drawing together
6. Save and load drawings as needed
7. Kick out users from the room if necessary
## Future Improvements
1. Implement better user management and authentication
2. Improve the drawing tools and add more options
3. Add support for multiple boards per room
5. Implement undo/redo functionality
6. Add support for touch screens and tablets


