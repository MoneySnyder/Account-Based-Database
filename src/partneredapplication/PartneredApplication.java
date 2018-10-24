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

/**
 * Group Members
 * @author cs.evsnyder
 * @author cs.cwberg
 */

public class PartneredApplication {

    // Account Information Database
    public static ArrayList<String> StoredUsernames = new ArrayList<String>();
    public static ArrayList<String> StoredPins = new ArrayList<String>();

    // Main Method
    public static void main(String[] args) {

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
}
