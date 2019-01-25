package fr.thedarven.game.enums;

import fr.thedarven.main.LGUHC;
import fr.thedarven.roles.*;

public enum EnumCompo {
	ANCIEN(new Ancien(),1,0),
	ANGE(new Ange(),1,1),
	VILLAGEOISINFILTRÉ(new VillageoisInfiltré(),1,2),
	SORCIÈRE(new Sorcière(),1,3),
	CHAMAN(new Chaman(),1,4),
	CUPIDON(new Cupidon(),1,5),
	ENFANTSAUVAGE(new EnfantSauvage(),1,6),
	SOEUR(new Soeur(),2,7),
	PETITEFILLE(new PetiteFille(),1,8),
	RENARD(new Renard(),1,9),
	MONTREURDOURS(new MontreurDours(),1,10),
	VOYANTE(new Voyante(),1,11),
	VOYANTEBAVARDE(new VoyanteBavarde(),1,12),
	SALVATEUR(new Salvateur(),1,13),
	SIMPLEVILLAGEOIS(new Villageois(),20,14),
	
	INFECTPÈREDESLOUPS(new InfectPèreDesLoups(),1,20),
	LOUPGAROU(new Loups(),20,18),
	LOUPGAROUBLANC(new LoupBlanc(),1,19),
	
	ASSASSIN(new Assassin(),1,27),
	PETITASSASSIN(new PetitAssassin(),1,28);
	
	private RolesBis<?> role;
	private int maxNumber;
	private int position;
	
	EnumCompo(RolesBis<?> role, int number, int position){
		this.role = role;
		this.maxNumber = number;
		this.position = position;
	}
	
	public String getRoleName(){
		return role.getName();
	}
	
	public void addRole(){
		if((role instanceof LoupBlanc && getNumberOf(new Loups()) < 2) || (role instanceof PetitAssassin && getNumberOf(new Assassin()) == 0)){
			return;
		}
		if(role instanceof Voyante && getNumberOf(new VoyanteBavarde()) == 1 || role instanceof VoyanteBavarde && getNumberOf(new Voyante()) == 1){
			VOYANTE.removeRole();
			VOYANTEBAVARDE.removeRole();
		}
		int iterator = 0;
		if(LGUHC.composition != null){
			for(int i = 0; i < LGUHC.composition.size(); i++){
				if(LGUHC.composition.get(i).getName().equals(role.getName())){
					iterator++;
				}
			}
		}
		if(iterator < maxNumber){
			try {
				LGUHC.composition.add(role.getClass().newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			if(role instanceof Soeur){
				SOEUR.addRole();
			}
		}
	}
	
	public void removeRole(){
		if(LGUHC.composition == null){
			return;
		}
		int index = -1;
		for(int i = 0; i < LGUHC.composition.size(); i++){
			if(LGUHC.composition.get(i).getName().equals(role.getName())){
				index = i;
			}
		}
		if(index != -1){
			LGUHC.composition.remove(index);
			if(getNumberOf(new LoupBlanc()) == 1 && getNumberOf(new Loups()) < 2){
				LOUPGAROUBLANC.removeRole();
			}else if(getNumberOf(new PetitAssassin()) == 1 && getNumberOf(new Assassin()) == 0){
				PETITASSASSIN.removeRole();
			}else if(getNumberOf(new Soeur()) == 1) {
				SOEUR.removeRole();
			}
		}
	}
	
	public int getNumberOf(RolesBis<?> role){
		int iterator = 0;
		if(LGUHC.composition != null){
			for(int i = 0; i < LGUHC.composition.size(); i++){
				if(LGUHC.composition.get(i).getName().equals(role.getName())){
					iterator++;
				}
			}
			return iterator;
		}
		return 0;
	}
	
	public int getPosition(){
		return this.position;
	}

}