package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
public class ApprovalEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "create_by", columnDefinition = "varchar(300)", length = 300)
    private String createBy;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "modify_by", columnDefinition = "varchar(300)", length = 300)
    private String modifyBy;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column
    private Integer total;

    @Column
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProjectEntity projectEntity;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifyDate = LocalDateTime.now();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
