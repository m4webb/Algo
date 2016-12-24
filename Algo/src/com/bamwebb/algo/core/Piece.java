package com.bamwebb.algo.core;

import java.util.UUID;

public class Piece {
	
	final private int strength;
	final private int toughness;
	final private int speed;
	final private int range;
	final private int sight;
	final private int glory;
	final private UUID uuid;
	
	public static int cost(Piece piece) {
		int strength = piece.getStrength();
		int toughness = piece.getToughness();
		int speed = piece.getSpeed();
		int range = piece.getRange();
		int sight = piece.getSight();
		int glory = piece.getGlory();
		
		double strengthCost = Math.exp((double)strength/Configuration.STRENGTH_PER_UNIT_COST);
		double toughnessCost = Math.exp((double)toughness/Configuration.TOUGHNESS_PER_UNIT_COST);
		double speedCost = Math.exp((double)speed/Configuration.SPEED_PER_UNIT_COST);
		double reachCost = Math.exp((double)range/Configuration.REACH_PER_UNIT_COST);
		double sightCost = Math.exp((double)sight/Configuration.SIGHT_PER_UNIT_COST);
		double gloryCost = Math.exp((double)glory/Configuration.GLORY_PER_UNIT_COST);
		
		int totalCost = (int)(Math.sqrt(Math.pow(strengthCost, 2) + Math.pow(toughnessCost, 2) +
		        Math.pow(speedCost, 2) + Math.pow(reachCost, 2) + Math.pow(sightCost, 2) +
		        Math.pow(gloryCost, 2)) * Configuration.COST_TO_FARM_UNITS_MULTIPLIER);
		
		return totalCost;
	}
	
	@Override
	public int hashCode() {
	    return uuid.hashCode();
	}
	
	@Override
	public boolean equals(Object object) {
	    Piece piece;
	    if (object == null) return false;
	    if (object.getClass() == this.getClass()) {
	        piece = (Piece) object;
	        return piece.getUuid() == uuid;
	    }
	    return false;
	}
	
	public Piece(int strength, int toughness, int speed, int range, int sight, int glory) {
	    this.strength = strength;
		this.toughness = toughness;
		this.speed = speed;
		this.range = range;
		this.sight = sight;
		this.glory = glory;
		this.uuid = UUID.randomUUID();
	}
	
	public int getStrength() {
		return strength;
	}
	
	public int getToughness() {
		return toughness;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getRange() {
		return range;
	}
	
	public int getSight() {
	    return sight;
	}
	
	public int getGlory() {
	    return glory;
	}
	
	public UUID getUuid() {
		return uuid;
	}
}
