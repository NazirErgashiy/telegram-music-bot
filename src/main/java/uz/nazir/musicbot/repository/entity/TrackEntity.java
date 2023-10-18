package uz.nazir.musicbot.repository.entity;

import lombok.Data;
import uz.nazir.musicbot.repository.util.StandardTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tracks")
public class TrackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "path")
    private String path;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @PrePersist
    public void prePersist() {
        if (createDate == null) {
            createDate = StandardTime.nowIso8601();
        }
    }
}
