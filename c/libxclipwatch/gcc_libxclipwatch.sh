#!/bin/bash
gcc -m64 -fPIC -shared -o libxclipwatch.so xclipwatch.c -lX11 -lXfixes
gcc -m64 -fPIC  -o xclipwatch xclipwatch.c -lX11 -lXfixes
