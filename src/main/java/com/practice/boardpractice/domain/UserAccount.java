package com.practice.boardpractice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends AuditingFields {
    @Id @Column(length = 50) private String userId;
    @Setter @Column(nullable = false) private String userPassword;

    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickname;
    @Setter private String memo;


    protected UserAccount() {}

    private UserAccount(String userId, String userPassword, String email, String nickname, String memo) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
    }

    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo) {
        return new UserAccount(userId, userPassword, email, nickname, memo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;

        // JPA에서 지연로딩을 했을 경우, id 필드가 null 일 경우가 있다.
        // 이런경우를 대비해서 id값을 getter로 조회하도록 수정한다.
        return this.getUserId() != null && this.getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        // JPA에서 지연로딩을 했을 경우, id 필드가 null 일 경우가 있다.
        // 이런경우를 대비해서 id값을 getter로 조회하도록 수정한다.
        return Objects.hash(this.getUserId());
    }

}
