mvn package;
ssh -i key.txt leny@130.193.36.186 "rm aliance/aliance3-0.0.1-SNAPSHOT.jar";
scp -i key.txt target/aliance3-0.0.1-SNAPSHOT.jar leny@130.193.36.186:aliance;
curl http://130.193.36.186:1026/kill;
ssh -i key.txt leny@130.193.36.186 "java  -Xmx1024m -jar -Dspring.profiles.active=prod aliance/aliance3-0.0.1-SNAPSHOT.jar" &;
sleep 10;
exit 0;
