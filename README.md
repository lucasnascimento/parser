Deliverables
------------

1. Java program that can be run from command line

Compile project using command:
```
$ ./gradlew clean build
```
Then run (All are examples of execution inside challenge document):
```
$ java -jar build/libs/parser.jar --accesslog=src/main/resources/data/access.log --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500
$ java -jar build/libs/parser.jar --accesslog=src/main/resources/data/access.log --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100
$ java -jar build/libs/parser.jar --accesslog=src/main/resources/data/access.log --startDate=2017-01-01.13:00:00 --duration=daily --threshold=250
```

> Each execution will destroy data and reconstruct log table structure. In a production environment we need to understand
how it's program supposed work to increase performance avoiding this reload, wich takes arroung 2 minutes execution.

2. Source Code for the Java program

You can find in this repository

3. MySQL schema used for the log data

[You can find here](https://github.com/lucasnascimento/parser/blob/master/src/main/resources/schema-all.sql)

4. SQL queries for SQL test
```
# SQL (1)
select ip, count(*) from log where date between '2017-01-01 13:00:00' and '2017-01-02 13:00:00' group by ip having count(*) > 250
# SQL (2)
select * from log where ip = '192.168.62.176';
```

Improvement's proposal:
- 
* Create control of wich file were processed, to improve load performance.
* ~~Unit test layer~~ - DONE
* Add relationship between log and blocked to make more easy find why some blocked ip were blocked.