package core.utils;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Page<T> {
    private long number, total, size;
    private Collection<T> content;

    public <R> Page<R> map(Function<T, R> mapper) {
        Page<R> newPage = new Page<>(number, total, size,
                content.stream().map(mapper).collect(Collectors.toList()));
        return newPage;
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }
}
