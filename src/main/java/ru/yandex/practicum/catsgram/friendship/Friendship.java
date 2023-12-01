package ru.yandex.practicum.catsgram.friendship;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(schema = "public", name = "friendship")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @JoinColumn(name = "user_id")
    Long userId;
    @JoinColumn(name = "friend_id")
    Long friendId;
    @Column(name = "confirmed")
    boolean confirmed;
}
