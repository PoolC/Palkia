package org.poolc.api.activity.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "ActivityTag")
@Getter
@Table(name = "ActivityTag",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"activityID", "content"})})
public class ActivityTag {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "activityID", referencedColumnName = "ID", nullable = false)
    private Activity activity;

    private String content;

    public ActivityTag(Activity activity, String content) {
        this.activity = activity;
        this.content = content;
    }

    protected ActivityTag() {

    }
}