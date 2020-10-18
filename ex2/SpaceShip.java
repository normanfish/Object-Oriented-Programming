import java.awt.Image;

import oop.ex2.*;


/**
 * The API spaceships need to implement for the SpaceWars game.
 * It is your decision whether SpaceShip.java will be an interface, an abstract class,
 * a base class for the other spaceships or any other option you will choose.
 *
 * @author oop
 */
abstract public class SpaceShip {

    /**
     * the ship's physics engine. Holds information about the ship's position and is used to communicate with other
     * ships.
     */
    protected SpaceShipPhysics shipPhysics;
    /**
     * the maximal energy level the ship has.
     */
    protected int maximalEnergyLevel;
    /**
     * the current energy level of the ship.
     */
    protected int currentEnergyLevel;
    /**
     * the ship's current health level.
     */
    protected int healthLevel;
    /**
     * a boolean flag indicating whether the ship's shield is currently active.
     */
    protected boolean isShieldOn;
    /**
     * the ship's cool-off period countdown clock. After every shot, the ship has to wait a set amount of time before
     * it can fire again.
     */
    protected int coolOff;

    protected int MAXIMUM_ENERGY_LEVEL = 210;
    protected int INITIAL_ENERGY_LEVEL = 190;
    protected int INITIAL_HEALTH_LEVEL = 22;
    /**
     * The amount of damage a ship takes when it is hit (either by collision or by being fired at).
     */
    protected int HIT_DAMAGE = 1;
    /**
     * The amount of energy a ship losses when it takes a hit.
     */
    protected int ENERGY_LOST_WHEN_HIT = 10;

    protected int FIRING_ENERGY_REQUIRED = 19;
    protected int TELEPORTING_COST = 140;
    protected int SHIELD_COST = 3;
    /**
     * The amount of energy a ship gets after a successful "bashing" (crashing with another ship while shields are on)
     */
    protected int ENERGY_BONUS_WHEN_BASHED = 18;

    /**
     * the amount of energy restored at the end of the turn.
     */
    protected int ROUND_ENERGY_INCREASE = 1;
    /**
     * the amount reduced from the cool-off clock at the end of every turn.
     */
    protected int COOLOFF_REVIVAL = 1;

    final private static int ADD = 1;
    final private static int SUBTRACT = 2;

    /**
     * The constructor for a spaceship type.
     * As described in the readme, since SpaceShip is an abstract type, this is the default constructor to be used when
     * creating a sub-class of a spaceship (for instance a basher ship), and is discouraged from being used to create a
     * "blank" spaceship without any special attributes. Other subclasses can use their own constructors (for instance,
     * special SpaceShip uses it's own constructor), however, since the default constructor addresses the needs for
     * most of the common spaceship, in the interest of avoiding code duplication, the constructor is implemented here.
     */
    protected SpaceShip() {
        shipPhysics = new SpaceShipPhysics();
        maximalEnergyLevel = MAXIMUM_ENERGY_LEVEL;
        currentEnergyLevel = INITIAL_ENERGY_LEVEL;
        healthLevel = INITIAL_HEALTH_LEVEL;
        coolOff = 0;
        isShieldOn = false;
    }

    /**
     * Does the actions of this ship for this round.
     * This is called once per round by the SpaceWars game driver.
     * ADDITION: since this method changes with every spaceship (each spaceship has a different logic or method for each
     * turn), this method is abstract (meaning, each method has to implement it).
     *
     * @param game the game object to which this ship belongs.
     */
    abstract public void doAction(SpaceWars game);


    /**
     * This method is called every time a collision with this ship occurs
     */
    public void collidedWithAnotherShip() {
        if (!isShieldOn)
            gotHit();
        else
            bashWithShield();
    }

    /**
     * This method is called whenever a ship has died. It resets the ship's
     * attributes, and starts it at a new random position.
     */
    public void reset() {
        shipPhysics = new SpaceShipPhysics();
        maximalEnergyLevel = MAXIMUM_ENERGY_LEVEL;
        currentEnergyLevel = INITIAL_ENERGY_LEVEL;
        healthLevel = INITIAL_HEALTH_LEVEL;
        isShieldOn = false;
        coolOff = 0;
    }

    /**
     * Checks if this ship is dead.
     *
     * @return true if the ship is dead. false otherwise.
     */
    public boolean isDead() {
        return healthLevel == 0;
    }

    /**
     * Gets the physics object that controls this ship.
     *
     * @return the physics object that controls the ship.
     */
    public SpaceShipPhysics getPhysics() {
        return shipPhysics;
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship
     * gets hit by a shot.
     */
    public void gotHit() {
        if (!isShieldOn) {
            lowerHealth();
            maximalEnergyLevel = Math.max(maximalEnergyLevel - ENERGY_LOST_WHEN_HIT, 0);
            changeEnergy(0, SUBTRACT);
        }
    }

    /**
     * Gets the image of this ship. This method should return the image of the
     * ship with or without the shield. This will be displayed on the GUI at
     * the end of the round.
     * ADDITION: I chose to make this function an abstract function for extensibility considerations, as if we expand
     * the game to include more pictures, so that each spaceship will have its own picture, and for that reason we can't
     * implement a "one for all" solution in the parent class.
     *
     * @return the image of this ship.
     */
    abstract public Image getImage();

    /**
     * Attempts to fire a shot.
     *
     * @param game the game object.
     */
    public void fire(SpaceWars game) {
        if (reduceEnergyIfPossible(FIRING_ENERGY_REQUIRED) && coolOff == 0) {
            game.addShot(shipPhysics);
            coolOff = 7;
        }
    }

    /**
     * Attempts to turn on the shield.
     */
    public void shieldOn() {
        if (reduceEnergyIfPossible(SHIELD_COST))
            isShieldOn = true;
    }

    /**
     * turns the shield off.
     */
    protected void shieldOff() {
        isShieldOn = false;
    }

    /**
     * Thhis function runs at the end of every ship's move. It restores the ship's energy by the a fixed amount and
     * "ticks" off a round on the fire cool-off clock.
     */
    protected void endTurn() {
        changeEnergy(this.ROUND_ENERGY_INCREASE, ADD);
        coolOff = Math.max(this.coolOff - this.COOLOFF_REVIVAL, 0);
    }

    /**
     * Attempts to teleport.
     */
    public void teleport() {
        if (reduceEnergyIfPossible(TELEPORTING_COST))
            shipPhysics = new SpaceShipPhysics();
    }

    /**
     * This function is called when the ship has successfully "bashed" with another ship. It raises the ships maximum
     * and current energy levels.
     */
    private void bashWithShield() {
        maximalEnergyLevel += ENERGY_BONUS_WHEN_BASHED;
        changeEnergy(ENERGY_BONUS_WHEN_BASHED, ADD);
    }

    /**
     * This function adds or subtracts from the current energy level, based on the input parameters. This function uses
     * "safe" addition, in ordewr to maintain the energy level don't go below zero or above the maximum energy alloted
     * for a ship.
     *
     * @param delta    - the amount to be added or reduced
     * @param operator - the integer which indicates whether to add or subtract from the energy level
     */
    private void changeEnergy(int delta, int operator) {
        if (operator == ADD)
            currentEnergyLevel = Math.min(currentEnergyLevel + delta, maximalEnergyLevel);
        if (operator == SUBTRACT)
            currentEnergyLevel = Math.max(currentEnergyLevel - delta, 0);
    }

    /**
     * The function checks if the ship has enough energy to perform a task, which costs a certain amount of points.
     *
     * @param cost - the cost the action requires.
     * @return - true if the current energy level exceed the cost, and false otherwise.
     */
    private boolean canPerformAction(int cost) {
        return currentEnergyLevel >= cost;
    }

    /**
     * The function first checks if performing an action requiring "cost" energy is possible, and if so, subtracts it
     * from the current energy level. if the reduction was done successfully, the function returns true, and false
     * otherwise.
     *
     * @param cost - The amount to be subtracted.
     * @return - true if the cost was reduced successfuly and the ship's new energy level is updates, and false otherwise.
     */
    private boolean reduceEnergyIfPossible(int cost) {
        if (canPerformAction(cost)) {
            changeEnergy(cost, SUBTRACT);
            return true;
        }
        return false;
    }

    /**
     * The function lowers the ship's health level by "HIT DAMAGE", which is a class variable. This is also a "safe"
     * subtraction, meaning the ship's health cannot go under zero.
     */
    private void lowerHealth() {
        healthLevel = Math.max(healthLevel - HIT_DAMAGE, 0);
    }

    /**
     * The function checks the direction of the ship in relation to another ship,
     * and returns a number based on that angle.
     *
     * @param ship - the other ship which we are checking the angle from the ship activating the function.
     * @return - 1 if the angle is between 0 and 180, -1 if the angle is between 180 and 360, and 0 if the angle is 0 (
     * not including)
     */
    protected int directionOfAttack(SpaceShip ship) {
        double angleInRadians = shipPhysics.angleTo(ship.getPhysics());
        if (angleInRadians > 0)
            return 1;
        if (angleInRadians < 0)
            return -1;
        else
            return 0;

    }

}
