
import oop.ex2.GameGUI;

import java.awt.*;

/**
 * This class implements The "Human" spaceship, which is controlled by user commands.
 */
public class HumanSpaceShip extends SpaceShip {
    /**
     * the logic behind the runner ship.
     * as stated, this spaceship is controlled by the user. The keys are: left-arrow and right-arrow to
     * turn, up-arrow to accelerate, 'd' to fire a shot, 's' to turn on the shield, 'a' to teleport.
     *
     * @param game the game object to which this ship belongs.
     */
    @Override
    public void doAction(SpaceWars game) {
        // turn off the shield from the previous round
        shieldOff();
        // play according to the user input
        runCommandsFromUser(game);
        // increase the energy by 1 and lower the cool off period
        endTurn();
    }

    /**
     * @return - an image of the ship, depending on whether it has a shield active
     */
    public Image getImage() {
        if (isShieldOn)
            return GameGUI.SPACESHIP_IMAGE_SHIELD;
        else
            return GameGUI.SPACESHIP_IMAGE;
    }

    /**
     * The function translates the keys pressed by the user to actualy spaceship actions. this is done by using the GUI
     * varaible of the game class. The order of each action is determined by their precedence, as defined in the
     * assignment page.
     *
     * @param game - the game itself.
     */
    private void runCommandsFromUser(SpaceWars game) {
        // teleport

        if (game.getGUI().isTeleportPressed())
            teleport();

        //steer the ship
        /*
        If the user pressed the left and the right keys, then the ship won't turn at all. the variable "turn" is used to
        tally all the user's steer commands, and send to the "move" function the final direction for steering.
         */
        int turn = 0;
        if (game.getGUI().isLeftPressed())
            turn++;
        if (game.getGUI().isRightPressed())
            turn--;
        getPhysics().move(game.getGUI().isUpPressed(), turn);

        //activate shield

        if (game.getGUI().isShieldsPressed())
            shieldOn();

        //fire!

        if (game.getGUI().isShotPressed())
            fire(game);
    }
}

