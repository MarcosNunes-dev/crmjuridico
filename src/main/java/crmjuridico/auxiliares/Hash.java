package crmjuridico.auxiliares;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash
{
    private Hash() { // construtor vazio e privado, para não ser criado objeto dessa classe
    }

    public static String sha256(String pass) {
        try {
            MessageDigest msg = MessageDigest.getInstance("SHA-256"); // sha-256 algoritmo de cripto, 64 caracteres
            byte[] hash = msg.digest(pass.getBytes("UTF-8")); // transforma a senha em byte
            StringBuilder passHex = new StringBuilder();
            for (byte b : hash) {
                passHex.append(String.format("%02x", b)); // transforma em hexa e guarda a pass
            }
            return passHex.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException("Erro: hash não calculada ", e);
        }
    }
}
