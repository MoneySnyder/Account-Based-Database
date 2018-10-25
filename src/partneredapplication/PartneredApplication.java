/*
 * Program Development Start Date : 10/23/2018
 * Partnered Application Project
 * Problem :
 */
package partneredapplication;
// Main Utility
import java.util.*;
import java.util.ArrayList;

// XML Parsing Packages
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
// File Handling Packages
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// XML Handlers
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// Encryption Packages
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Group Members
 * @author cs.evsnyder
 * @author cs.cwberg
 */

public class PartneredApplication {

    // Account Information Database
    public static ArrayList<String> StoredUsernames = new ArrayList<String>();
    public static ArrayList<String> StoredPins = new ArrayList<String>();
    public static ArrayList<String> StoredIds = new ArrayList<String>();

    // Main Method
    public static void main(String[] args) {
      // Load XML File and create local storage.
        try{
          // Instance a document-builder in order to read file.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            File file = new File("src/partneredapplication/accountStorage.xml");
            Document document = builder.parse(file);
            Element accountStorage = document.getDocumentElement();
            NodeList accounts = accountStorage.getChildNodes();
            for(int i = 0, ii = 0, n = accounts.getLength(); i<n; i++){
                Node child = accounts.item(i);
                if(child.getNodeType() != Node.ELEMENT_NODE)
                    continue;
               Element account = (Element)child;
               ii++;

               // Add items / attributes to local storage.
               StoredIds.add(account.getAttribute("id"));
               StoredUsernames.add(getAccountData(findFirstNamedElement(child,"username")));
               StoredPins.add(getAccountData(findFirstNamedElement(child,"pin")));

            }

        }  catch(IOException | ParserConfigurationException | SAXException e){
            e.printStackTrace();
        }

      // Creates account if one is not created.
        String computerUsername = System.getProperty("user.name");
        boolean accountExists = verifyCredentials(computerUsername);

      // Prompt UI in both cases.

    }

    // Prerequisite Verification / 4-Digit Pin Code.
    public static boolean verifyCredentials(String username, String pin){
        for(int i=0; i<(StoredUsernames.size()); i++){
            if(username.equals((String)StoredUsernames.get(i))){
                if(((String)StoredPins.get(i)).equals(pin)){
                    //System.out.println("PinCorrectCredentials");
                    return true;
                }
                if(((String)StoredPins.get(i)).equals("NoPin")){
                    //System.out.println("NoPinCorrectCredentials");
                    return true;
                }
                else{
                    System.out.println("Incorrect Pin.");
                    return false;
                }
            }
        }
        System.out.println("Account does not exist, prompting account creation for "+username);
        return false;
    }

    // Verify Existance in case of duplicate prevention.
    public static boolean verifyExistance(String username){
        for(int i=0; i<(StoredUsernames.size()); i++){
          if(username.equals((String)StoredUsernames.get(i))){
            //System.out.println("Account Exists.");
            return true;
          }
          else{
            //System.out.println("Account does not exist.");
            return false;
          }
        }
        return false;
      }

    // Function for creating account if account is not already created.
    public static void createAccount(String username, String pin){
      boolean existingaccount = verifyExistance(username);
      if(existingaccount == true){
        return;
      }
      else{
        StoredUsernames.add(username);
        StoredPins.add("NoPin");
      }
    }

    // DES Encryption
    public String encrypt(String str){
      try {
          // Encode the string into bytes using utf-8
          byte[] utf8 = str.getBytes("UTF8");

          // Encrypt
          byte[] enc = ecipher.doFinal(utf8);

          // Encode bytes to base64 to get a string
          return new sun.misc.BASE64Encoder().encode(enc);
      } catch (javax.crypto.BadPaddingException e) {
      } catch (IllegalBlockSizeException e) {
      } catch (UnsupportedEncodingException e) {
      } catch (java.io.IOException e) {
      }
      return null;
  }

  // DES Decryption
  public String decrypt(String str) {
    try {
        // Decode base64 to get bytes
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

        // Decrypt
        byte[] utf8 = dcipher.doFinal(dec);

        // Decode using utf-8
        return new String(utf8, "UTF8");
    } catch (javax.crypto.BadPaddingException e) {
    } catch (IllegalBlockSizeException e) {
    } catch (UnsupportedEncodingException e) {
    } catch (java.io.IOException e) {
    }
    return null;
  }


}
