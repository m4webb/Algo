package com.bamwebb.algo;

import java.util.UUID;

public class Piece {
	
	final private int strength;
	final private int toughness;
	final private int speed;
	final private int reach;
	final private int sight;
	final private UUID uuid;
	
	public static int cost(Piece piece) {
		int strength = piece.getStrength();
		int toughness = piece.getToughness();
		int speed = piece.getSpeed();
		int reach = piece.getReach();
		int sight = piece.getSight();
		
		double strengthCost = Math.exp((double)strength);
		double toughnessCost = Math.exp((double)toughness);
		double speedCost = Math.exp((double)speed);
		double reachCost = Math.exp((double)reach);
		double sightCost = Math.exp((double)sight);
		
		int totalCost = (int)Math.sqrt(Math.pow(strengthCost, 2) + Math.pow(toughnessCost, 2) +
		        Math.pow(speedCost, 2) + Math.pow(reachCost, 2) + Math.pow(sightCost, 2));
		
		return totalCost;
	}
	
	public Piece(int strength, int toughness, int speed, int reach, int sight, Square square) {
		this.strength = strength;
		this.toughness = toughness;
		this.speed = speed;
		this.reach = reach;
		this.sight = sight;
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
	
	public int getReach() {
		return reach;
	}
	
	public int getSight() {
	    return sight;
	}
	
	public UUID getUuid() {
		return uuid;
	}
}
