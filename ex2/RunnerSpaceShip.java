import oop.ex2.GameGUI;

import java.awt.*;

/**
 * This class implements The "Runner" type spaceship, which always turns away from enemy aircraft and tries to teleport
 * if it gets too close.
 */
public class RunnerSpaceShip extends SpaceShip {

    /**
     * The distance and angle the runner ship will attempt to teleport away from
     */
    final private double TELEPORTING_DISTANCE = 0.25;
    final private double TELEPORTING_ANGLE = 0.23;
    /**
     * the logic behind the runner ship.
     * This spaceship attempts to run away from the ght. It will always accelerate, and
     * will constantly turn away from the closest ship. If the nearest ship is closer than 0.25 units,
     * and if its angle to the Runner is less than 0.23 radians, the Runner feels threatened and will
     * attempt to teleport.
     *
     * @param game the game object to which this ship belongs.
     */
    @Override
    public void doAction(SpaceWars game) {
        /*
         * turns the shield off from the previous round (if it wasn't active in the first place, then the function
         * doesn't do anything)
         */
        shieldOff();
        /*
         * move the ship in the opposite direction of the nearest sship.
         */
        getPhysics().move(true, runningConstant(game));
        /*
         * Checks if the ship is dangerously close to another ship, and tries to teleport away if so.
         */
        if (getPhysics().angleTo(game.getClosestShipTo(this).getPhysics()) <= TELEPORTING_ANGLE &&
                getPhysics().distanceFrom(game.getClosestShipTo(this).getPhysics()) <= TELEPORTING_DISTANCE)
            teleport();
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

    /**
     * gives the direction to turn in order to avoid the nearest ship.
     *
     * @param game - the current game being played
     * @return -1 if the nearest ship's angle is betwenn 0 and 180 (not including), 1 if the nearest ship's angle is
     * between 180 and 360 (not including) and if the nearest ship is dead ahead (meaning at 0 degrees), the ship will
     * turn left (arbitrary decision).
     */
    private int runningConstant(SpaceWars game) {
        int turningDirection = directionOfAttack(game.getClosestShipTo(this));
        if (turningDirection != 0)
            return -1 * turningDirection;
        else
            return 1;
    }
}
