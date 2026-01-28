package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component  // 싱글톤 객체로 만들어야함.
@Transactional
public class PostScheduler {

    private final PostRepository postRepository;
    public PostScheduler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

//    fixedDelay를 통해 간단히 주기적인 작업 수행. 메서드마다 scheduled 어노테이션 있어야함.
//    @Scheduled(fixedDelay = 1000)  //1초마다 실행
//    public void simpleSchedoler() {
//        log.info("==== 스케줄러 시작  ==== ");
//        log.info("====스케줄러 로직 수행  =====");
//        log.info("==== 스케줄러 끝 ====");
//    }

//    cron을 통해 작업 수행 미세조정 가능
//    cron의 각 자리는 "초 분 시간 일 월 요일"의 의미
//    * * * * * * : 매월 매일 매시간 매분 매초마다의 의미 : 매 초 실행.
//    0 0 * * * * : 매월 매일 매시간 0분 0초에 의미 : 1시간에 1번 실행.
//    0 0 11 * * * : 매월 매일 11시 0분 0초에 의미 : 매일 11시에 실행.
//    0 0/1 * * * * : 매월 매일 매시간 1분 마다의 의미 : 1분에 1번 실행
    @Scheduled(cron = "0 0/1 * * * *")
    public void postSchedule() {
        log.info("==== 스케줄러 시작  ==== ");

//        post전체 중 Y인 건을 조회 후, 그 중 현재시간보다 이전인 데이터는 N으로 변경.
        List<Post> postList = postRepository.findAllByAppointment("Y");
        LocalDateTime now = LocalDateTime.now();

        for(Post p : postList) {
            if( p.getAppointmentTime().isBefore(now)) {
                p.updateAppointment("N");
            }
        }
        log.info("==== 스케줄러 끝 ====");
    }

}
