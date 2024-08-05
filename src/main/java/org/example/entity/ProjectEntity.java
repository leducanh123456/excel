package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;


import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ProjectEntity {
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
    private String projectName;

    @Column
    private BigDecimal budget;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @Column
    private Integer total;

    @Column
    private String code;

    @Column
    private String sapCode;

    @OneToMany(mappedBy = "projectEntity", fetch = FetchType.LAZY)
    private List<PolicyEntity> policyEntities;

    @OneToMany(mappedBy = "projectEntity", fetch = FetchType.LAZY)
    private List<ApprovalEntity> approvalEntities;

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
