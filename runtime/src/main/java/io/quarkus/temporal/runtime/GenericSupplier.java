package io.quarkus.temporal.runtime;

import java.util.function.Supplier;

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
