package com.beyond.basic.b2_board.common;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

// 기본적으로는 Entity는 상속이 불가능한 구조. MappedSuperclass 어노테이션 사용시 상속관계 가능.
@MappedSuperclass
//선후관계 문제가 있어서 상속이 먼저라서, 여기에 getter 붙여줘야함
@Getter
public class BaseTimeEntity {
    //    creationTimeStamp : 날짜값 자동 세팅해주는 어노테이션
    @CreationTimestamp
    private LocalDateTime createdTime;
    //    업데이트할때마다 수정시간 추가
    @UpdateTimestamp
    private LocalDateTime updatedTime;
}
