import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class HackerTracker 
{
    /**
     * unique methode de la classe qui parcourt les differents fichers
     * ecrit dans d'autres 
     * et affiche dans la console le resultat d'une recherche
     * @throws IOException
     * @throws ParseException
     */
	public void hacker() throws IOException, ParseException 
    {
    	//recuperation des donnees du fichier connexion.log
    	String fichierConnexion = "connexion.log";  
        File fichier = new File(fichierConnexion);
        Scanner lectureScanner = new Scanner(fichier);
        
        //declaration des differentes listes
        List<String> listeUtilisateur = new ArrayList<String>(); 
        List<String> listeSuspect = new ArrayList<String>();
        List<String> listeWarning = new ArrayList<String>();
        List<String> listeWarning2 = new ArrayList<String>();

        /**
         * premiere boucle pour lire les lignes du fichier
         */
        while (lectureScanner.hasNextLine()) 
        {
            //recuperation du nom de l'utilisateur avec un split qu'on ajoute a la liste
        	String connexion = lectureScanner.nextLine();
            String[] connexionContenu = connexion.split(";");
            String nomUtilisateur = connexionContenu[1];
            listeUtilisateur.add(nomUtilisateur);
            
            //la liste contient des doublons qu'on supprime avec HashSet puis on ecrit la nouvelle liste dans le fichier utilisateurs.txt 
            Set<String> set = new HashSet<String>(listeUtilisateur);
            List<String> utilisateursNoDoublon = new ArrayList<String>(set); 
            String fichierUtilisateurs = "utilisateurs.txt";
            Files.write(Paths.get(fichierUtilisateurs),utilisateursNoDoublon);
            
            //recuperation de l'heure avec un nouveau split et de l'ip de connexion
            String[] contenuHeure = connexion.split(" ");
            String heureUtilisateur = contenuHeure[1];
            String ip = connexionContenu[0];
            
            //on etablit la variable dateLimite a 19h
            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
            java.util.Date dateBase = dateFormat.parse(heureUtilisateur);
            java.util.Date dateLimite = dateFormat.parse("19:00");
            
            //si l'heure de connexion depasse 19h, l'ip et le nom sont stockes dans une nouvelle liste
            if (dateBase.after(dateLimite))
            {
                listeSuspect.add(ip);
                listeSuspect.add(nomUtilisateur);
            }   
            
            //recuperation des donnees du fichier warning.txt
            String fichierConnexion2 = "warning.txt";
            File fichier2 = new File(fichierConnexion2);
            Scanner lectureScanner2 = new Scanner(fichier2);

            /**
             * deuxième boucle pour lire les lignes du fichier
             */
            while (lectureScanner2.hasNextLine()) 
            {
            	//recuperation du contenu avec un split
                String warning = lectureScanner2.nextLine();
                String[] warningContenu = warning.split(" ");

                //si le contenu correspond avec l'ip du precedent fichier, on ajoute le nom correspondant dans une nouvelle liste
                if (warningContenu[0].equals(ip))
                {
                    listeWarning.add(nomUtilisateur);
                    
                    //on utilise Collections.frequency pour connaitre la frequence d'apparition du nom stocke dans la liste etablie precedemment
                    int suspectNombre = Collections.frequency(listeWarning,nomUtilisateur);
                    
                    //ajout du nom et de sa frequence dans une nouvelle liste
                    listeWarning2.add(nomUtilisateur+";"+suspectNombre);
                }
            }
        }
        
        //utilisation de HashSet pour supprimer les doublons
        Set<String> set2 = new HashSet<String>(listeSuspect);
        List<String> suspectNoDoublon = new ArrayList<String>(set2);
        System.out.println("Nous avons retrouvé le suspect avec son nom d'utilisateur et son adresse ip ! Le voici : "+suspectNoDoublon);
        
        //ecriture dans le fichier suspect.txt de la liste des noms avec leur frequence d'apparition dans celle-ci
        String fichierSuspect = "suspect.txt";
        Files.write(Paths.get(fichierSuspect),listeWarning2);
    }
    
    public static void main(String[] args) throws IOException, ParseException 
    {
        HackerTracker newHT = new HackerTracker();
        newHT.hacker();
    }
}