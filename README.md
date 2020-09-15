# Responsive Web Shop

Alpha release. In development. 
* Production versions are also available per request. 

![Gif of Application Responsive Web shop](https://raw.githubusercontent.com/a-dridi/Responsive_Web_Shop/master/responsive-web-shop.gif)


This is a responsive web shop that is written in JSF and uses Postgresql as a database.
Your customers can order products directly without registration. They can pay through Paypal. An order of a customer can be checked by entering the customer email and order number. 

This application is multilingual (i18n). Translation is not finished. You can translate and add another languages in the folder "languages" in the folder "resources". 

## Video of the application

https://youtu.be/XfQeOiCm28g


## Configuration - Installation

Go to the folder "WEB-INF". Open the file "web.xml" and change the "javax.faces.PROJECT_STAGE" from "Development" to "Production".
Add your database settings in the file "hibernate.cfg" which is in the resources folder. Please add also your Paypal SDK account credentials in the file "credentials.properties" in the folder "credentials". This folder is also in the resources folder.
You need to compile this application to get the "war" file, which is in the folder "target". 

Copy this "war" file to the folder "webapps" of your web server "Apache Tomee Plume" Server. You have to configure also your web server. 


## Authors

* **A. Dridi** - [a-dridi](https://github.com/a-dridi/)
* See also License file

## Screenshots
![Screenshot of Application Responsive Web shop](https://raw.githubusercontent.com/a-dridi/Responsive_Web_Shop/master/screenshot.PNG)
![Screenshot 3 of Application Responsive Web shop](https://raw.githubusercontent.com/a-dridi/Responsive_Web_Shop/master/screenshot3.PNG)
![Screenshot 2 of Application Responsive Web shop](https://raw.githubusercontent.com/a-dridi/Responsive_Web_Shop/master/screenshot2.PNG)