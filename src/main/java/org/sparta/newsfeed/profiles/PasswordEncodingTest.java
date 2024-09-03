package org.sparta.newsfeed.profiles;

import org.sparta.newsfeed.profiles.config.PasswordEncoder;

public class PasswordEncodingTest {
    public static void main(String[] args) {
        // PasswordEncoder 객체 생성
        PasswordEncoder encoder = new PasswordEncoder();

        // 테스트할 비밀번호
        String rawPassword = "Password123!";

        // 비밀번호 인코딩
        String encodedPassword = encoder.encode(rawPassword);

        // 인코딩된 비밀번호 출력
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
    }
}
