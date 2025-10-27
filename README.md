CST 438: Project 02 Retrospective
Team 12
October 26th 2025
Dr. Drew A. Clinkenbeard


Project 02 Retrospective
Armando Vega, Justin Park, Alexis Guzman, Darius Cuevas

Github Link: 
Backend: https://github.com/Wei-HaiMing/group12bettingprojBE 
Frontend: https://github.com/Wei-HaiMing/cstproj2group12 
Introduction
We picked a sports betting app to provide a backend for. We created several tables and routes corresponding to what the application would want to use in the frontend features.
We communicated primarily through Discord, Slack, and in person meetings.
We initially considered about 8 user stories that resulted in 32 github issues.  In the end we completed 27 of the 32 issues.

Introduction	1
Team Member Retrospectives	2
Armando Vega                                                                                                                                                      2
Justin Park                                                                                                                                                            4
Darius Cuevas                                                                                                                                                      3
Alexis Guzman                                                                                                                                                     6
Conclusions	7
How successful was the project?	7
What was the largest victory?	7
Final assessment of the project	7


Team Member Retrospectives
Armando Vega
I tried my best to plan out how to break up stories into issues for each member to complete.
What was your role / which stories did you work on
My role was steering the team into figuring out what specifically we need from each feature. In this case, since everything was in the backend, making sure everyone knows how to set up database tables with POJOs and DAOs, explaining what exactly that means, etc.
I worked on the user and favoriteTeam tables and their corresponding Repo files and routes in the controller, logging in with OAuth2 (GitHub), and setting up the backend database with JAWSDB and Heroku
How much time was spent working outside of class
I spent around 4-6 hours each week outside of class
What was the biggest challenge? 
Getting OAuth2 to work. Since expo go uses the device IP to determine where it’s running from (which changes depending on what network you’re connected to), figuring out how to pass the IP to the backend through sessions was certainly a task. Understanding the OAuth2 components in the first place such as the security configuration after GitHub responds to the user authorization and what it’s supposed to do was the first difficult part.
Why was it a challenge?
At first, I didn’t know why the OAuth was working at school but not at home. And then once I figured out that it was dependent on the network IP, I needed to figure out how to dynamically retrieve the device IP to redirect straight into the app. 
How was it addressed?
I found out through of course many back and forth deliberations from Claude, but ultimately, it was from observing how the funny numbers in my device IP changed from what I was used to seeing. See, from staring at the startup message constantly at school, I was able to tell that the IP was different from what it was supposed to be routing to, so I determined that I needed to find a way to pass that IP dynamically which was through sessions. 
Favorite / most interesting part of this project
My most favorite part of this project was setting up the API routes.
If you could do it over, what would you change?
I'd make sure that we had a clearer road map of what needed to be done each week.
What is the most valuable thing you learned?
How the OAuth2 flow works and how to implement it.
Darius Cuevas
I tried my best on working on the team's database and repository. Making test cases for it and making good routes for it to work.
What was your role / which stories did you work on
My role was working on the team database and repository. So that a team's score can be tracked since a lot of gambling relies on who wins the game. I set up the repository to make it easier for the table to be consistently updated. I made test cases for the table to ensure that it actually works.
How much time was spent working outside of class
I spent 3-4 hours a week outside of class.
What was the biggest challenge? 
My biggest challenge was setting up the table and being able to insert data into the database and then be able to retrieve it without any errors. I’m not that experienced with databases and repositories since I am more use to using FastComet and the services they provide.
Why was it a challenge?
It was something new to me. I’m not really familiar with it but it was definitely an interesting task. I didn’t know how to prompt some queries correctly so my repository would work properly with the database.
How was it addressed?
I was able to address it by researching how to create a repository and learn how it worked. I watched videos on youtube to learn how a repository works and what is needed for it to work.
Favorite / most interesting part of this project
My favorite part was getting the routes to work and see how they connect with the database. I especially really liked seeing the route show team scores.
If you could do it over, what would you change?
If i could do it over again, i would’ve taken more time to learn how to write better databases that weren’t subject to issues and take my time overlooking them to ensure that they work perfectly.
What is the most valuable thing you learned?
Databases are easy on local host but harder to have persistent data.



Justin Park
I’d say I contributed fairly to the best of my ability on this project. I think I could’ve definitely done more and be a better group mate. I created the stories/issues for both repo’s, made a google doc at the beginning of the project to facilitate brainstorming, deadlines, and timelines. I helped keep the group on pace and understand what needed to be done by certain timelines and weeks. I also assisted with helping get the project started with basic api testing and routes initially. 


Here is the provided google doc I made for the group: https://docs.google.com/document/d/1jFFDs1PmSpeCEBzDP_h5f_KWDRDAF0B78mOOpsPrXj0/edit?usp=sharing

What was your role / which stories did you work on
My role acted as being the secretary of the group, creating documents to refer to, working on the Games table, and connectivity to the front end.
I also created the associated controller, model, and repo’s for the games table, and the layout navigator for the front end as well as the games feature on the front end.
How much time was spent working outside of class
I would say I spent around 5-6 hours on average a week working on the project outside of class. 
What was the biggest challenge? 
The biggest challenge for me was setting up unit tests and being able to actually get those running based on the dependencies. Also, being able to connect to the front end and get something running initially was a challenge in itself.
Why was it a challenge?
For the unit tests, it was troublesome for me because I am inexperienced with unit testing and this would technically be the first time I made one that actually got working.
As for the front end, it was difficult because I didn’t expect it to be difficult to navigate how to connect it based on the necessary components.
How was it addressed?
It was addressed by going to office hours and asking for guidance from peers. 
Favorite / most interesting part of this project
I really enjoyed making the routes and endpoints. I thought it was satisfying and interesting to be able to create routes to see in real time and interact with as a result. 
If you could do it over, what would you change?
I would change how the front end was approached. We didn’t fully even understand what was going on with the front end and what to make based off it. For example, we didn’t realize it was the NBA when we opted for the NFL. 
What is the most valuable thing you learned?
Most valuable thing I learned was making API’s, endpoints, routes, and making my first ever unit test!


Alexis Guzman
I implemented testing along with GitHub Actions. I also worked on tables with their respective repositories and routes.
What was your role / which stories did you work on
My role was setting up testing and github actions for every push/pull request. I worked on issues such as test setup(as mentioned), player table, repo, routes.
How much time was spent working outside of class
I spent 4-5 hours a week outside of class.
What was the biggest challenge? 
My biggest challenge was getting the test suite up and having it be compatible with GitHub Actions. Since we used springboot the setup was a bit different than I was familiar with, but we were able to make it work.
Why was it a challenge?
It was a challenge because I have been used to using the same python, react, javascript tech stack for github actions. This was my first time setting it up using junit/springboot.
How was it addressed?
I resorted to script setups for springboot which I found online and just replaced the data with our own.
Favorite / most interesting part of this project
My favorite part of the project was working on the routing. To be completely honest, it was my favorite part because I found it the easiest, seemed like everything else I struggled with.
If you could do it over, what would you change?
If I could do it over again, I would’ve setup the test suite before anything else. This way we could’ve made it mandatory very early to have tests for every table, routes, and repositories.
What is the most valuable thing you learned?
The most valuable thing I learned about this project is that building from scratch is way easier and less annoying than trying to adapt to an already built project.

Conclusions
How successful was the project?
I would say that the project was about 90% successful. We worked on all of the aspects of the backend that the frontend would utilize, but did not implement the features fully through the frontend through the UI. But we were able to understand the flow of how the database is set up and how things are hosted on a server.
What was the largest victory?
The biggest victory was probably getting OAuth2 to work as well as getting JUnit working with GitHub Actions. It was nice and convenient to see the JUnit tests always running to ensure that the base of our backend wasn’t breaking with every change.
Final assessment of the project
The project was a valuable part of learning how the structure of a backend hosted on a server and how tables are set up with a DAO. The interaction between the database on the server through API routes is something valuable to take away from this project. Learning how the OAuth flow works between the service providing it and the application was ultimately a pro in the takeaways from this project.
