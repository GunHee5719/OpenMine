# Open Mine
## Project Name
 - Open Mine : Automatic Minesweeper Gameplay Program

## Project Goal
 - To make program that automatically resolves Minesweeper that one of the basic games provided in Window 7, by recognizing monitor screen as OpenCV
 
## Project Function
 - Recognizing monitor screen and Identifying minesweeper screen.
 - Search mine position.
 - Remove mine by clicking right button of mouse.
 - When fail game, restart game.

## Development Tools
 - OpenCV 3.4.3
  > https://opencv.org/releases.html
  
 - Java JDK 11.0.1
  > https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html
  
 - IntelliJ IDE
  > https://www.jetbrains.com/idea/
  
  
  **If opencv file is extracted on c:\ route, this project will work well. If not, set your project refer to this site** 
  >https://medium.com/@aadimator/how-to-set-up-opencv-in-intellij-idea-6eb103c1d45c

## Development Survey
### 1. Mouse Event
 - Because we must control the computer’s mouse in our program, we will use Mouse Event, which is **not** implemented function, implemented by ourselves. And then we will use it to click the minesweeper’s window area for detecting mine.
 
### 2. Window Detection
 - We need to know all of the window size, position, coordinates, etc. while processing whole program. So we will implement our function based on **Harris corner detector algorithm** so we will make our program to detect corners well.
 
### 3. Number Detection
 - Because our program works by sensing numbers, Number Detection is very important. There are various libraries like Detecting Car license plate, or Business card, and so on. So we will use the basic algorithm of them to adapt to our purposes.

## Plan
### 1. Week 1 (11/09 ~ 11/14)
 - Team struct & Project planning
### 2. Week 2 (11/15 ~ 11/22)
 - Build minesweeper algorithm & Recognize minesweeper screen
### 3. Week 3 (11/23 ~ 11/29)
 - Implement minesweeper algorithm & Mouse event
### 4. Week 4 (11/30 ~ 12/06)
 - Program test & Write final report
