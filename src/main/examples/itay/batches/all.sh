#!/bin/bash
./vga -r -q -n 1 > 1.txt &
./vga -r -q -n 10 > 10.txt &
./vga -r -q -n 100 > 100.txt &
./vga -r -q -n 1000 > 1000.txt &
./vga -r -q -n 10000 > 10000.txt
