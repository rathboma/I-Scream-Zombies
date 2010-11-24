package zombies;
import java.util.*;


public class Randomizer{
	
	static Random math = new Random();
	static int maxP = 6, maxZ = 6;

	
	public static ModifierBase getModifier(){
		
		return null;
	}
	
	public static Customer[] getCustomers(){
		int number = (Math.abs(math.nextInt()) % maxP) + 1;
		Customer[] results = new Customer[number];
		for(int i = 0; i < number ; i++){
			results[i] = new Customer();
		}
		return results;
	}
	
	public static Zombie[] getZombies(Customer[] customers){
		int number = (Math.abs(math.nextInt()) % maxP) + 1;
		Zombie[] results = new Zombie[number];
		for(int i = 0; i < number; i++){
			results[i] = new Zombie();
		}
		
		
		return results;
	}
	
	
	
}