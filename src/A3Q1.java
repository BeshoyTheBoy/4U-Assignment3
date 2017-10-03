/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author awadb3223
 */
public class A3Q1 {
//swapping method

    public void swap(int[] array, int p1, int p2) {
        int temp = array[p1];
        array[p1] = array[p2];
        array[p2] = temp;
    }

    //SELECTION SORT***
    public void selectionSort(int[] array) {

        //to keep track of which position we are sorting
        for (int position = 0; position < array.length-1; position++) {
            //assume the first number position is the smallest number
            int smallest = position;
            //Go through the rest of array looking for the smallest number
            for (int i = position + 1; i < array.length; i++) {
                //if i is less than the initial "smallest" position
                if (array[i] < array[smallest]) {
                    //set smallest to number at i
                    smallest = i;
                }
            }
            swap(array, smallest, position);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        A3Q1 test = new A3Q1();
        //making random array of ints
        int[] numbers = new int[10];
        for (int i = 0; i < numbers.length; i++) {
            //make a random number
            numbers[i] = (int) (Math.random() * 12);
        }
        //BEFORE THE SORT
        System.out.println("BEFORE:");
        for (int i = 0; i < numbers.length; i++) {
            System.out.println(numbers[i]);
        }
        System.out.println("");
        System.out.println("");

        //SORT METHOD
        test.selectionSort(numbers);

        //AFTER
        System.out.println("AFTER:");
        for (int i = 0; i < numbers.length; i++) {
            System.out.println(numbers[i]);
        }
    }
}