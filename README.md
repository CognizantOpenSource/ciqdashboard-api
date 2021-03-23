#ciqdashboard-api

- swaggerurl: `http://localhost:2019/swagger-ui.html`
- mongodb-url: `mongodb://<username>:<password>@host/<databasename>`
#Command to Execute JAR file
- java -jar ciqdashboard-api-3.1.0.jar --server.port=<serverName> --spring.data.mongodb.uri=mongodb://<DBUsername>:${spring.data.mongodb.credents}@<servername>:<DBPort>/<DBname> --spring.data.mongodb.credents=ENC(JasyptEncryptedDBPassword) --jasypt.encryptor.password=<Base64EncodeKey> 


