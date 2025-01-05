package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FollowId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer followeeId;
    private Integer followerId;

    // Construtor padrão
    public FollowId() {}

    // Construtor com parâmetros
    public FollowId(Integer followeeId, Integer followerId) {
        this.followeeId = followeeId;
        this.followerId = followerId;
    }

    // Getters e Setters
    public Integer getFolloweeId() {
        return followeeId;
    }

    public void setFolloweeId(Integer followeeId) {
        this.followeeId = followeeId;
    }

    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
    }

    // Métodos equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowId followId = (FollowId) o;
        return Objects.equals(followeeId, followId.followeeId) &&
                Objects.equals(followerId, followId.followerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followeeId, followerId);
    }
}
