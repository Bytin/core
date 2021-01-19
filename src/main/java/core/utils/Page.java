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

    private Page(){
        number = 0;
        size = 1;
        total = 0;
    }

    public <R> Page<R> map(Function<T, R> mapper) {
        Page<R> newPage = new Page<>();
        newPage.number = number;
        newPage.total = total;
        newPage.size = size;
        newPage.content = content.stream().map(mapper).collect(Collectors.toList());
        return newPage;
    }

    public boolean isEmpty() {
      return content.isEmpty();
    }
}
