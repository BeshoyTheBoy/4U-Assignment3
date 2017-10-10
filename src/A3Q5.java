/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author awadb3223
 */
public class A3Q5 {

    public void swapString(String[] array, int p1, int p2) {
        String temp = array[p1];
        array[p1] = array[p2];
        array[p2] = temp;
    }

    // insertion sort
    public void insertionSort(String[] array) {
        // start going through the array
        for (int i = 0; i < array.length - 1; i++) {
            // store position
            int position = i;
            // check values beside each other 
            while (position >= 0 && array[position].compareTo(array[position + 1]) > 0) {
                // if out of place, swap it downwards
                // until correct position is reached 
                swapString(array, position + 1, position);
                position--;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // TODO code application logic here
        A3Q5 test = new A3Q5();

        String[] array = new String[5];

        array[0] = "bun";
        array[1] = "cheese";
        array[2] = "cat";
        array[3] = "apple";
        array[4] = "promethazene";


        test.insertionSort(array);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
}
