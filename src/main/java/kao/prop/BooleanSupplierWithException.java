package kao.prop;

@FunctionalInterface
public interface BooleanSupplierWithException<E extends Exception> {
    public boolean getAsBoolean() throws E;
}

