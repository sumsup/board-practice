package com.practice.boardpractice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true) // AuditingFields 의 데이터 출력도 toString으로 찍겠다고 함.
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
//@EntityListeners(AuditingEntityListener.class) // Auditing 설정 추가. Auditing Class로 뺀다.
@Entity
public class Article extends AuditingFields { // AuditingFields 클래스로 공통 부분을 뽑고 상속해서 쓴다.

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL은 Identity방식으로 생성. <-> 오라클은 Sequel.
    private Long id;

    @Setter @ManyToOne(optional = false) @JoinColumn(name = "userId") private UserAccount userAccount; // 유저 정보 (ID)

    @Setter @Column(nullable = false) private String title;
    @Setter @Column(nullable = false, length = 10000) private String content;

    @Setter private String hashtag;

    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @ToString.Exclude // Article과 ArticleComments 간의 순환참조 발생하기 때문에 ToString에서 제외.
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    private Article(UserAccount userAccount, String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    protected Article() {
    }

    // new 키워드 없이, 생성하기 편하게 하기 위해 팩토리 패턴으로 of 메서드 추가.
    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return new Article(userAccount, title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
