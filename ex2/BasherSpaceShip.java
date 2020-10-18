import oop.ex2.GameGUI;

import java.awt.*;

/**
 * This class implements The "Basher" type spaceship, which always tries to ram into other spaceships with its shields
 * on.
 */
public class BasherSpaceShip extends SpaceShip {
    /**
     * The distance from which the basher ship will activate shields.
     */
    final private double DISTANCE_TO_ACTIVATE_SHIELD_FROM = 0.19;

    /**
     * the logic behind the basher ship.
     * This ship attempts to collide with other ships. It will always accelerate, and will
     * constantly turn towards the closest ship. If it gets within a distance of 0.19 units (inclusive)
     * from another ship, it will attempt to turn on its shields.
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
         * if the distance is smaller than the defined space, activate shields.
         */
        if (getPhysics().distanceFrom((game.getClosestShipTo(this).getPhysics())) <= DISTANCE_TO_ACTIVATE_SHIELD_FROM)
            shieldOn();
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
