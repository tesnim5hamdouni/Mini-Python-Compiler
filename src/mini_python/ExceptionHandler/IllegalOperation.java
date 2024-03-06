package mini_python.ExceptionHandler;

public class IllegalOperation extends Error {
    public IllegalOperation(String message) {
        super(message);
    }

    public IllegalOperation() {
        this("Illegal Operation");
    }

}
