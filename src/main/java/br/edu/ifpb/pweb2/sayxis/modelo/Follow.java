import jakarta.persistence.*;


@Entity
@Table(name = "follow")
public class Follow {

    @EmbeddedId
    private FollowId id; // Chave composta

    @ManyToOne
    @MapsId("followeeId")
    @JoinColumn(name = "followee_id")
    private Photographer followee;

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    private Photographer follower;

    // Construtor padrão
    public Follow() {}

    // Construtor com parâmetros
    public Follow(FollowId id, Photographer followee, Photographer follower) {
        this.id = id;
        this.followee = followee;
        this.follower = follower;
    }

    // Getters e Setters
    public FollowId getId() {
        return id;
    }

    public void setId(FollowId id) {
        this.id = id;
    }

    public Photographer getFollowee() {
        return followee;
    }

    public void setFollowee(Photographer followee) {
        this.followee = followee;
    }

    public Photographer getFollower() {
        return follower;
    }

    public void setFollower(Photographer follower) {
        this.follower = follower;
    }
}
