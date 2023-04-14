package com.practice.boardpractice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true) // callSuper : 부모 클래스의 필드도 출력 대상으로.
@Table(indexes = {
        @Index(columnList = "hashtagName", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Hashtag extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude // ToString에서 제외처리. 그렇지 않으면 순환참조해서 ToString을 무한 출력함.
    @ManyToMany(mappedBy = "hashtags")
    private Set<Article> articles = new LinkedHashSet<>(); // 순서, 정렬된 상태를 이용하려면 LinkedHashSet이용. 아님 그냥 HashSet 괜찮.

    @Setter @Column(nullable = false) private String hashtagName;

    protected Hashtag() {}

    private Hashtag(String hashtagName) {
        this.hashtagName = hashtagName;
    }

    public static Hashtag of(String hashtagName) {
        return new Hashtag(hashtagName);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hashtag that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
