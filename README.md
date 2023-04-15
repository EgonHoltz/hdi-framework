# hdi-framework
Data Integration Framework

## MqSftpService

Project declared on MqSftpService folder
### Installation of environment

Local:

> Change /home/egonh to your own local disk

```sh
docker run -d --name hdi-mongo -p 27017:27017 -v /home/egonh/projects/docker/mongodb-hdi:/data/db mongo
```

Creating RabbitMQ server to allow MQ Queue

```sh
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq
```
After server starts, perform the configuration:
```sh
rabbitmqctl add_user quser qpass
rabbitmqctl set_permissions -p / quser ".*" ".*" ".*"
abbitmq-plugins enable rabbitmq_management
```
It creates a generic user, set all permissions to it and enable the UI user management

Now, with user and pass "guest" it is possible to configure new MQ Queues by interface.

### Testing suite

To test SFTP, a server was created:
```sh
docker run -p 8022:22 -v /home/egonh/projects/docker/sftp:/home/client/recv -d --name sftp_test_svr -e SFTP_USERS="client:clientPass:1001:100:recv" atmoz/sftp
```



## GrpcService
Project declared on GrpcServiceApplication folder
### Installation and build

After the push of project, please build the maven using the "package" target

```sh
mvn package -DskipTests 
```

After success, change the project classpath to add the generated classes. It will be generated on:
```
/target/generated-sources/protobuf
```
Go to the folders below and add it to the classpath:

/target/generated-sources/protobuf/grpc-java

/target/generated-sources/protobuf/java

Right click on the folder > Buildpath > User as source folder


## Rest Service
Project declared on RestServiceApplication folder


