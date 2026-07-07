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