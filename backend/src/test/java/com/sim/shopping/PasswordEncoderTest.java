package com.sim.shopping;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密/验证工具类
 *
 * 使用方法：
 *   1. 运行 mvn test -Dtest=PasswordEncoderTest -q   (控制台输出加密结果)
 *   2. 在 IDE 中直接运行 testEncode() 方法
 *   3. 修改 main 方法中的参数，运行 main 方法加密指定密码
 *
 * 注意：BCrypt 是单向加密，每次加密结果不同，但 verify 可验证匹配。
 */
public class PasswordEncoderTest {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("  BCrypt 密码加密工具");
        System.out.println("==============================\n");

        String password = args.length > 0 ? args[0] : "123456";
        encodeSingle(password);
    }

    @Test
    public void testDecode() {
        System.out.println();
        System.out.println("==============================");
        System.out.println("  BCrypt 密文解密工具（暴力匹配）");
        System.out.println("==============================\n");

        // ============================================
        // 在这里粘贴数据库中的 BCrypt 密文，即可尝试匹配
        // ============================================
        String[] dbHashes = {
            // 例："$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
        };

        // 常见密码字典（可自行扩展）
        String[] dictionary = {
            "123456", "12345678", "123456789", "1234567890",
            "password", "admin", "admin123", "admin888",
            "123123", "654321", "111111", "000000",
            "qwerty", "abc123", "test", "test123",
            "user123", "root", "root123", "pass",
            "888888", "666666", "112233", "123321",
            "user", "guest", "demo", "demo123",
            "zxcvbn", "asdfgh", "1q2w3e", "qwe123",
            "password123", "admin@123", "Aa123456",
            "p@ssw0rd", "iloveyou", "welcome",
            "monkey", "dragon", "master", "login",
            "11111111", "1234", "12345", "1234567",
        };

        for (String hash : dbHashes) {
            System.out.println("密文: " + hash);
            boolean found = false;
            for (String candidate : dictionary) {
                if (verify(candidate, hash)) {
                    System.out.println(">>> 匹配成功! 明文密码是: " + candidate);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println(">>> 未匹配到常见密码，请扩展字典或使用 verify() 方法手动验证");
            }
            System.out.println();
        }

        if (dbHashes.length == 0 || (dbHashes.length == 1 && dbHashes[0].startsWith("例"))) {
            System.out.println("提示：请在 testDecode() 方法中的 dbHashes 数组里填入数据库密文");
            System.out.println("然后运行: mvn test -Dtest=PasswordEncoderTest#testDecode -q");
        }
    }

    /**
     * 单个密文解密 — 通过命令行使用
     * 用法: mvn test -Dtest=PasswordEncoderTest#testDecodeSingle -q
     * 也可以直接在 main 中调用
     */
    public static String decodeSingle(String encodedPassword) {
        System.out.println("正在尝试匹配密文: " + encodedPassword);

        String[] dictionary = {
            "123456", "12345678", "123456789", "1234567890",
            "password", "admin", "admin123", "admin888",
            "123123", "654321", "111111", "000000",
            "qwerty", "abc123", "test", "test123",
            "user123", "root", "root123", "pass",
            "888888", "666666", "112233", "123321",
            "user", "guest", "demo", "demo123",
            "zxcvbn", "asdfgh", "1q2w3e", "qwe123",
            "password123", "admin@123", "Aa123456",
            "p@ssw0rd", "iloveyou", "welcome",
            "monkey", "dragon", "master", "login",
            "11111111", "1234", "12345", "1234567",
        };

        for (String candidate : dictionary) {
            if (verify(candidate, encodedPassword)) {
                System.out.println(">>> 匹配成功! 明文密码是: " + candidate);
                return candidate;
            }
        }
        System.out.println(">>> 未匹配到常见密码");
        return null;
    }

    @Test
    public void testEncode() {
        System.out.println();
        System.out.println("==============================");
        System.out.println("  BCrypt 密码加密工具");
        System.out.println("==============================\n");

        String[] passwords = {"123456", "admin123", "password"};

        for (String pwd : passwords) {
            String hash = encode(pwd);
            boolean match = verify(pwd, hash);
            System.out.println("明文: " + pwd);
            System.out.println("密文: " + hash);
            System.out.println("验证: " + (match ? "✓ 匹配" : "✗ 不匹配"));
            System.out.println();
        }

        System.out.println("======================");
        System.out.println("  SQL 可直接复制使用");
        System.out.println("======================\n");

        // 生成一个 123456 的哈希用于 SQL
        String hash123456 = encode("123456");
        System.out.println("-- 密码 123456 的 BCrypt 哈希:");
        System.out.println("'" + hash123456 + "'");
    }

    /** 加密明文密码 */
    public static String encode(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    /** 验证明文密码是否匹配密文 */
    public static boolean verify(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }

    private static void encodeSingle(String password) {
        String hash = encode(password);
        System.out.println("明文密码: " + password);
        System.out.println("BCrypt  : " + hash);
        System.out.println("验证结果: " + (verify(password, hash) ? "✓ 匹配" : "✗ 不匹配"));
        System.out.println("\n--- 复制到 SQL 中使用 ---");
        System.out.println("'" + hash + "'");
    }
}