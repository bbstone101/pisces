rem set PATH=%JAVA_HOME%\bin;%PATH%
rem java -Xmx2048m -Xms1024m -cp pisces.jar -Dfile.encoding=UTF-8 com.bbstone.pisces.server.ServerStarter


java -Dappdir=.. -cp ../lib/*;../conf/; com.bbstone.pisces.server.ServerStarter
