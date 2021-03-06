package org.example;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void pruebaReduce() {
        maxTrailing(Stream.of(7, 2, 3, 10, 2, 4, 8, 1).collect(Collectors.toList()));
    }

    public static int maxTrailing(List<Integer> arr) {
        List<Integer> seekedList = new ArrayList<>();
        seekedList.add(arr.get(0));
        return arr.subList(1, arr.size()).stream().reduce(-1, (r, e) -> {
            System.out.println("result: " + r + ", element: " + e);
            Integer currentDiff = seekedList.stream().reduce(-1, (maxDiff, current) -> {
                System.out.println("e - current: " + e + "-" + current + "=" + (e -current));
                return (((e-current) > maxDiff)? (e-current): maxDiff);
            });
            seekedList.add(e);
            //System.out.println(currentDiff.get());
            return(r > currentDiff)? r: currentDiff.intValue();
        });
    }
}
