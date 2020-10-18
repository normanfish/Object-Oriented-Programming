import oop.ex2.GameGUI;
import oop.ex2.SpaceShipPhysics;

import java.awt.*;

/**
 * This class implements the "special" spaceship. which is otherwise known as the "mosquito". This Spaceship has less
 * life than a normal ship, but faster recharge time and more energy. This ship's purpose is to harass other ships
 * and be disposable. In addition, due to the complex space time calculations, the mosquito activates it's shield
 * once every 5 turns.
 */
public class SpecialSpaceShip extends SpaceShip {
    /*
        This next part changes the class constants to better fit the mosquito's characteristics. The mosquito fires
        more, has more energy, and activates its shields less. Side note, this is the main reason why the class variables
        are protected as opposed to private (so that more spaceships could, in the future, be added and fitted differently
        than the default.
     */
    final protected double DISTANCE_TO_ATTACK = 0.2;
    protected int shieldTurn = 0;


    protected int SPECIAL_INITIAL_HEALTH_LEVEL = 10;
    protected int SPECIAL_INITIAL_ENERGY_LEVEL = 230;
    protected int SPECIAL_FIRING_ENERGY_REQUIRED = 8;
    protected int SPECIAL_SHIELD_COST = 5;
    protected int SPECIAL_MAXIMUM_ENERGY_LEVEL = 300;
    protected int SPECIAL_COOLOFF_REVIVAL = 2;
    protected int SPECIAL_HIT_DAMAGE = 2;
    protected int SPECIAL_ROUND_ENERGY_INCREASE = 3;

    /**
     * The new constructor. Since this spaceship changes the default setting of other ships, it has it's own constructor
     * that is pretty much identical to the normal one, just redefines the constants in the game.
     */
    protected SpecialSpaceShip() {
        this.shipPhysics = new SpaceShipPhysics();
        this.maximalEnergyLevel = SPECIAL_MAXIMUM_ENERGY_LEVEL;
        this.currentEnergyLevel = SPECIAL_INITIAL_ENERGY_LEVEL;
        this.healthLevel = SPECIAL_INITIAL_HEALTH_LEVEL;
        this.coolOff = 0;
        this.isShieldOn = false;
        this.HIT_DAMAGE = SPECIAL_HIT_DAMAGE;
        this.SHIELD_COST = SPECIAL_SHIELD_COST;
        this.ROUND_ENERGY_INCREASE = SPECIAL_ROUND_ENERGY_INCREASE;
        this.FIRING_ENERGY_REQUIRED = SPECIAL_FIRING_ENERGY_REQUIRED;
        this.COOLOFF_REVIVAL = SPECIAL_COOLOFF_REVIVAL;
    }

    /**
     * the logic behind the runner ship.
     * the sip has 3 main commands.
     * 1. If the ships life is beloew 20%, teleport away from the action.
     * 2. Move to wards the nearest ship, and fire if close enough
     * 3. every 5th turn, when energy levels have been replenished, activate a shield
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

        // if the ship's health is below 20%, teleport.
        if (healthLevel <= INITIAL_HEALTH_LEVEL / 5)
            teleport();

        // similar to the aggresive ship, the special ship will move toward the nearest ship and fire if close enough.
        getPhysics().move(true, directionOfAttack(game.getClosestShipTo(this)));
        if (getPhysics().distanceFrom(game.getClosestShipTo(this).getPhysics()) <= DISTANCE_TO_ATTACK)
            fire(game);

        //activate the shield every 5th turn.
        if (shieldTurn == 5) {
            shieldOn();
            shieldTurn = 0;
        } else
            shieldTurn++;

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
