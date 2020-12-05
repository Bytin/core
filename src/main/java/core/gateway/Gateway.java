package core.gateway;

import java.util.Collection;

public interface Gateway<T, ID> {
    T findById(ID id);

    boolean existsById(ID id);

    void save(T o);

    Collection<T> findAll();
}
