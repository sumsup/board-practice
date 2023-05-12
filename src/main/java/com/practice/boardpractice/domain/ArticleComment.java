package com.practice.boardpractice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
//@EntityListeners(AuditingEntityListener.class) // Auditing 설정 추가. Auditing Class로 뺀다.
@Entity
public class ArticleComment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL은 Identity방식으로 생성. <-> 오라클은 Sequel.
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    private Article article;

    @Setter
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false)
    private UserAccount userAccount; // 유저 정보 (ID)

    @Setter
    @Column(updatable = false)
    private Long parentCommentId; // 부모 댓글 ID.

    @ToString.Exclude // 스스로를 ToString 하면 ToString이 재귀적으로 무한으로 생성되므로 오버 플로우 발생. Exclude 해준다.
    @OrderBy("createdAt ASC")
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL) // 부모 댓글을 삭제하면, 자식 댓글도 삭제 되도록.
    private Set<ArticleComment> childComments = new LinkedHashSet<>(); // 순서가 있으니 LinkedHashSet 으로.

    @Setter
    @Column(nullable = false, length = 500)
    private String content;

    protected ArticleComment() {
    }

    private ArticleComment(Article article, UserAccount userAccount, Long parentCommentId, String content) {
        this.article = article;
        this.userAccount = userAccount;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public static ArticleComment of(Article article, UserAccount userAccount, String content) {
        return new ArticleComment(article, userAccount, null, content);
    }

    public void addChildComment(ArticleComment child) {
        child.setParentCommentId(this.getId());
        this.getChildComments().add(child);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
