#!/bin/bash
gcc -m32 -fPIC -shared -o libxclipwatch32.so xclipwatch.c -lX11 -lXfixes
