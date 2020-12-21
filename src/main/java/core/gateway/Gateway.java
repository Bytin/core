package core.gateway;

import java.util.*;

public interface Gateway<T, ID> {
    Optional<T> findById(ID id);

    boolean existsById(ID id);

    void save(T o);

    int getSize();

    Collection<T> findAll();
}
