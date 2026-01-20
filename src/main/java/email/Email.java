package email;

import java.util.HashSet;
import java.util.Set;

public class Email {

    private static final Set<String> allEmails = new HashSet<>();

    private final String originalAddress;
    private final String normalizedAddress;
    private boolean registered = false;

    public Email(String address){
        this.originalAddress = address;
        this.normalizedAddress = normalize(address);
    }

    //Metod som gör alla bokstäver små så det går att jämföra emails oavsett om de har
    //stora eller små bokstäver.
    private static String normalize(String address){
        if(address == null){
            return null;
        }
        return address.trim().toLowerCase();
    }

    //Registrera mails som är valida och som inte redan finns i registret sen tidigare
    public boolean register(){
        if(!isValid()){
            return false;
        }

        if(isEmailTaken(normalizedAddress)){
            return false;
        }

        allEmails.add(normalizedAddress);
        registered = true;
        return true;

    }

    public boolean isRegistered(){
        return registered;
    }

    //Kollar om en email har en valid adress
    public boolean isValid(){
        if(originalAddress == null || originalAddress.isBlank()){
            return false;
        }
        String email = normalizedAddress;

        //regler för vad email ska innehålla
        return email.contains("@")
                && email.contains(".")
                && !email.startsWith("@")
                && !email.endsWith("@")
                && !email.contains(" ")
                && !email.contains("..");
    }

    public static boolean isEmailTaken(String address){
        if(address == null || address.isEmpty()){
            return false;
        }
        return allEmails.contains(normalize(address));
    }

    public static void clearRegistry(){
        allEmails.clear();
    }

}
