package ru.yandex.practicum.catsgram.post;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.yandex.practicum.catsgram.Constants;
import ru.yandex.practicum.catsgram.like.Like;
import ru.yandex.practicum.catsgram.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@DynamicUpdate
@Table(name = "posts", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    User user;

    @Column(nullable = false)
    LocalDateTime createdOn;

    @Column
    LocalDateTime updatedOn;

    @Column(nullable = false, length = Constants.MAX_LENGTH_POST_DESCRIPTION)
    String description;

    @Column(nullable = false, length = Constants.MAX_LENGTH_PHOTO_URL)
    String photoUrl;

    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    Set<Like> likes;

}
