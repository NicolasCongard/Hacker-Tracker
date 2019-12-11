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
    public void utilisateurs() throws IOException, ParseException 
    {
        String fichierConnexion = "connexion.log";
        File fichier = new File(fichierConnexion);
        Scanner lectureScanner = new Scanner(fichier);
        
        List<String> listeUtilisateur = new ArrayList<String>();
        List<String> listeSuspect = new ArrayList<String>();
        List<String> listeWarning = new ArrayList<String>();
        List<String> listeWarning2 = new ArrayList<String>();

        while (lectureScanner.hasNextLine()) 
        {
            String connexion = lectureScanner.nextLine();
            String[] connexionContenu = connexion.split(";");
            String nomUtilisateur = connexionContenu[1];
            listeUtilisateur.add(nomUtilisateur);
            Set<String> set = new HashSet<String>(listeUtilisateur);
            List<String> utilisateursNoDoublon = new ArrayList<String>(set); 
            String fichierUtilisateurs = "utilisateurs.txt";
            Files.write(Paths.get(fichierUtilisateurs),utilisateursNoDoublon);
            
            String[] contenuHeure = connexion.split(" ");
            String heureUtilisateur = contenuHeure[1];
            String ip = connexionContenu[0];
            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
            java.util.Date dateBase = dateFormat.parse(heureUtilisateur);
            java.util.Date dateLimite = dateFormat.parse("19:00");
            
            
            if (dateBase.after(dateLimite))
            {
                listeSuspect.add(ip);
                listeSuspect.add(nomUtilisateur);
            }   
            
            String fichierConnexion2 = "warning.txt";
            File fichier2 = new File(fichierConnexion2);
            Scanner lectureScanner2 = new Scanner(fichier2);

            while (lectureScanner2.hasNextLine()) 
            {
                String warning = lectureScanner2.nextLine();
                String[] warningContenu = warning.split(" ");

                if (warningContenu[0].equals(ip))
                {
                    listeWarning.add(nomUtilisateur);
                    int suspectNombre = Collections.frequency(listeWarning,nomUtilisateur);
                    listeWarning2.add(nomUtilisateur+";"+suspectNombre);
                }
            }
        }
        
        Set<String> set2 = new HashSet<String>(listeSuspect);
        List<String> suspectNoDoublon = new ArrayList<String>(set2);
        System.out.println("Nous avons retrouvé le suspect avec son nom d'utilisateur et son adresse ip ! Le voici : "+suspectNoDoublon);
        
        String fichierSuspect = "suspect.txt";
        Files.write(Paths.get(fichierSuspect),listeWarning2);
    }
    
    public static void main(String[] args) throws IOException, ParseException 
    {
        HackerTracker newHT = new HackerTracker();
        newHT.utilisateurs();
    }
}