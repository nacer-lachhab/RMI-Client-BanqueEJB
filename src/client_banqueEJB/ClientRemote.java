package client_banqueEJB;

import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import metier.IBanqueRemote;
import metier.entities.Compte;

public class ClientRemote {

	public static void main(String[] args) {
		
		try {
			Context ctx = new InitialContext();
			
			String appName = "";
			String moduleName = "banqueEJB";//ejb Project = module
			String beanName = "Bank";
			String remoteInterface = IBanqueRemote.class.getName();//"metier.IBanqueRemote"
			StringBuilder nameProxy = new StringBuilder("ejb:");
			nameProxy.append(appName)
					 .append("/")
					 .append(moduleName)
					 .append("/")
					 .append(beanName)
					 .append("!")
					 .append(remoteInterface);
			System.out.println(nameProxy.toString());
			
			IBanqueRemote stubProxy= (IBanqueRemote)ctx.lookup(nameProxy.toString());
			
			stubProxy.addCompte(new Compte());
			stubProxy.addCompte(new Compte());
			stubProxy.addCompte(new Compte());
			
//			Compte compte1 = stubProxy.getCompte(1L);
//			System.out.println(compte1.toString());
			
			/*************	lecture des comptes	****************/
			
			List<Compte> allAccountsT0 = stubProxy.allComptes();
			allAccountsT0.stream()
					   .forEach(c-> System.out.println(c));
			
			
			/********    Oprations & Modifications    *********/
			System.out.println("Oprations & Modifications");
			
			stubProxy.verser(2l, 2500);
			stubProxy.retrait(2l, 100);
			stubProxy.virement(2l, 3l, 500);
			stubProxy.virement(2l, 1l, 200.67);
			
			List<Compte> allAccounts = stubProxy.allComptes();
			Date now = new Date();
			int i =0;
			for(Compte cp:allAccounts) {
				Compte updatedCompte = new Compte();
				now.setMinutes(i);
				i++;
				updatedCompte.setSolde(cp.getSolde());//conserver le solde
				updatedCompte.setDateCreation(now);
				stubProxy.editCompte(cp.getId(),updatedCompte);
				System.out.println(cp);
			}
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
