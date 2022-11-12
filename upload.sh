mvn package;
ssh -i key.txt leny@130.193.36.186 "rm aliance/aliance3-0.0.1-SNAPSHOT.jar";
scp -i key.txt target/aliance3-0.0.1-SNAPSHOT.jar leny@130.193.36.186:aliance;
ssh -i key.txt leny@130.193.36.186 \
  "curl http://localhost:1026/kill; \
   source ~/.profile; \
   java \
  -Xmx1024m \
  -Dspring.profiles.active=prod \
  -jar  aliance/aliance3-0.0.1-SNAPSHOT.jar &" &;
sleep 10;
exit 0;
