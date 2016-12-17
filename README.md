# hacker-rank-test-preparation
---------------------------------------------------
Thank you for considering Amazon for your next career. We know there are loads of great companies out there so we are super 
excited that you have decided to think about us.

As part of the interview process for Software Development Engineers here at Amazon we would like you to complete the following 
technical assignment.

Please liaise with your Recruiter or Agent if you have any questions

Hope the test goes well.

All the best

You have been invited to attend the challenge SCP London Screening. You can take this challenge any time. 
The duration of the challenge will be 75 mins from the time you start.
---------------------------------------------------

The test will be from Hacker Rank.
 
·         You will need to do some practice tests on the site first of all BEFORE taking the real one.  
          This is essential so you get used to the system and way of coding.

·         There are no IDEs, script/text editors. This is the format of programming Amazon use for tests and interviews.

·         Speed of how quickly you complete the test also plays a part – although good, clean concise code is paramount.

·         I have attached Amazon Interview Prep – worth reading through the technical areas discussed

·         Attached – Crack the Code – Chapter 6 is useful here since the logic based questions form the basis of some of the questions.
          Other chapters also useful to give you an idea, but the practice tests are more useful if time is tight for preparation.

·         There are other sites you can practise on as well: http://www.topcoder.com/, http://www.codechef.com/
 
Also look at these videos: https://www.youtube.com/watch?v=aClxtDcdpsQ + https://www.youtube.com/watch?v=BN0B4mOtwX0
 
Best of luck!
--------------------------------------------------------

from amazon description:

Algorithms
-------------------
Your interview with Amazon will not be focused on rote memorization of algorithms; however, having a good
understanding of the most common algorithms will likely make solving some of the questions we ask a lot easier. Consider
reviewing traversals, divide and conquer, and any other common algorithms you feel might be worth brushing up on. For
example, it might be good to know how and when to use a breadth-first search versus a depth-first search, and what the
tradeoffs are. Knowing the runtimes, theoretical limitations, and basic implementation strategies of different classes of
algorithms is more important than memorizing the specific details of any given algorithm.

Coding
------------
Expect to be asked to write syntactically correct code—no pseudo code. If you feel a bit rusty coding without an IDE or
coding in a specific language, it’s probably a good idea to dust off the cobwebs and get comfortable coding with a pen and
paper. The most important thing a Software Development Engineer does at Amazon is write scalable, robust, and welltested code.
hese are the main criteria by which your code will be evaluated, so make sure that you check for edge cases
and validate that no bad input can slip through. A few missed commas or typos here and there aren’t that big of a deal, but
the goal is to write code that’s as close to production ready as possible. This is your chance to show off your coding ability

Object-Oriented Design
----------------------------------------
Good design is paramount to extensible, bug free, long-lived code. It’s possible to solve any given software problem in an
almost limitless number of ways, but when software needs to be extensible and maintainable, good software design is
critical to success. Using Object-oriented design best practices is one way to build lasting software. You should have a
working knowledge of a few common and useful design patterns as well as know how to write software in an objectoriented way,
with appropriate use of inheritance and aggregation. You probably won’t be asked to describe the details of
how specific design patterns work, but expect to have to defend your design choices.

Databases
------------------
Most of the software that we write is backed by a data store, somewhere. Many of the challenges we face arise when
figuring out how to most efficiently retrieve or store data for future use. Amazon has been at the forefront of the nonrelational
DB movement. We have made Amazon Web Services such as SimpleDB and DynamoDB available for the
developer community that let them easily leverage the benefits of non-relational databases. The more you know about how
relational and non-relational databases work and what tradeoffs exist between them, the better prepared you will be.
However, we don’t assume any particular level of expertise.

Distributed Computing
--------------------------------------
Systems at Amazon have to work under very strict tolerances at a high load. While we have some internal tools that help us
with scaling, it’s important to have an understanding of a few basic distributed computing concepts. Having an
understanding of topics such as service oriented architectures, map-reduce, distributed caching, load balancing, etc. could
help you formulate answers to some of the more complicated distributed architecture questions you might encounter.

Operating Systems
----------------------------------
You won’t need to know how to build your own operating system from scratch, but you should be familiar with some OS
topics that can affect code performance, such as: memory management, processes, threads, synchronization, paging, and
multithreading.

Internet Topics
---------------------------
You’re interviewing at Amazon. We do a lot of business online, and we expect our engineers to be familiar with at least the
basics of how the internet works. You might want to brush up on how browsers work at a high level, from DNS lookups and
TCP/IP, to socket connections. We aren’t looking for network engineer qualifications, but a solid understanding of the
fundamentals of how the web works is a requirement.


This was a relatively long list of topics to review, and might seem somewhat overwhelming. Your interviewers won’t be
evaluating your ability to memorize all of the details about each of these topics. What they will be looking for is your ability
to apply what you know to solve problems efficiently and effectively. Given a limited amount of time to prepare for a
technical interview, practicing coding outside of an IDE and reviewing CS fundamentals will likely yield the best results for
your time.


========================================================================================================================
Further Reading and trainings:
---------------------------------------

HOW can you get better at algorithms???
-take an "DS" and "Algo" course and practice from geeksforgeeks.org
as simple as that

Can I read about previous Amazon interviews?
http://www.geeksforgeeks.org/amazon-interview-experience-set-317-sde-1-off-campus/
etc (http://www.geeksforgeeks.org/tag/amazon/)

https://projecteuler.net/archives

https://codility.com/


so as same as in Google 3 most things be tested:
1 Coding (proficiently, quickly (but ask questions), compiled code written on whiteboard)
2 Algorithms
    Graphs:
        search BFS, DFS,
        shortest path - Dijkstra's algorithm, Bellman Ford, Floyd - Warshall
        max flow - Ford - Fulkerson

    Dynamic Programming:
        1D, 2D, ..., correctness algorithm, relation (math tool)
        (greedy)

    Data Structures:
        arrays, maps, queues, stacks, binary search trees (BST), self balansing BST, B-trees, segment/interval trees, KD - trees
        string datastructures: tries (with classical alg-s of string matching, sliding window)

    Complexity: need to know your computability and complexity classes - Big O, Omega, Theta, NP complete

    Discrete Mathematic, discrete probability, simple probability proofs etc

    Books recommended -  "Introduction to Algorithms" - by Thomas H. Cormen & co, "Algorithms" by Sanjoy Dasgupta
    Web sites: spoj.com/problems/classical/ , codeforces.com , train.usaco.org

    eg classical example - you will get points on map task - big field of static points and you haave to find k closest neighboors,
    proof it fast and analize complexity, you will be asked to write real code and if you complete same task complexity maay be increased,
    like a 3D


3 Design
  Building large production software
    (eg how to design Google Hangouts (Chat) - how user communicates via browser with server with http
        and how to do it well, how to build up server - MVC etc bla bla, chat message, user, conversation - as model , etc bla blah,
        should properly describe what code runs on client , server, http, database :D
     then complexity grows - if users too much, if web servers too much , how to scale - Load Balancers, DB slaves and propagation between them,
     secure encrypted protocols)

     -multitier
     -orthgonality
     -separation of concerns
     -separate component in order to assign to different teams
     -estimation quaestions (how many people/how long it would take to build/how much money do you need)
     (but ask questions!!! - like is it real time chat (via web sockets), how many connections)
     -network protocols and how do they work (DNS, HTTP, UDP, SSL/TLS, Web Sockets, compression)
     -optimization



   books: Gof :D, POSA, Fowler, JEE Patterns, etc


   So, the general exam is like a 2 algotithms questions, 2 desing questions and one about probability proof plus
   one task for security mistakes in code
