#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>
#include <string>

// Tracy profiling header
#define TRACY_ENABLE
#include "tracy/public/tracy/Tracy.hpp"

#define PORT 8085
#define BUFFER_SIZE 1024

// Function to perform arithmetic operation
std::string perform_operation(const std::string& request) {
    // Zone for profiling this function
    ZoneScoped;

    double num1, num2;
    char operation;

    // Parse the request
    if (sscanf(request.c_str(), "%lf %c %lf", &num1, &operation, &num2) != 3) {
        return "Error: Invalid format";
    }

    // Track operation type
    TracyMessage(("Operation: " + std::string(1, operation)).c_str(),
                 11 + (operation != '\0'));

    // Perform the operation
    switch (operation) {
        case '+': {
            ZoneScopedN("Addition");
            return std::to_string(num1 + num2);
        }
        case '-': {
            ZoneScopedN("Subtraction");
            return std::to_string(num1 - num2);
        }
        case '*': {
            ZoneScopedN("Multiplication");
            return std::to_string(num1 * num2);
        }
        case '/': {
            ZoneScopedN("Division");
            if (num2 == 0) {
                return "Error: Division by zero";
            }
            return std::to_string(num1 / num2);
        }
        default:
            return "Error: Unsupported operation";
    }
}

int main() {
    // Main zone for entire program execution
    ZoneScoped;

    int server_fd, new_socket;
    struct sockaddr_in address;
    int opt = 1;
    int addrlen = sizeof(address);
    char buffer[BUFFER_SIZE] = {0};

    // Create socket file descriptor
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        std::cerr << "Socket creation error" << std::endl;
        return EXIT_FAILURE;
    }

    // Set socket options
    if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt))) {
        std::cerr << "Setsockopt error" << std::endl;
        return EXIT_FAILURE;
    }

    // Configure server address
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);

    // Bind the socket to the specified port
    if (bind(server_fd, (struct sockaddr *)&address, sizeof(address)) < 0) {
        std::cerr << "Bind error" << std::endl;
        return EXIT_FAILURE;
    }

    // Start listening for incoming connections
    if (listen(server_fd, 3) < 0) {
        std::cerr << "Listen error" << std::endl;
        return EXIT_FAILURE;
    }

    std::cout << "Server is listening on port " << PORT << std::endl;

    // Accept and handle incoming connections
    while ((new_socket = accept(server_fd, (struct sockaddr *)&address, (socklen_t*)&addrlen)) >= 0) {
        // New connection zone
        ZoneScopedN("Client Connection");

        memset(buffer, 0, BUFFER_SIZE);

        // Receive data
        {
            ZoneScopedN("Receive Data");
            read(new_socket, buffer, BUFFER_SIZE);
            std::cout << "Received request: " << buffer << std::endl;
        }

        // Process request and generate response
        std::string response;
        {
            ZoneScopedN("Process Request");
            response = perform_operation(buffer);
        }

        // Send response
        {
            ZoneScopedN("Send Response");
            send(new_socket, response.c_str(), response.length(), 0);
            std::cout << "Sent response: " << response << std::endl;
        }

        close(new_socket);
    }

    // Close the server socket
    close(server_fd);
    return 0;
}
