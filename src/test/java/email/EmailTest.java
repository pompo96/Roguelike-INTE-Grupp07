package email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    @BeforeEach
    void setUp(){
        Email.clearRegistry();
    }

    //Valida email format
    @ParameterizedTest(name = "Valid email: \"{0}\" should be accepted")
    @ValueSource(strings ={
            "player@example.com",
            "first.last@example.com",
            "first_last@student.su.se",
            "first_middle+last@example.com",
            "first19@example.com"
    })
    void validEmailFormatAreCorrect(String adress){
        Email email = new Email(adress);

        assertTrue(email.isValid(), "Expected valid email: " + adress);
    }

    //Invalida email format
    @ParameterizedTest(name = "Invalid email: \"{0}\" should be rejected")
    @ValueSource(strings ={
            "playername",
            "@firstname.com",
            "firstname@",
            "firstname.com",
            "firstname@lastname",
            "firstname@.com",
            "firstname@@example.com",
            "first name@example.com",
            "player@example..com"
    })
    void invalidEmailFormatAreIncorrect(String adress){
        Email email = new Email(adress);

        assertFalse(email.isValid(), "Expected invalid email: " + email);
        assertFalse(email.isRegistered());
    }


    //Emails blir standardiserade innan de registreras
    @ParameterizedTest
    @CsvSource({
            "FirstName@Example.com, firstname@example.com",
            "FIRSTNAME@EXAMPLE.COM, firstname@example.com",
            " firstname@example.com , firstname@example.com"
    })
    @DisplayName("Emails are standarlized/normalized before registration")
    void emailIsStandarlized(String adress, String standarlized){
        Email email = new Email(adress);
        email.register();

        assertTrue(Email.isEmailTaken(standarlized));
    }

    @ParameterizedTest
    @CsvSource({
            "firstname@example.com, FIRSTNAME@example.com",
            "Firstname@Example.com, firstname@example.com"
    })
    @DisplayName("Emails differing only by case are considered the same")
    void emailValidationIsCaseInsensitive(String firstAdress, String secondAdress){
        Email emailOne = new Email(firstAdress);
        Email emailTwo = new Email(secondAdress);

        assertTrue(emailOne.register());
        assertFalse(emailTwo.register());
    }

    //Testar om försök till registrering av invalid email påverkar
    //registrering av valida emails negativt
    @Test
    @DisplayName("Registering invalid email does not affect the registry")
    void invalidEmailRegistrationDoNotAffectRegistery(){
        Email invalidEmail = new Email("invalid.com");
        invalidEmail.register();

        Email validEmail = new Email("valid@email.com");
        assertTrue(validEmail.register());
        assertTrue(validEmail.isEmailTaken("valid@email.com"));
    }

    //Test som kollar att samma mail inte kan registreras två gånger
    @Test
    @DisplayName("Registering same email multiple times is not possible")
    void cannotRegisteringSameEmailTwice(){
        Email email = new Email("firstname@example.com");

        assertTrue(email.register());
        assertFalse(email.register());
        assertTrue(email.isRegistered());
    }

    //testar register() för email adresser som kan vara duplicerade, ogiltiga eller ha olika bokstavsform
    @ParameterizedTest
    @ValueSource(strings = {"first@example.com", "second@example.com", "FIRST@example.com", "invalid"})
    void combinationalPairs(String firstEmail){
        String[] allEmails = {"first@example.com", "second@example.com", "FIRST@example.com", "invalid"};

        //för varje firstEmail så loopas alla secondEmail för att kontrollera alla 16 olika kombinationer av par
        for(String secondEmail : allEmails){
            Email emailOne = new Email(firstEmail);
            Email emailTwo = new Email(secondEmail);

            //räknar hur många mailadresser som blir registrerade
            int registeredCount = 0;
            if(emailOne.registered()){
                registeredCount++;
            }
            if(EmailTwo.registered()){
                registeredCount++;
            }

            //om båda mailadresserna är samma (case-insensitivity) eller båda är ogiltiga kan som högst 1 registreras
            if(firstEmail.equalsIgnoreCase(secondEmail) || !emailOne.isValid() && !emailTwo.isValid()){
                assertTrue(registeredCount <= 1);
                //om båda är giltiga och unika så registrras 2
            } else if(emailOne.isValid() && emailTwo.isValid() && !firstEmail.equalsIgnoreCase(secondEmail)){
                assertEquals(2, registeredCount);
            }
        }
    }

    //Test som kollar att null inputs och okända email hanteras korrekt
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings ={"unknown@example.com"})
    @DisplayName("isEmailValid handles unknown and null inputs safely")
    void isEmailTakenHandlesUnknownAndNullInputs(String adress){
        assertFalse(Email.isEmailTaken(adress));
    }

}
