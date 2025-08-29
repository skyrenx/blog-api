#!/bin/bash

# Config
CONTAINER_NAME="blog-postgres"
DB_NAME="blog_db"
DB_USER="blog_user"
DB_PASSWORD="password123"
DB_PORT="5432"
VOLUME_NAME="blog-postgres-data"
INIT_SQL="./init.sql"

# Check if Docker is installed
if ! command -v docker &> /dev/null
then
    echo "Docker is not installed. Please install Docker first."
    exit 1
fi

# Remove any existing container
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    echo "Removing existing container '$CONTAINER_NAME'..."
    docker rm -f $CONTAINER_NAME
fi

# Remove existing volume (optional: reset DB)
if [ "$(docker volume ls -q -f name=$VOLUME_NAME)" ]; then
    echo "Removing existing volume '$VOLUME_NAME'..."
    docker volume rm $VOLUME_NAME
fi

# Run PostgreSQL container with init script
echo "Starting new PostgreSQL container with initialization..."
docker run -d \
  --name $CONTAINER_NAME \
  -e POSTGRES_DB=$DB_NAME \
  -e POSTGRES_USER=$DB_USER \
  -e POSTGRES_PASSWORD=$DB_PASSWORD \
  -p $DB_PORT:5432 \
  -v $VOLUME_NAME:/var/lib/postgresql/data \
  -v $(pwd)/init.sql:/docker-entrypoint-initdb.d/init.sql:ro \
  postgres:15

# Wait for DB to be ready
echo "Waiting for PostgreSQL to initialize..."
until docker exec -it $CONTAINER_NAME pg_isready -U $DB_USER -d $DB_NAME > /dev/null 2>&1; do
    sleep 1
done

echo "PostgreSQL is running and initialized!"
echo "Host: localhost"
echo "Port: $DB_PORT"
echo "Database: $DB_NAME"
echo "User: $DB_USER"
echo "Password: $DB_PASSWORD"

echo ""
echo "✅ PostgreSQL is ready for use!"
echo ""
echo "💻 Verify the container and database:"
echo "# Check running containers"
echo "docker ps"
echo ""
echo "# Connect to the database using psql"
echo "docker exec -it $CONTAINER_NAME psql -U $DB_USER -d $DB_NAME"
echo ""
echo "# List all tables"
echo "\\dt"
echo ""
echo "# Query sample data"
echo "SELECT * FROM users;"
echo "SELECT * FROM authorities;"
echo ""
echo "🔗 Connection info for TablePlus or any SQL client:"
echo "Host: localhost"
echo "Port: 5432"
echo "Database: blog_db"
echo "User: blog_user"
echo "Password: password123"
echo ""
echo "Connection URLs you can copy:"
echo "PostgreSQL URI: postgresql://blog_user:password123@localhost:5432/blog_db"
echo "JDBC URL: jdbc:postgresql://localhost:5432/blog_db?user=blog_user&password=password123"
echo ""
echo "ℹ️ How to exit PostgreSQL:"
echo "- If you're in the terminal using psql: type \\q and press Enter to quit the session."
echo "- If you connected via 'docker exec': quitting psql returns you to your shell, but the container keeps running."
echo "- If you're using a GUI (TablePlus, DBeaver, etc.): use the Disconnect button or close the connection tab."
echo ""
echo "⚠️ Note: Exiting psql does NOT stop the PostgreSQL container. To stop the container completely, run:"
echo "docker stop $CONTAINER_NAME"
echo ""
