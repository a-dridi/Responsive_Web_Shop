# Responsive Web Shop

Alpha release. In development. 
* Production versions are also available per request. 

This is a responsive web shop that is written in JSF and uses Postgresql as a database.
Your customers can order products directly without registration. They can pay through Paypal. An order of a customer can be checked by entering the customer email and order number. 

![Screenshot of Application Responsive Web shop](https://raw.githubusercontent.com/a-dridi/Responsive_Web_Shop/master/screenshot.PNG)

This application is multilingual (i18n). Translation is not finished. You can translate and add another languages in the folder "languages" in the folder "resources". 


## Configuration

Go to the folder "WEB-INF". Open the file "web.xml" and change the "javax.faces.PROJECT_STAGE" from "Development" to "Production".
Add your database settings in the file "hibernate.cfg" which is in the resources folder. Please add also your Paypal SDK account credentials in the file "credentials.properties" in the folder "credentials". This folder is also in the resources folder.
You need to compile this application to get the "war" file, which is in the target folder. 

## Authors

* **A. Dridi** - [a-dridi](https://github.com/a-dridi/)
* See also License file

## Screenshots

![Screenshot 2 of Application Responsive Web shop](https://raw.githubusercontent.com/a-dridi/Responsive_Web_Shop/master/screenshot2.PNG)