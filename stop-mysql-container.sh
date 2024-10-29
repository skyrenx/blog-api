#!/bin/bash

# Define the name of the MySQL container
CONTAINER_NAME="mysql-server"

# Stop the MySQL container
echo "Stopping the MySQL container..."
docker stop $CONTAINER_NAME

# Check if the container stopped successfully
if [ $? -eq 0 ]; then
    echo "MySQL container '$CONTAINER_NAME' stopped successfully."
else
    echo "Failed to stop the MySQL container."
    exit 1
fi

# Remove the MySQL container
echo "Removing the MySQL container..."
docker rm $CONTAINER_NAME

# Confirm the container was removed
if [ $? -eq 0 ]; then
    echo "MySQL container '$CONTAINER_NAME' removed successfully."
else
    echo "Failed to remove the MySQL container."
    exit 1
fi
