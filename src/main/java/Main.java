import java.util.*;
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the climate change quiz , here we test your basic knowledge that you have about climate change and help clear any doubt if possible");

        Scanner sc = new Scanner(System.in);
        int usersChoice;
        int difficultyChoice;
        do {
            System.out.println("Choose the type of question you want to enter, 1 for multiple choice question and 2  for a true and false question : ");
            while (!sc.hasNext()){
                System.out.println("Invalid input. Enter 1 or 2 as an integer");
                sc.nextLine();
            }
            usersChoice = sc.nextInt();
            sc.nextLine();
        } while (!(usersChoice == 1 || usersChoice == 2)) ;

        do {
            System.out.println("Choose the level of difficulty that you want. ");
            System.out.println(" Choose 1 for 'EASY'");
            System.out.print("Choose 2 for 'DIFFICULT' ");

            while ( (!sc.hasNext())){
                System.out.println("Invalid input Enter 1 or 2 as integer");
                sc.nextLine();
            }
            difficultyChoice = sc.nextInt();
            sc.nextLine();

        } while (!(difficultyChoice == 1 || difficultyChoice ==2));

        Difficulty selectedDifficulty;

        if (difficultyChoice == 1) {
            selectedDifficulty = Difficulty.EASY;
        } else {
            selectedDifficulty = Difficulty.HARD;
        }
    }
}