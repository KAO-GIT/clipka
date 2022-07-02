#!/bin/bash
gcc -o xi xi.c -lX11 -lXext -lXi
gcc -m64 -fPIC -shared -o libxi.so xi.c -lX11 -lXext -lXi

