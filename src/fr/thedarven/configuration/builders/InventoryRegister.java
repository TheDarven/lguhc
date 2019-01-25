package fr.thedarven.configuration.builders;

import org.bukkit.Material;

import fr.thedarven.atest.BloodDiamond;
import fr.thedarven.atest.CutClean;
import fr.thedarven.atest.DiamondLimit;
import fr.thedarven.atest.GoldenHead;
import fr.thedarven.atest.LavaLimiter;
import fr.thedarven.atest.NoEnderPearlDamage;
import fr.thedarven.atest.Pomme;
import fr.thedarven.atest.Pvp;
import fr.thedarven.atest.Silex;

public class InventoryRegister {
	
	public static InventoryGUI menu = new InventoryGUI("Menu", null, 1, Material.GRASS, null);
	public static InventoryGUI configuration = new InventoryGUI("Configuration", "Menu de configuration.", 2, Material.ANVIL, menu, 2);
	public static InventoryGUI cycle = new InventoryGUI("Cycle jour/nuit", "Menu de gestion du cycle jour/nuit.", 2, Material.WATCH, menu, 4);
	public static InventoryGUI composition = new InventoryGUI("Composition", "Menu de gestion de la composition de la partie.", 2, Material.PAPER, menu, 6);
	
	public static InventoryGUI timers = new InventoryGUI("Timers", "Menu des timers.", 1, Material.WATCH, configuration, 0);
	public static Pvp pvp = new Pvp("Pvp", "La minute à laquelle le pvp s'active.", Material.IRON_SWORD, timers, 0, 30, 10, 1, 2, "min", 1);
	public static OptionNumeric annonceroles = new OptionNumeric("Annonces rôles", "La minute à laquelle les rôles sont annoncés.",Material.PAPER, timers, 20, 70, 20, 10, 2, "min", 1);
	public static OptionNumeric votes = new OptionNumeric("Votes", "La minute à laquelle les votes s'activent.", Material.NAME_TAG, timers, 20, 60, 40, 20, 1, "min", 1);
	public static OptionNumeric murtime = new OptionNumeric("Début de la réduction", "Minute à laquelle le mur commence à se réduire.", Material.BARRIER, timers, 30, 180, 80, 5, 2, "min", 1);
	public static OptionNumeric teleportationvitesse = new OptionNumeric("Vitesse de la téléportation", "Le temps séparant la téléportation de chaque joueur.", Material.FEATHER, timers, 1, 10, 4, 1, 1, "s", 1);
	public static OptionNumeric dureescoreboard = new OptionNumeric("Durée des scoreboards", "La durée d'affichage de chaque scoreboard d'informations.", Material.SIGN, timers, 5, 60, 10, 1, 3, "s", 1);
	
	public static InventoryGUI mur = new InventoryGUI("Mur", "Menu du mur.", 1, Material.BARRIER, configuration, 2);
	public static OptionNumeric murtailleavant = new OptionNumeric("Taille avant la réduction", "La taille du mur avant le début de la réduction.", Material.STONE, mur, 500, 5000, 750, 50, 3, " blocs +/-", 1);
	public static OptionNumeric murtailleaprès = new OptionNumeric("Taille après la réduction", "La taille du mur à la fin de la réduction.", Material.BEDROCK, mur, 25, 200, 100, 5, 2, " blocs +/-", 1);
	public static OptionNumeric murvitesse = new OptionNumeric("Vitesse de la réduction", "La vitesse à laquelle le mur se réduit.", Material.DIAMOND_BARDING, mur, 20, 200, 100, 10, 2, " blocs/seconde", 100);
	
	public static InventoryGUI scenarios = new InventoryGUI("Scenarios", "Menu des scénarios.", 1, Material.PAPER, configuration, 4);
	public static CutClean cutclean = new CutClean("CutClean", "Aucun cuisson n'est nécessaire avec ce scénario.", Material.IRON_INGOT, scenarios, false);
	public static BloodDiamond bloodiamond = new BloodDiamond("Blood Diamond", "Les diamants infliges des dégats lorsqu'ils sont minés.", Material.TNT, scenarios, 0, 4, 0, 1, 1, "❤", 2);
	public static DiamondLimit OptionBoolean = new DiamondLimit("Diamond Limit", "Limite le nombre de diamant que chaque joueur peu miner dans la partie.", Material.DIAMOND, scenarios, 0, 50, 0, 1, 2, "", 1);
	public static LavaLimiter lava = new LavaLimiter("Lava Limiter", "Désactive le placement de lave proches des autres joueurs.", Material.LAVA_BUCKET, scenarios, false);
	public static NoEnderPearlDamage pearldamage = new NoEnderPearlDamage("No Enderpearl Damage", "Désactive les dégâts causés par les ender pearl.", Material.ENDER_PEARL, scenarios, false);
	
	public static InventoryGUI drop = new InventoryGUI("Drops", "Menu des drops.", 1, Material.NETHER_STAR, configuration, 6);
	public static Pomme pomme = new Pomme("Pommes", "Pourcentage de drop des pommes.", Material.APPLE, drop, 1, 200, 1, 1, 3, "%", 2);
	public static Silex silex = new Silex("Silexs", "Pourcentage de drop des silex.", Material.FLINT, drop, 1, 200, 20, 1, 3, "%", 2);
	
	public static InventoryGUI autre = new InventoryGUI("Autres", "Autres paramètres.", 1, Material.COMMAND, configuration, 8);
	public static OptionBoolean rolesvisiblespendant = new OptionBoolean("Roles visibles", "Activer ou non pendant la partie l'affichage des rôles dans le scoreboard.", Material.GLASS, autre, true);
	public static OptionBoolean rolesvisiblesavant = new OptionBoolean("Roles visibles avant annonce", "Activer ou non l'affichage des rôles avant leur annonce.", Material.FENCE, autre, true);
	public static OptionBoolean coordonneesvisibles = new OptionBoolean("Coordonnées visible", "Activer ou non les coordonnées au cours de la partie. Si désactivé, un message au dessus de l'inventaire indiquera une distance approximative du centre.", Material.EYE_OF_ENDER, autre, false);
	public static GoldenHead goldenhead = new GoldenHead("Golden Head", "Nombre de coeurs régénérés par les Golden Head.", Material.SKULL_ITEM, autre, 0, 8, 0, 1, 1, "❤", 2);
	public static OptionNumeric taillegroupes = new OptionNumeric("Taille des groupes", "Taille maximale de groupes autorisées.", Material.RAILS, autre, 0, 12, 0, 1, 1, "❤", 1);
}
