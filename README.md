# file-catalog
A client-server system built with Java RMI and JPA that allows users to upload and download files.

## How to build
Run the following command in the root project directory:
```bash
mvn package
```

## How to run
Start the server by executing the server.jar JAR located in .\server\target:
```bash
java -jar server.jar <port> 
```

Start the client by executing the client.jar JAR located in .\client\target:
```bash
java -jar client.jar <host>
```
## Database details
Make sure to have a MySQL instance running with a database with the specified tables and to update ./server/src/main/resources/META-INF/persistence.xml appropriately:
```sql
create table user
(
	id int auto_increment
		primary key,
	name varchar(45) not null,
	password varchar(300) not null
)
;

create table file
(
	id int auto_increment
		primary key,
	owner_id int not null,
	name varchar(45) not null,
	size int not null,
	content blob not null,
	readOnly tinyint(1) default '1' not null,
	constraint file_name_uindex
		unique (name),
	constraint file_user_id_fk
		foreign key (owner_id) references user (id)
			on update cascade on delete cascade
)
;

create index file_user_id_fk
	on file (owner_id)
;
```
