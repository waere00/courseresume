## Инструкции

Необходимо наличие Java 17 в системе. Запускать обязательно с -Dfile.encoding=UTF8.

Подключение к H2 БД: 
spring.datasource.url=jdbc:h2:~/resume_parse
spring.datasource.username=root
spring.datasource.password=pass

1. Собрать проект ResumeParseClient (mvn package)
2. Собрать проект ResumeParseServer (mvn package)
2. Запустить сервер: `java -Dfile.encoding=UTF8 -jar ResumeParseServer/target/ResumeParseServer-1.0-SNAPSHOT.jar`
3. Запустить клиент: `java -Dfile.encoding=UTF8 -jar ResumeParseClient/target/ResumeParseClient-1.0-SNAPSHOT.jar`

### Документация 

1. Собрать Javadoc документацию с помощью плагина javadoc:javadoc
