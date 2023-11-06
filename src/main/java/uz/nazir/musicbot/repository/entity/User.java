package uz.nazir.musicbot.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static uz.nazir.musicbot.repository.util.StandardTime.nowIso8601;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String name;

    @Column
    private String code;

    @Column
    private String search;

    @Column
    private Integer page;

    @Column(name = "joined_date")
    private LocalDateTime joinedDate;

    @Column(name = "last_used_date")
    private LocalDateTime lastUsedDate;

    @PrePersist
    @PreUpdate
    public void prePersistOrUpdate() {
        lastUsedDate = nowIso8601();
        if (joinedDate == null) {
            joinedDate = lastUsedDate;
        }
    }
}
