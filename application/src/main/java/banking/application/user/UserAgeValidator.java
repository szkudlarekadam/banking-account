package banking.application.user;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;
import lombok.extern.log4j.Log4j2;

@Log4j2
class UserAgeValidator implements Predicate<User> {

    @Override
    public boolean test(User user) {
        String pesel = user.getPesel();
        int year = Integer.parseInt(pesel.substring(0,2));
        int month = Integer.parseInt(pesel.substring(2,4));
        int day = Integer.parseInt(pesel.substring(4,6));

        if (month > 20) {
            year+=2000;
            month-=20;
        }
        else {
            year +=1900;
        }

        LocalDate birthDate = LocalDate.of(year, month, day);
        LocalDate now = LocalDate.now();

        if (ChronoUnit.YEARS.between(birthDate, now) < 18L) {
            log.info("User with pesel {} is under 18 years old", user.getPesel());
            throw new UserNotAdultException("User is under 18 years old");
        }

        return true;
    }
}
