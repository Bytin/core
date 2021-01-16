package core.utils;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Page<T> {
    private int number, total, size;
    private Collection<T> content;

    public <R> Page<R> map(Function<T, R> mapper) {
        Page<R> newPage = new Page<>();
        newPage.number = number;
        newPage.total = total;
        newPage.size = size;
        newPage.content = content.stream().map(mapper).collect(Collectors.toList());
        return newPage;
    }
}
