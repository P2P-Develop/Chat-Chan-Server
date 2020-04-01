package develop.p2p.chatchan.Message;

import develop.p2p.chatchan.util.IntToString;
import develop.p2p.chatchan.Main;

import java.security.*;
import java.util.*;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.*;

public class EncryptManager
{
    public static String encrypt(String text, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        byte[] ecb = getCipher(Cipher.ENCRYPT_MODE, key).doFinal(text.getBytes());
        return new String(ecb);
    }
    public static String decrypt(String text, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {

        byte[] dcb = getCipher(Cipher.DECRYPT_MODE, key).doFinal(text.getBytes());
        return new String(dcb);
    }

    private static Cipher getCipher(int mode, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
    {
        SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "Rijndael");
        Cipher cipher = Cipher.getInstance("Rijndael");
        cipher.init(mode, spec);
        return cipher;
    }

    public static String generateEncryptKey()
    {
        StringBuilder encryptKey = new StringBuilder();
        for (int ii = 0; ii < Main.keyLength; ii++)
        {
            long seed = getSeed(810L);
            Calendar cTime = Calendar.getInstance();
            seed = seed +
                    (cTime.get(Calendar.SECOND) * cTime.get(Calendar.HOUR) * 0x7c8a3b4L) +
                    (cTime.get(Calendar.SECOND) * 0x3a4bdeL) +
                    (cTime.get(Calendar.MINUTE) * cTime.get(Calendar.YEAR)) * 0x4307a7L +
                    (cTime.get(Calendar.MILLISECOND) * 0xf7defL) ^ 0x3ad8025f;
            seed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL;
            int cs = ((int) (seed >> 17)) % 62;
            encryptKey.append(IntToString.getStringFromInt(cs));
        }
        return encryptKey.toString();
    }

    public static String generateDecryptKey()
    {
        StringBuilder decryptKey = new StringBuilder();
        for (int ii = 0; ii < Main.keyLength; ii++)
        {
            Calendar cTime = Calendar.getInstance();
            long seed = getSeed(114L);
            seed = seed +
                    (cTime.get(Calendar.HOUR) * cTime.get(Calendar.MINUTE) * 0x4c1906) +
                    (cTime.get(Calendar.MILLISECOND) * 0x5ac0db) +
                    (cTime.get(Calendar.YEAR) * cTime.get(Calendar.SECOND)) * 0x4307a7L +
                    (cTime.get(Calendar.MONTH) * 0x5f24f) ^ 0x3ad8025f;
            seed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL;
            int cs = ((int) (seed >> 17)) % 62;
            decryptKey.append(IntToString.getStringFromInt(cs));
        }
        return decryptKey.toString();
    }

    private static long getSeed(long seedBase)
    {
        Random random = new Random();
        do
            seedBase = seedBase + ((seedBase ^ 0x5DEECE66DL + 0xBL) & (0xFFFFFFFFFFFFL)) * new Random().nextLong();
        while((new Random(random.nextInt(24)).nextInt(1024) > 999));
        return seedBase;
    }

}
