import oop.ex2.GameGUI;

import java.awt.*;
import java.util.Random;

/**
 * This class implements the "Drunkard" spaceship, which every turn randomly chooses one of three modes to be in:"
 * 1. Sleepy Drunk - This pilot is very sleepy, and will simply keep hovering along at a constant speed forward.
 * 2. Party Hard Drunk - will try to press all the buttons on the spaceship (besides teleporting, too much movement
 * and all isn't good after 15 tequila and rum shots."
 * 3. Secretely vindictive Drunk - moves toward the nearest spaceship and fires
 */
public class DrunkardSpaceShip extends SpaceShip {

    final private int TYPES_OF_DRUNK = 3;
    final private int SLEEPY_DRUNK = 0;
    final private int PARTY_HARD_DRUNK = 1;
    final private int SECRECTLY_VINDICTIVE_DRUNK = 2;

    @Override
    public void doAction(SpaceWars game) {
        /*
         * turns the shield off from the previous round (if it wasn't active in the first place, then the function
         * doesn't do anything)
         */
        shieldOff();
        Random random = new Random();
        // chooses the drunk type for this turn
        int typeOfDrunk = random.nextInt(TYPES_OF_DRUNK);
        switch (typeOfDrunk) {
            /*
            sleepy drunk. simply keeps moving without change (shhhhh!!!! don't wake the captain up).
             */
            case SLEEPY_DRUNK:
                getPhysics().move(false, 0);
                break;
                /*
                party hard drunk - loves pressing buttons and moving the control stick randomly!
                 */
            case PARTY_HARD_DRUNK:
                getPhysics().move(true, random.nextInt(3) - 1);
                shieldOn();
                fire(game);
                break;
                /*
                secretly vindictive drunk - after texting his ex and getting no response, the pilot feels the need to
                fire at the nearest ship. So it will turn towards the nearest ship and fire (even if it's fire away).
                 */
            case SECRECTLY_VINDICTIVE_DRUNK:
                getPhysics().move(true, directionOfAttack(game.getClosestShipTo(this)));
                fire(game);
                break;
        }
        /*
         * replenish the ships energy and tick the cool-off clock
         */
        endTurn();
    }

    /**
     * @return - an image of the ship, depending on whether it has a shield active
     */
    public Image getImage() {
        if (isShieldOn)
            return GameGUI.ENEMY_SPACESHIP_IMAGE_SHIELD;
        else
            return GameGUI.ENEMY_SPACESHIP_IMAGE;
    }
}
