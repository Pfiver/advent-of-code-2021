package day01;

import java.util.AbstractList;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.ORDERED;

public class Window<T> extends AbstractList<T> {
    Object[] buffer;

    Window(int size) {
        buffer = new Object[size];
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        return (T) buffer[index];
    }

    public int size() {
        return buffer.length;
    }

    void push(T v) {
        System.arraycopy(buffer, 1, buffer, 0, buffer.length - 1);
        buffer[buffer.length - 1] = v;
    }

    public static <T> Stream<Window<T>> apply(Stream<T> upstream, int windowSize) {

        Window<T> window = new Window<>(windowSize);
        Spliterator<T> uSplit = upstream.spliterator();
        Spliterator<Window<T>> spliterator = new Spliterators.AbstractSpliterator<>(Long.MAX_VALUE, ORDERED | IMMUTABLE) {

            BooleanSupplier pollUpstream = () -> {
                pollUpstream = () -> uSplit.tryAdvance(window::push);
//                    return IntStream.range(0, window.size()).allMatch(i -> updateWindow.get());
                return IntStream.range(0, window.size()).allMatch(i -> uSplit.tryAdvance(window::push));
            };

            public boolean tryAdvance(Consumer<? super Window<T>> action) {
                if (pollUpstream.getAsBoolean()) {
                    action.accept(window);
                    return true;
                }
                return false;
            }
        };

        return StreamSupport.stream(spliterator, false);
    }
}
