package timetogeter.context.group.application.service.util;

import javax.crypto.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptUtil {
    private static final String AES = "AES";
    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 16;  // GCM 인증 태그 길이 (16바이트)
    private static final int IV_LENGTH = 12;        // IV 길이 (12바이트 권장)

    /**
     * AES-GCM 방식으로 데이터 암호화
     * @param data 평문 데이터
     * @param base64MasterKey Base64 인코딩된 AES 키
     * @return Base64 인코딩된 (IV + 암호문)
     * @throws Exception
     */
    public static String encryptAESGCM(String data, String base64MasterKey) throws Exception {
        // Base64로 인코딩된 키를 디코딩해 SecretKey 생성
        byte[] keyBytes = Base64.getDecoder().decode(base64MasterKey);
        SecretKey secretKey = new SecretKeySpec(keyBytes, AES);

        // 보안 랜덤으로 12바이트 IV 생성
        byte[] iv = new byte[IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        // AES/GCM/NoPadding 모드의 Cipher 초기화
        Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

        // 평문 암호화
        byte[] ciphertext = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // IV와 암호문을 하나로 합쳐서 전송 및 저장 (IV는 복호화에 필요)
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + ciphertext.length);
        byteBuffer.put(iv);
        byteBuffer.put(ciphertext);

        // 최종 결과를 Base64 문자열로 인코딩해 반환
        return Base64.getUrlEncoder().withoutPadding().encodeToString(byteBuffer.array());
    }

    /**
     * AES-GCM 복호화
     * @param base64CipherText Base64 인코딩된 (IV + 암호문)
     * @param base64MasterKey Base64 인코딩된 AES 키
     * @return 복호화된 평문 문자열
     * @throws Exception
     */
    public static String decryptAESGCM(String base64CipherText, String base64MasterKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64MasterKey);
        SecretKey secretKey = new SecretKeySpec(keyBytes, AES);

        byte[] cipherMessage = Base64.getUrlDecoder().decode(base64CipherText);

        // IV와 암호문 분리
        ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);
        byte[] iv = new byte[IV_LENGTH];
        byteBuffer.get(iv);

        byte[] ciphertext = new byte[byteBuffer.remaining()];
        byteBuffer.get(ciphertext);

        Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

        byte[] plainTextBytes = cipher.doFinal(ciphertext);

        return new String(plainTextBytes, StandardCharsets.UTF_8);
    }

    // 랜덤한 16바이트 AES 키 생성 후 Base64로 반환
    public static String generateRandomBase64AESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        keyGen.init(128); // 128-bit
        SecretKey key = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
