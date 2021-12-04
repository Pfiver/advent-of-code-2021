package common;

import java.util.List;
import java.util.function.LongSupplier;
import java.util.stream.Stream;

public class Run {

    public static void attempt(int taskNumber, LongSupplier... longSuppliers) {

        List<Long> results = Stream.of(longSuppliers)
                .map(LongSupplier::getAsLong)
                .peek(System.out::println)
                .distinct()
                .toList();

        if (results.size() != 1) {
            throw new IllegalStateException("fail");
        }

        IO.writeResult(taskNumber, results.get(0).toString());
    }
}
