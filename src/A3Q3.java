/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author awadb3223
 */
public class A3Q3 {

    public int countOnes(int[] array) {
        //create int to store number of 1s found
        int ones = 0;
        //start at first position
        for (int position = 0; position < array.length; position++) {

            //if the number in the position equals 1, store it in variable
            if (array[position] == 1) {
                ones = ones + 1;
            }
        }
        return ones;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        A3Q3 test = new A3Q3();
        //create array
        int[] array = new int[5];
        //make array
        array[0] = 0;
        array[1] = 1;
        array[2] = 1;
        array[3] = 0;
        array[4] = 1;
        
        System.out.println("The number of ones is: " + test.countOnes(array));
    }
}
