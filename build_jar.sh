#/bin/bash
echo "Re-compiling all java files..."
javac ./*/*/*/*.java
cd src
echo "Creating jar..."
jar cf midas.jar midas/*
cd ..
mv src/midas.jar .
echo "Jar has been created and labelled as 'midas.jar'"