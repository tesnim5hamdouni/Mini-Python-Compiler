package mini_python.ExceptionHandler;

public class UnknownType extends Error {
    public UnknownType(String message) {
        super(message);
    }

    public UnknownType() {
        this("Unknown Type");
    }

}
