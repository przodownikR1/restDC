package pl.java.scalatech.util;

import static com.google.common.collect.Lists.newArrayList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import pl.java.scalatech.entity.BankAccount;
import pl.java.scalatech.entity.User;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;

public final class DataGenerator {

    private static Random r = new Random();

    private static List<String> cities = Lists.newArrayList("Warsaw", "Radom", "Ilza", "London", "Paris", "Berlin");
    private static List<String> addresses = Lists.newArrayList("Polna", "al.Jerozoliskie", "Popieluszki", "dolna", "gorna", "rybia");

    public static List<User> generateUserData(int size) {
        List<User> users = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
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
                    .city(cities.get(r.nextInt(cities.size() - 1))).login("p_" + i).email("p_" + i + "@gmail.com").passwd(passwd).phone("" + i)

                    .build();
            users.add(user);
        }

        return users;
    }
}
