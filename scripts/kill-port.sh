#!/bin/bash

# Check if a port number is provided as an argument
if [ $# -ne 1 ]; then
    echo "Usage: $0 <port-number>"
    exit 1
fi

PORT=$1

# Find the process ID (PID) of the process running on the specified port
echo "Checking for processes running on port $PORT..."
PIDS=$(lsof -t -i:$PORT)

# Check if there are any processes found
if [ -z "$PIDS" ]; then
    echo "No processes are running on port $PORT."
else
    echo "Processes running on port $PORT:"
    # List the details of the processes
    lsof -i:$PORT
    
    # Kill the processes
    echo "Killing processes with PIDs: $PIDS"
    kill -9 $PIDS
    
    echo "Processes killed."
fi
