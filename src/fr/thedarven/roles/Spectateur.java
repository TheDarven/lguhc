package fr.thedarven.roles;

import java.util.ArrayList;
import fr.thedarven.main.PlayerLG;

public class Spectateur extends RolesBis<Boolean>{

	public Spectateur() {
		this.active = true;
		this.infecte = false;
		this.kit = true;
		this.name = "Spectateur";
		this.pouvoir = false;
		this.taupelist = false;
		this.maxhealth = 20.0;
		
		this.effect = new ArrayList<EffetClass>();
	}
	
	@Override
	public void startRole(PlayerLG pl) {  }

	public void verifRole(PlayerLG pl) {  }

	@Override
	public void endRole(PlayerLG pl) {	}
}
