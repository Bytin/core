package core.gateway;

import java.util.*;

public interface Gateway<T, ID> {
    Optional<T> findById(ID id);

    boolean existsById(ID id);

    void save(T o);

    long count();

    Collection<T> findAll();

    void deleteById(ID id);

    void delete(T t);
}
