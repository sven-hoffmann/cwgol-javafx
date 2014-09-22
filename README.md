cwgol-javafx
============

A primitive implementation and visualization of Conway's Game of Life with JavaFX.

I created this for last years global day of code retreat. The only goal of this code is to have an animated visualization of Conway's Game of Life.

The most important aspects of the visualization can be controlled by the static members:
- FPS
- SQUARE_SIZE
- X_MAX
- Y_MAX

The application can be run by:
- starting the main class
- running mvn com.zenjava:javafx-maven-plugin:8.1.2:run in the console

project requirements are:
- java 8+ is recommended because it works out of the box, for 7u9+ see http://zenjava.com/javafx/maven/fix-classpath.html
- maven 3+
