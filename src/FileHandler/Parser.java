package FileHandler;

import Model.Account;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private List<Account> accountList;

    public Parser() {
        accountList = new ArrayList<>();
    }

    public void parse() {
        File input = new File("D:\\Courses\\Bank\\src\\input");
        File[] txtFiles = input.listFiles();

        for (File file : txtFiles) {
            if (!file.getName().endsWith(".txt")) {
                continue;
            }

            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader);
                 FileWriter fileWriter = new FileWriter("D:\\Courses\\Bank\\src\\archive\\archive.txt")) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] fields = line.split("\\|");

                    if (fields.length != 3) {
                        continue;
                    }

                    String fromNumber = fields[0].trim();
                    String toNumber = fields[1].trim();
                    int amount = Integer.parseInt(fields[2].trim());

                    if (!fromNumber.matches("\\d{5}-\\d{5}") && !toNumber.matches("\\d{5}-\\d{5}")) {
                        System.out.println("Number is not valid " + line);
                        continue;
                    }
                    if (amount <= 0) {
                        System.out.println("Amount is not correct " + line);
                        continue;
                    }

                    Account fromAccount = getAccount(fromNumber);
                    Account toAccount = getAccount(toNumber);

                    if (fromAccount != null && toAccount != null) {
                        fromAccount.setBalance(fromAccount.getBalance() - amount);
                        toAccount.setBalance(toAccount.getBalance() + amount);
                    } else {
                        System.out.println("Numbers of account is not valid " + line);
                    }
                }
                fileWriter.write(String.valueOf(file));
                fileWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Account getAccount(String accountNumber) {
        for (Account account : accountList) {
            if (account.getNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
}
