package org.example;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
            Optional<Integer> currentDiff = seekedList.stream().reduce((maxDiff, current) -> {
                System.out.println("e - current: " + e + "-" + current + "=" + (e -current));
                return (((e-current) > maxDiff)? (e-current): maxDiff);
            });
            seekedList.add(e);
            //System.out.println(currentDiff.get());
            if(r > currentDiff.get()) {
                return r;
            } else {
                return currentDiff.get();
            }
        });
    }
}
