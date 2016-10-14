@ECHO off
SET DIR=%~dp0
SET LIB="%DIR%lib"
SET CP=%LIB%\*;%LIB%
java -cp %CP% il.ac.huji.cs.itays04.cli.Main %*