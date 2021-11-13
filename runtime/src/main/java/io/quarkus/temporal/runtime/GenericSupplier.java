package io.quarkus.temporal.runtime;

import java.util.function.Supplier;

/**
 * @param <T>
 * @Author Mostafa
 * Generic Supplier for handle workflow implementation supplier
 */
public class GenericSupplier<T> implements Supplier<T> {

    T data;

    public GenericSupplier(T data) {
        this.data = data;
    }


    public GenericSupplier() {
    }

    @Override
    public T get() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
