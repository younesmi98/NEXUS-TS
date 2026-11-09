@echo off
set ROOT=%~dp0
if not exist "%ROOT%upstream\ts3x-client\gradlew.bat" (
  echo Missing pinned TeamSpeak source. Run: bash bootstrap.sh
  exit /b 1
)
call "%ROOT%upstream\ts3x-client\gradlew.bat" %*
