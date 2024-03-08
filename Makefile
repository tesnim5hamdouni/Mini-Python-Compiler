all:
	mkdir -p bin
	javac -cp lib/java-cup-11a-runtime.jar -d bin src/mini_python/ExceptionHandler/*.java
	javac -cp lib/java-cup-11a-runtime.jar:bin -d bin src/mini_python/*.java
	java -cp lib/java-cup-11a-runtime.jar:bin mini_python.Main --debug test.py
	gcc custom.c test.s -o output

clean:
	rm -rf bin output test.s