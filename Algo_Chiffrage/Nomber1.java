/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Algo_Chiffrage;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey ;
import java.security.PublicKey ;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class Nomber1 {
    /*Avec cette methode nous allons charger la clé public à partir du fichier pem 
    dans notre classe java*/
  private static PublicKey loadPublicKey(String public_Keypem) throws Exception{
        String Cle = new String(Files.readAllBytes(Paths.get(public_Keypem)))  
             .replace("-----BEGIN PUBLIC KEY-----", "") // Corrige l'en-tête
                .replace("-----END PUBLIC KEY-----", "")   // Corrige le pied
                .replaceAll("\\s", "");   
        byte[]keyByets =java.util.Base64.getDecoder().decode(Cle);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyByets);
        return
           KeyFactory.getInstance("RSA").generatePublic(spec);        
    }
  
    /*Avec cette methode nous allons charger la clé privée à partir du fichier pem 
    dans notre classe java*/
   private static PrivateKey loadPrivateKey(String private_Keypem)throws Exception{
       String Cle = new String(Files.readAllBytes(Paths.get(private_Keypem))) 
            .replace("-----BEGIN PRIVATE KEY-----", "") // Corrige l'en-tête
                .replace("-----END PRIVATE KEY-----", "")   // Corrige le pied
                .replaceAll("\\s", ""); 
       byte[]keyBytes = java.util.Base64.getDecoder().decode(Cle);
       PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
       return 
               KeyFactory.getInstance("RSA").generatePrivate(spec);
    }
   
   
   /*Dans cette methode nous ecrirons le code qui permettra le chiffremment des messages */
    private static byte[] encryptWithPublicKey(byte[]MessageCrypter , PublicKey Clepublic)throws Exception{
       Cipher cipher =  Cipher.getInstance("RSA");
       cipher.init(Cipher.ENCRYPT_MODE, Clepublic);
       return
        cipher.doFinal(MessageCrypter);
    }
    
    /*Dans celle-ci est écrit le code qui permettra le déchiffremment des message */
    private static byte[] decryptWithPrivateKey(byte[] MessageCrypter, PrivateKey Cleprive)throws Exception{
    
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, Cleprive);
    return 
       cipher.doFinal(MessageCrypter);
    }
      
   //Corps du programme 
    public static void main(String[]args) throws Exception {
        //Chargements des clés généreré par Openssl
        PublicKey Clepublic = loadPublicKey("C:\\Program Files\\OpenSSL-Win64\\bin\\public_Key.pem");//Appel de la cle public chargé avec la methode " loadPublicKey " 
        PrivateKey Cleprive = loadPrivateKey("C:\\Program Files\\OpenSSL-Win64\\bin\\private_Key.pem") ; // Appel de la cle privée chargé avec la methode " loadPublicKey " 
        
        //Données à chiffrer 
        String message = "Je vais niquer ta Mère ! Fcuk " ;
        
       // System.out.println("Message Original :"+message);
        
        //Chiffrement du message avec la clé public
        byte[] MessageCrypter = encryptWithPublicKey(message.getBytes(),Clepublic) ;
        System.out.println("Messsage chiffré :"+new  String (MessageCrypter));
        
        /*Déchiffrement du message crypter
        byte[] MessageDecry = decryptWithPrivateKey(MessageCrypter, Cleprive) ;
        System.out.println("Message déchiffré :"+new  String(MessageDecry));   */
      
    }

   
}
