package crmjuridico.auxiliares;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class IN
{
    private static final String RESET  = "\u001B[0m";
    private static final String VERMELHO = "\u001B[31m";
    private static final Scanner tec = new Scanner(System.in);
    /*
    Fiz um um ciclo while true para validar os dados
    o ciclo é infinito, porém quebrado pelo return
    o seja enquanto o tipo de dado não estiver correto, vai pedir para digitar.
     */
    public static int getInt() {
        while (true) {
            try
            {
                return Integer.parseInt(tec.nextLine());
            }
            catch (Exception e)
            {
                System.out.println(VERMELHO + "ERRO: valor digitado é inválido!" + RESET);
                System.out.print("\nDigite um número: ");
            }
        }
    }
    public static String getEnter() {
        String x = tec.nextLine();
        return x;
    }
    public static String getString() {
        while (true){
            String x = tec.nextLine();
            if(!x.isBlank())
            {
                return x;
            }
            else
            {
                System.out.println(VERMELHO + "ERRO: Campo obrigatório!" + RESET);
                System.out.print("\nDigite : ");
            }
        }
    }
    public static int getNif() {
        while (true) {
            String x = tec.nextLine();
            if (x.matches("[0-9]{9}"))
                return Integer.parseInt(x);
            else {
                System.out.println(VERMELHO + "ERRO: NIF inválido! Deve ter exactamente 9 dígitos." + RESET);
                System.out.print("\nDigite o NIF: ");
            }
        }
    }
    public static int getTelefone() {
        while (true) {
            String x = tec.nextLine();
            if (x.matches("[2-9][0-9]{8}"))
                return Integer.parseInt(x);
            else {
                System.out.println(VERMELHO + "ERRO: Telefone inválido! Deve ter exactamente 9 dígitos." + RESET);
                System.out.print("\nDigite o Telefone: ");
            }
        }
    }
    public static float getFloat() {
        while (true) {
            try
            {
                return Float.parseFloat(tec.nextLine());
            }
            catch (Exception e)
            {
                System.out.println(VERMELHO + "ERRO: valor digitado é inválido!" + RESET);
                System.out.print("\nDigite um número: ");
            }
        }
    }
    public static LocalDate getLocalDate() {
        while (true) {
            String dataString = getString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            try {
                return LocalDate.parse(dataString, formatter);
            }
            catch (DateTimeParseException e) {
                System.out.println(VERMELHO + "ERRO: Data inválida! Use o formato AAAA/MM/DD" + RESET);
                System.out.print("\nDigite a data: ");
            }
        }
    }

}
