#/bin/bash
echo "Re-compiling all java files..."
javac ./*/*/*/*.java
cd src
echo "Creating jar..."
jar cf middleman.jar middleman/*
cd ..
mv src/middleman.jar .
echo "Jar has been created and labelled as 'middleman.jar'"