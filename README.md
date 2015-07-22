# userLogin
a simple userLogin

## manuscript
![alt entries and flow](https://raw.githubusercontent.com/zhengda/userLogin/master/doc/IMG_20150721_205025.jpg)
![alt scenarios](https://raw.githubusercontent.com/zhengda/userLogin/master/doc/IMG_20150721_205035.jpg)

## UserDAO
It holds **a map as a very simple storage** to store username and user object to simplify it's structure. 
The capacity of the storage is 100 to avoid OOM. Not much but quiet enough for testing.
Account [sysadmin/asdfzxcv] is a predefined record for the first time login and it's immutable purposely.
Practically ORM framework like hibernate will be utilized to handle complex data schema and interaction with SQL database.

## login.jsp , LoginControl & AccessControlFilter
A regular structure to achieve access control of web page. 
Of course many objects are involved. Basically here I used filter to block unauthenticated user and only expose login page.

The interested part I spend more time on is  ExpiringLRUMap.
ExpiringLRUMap records access frequency of all incoming IP and has some setup to ban IP if the IP is suspicious of misusing the system.

Currently it's simple enough for review. When the system get bigger, security framework with full function of configuration and support might be introduced into the system. 

## create/read/update/delete/list user
Some CRUD function are made with crude UI.
UserControl is the core object organizing those function. It works as a controller in MVC.

## RESTful API on /api/user
ApiServlet is dedicated to provide a RESTful API to manage user objects in system.
The mechanism of authentication is bond with POST method. It only activates when request header contains "auth=login" or "auth=logout".

Authtoken can be obtained and be released at will.
All interactions between client and restful api is list in the test case, ApiServletTest. 
It would be the first step to figure out how they works.

Besides ApiServletText, RESTClient is a good cliene I would recommand for test.
