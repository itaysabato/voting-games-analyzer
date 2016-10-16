#!/bin/bash
./vga -r -q -n 1 -nc > nc1.txt &
./vga -r -q -n 10 -nc > nc10.txt &
./vga -r -q -n 100 -nc > nc100.txt &
./vga -r -q -n 1000 -nc > nc1000.txt &
./vga -r -q -n 10000 -nc > nc10000.txt
