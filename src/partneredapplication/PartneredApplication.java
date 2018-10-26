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
import java.io.IOException;
import javax.crypto.KeyGenerator;
import java.io.UnsupportedEncodingException; 
import java.security.InvalidKeyException; 
import java.security.NoSuchAlgorithmException; 
import java.security.spec.InvalidKeySpecException; 
import javax.crypto.BadPaddingException; 
import javax.crypto.Cipher; 
import javax.crypto.IllegalBlockSizeException; 
import javax.crypto.NoSuchPaddingException; 
import javax.crypto.SecretKey; 
import javax.crypto.SecretKeyFactory; 
import javax.crypto.spec.DESKeySpec; 

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
    public static ArrayList<SecretKey> StoredKeys = new ArrayList<SecretKey>();

    // Main Method
    public static void main(String[] args) throws Exception {
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
        boolean accountExists = verifyExistance(computerUsername);
        
      // Encryption Example
      
      /* 
       * SecretKey key = KeyGenerator.getInstance("DES").generateKey();
       * DesEncrypter encrypter = new DesEncrypter(key);
       * String encrypted = encrypter.encrypt("1h8f&n101");
       * String decrypted = encrypter.decrypt(encrypted);
      */
      
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
    
    // Find XML Node at the Given Element Name.
    static private Node findFirstNamedElement(Node parent, String tagName){
        NodeList children = parent.getChildNodes();
        for (int i = 0, in = children.getLength(); i<in; i++){
            Node child = children.item(i);
             if ( child.getNodeType() != Node.ELEMENT_NODE )
                continue;
             if ( child.getNodeName().equals(tagName) )
                return child;
        }
        return null;
    }
    
    // Find the Main Node for the Account
    static private String getAccountData(Node parent){
        StringBuilder text = new StringBuilder();
        if(parent == null)
            return text.toString();
        NodeList children = parent.getChildNodes();
        for(int k = 0, kn = children.getLength(); k < kn; k++){
            Node child = children.item(k);
            if(child.getNodeType() != Node.TEXT_NODE)
                break;
            text.append(child.getNodeValue());
        }
        return text.toString();
    }
    
    // Clear Local Data for Updating.
    public static void removeAll(Node node, short nodeType, String name, Object id) {
        
        if (node.getNodeType() == nodeType && (name == null || node.getNodeName().equals(name))) {
          Element acID = (Element)node;
          if(acID.getAttribute("id").equals(id)){
            node.getParentNode().removeChild(node);
            }
        } else {
          NodeList list = node.getChildNodes();
          for (int i = 0; i < list.getLength(); i++) {
            removeAll(list.item(i), nodeType, name, id);
          }
        }
    }
    
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
       public static String randomAlphaNumeric(int count) {
           StringBuilder builder = new StringBuilder();
       while (count-- != 0) {
           int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
           builder.append(ALPHA_NUMERIC_STRING.charAt(character));
       }
       return builder.toString();
    }
}
