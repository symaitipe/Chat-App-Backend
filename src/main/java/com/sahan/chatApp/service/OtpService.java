package com.sahan.chatApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
public class OtpService {

    private static final int OTP_LENGTH = 6;
    private static final Duration OTP_TTL = Duration.ofMinutes(5);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public String createOtpForPhone(String phoneNumber) {
        String otp = generateOtp();
        String redisKey = getRedisKey(phoneNumber);
        redisTemplate.opsForValue().set(redisKey, otp, OTP_TTL);
        return otp;
    }

    public boolean verifyOtp(String phoneNumber, String otp) {
        String redisKey = getRedisKey(phoneNumber);
        String cachedOtp = redisTemplate.opsForValue().get(redisKey);
        if (cachedOtp != null && cachedOtp.equals(otp)) {
            redisTemplate.delete(redisKey); // OTP is one-time use
            return true;
        }
        return false;
    }

    private String getRedisKey(String phoneNumber) {
        return "otp:" + phoneNumber;
    }
}

