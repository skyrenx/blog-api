# Local Development Setup

This Markdown file contains all the useful information for running the blog API locally with a PostgreSQL database in Docker.

---

## 🛠️ Local Development Setup

This section explains how to run the blog API locally with a PostgreSQL database using Docker.

---

### 1. Prerequisites

- Docker installed on your machine
- Maven installed for building Spring Boot
- `init.sql` file present in the project root (contains schema and sample data)

---

### 2. Run PostgreSQL in Docker

We provide a helper script `run-postgres.sh` to start a PostgreSQL container for local development:

```bash
chmod +x run-postgres.sh
./run-postgres.sh
```

The script will:

- Remove any existing container named `blog-postgres`
- Remove the existing volume (resets the database)
- Create a new container with PostgreSQL
- Initialize the database and tables with `init.sql`
- Wait until the DB is ready

---

### 3. Verify the Database

After running the script, you can verify your database is up:

```bash
# List running containers
docker ps

# Connect to the database using psql
docker exec -it blog-postgres psql -U blog_user -d blog_db

# Inside psql, list tables
\dt

# Query sample data
SELECT * FROM users;
SELECT * FROM authorities;
```

---

### 4. Connection Info (TablePlus / GUI)

| Field    | Value             |
|----------|-----------------|
| Host     | localhost        |
| Port     | 5432             |
| Database | blog_db          |
| User     | blog_user        |
| Password | password123      |

**Connection URLs:**

- PostgreSQL URI: `postgresql://blog_user:password123@localhost:5432/blog_db`  
- JDBC URL: `jdbc:postgresql://localhost:5432/blog_db?user=blog_user&password=password123`  

---

### 5. Exiting the Database

- If using `psql` in terminal: type `\q` and press Enter  
- If using `docker exec`: quitting `psql` returns you to your shell; container keeps running  
- If using a GUI (TablePlus, DBeaver, etc.): use Disconnect or close the connection tab  

> ⚠️ Exiting `psql` does **not** stop the PostgreSQL container. To stop it completely:

```bash
docker stop blog-postgres
```

---

### 6. Spring Boot Configuration for Local Dev

Update your `application-dev.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blog_db
spring.datasource.username=blog_user
spring.datasource.password=password123
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

- `ddl-auto=update` is convenient for dev, but consider `validate` for production.  
- Make sure your `User` entity maps `enabled` as `boolean` to match the DB schema.

---

### 7. Running the Spring Boot API Locally

```bash
mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Your API should now be running locally at `http://localhost:8080`.

---

### 8. Notes

- This setup is **only for local development**.  
- Every time you run `run-postgres.sh`, the database is reset.  
- Use this to test authentication, blog entries, and sample data without affecting production.

