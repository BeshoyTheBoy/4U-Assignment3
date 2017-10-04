/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author awadb3223
 */
public class A3Q2 {

    public int smallestMissingNumber(int[] array) {
        //look through array and see if position is 1 less than i
        for (int position = 0; position < array.length - 1; position++) {
            //look at number after position
            int i = position + 1;
            //compare
            //if the number in 1st position is NOT equal to one less than the number in the 2nd position
            if (array[position] != array[i] - 1) {
                return i;
            }
        }
        //if numbers organized
        return -1;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        A3Q2 test = new A3Q2();
        //Make array
        int[] array = new int[5];
        //organize array
        array[0] = 0;
        array[1] = 1;
        array[2] = 2;
        array[3] = 4;
        array[4] = 5;

        System.out.println("The missing number is " + test.smallestMissingNumber(array));
    }
}
