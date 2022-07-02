package kao.prop;

@FunctionalInterface
public interface RunnableWithException<E extends Exception> {
    public void run() throws E;
}

