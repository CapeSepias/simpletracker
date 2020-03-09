# Simple tracker implementation

### Description
This is a simple tracking implementation for the Funnel.io code test.  
The page to be tracked should contain the tracker img tag \<img src="/tracker.gif" alt="" height="1px" width="1px">  
This implementation contains three example pages: home.html, about.html and contact.html for test use.  

### Tracker implementation
Java 11 is used for this implementation.  
Gradle 4.10+ is required.  
The access log is written to /var/log/funnel/access.log and the implementation assumes write permissions to this location. File location can if necessary be changed in log4j2.xml.   
The tracker implementation can be run with "gradle bootRun" from the project root folder.

### Log reader implementation
The log reader is written in perl and 5.20+ is required on the running server. The log reader assumes access.log file in /var/log/funnel/. File location can be changed in logreader.pl to match log4j2.xml as needed.  
logreader.pl takes two arguments, from_date and to_date. Date format is yyyy-mm-ddThh:mm:ss

### Testing
Unit tests are run with "gradle test" for the tracker implementation. Run "perl test-logreader.pl" for log reader unit test.   
When the SimpleTrackerImplementation is run, three pages containing the tracker img tag are available for testing: 
[http://localhost:8080/home.html](http://localhost:8080/home.html)  
[http://localhost:8080/about.html](http://localhost:8080/about.html)  
[http://localhost:8080/contact.html](http://localhost:8080/contact.html)

### Notes
This implementation does not handle referrer hiding  
This implementation does not include a cookie consent mechanism

