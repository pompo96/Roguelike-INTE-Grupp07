package email;

import java.util.HashSet;
import java.util.Set;

public class Email {

    private static final Set<String> allEmails = new HashSet<>();

    private String rawAdress;
    private String normalizedAdress;
    private boolean registered = false;

    public Email(String adress){
        this.rawAdress = adress;
        this.normalizedAdress = normalize(adress);
    }

    //Metod som gör alla bokstäver små så det går att jämföra emails oavsett om de har
    //stora eller små bokstäver.
    private static String normalize(String adress){
        if(adress == null){
            return null;
        }
        return adress.trim().toLowerCase();
    }

    //Registrera mails som är valida och som inte redan finns i registret sen tidigare
    public boolean register(){
        if(!isValid()){
            return false;
        }

        if(isEmailTaken(normalizedAdress)){
            return false;
        }

        allEmails.add(normalizedAdress);
        registered = true;
        return true;

    }

    //Kollar om en email har en valid adress
    public boolean isValid(){

    }

    public boolean isEmailTaken(String adress){
        if(adress == null || adress.isEmpty()){
            return false;
        }
        return allEmails.contains(normalize(adress));
    }


}
