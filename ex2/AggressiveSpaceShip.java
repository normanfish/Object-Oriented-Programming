import oop.ex2.GameGUI;

import java.awt.*;

/**
 * This class implements The "Aggressive" type spaceship, which always tries to get near other spaceships and fire at
 * them.
 */
public class AggressiveSpaceShip extends SpaceShip {
    /**
     * The angle from which the aggressive ship will fire.
     */
    final private double ANGLE_TO_FIRE_FROM = 0.21;

    /**
     * the logic behind the aggressive ship.
     * This ship pursues other ships and tries to fire at them. It will always accelerate,
     * and turn towards the nearest ship. When its angle to the nearest ship is less than 0.21
     * radians, it will try to fire.
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
         * moves the ship in the direction of the closest ship.
         */
        getPhysics().move(true, directionOfAttack(game.getClosestShipTo(this)));
        /*
         * if the angle is smaller than the defined angle, fire at the ship!
         */
        if (getPhysics().angleTo(game.getClosestShipTo(this).getPhysics()) <= ANGLE_TO_FIRE_FROM)
            fire(game);
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
