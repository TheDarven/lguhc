package fr.thedarven.confiavant;

import java.util.ArrayList;

import fr.thedarven.game.enums.EnumTime;
import fr.thedarven.main.LGUHC;

public class Cycle{
	
	private ArrayList<EnumTime> cycle = new ArrayList<EnumTime>();
	
	public Cycle(){
		for(int i=0; i<20; i++) {
			cycle.add(EnumTime.DAY);
		}
		
		for(int i=0; i< 49; i++) {
			for(int j=0; j<2; j++) {
				for(int k=0; k<5; k++) {
					cycle.add(EnumTime.DAY);
				}
				for(int k=0; k<5; k++) {
					cycle.add(EnumTime.NIGHT);
				}
			}	
		}
	}
	
	public void changeOnMinute(int minute){
		if(cycle.size() >= minute){
			if(cycle.get(minute-1).equals(EnumTime.DAY)) {
				cycle.set(minute-1, EnumTime.NIGHT);
			}else {
				cycle.set(minute-1, EnumTime.DAY);
			}
		}
	}
	
	public EnumTime getAtMinute(int minute){
		if(cycle.size() >= minute){
			return cycle.get(minute-1);
		}
		return null;
	}
	
	public EnumTime getAtSecond(int second){
		if(cycle.size() >= second/60){
			return cycle.get((second)/60);
		}
		return null;
	}
	
	public EnumTime getNow() {
		if(cycle.size() >= LGUHC.timer/60){
			return cycle.get((LGUHC.timer)/60);
		}
		return null;
	}
	
	public void cloneDay(int day){
		ArrayList<EnumTime> dayReference = new ArrayList<EnumTime>();
		
		for(int i=0; i<20; i++) {
			dayReference.add(this.getAtMinute((day-1)*20+i+1));
		}
		
		for(int i=(day*20);i<1000;i++){
			this.cycle.set(i, dayReference.get(i%20));
		}
	}
}
