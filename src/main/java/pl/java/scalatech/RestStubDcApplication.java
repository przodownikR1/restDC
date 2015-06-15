package pl.java.scalatech;

import static com.google.common.collect.Lists.newArrayList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pl.java.scalatech.entity.BankAccount;
import pl.java.scalatech.entity.User;
import pl.java.scalatech.service.userInformation.UserAccountService;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;

@SpringBootApplication
public class RestStubDcApplication implements CommandLineRunner {
    @Autowired
    private UserAccountService userAccountService;

    private Random r = new Random();

    private List<String> cities = Lists.newArrayList("Warsaw", "Radom", "Ilza", "London", "Paris", "Berlin");
    private List<String> addresses = Lists.newArrayList("Polna", "al.Jerozoliskie", "Popieluszki", "dolna", "gorna", "rybia");

    public static void main(String[] args) {
        SpringApplication.run(RestStubDcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 20; i++) {
            boolean isCredit = r.nextBoolean();
            BankAccount ba = null;
            if (isCredit) {
                ba = BankAccount.builder().amount(new BigDecimal(r.nextInt(100000))).credit(new BigDecimal(r.nextInt(10000))).creditCardNumer("11110000" + i)
                        .build();
            } else {
                ba = BankAccount.builder().amount(new BigDecimal(r.nextInt(100000))).debit(new BigDecimal(r.nextInt(10000))).creditCardNumer("11110000" + i)
                        .build();
            }
            String passwd = Hashing.md5().hashString("" + i, Charsets.UTF_8).toString();
            User user = User.builder().accounts(newArrayList(ba)).address(addresses.get(r.nextInt(addresses.size() - 1)))
                    .city(cities.get(r.nextInt(cities.size() - 1))).login("p:" + i).email("p:" + i + "@gmail.com").passwd(passwd).phone("" + i)

                    .build();
            userAccountService.saveUser(user);
        }
    }
}
