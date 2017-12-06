# HackerNewsReader

This sample project is to demostrate an android app built for https://github.com/HackerNews/API. It has 2 pages, top stories and comment. 

The objective of the project is to illustrate:
- implementation of MVP
- unit testing 
- android testing
- unified test coverage report using jacoco

In order to run testing and generate coverage report, switch to mockDebug build variant and hit the following command in Android Terminal.

`gradlew clean jacocoTestReport`

Coverage report is located at \app\build\reports\jacoco\jacocoTestReport\html\index.html

Ensure that you have already turned off device animation before runs the android test as documented [here](https://developer.android.com/training/testing/espresso/setup.html#set-up-environment).
