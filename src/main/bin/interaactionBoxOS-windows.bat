@echo on

SET JAVA_HOME=..\lib\jre
SET PATH=%JAVA_HOME%\bin;%PATH%;%LocalAppData%\TobiiStreamEngineForJava\lib\tobii\x64

java -cp "..\lib\*" main.Main

pause
