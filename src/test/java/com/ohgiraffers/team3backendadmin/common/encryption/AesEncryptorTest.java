package com.ohgiraffers.team3backendadmin.common.encryption;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class AesEncryptorTest {

    private AesEncryptor aesEncryptor;

    @BeforeEach
    void setUp() {
        // 테스트용 Base64 인코딩된 256비트 키
        byte[] keyBytes = new byte[32];
        for (int i = 0; i < 32; i++) {
            keyBytes[i] = (byte) i;
        }
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        aesEncryptor = new AesEncryptor(base64Key);
    }

    @Nested
    @DisplayName("encrypt 메서드")
    class Encrypt {

        @Test
        @DisplayName("평문을 암호화하면 null이 아닌 Base64 문자열을 반환한다")
        void encryptReturnsNonNullBase64String() {
            // when
            String encrypted = aesEncryptor.encrypt("test@company.com");

            // then
            assertNotNull(encrypted);
            assertDoesNotThrow(() -> Base64.getDecoder().decode(encrypted));
        }

        @Test
        @DisplayName("null 입력 시 null을 반환한다")
        void encryptNullReturnsNull() {
            // when & then
            assertNull(aesEncryptor.encrypt(null));
        }

        @Test
        @DisplayName("같은 평문을 두 번 암호화하면 동일한 결과를 반환한다 (결정론적)")
        void encryptIsDeterministic() {
            // when
            String first = aesEncryptor.encrypt("hello@test.com");
            String second = aesEncryptor.encrypt("hello@test.com");

            // then
            assertEquals(first, second);
        }

        @Test
        @DisplayName("서로 다른 평문은 서로 다른 암호문을 생성한다")
        void differentInputsProduceDifferentCiphertexts() {
            // when
            String encrypted1 = aesEncryptor.encrypt("user1@company.com");
            String encrypted2 = aesEncryptor.encrypt("user2@company.com");

            // then
            assertNotEquals(encrypted1, encrypted2);
        }
    }

    @Nested
    @DisplayName("decrypt 메서드")
    class Decrypt {

        @Test
        @DisplayName("암호화 후 복호화하면 원본 평문과 일치한다")
        void decryptReturnsOriginalPlainText() {
            // given
            String original = "test@company.com";
            String encrypted = aesEncryptor.encrypt(original);

            // when
            String decrypted = aesEncryptor.decrypt(encrypted);

            // then
            assertEquals(original, decrypted);
        }

        @Test
        @DisplayName("null 입력 시 null을 반환한다")
        void decryptNullReturnsNull() {
            // when & then
            assertNull(aesEncryptor.decrypt(null));
        }

        @Test
        @DisplayName("다양한 한글 포함 문자열을 암호화/복호화 라운드트립 테스트")
        void roundTripWithKoreanText() {
            // given
            String original = "서울시 강남구 테헤란로 123";

            // when
            String encrypted = aesEncryptor.encrypt(original);
            String decrypted = aesEncryptor.decrypt(encrypted);

            // then
            assertEquals(original, decrypted);
        }
    }
}
