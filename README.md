TypeAhead Query Engine
===========



<p align="center">
</p>


How to run
==========

First run `mvn clean install` 

- From command line

    `java -Dtypeahead.conf.path=../conf/ -jar target/typeahead-1.0-SNAPSHOT-jar-with-dependencies.jar`
    
- From Maven

    `mvn exec:java -Dexec.mainClass="com.knightRider.typeahead.server.SearchServer" -Dtypeahead.conf.path=../conf/`
    
If you don't pass `typeahead.conf.path` then Typeahead jar will look for file from `user.dir` which would
be the location from where you are executing jar/maven command.