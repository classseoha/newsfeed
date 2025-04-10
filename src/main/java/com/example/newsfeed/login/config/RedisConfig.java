package com.example.newsfeed.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// Redis란? 메모리 기반의 초고속 키-값(데이터) 저장소 >> DB처럼 데이터를 저장하지만, 하드 대신 메모리(RAM)을 써서 빠름, 다양한 자료구조(문자열, 리스트, 해시, 셋, 정렬된 셋 등) 지원
// 주로 로그인 인증, 캐시 저장, 세션 관리, 실시간 처리(채팅, 게임 랭킹, 좋아요 수 집계 등) 빠른 처리가 필요할 때 사용
// RedisTemplate 을 주입받아, redisTemplate.opsForValue().set(...) 등 쉽게 Redis 사용 가능하게 하는 클래스 구현
@Configuration
public class RedisConfig {

    // RedisConnectionFactory 라는 Redis 서버와 연결을 만들어주는 팩토리 객체 생성
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        return new LettuceConnectionFactory(); // 기본 설정으로 localhost:6379 연결
    }

    // Redis에서 Key-Value 형태로 데이터를 저장하거나 읽어오는 도구
    @Bean
    @Primary
    public RedisTemplate<String, String> redisTemplate() {

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory()); // setConnectionFactory(...) 로 위에서 만든 Redis 연결 팩토리를 연결

        // setKeySerializer(...), setValueSerializer(...) 로 Key와 Value를 문자열로 직렬화해서 저장하도록 설정
        // Redis는 내부적으로 바이트 형태로 데이터를 저장하기 때문에, Java 객체를 저장하려면 직렬화 과정 필요
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }
}