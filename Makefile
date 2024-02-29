
all:
	mkdir -p bin
	javac -cp lib/java-cup-11a-runtime.jar -d bin src/mini_python/*.java
	java -cp lib/java-cup-11a-runtime.jar:bin mini_python.Main --debug test.py

