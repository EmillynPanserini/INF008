# Library Management System - Microkernel Architecture


```bash
# 1.Navigate to the project directory
cd microkernel

# 2.Start the database
docker-compose up -d

# 3. Compile
mvn clean install

# 4. Execute application
mvn exec:java -pl app
```

**Database Settings:**

- Host: localhost:3306
- Usuário: root / Senha: root
- Database: library



**Compile and run**:

```bash
mvn clean install
mvn exec:java -pl app
```


# Compile only a specific plugin
cd plugins/book-plugin && mvn clean package



