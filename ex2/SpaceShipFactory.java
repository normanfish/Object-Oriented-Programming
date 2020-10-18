import oop.ex2.*;

/**
 * This class implements the Spaceship factory, and has a single static method (createSpaceships(String[])).
 * It is used by the supplied driver to create all the spaceship objects according to the command line arguments.
 * E.g., if the supplied array contains the strings "a" and "b", the method will return an array containing
 * an Aggressive ship and a Basher ship.
 */
public class SpaceShipFactory {
    /**
     * The class (read: factory) constructor. it recieves as input the command live, creates the according spaceships,
     * and returns the array of the spaceships.
     *
     * @param args - the command line arguments
     * @return - an array of spaceships of the type dictated by the command line.
     */
    public static SpaceShip[] createSpaceShips(String[] args) {
        // the array of spaceships to be returned
        SpaceShip[] spaceShips = new SpaceShip[args.length];
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                /**
                 * a human spaceship.
                 */
                case "h":
                    spaceShips[i] = new HumanSpaceShip();
                    break;
                /**
                 * a runner spaceship.
                 */
                case "r":
                    spaceShips[i] = new RunnerSpaceShip();
                    break;
                /**
                 * a basher spaceship.
                 */
                case "b":
                    spaceShips[i] = new BasherSpaceShip();
                    break;
                /**
                 * an aggressive spaceship.
                 */
                case "a":
                    spaceShips[i] = new AggressiveSpaceShip();
                    break;
                /**
                 * a drunkard spaceship
                 */
                case "d":
                    spaceShips[i] = new DrunkardSpaceShip();
                    break;
                /**
                 * a special spaceship.
                 */
                case "s":
                    spaceShips[i] = new SpecialSpaceShip();
                    break;
                /**
                 * as per the assignment instruction, we were told to assume that the input parameters are valid. Therefor,
                 * we shouldn't be able to reach this line.
                 */
                default:
                    System.out.println("Invalid Input");
                    break;
            }
        }
        return spaceShips;
    }
}
