package com.practice.boardpractice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true) // AuditingFields 의 데이터 출력도 toString으로 찍겠다고 함.
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
//@EntityListeners(AuditingEntityListener.class) // Auditing 설정 추가. Auditing Class로 빼고 extends 함.
@Entity
public class Article extends AuditingFields { // AuditingFields 클래스로 공통 부분을 뽑고 상속해서 쓴다.

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL은 Identity방식으로 생성. <-> 오라클은 Sequel.
    private Long id;

    @Setter
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false)
    private UserAccount userAccount; // 유저 정보 (ID)

    @Setter @Column(nullable = false) private String title;
    @Setter @Column(nullable = false, length = 10000) private String content;

    @ToString.Exclude
    @JoinTable( // @JoinTable : Master 테이블에 붙임. Sub 테이블에는 @ManyToMany를 붙여줌.
            name = "article_hashtag",
            joinColumns = @JoinColumn(name = "articleId"),
            inverseJoinColumns = @JoinColumn(name = "hashtagId")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // PERSIST는 insert, MERGE는 update의 경우. 동기화 하겠다는 것.
    private Set<Hashtag> hashtags = new LinkedHashSet<>();

    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @ToString.Exclude // Article과 ArticleComments 간의 순환참조 발생하기 때문에 ToString에서 제외.
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    private Article(UserAccount userAccount, String title, String content) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
    }

    protected Article() {
    }

    // new 키워드 없이, 생성하기 편하게 하기 위해 팩토리 패턴으로 of 메서드 추가.
    public static Article of(UserAccount userAccount, String title, String content) {
        return new Article(userAccount, title, content);
    }

    public void addHashtag(Hashtag hashtag) {
        this.getHashtags().add(hashtag);
    }

    public void addHashtags(Collection<Hashtag> hashtags) {
        this.getHashtags().addAll(hashtags);
    }

    public void clearHashtags() {
        this.getHashtags().clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article that)) return false;

        // JPA에서 지연로딩을 했을 경우, id 필드가 null 일 경우가 있다.
        // 이런경우를 대비해서 getter로 반환하도록 수정한다.
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        // JPA에서 지연로딩을 했을 경우, id 필드가 null 일 경우가 있다.
        // 이런경우를 대비해서 getter로 반환하도록 수정한다.
        return Objects.hash(this.getId());
    }
}
