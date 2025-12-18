/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Algo_Chiffrage;

import java.io.File;
import java.io.*;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;


 
public class Programme {
    public static void main(String[]args) throws IOException, Exception{
           Scanner scanner = new Scanner(System.in);
           
           System.out.println("Déseirez-vous : \n 1.Crypter un fichier \t 2.Décrypter un fichier");  
            int Ans = scanner.nextInt();
            
            if (OptionalDataException(1).equals(Ans)){
                 JFileChooser choixFile = new JFileChooser() ;
             int resultat = choixFile.showOpenDialog(null); 
            if ( resultat != JFileChooser.APPROVE_OPTION){
                System.out.println("Aucun fichier sélectionné.");
                return ;
            }
            //Selection du fichier à crypter
            File files = choixFile.getSelectedFile() ;
            System.out.println("Fichiere selectionné :"+files.getAbsolutePath());
             
             // 2.Lecture du fichier
             byte[] Lect = readFile(files) ;
            
             // Proposition au user
              SecretKey cles  = null ;
            while (true) {                
                System.out.println("Désirez-vous charger une clé de cryptage (Oui/Non)?");
                         
             
             
                 String reponse = scanner.nextLine().trim().toLowerCase();
                if(reponse.isEmpty()){
                 System.out.println("Aucune réponse détectée. Veuillez répondre par "
                         + "'Oui' ou 'Non'");
                 continue; //Déclenche un retour a la proposition
                }
                if(!reponse.equals("oui") && !reponse.equals("non")){
                 System.out.println("Réponse invalide. Veuillez 'Oui' ou 'Non'");
                 continue; //Retour 
                }
                System.out.println("Vous avez saisi :\t"+reponse.toUpperCase()+". \nConfirmez-vous votre choix?(Oui/Non)");
                String confirmation = scanner.nextLine().trim().toLowerCase() ;
            
                if(!confirmation.equals(reponse)){
                 System.out.println("Choix non confirmer. Retour à la saisie?");
                 continue ;
                }
             
                if(confirmation.equals("oui")){
                //chargement du fihier clé
                
                    JFileChooser filcryp = new JFileChooser() ;
                    int resultats = filcryp.showOpenDialog(null); 
                    if ( resultats != JFileChooser.APPROVE_OPTION){
                        System.out.println("Aucun fichier clé selectionné.");
                        continue ;
                    }
                    //Selection de la clé de cryptage
                    File cry = filcryp.getSelectedFile() ;
                    System.out.println("Fichiere selectionné :"+cry.getAbsolutePath());
                
                    //lecture de la clé à partir du fichier
                    byte[] clebytes = readFile(cry) ;
                    cles = new SecretKeySpec(clebytes, "AES") ; 
                 
                 
                     File encryfile = new File(files.getParent(), "encrypted"+files.getName());
                    writeFile(encryfile, clebytes);
                    System.out.println("Fichier crypter et sauvegarder sous :"+encryfile.getAbsolutePath());
                    break ;
                } 
                else if (confirmation.equals("non")) {
                
                    //3. Générer une nouvell clé AES
                    KeyGenerator cleGene = KeyGenerator.getInstance("AES") ;
                    cleGene.init(256);
                    cles = cleGene.generateKey();
                
                    //Sauvegarde de la clé dans un fichier
                    File fichierCle = new File(files.getParent(), "Clé_générer_Crypt.key") ;
                    writeFile(fichierCle,  cles.getEncoded()) ;
                    System.out.println("CLé de cryptage générée et Enrégistré dans :"+fichierCle.getAbsolutePath());
                    
                    //4. Crypter le contenu du fichier
                    byte[] encryptedData  = encrypt(Lect, cles) ;
                    System.out.println("FIchier crypter Avec succés !");
             
                    //5. Sauvegarder le fichier crypter
                    File Filecrypter = new File(files.getParent(), "encrypted_"+files.getName()) ;
                    WriteFile(Filecrypter, encryptedData) ;
                    System.out.println("Fichier crypter sauvegarder sous :"+Filecrypter.getAbsolutePath());
                    break ;
                } else {
                    System.out.println("Réponse invalide.Opération annuléé");
                }
              
            }
                return ;
                
            }
           
           
    }

    private static byte[] readFile(File cry)throws IOException{
      try(FileInputStream fis = new FileInputStream(cry)){
          return 
                  fis.readAllBytes();
      }  
    }

    private static void writeFile(File fichierCle, byte[] encoded)throws IOException{
      try(FileOutputStream fos = new FileOutputStream(fichierCle)){
          fos.write(encoded);
      }
    }

    private static byte[] encrypt(byte[] Lect, SecretKey cles) throws Exception{
        Cipher cipher = Cipher.getInstance("AES") ;
        cipher.init(Cipher.ENCRYPT_MODE, cles);
         return cipher.doFinal(Lect);
    }

    private static void WriteFile(File Filecrypter, byte[] encryptedData)throws IOException{
       try(FileOutputStream fosa = new FileOutputStream(Filecrypter)){
           fosa.write(encryptedData);
       }
    }

    private static Object OptionalDataException(int i) {
        
        
        return  i;
        
        
    }
             
             
      
  }
  
  

    
    
   
    

