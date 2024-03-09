#!/bin/bash
#chmod +x ./myscript

cp ../mars/target/mars-0.0.1-SNAPSHOT.jar ./
scp mars-0.0.1-SNAPSHOT.jar root@193.124.114.82:../peresvet
#http://193.124.114.82:8081/


cp ../earth/target/earth-0.0.1-SNAPSHOT.jar ./
cp ../earth/src/main/resources/periods.json ./
scp earth-0.0.1-SNAPSHOT.jar root@193.124.113.4:../peresvet
scp periods.json root@193.124.113.4:../peresvet


#java -jar -Dperiods.path=periods.json earth-0.0.1-SNAPSHOT.jar &
#java -jar -DcurrentRemoteServer.host=http://193.124.113.4:8082 mars-0.0.1-SNAPSHOT.jar &

#rm ~/.ssh/known_hosts
