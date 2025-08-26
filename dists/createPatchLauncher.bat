@echo off

echo Creating patch...
java -Djbsdiff.compressor=gz -jar %~dp0jbsdiff-1.0-all.jar diff %~dp0../lib/Cosmic-Reach.jar %~dp0../build/libs/Paradox-1.0-SNAPSHOT-all.jar %~dp0jarpatch.patch
cd %~dp0

echo Copying patch to resources
xcopy "%~dp0jarpatch.patch" "%~dp0../launcher/src/main/resources/jarpatch.patch" /y
del %~dp0jarpatch.patch
echo Done!