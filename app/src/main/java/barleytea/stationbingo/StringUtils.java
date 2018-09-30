package barleytea.stationbingo;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringUtils {
    public static String repeat(String str, int n) {
        return IntStream.range(0, n).mapToObj(i -> str).collect(Collectors.joining(","));
    }
}
