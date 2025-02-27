#!/bin/bash

# Variables for MySQL container setup
CONTAINER_NAME="mysql-server"
MYSQL_ROOT_PASSWORD="my-secret-pw"
MYSQL_DATABASE="mydb"
MYSQL_USER="myuser"
MYSQL_PASSWORD="mypassword"
MYSQL_PORT=3306

# Pull the MySQL image if not available
echo "Checking for MySQL image..."
docker image inspect mysql:latest > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "MySQL image not found. Pulling latest MySQL image..."
    docker pull mysql:latest
else
    echo "MySQL image found."
fi

# Run the MySQL container
echo "Starting MySQL container..."
docker run --name $CONTAINER_NAME \
    -e MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD \
    -e MYSQL_DATABASE=$MYSQL_DATABASE \
    -e MYSQL_USER=$MYSQL_USER \
    -e MYSQL_PASSWORD=$MYSQL_PASSWORD \
    -p $MYSQL_PORT:3306 \
    -d mysql:latest

# Check if the container started successfully
echo "Verifying if the MySQL container is running..."
if [ $(docker ps -q -f name=$CONTAINER_NAME | wc -l) -gt 0 ]; then
    echo "MySQL container '$CONTAINER_NAME' is running successfully."

    # Wait a few seconds for MySQL to initialize
    echo "Waiting for MySQL to initialize..."
    sleep 10

    # Execute the SQL file to create the blog_entry table
    echo "Creating blog_entry table from blog_entries_schema.sql..."
    #TODO update path to blog_entries_schema
    docker exec -i $CONTAINER_NAME mysql -u$MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE < blog_entries_schema.sql
    #TODO exeute SQL file to create the users/authorities tables
    echo "blog_entry table created successfully."
else
    echo "Failed to start MySQL container."
    exit 1
fi
