import java.util.Random;

public class gtest {
    public static void main(String[] args){
        Random random = new Random();
        for(int i=0;i<100;i++) {
            int typeOfDrunk = random.nextInt(3);
            System.out.println(typeOfDrunk + " ");
        }
    }
}
