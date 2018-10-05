# Centralized Locking

This project is a simple implementation for a centralized locking system to manage shared resources.

It contains the server which has all the threading/locking logic, and a sample client to test out. However, to fully get the idea of locks, you need to run more than one client.

A simple way to run several clients is to build the included client and run as many instances as you wish, or simply connect to the server using telnet:
```
Using default server & port:

> telnet –o localhost 1234
```
Check out my other repository [More Locking](https://github.com/sandrows/more-locking), for different locking techniques. 