package edu.tamu.app.model;

import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.FetchMode.SELECT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import edu.tamu.app.enums.FeatureProposalState;
import edu.tamu.app.model.validation.FeatureProposalValidator;

@Entity
@JsonIgnoreProperties(value = { "voters" }, allowGetters = true)
public class FeatureProposal extends AbstractIdea {

    @OneToMany(fetch = EAGER, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE }, mappedBy = "featureProposal")
    @Fetch(value = SELECT)
    private List<Idea> ideas;

    @ManyToMany(fetch = EAGER, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE })
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, scope = User.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinTable(uniqueConstraints = @UniqueConstraint(columnNames = { "feature_proposal_id", "voters_id" }))
    @Fetch(value = SELECT)
    private List<User> voters;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeatureProposalState state;

    @Column(nullable = false)
    private Boolean isPublic;

    public FeatureProposal() {
        super();
        this.modelValidator = new FeatureProposalValidator();
        setup();
    }

    public FeatureProposal(String title, String description) {
        super(title, description);
        setup();
    }

    public FeatureProposal(String title, String description, User author) {
        super(title, description, author);
        setup();
    }

    public FeatureProposal(String title, String description, User author, Service service) {
        super(title, description, author, service);
        setup();
    }

    public FeatureProposal(Idea idea) {
        super(idea.getTitle(), idea.getDescription(), idea.getAuthor(), idea.getService());
        setup();
        addIdea(idea);
    }

    private void setup() {
        this.ideas = new ArrayList<Idea>();
        this.voters = new ArrayList<User>();
        this.state = FeatureProposalState.IN_PROGRESS;
        this.isPublic = true;
    }

    public List<Idea> getIdeas() {
        return ideas;
    }

    public void setIdeas(List<Idea> ideas) {
        this.ideas.forEach(idea -> {
            removeVoter(idea.getAuthor());
        });
        HashSet<Idea> ideaSet = new HashSet<Idea>();
        ideaSet.addAll(ideas);
        this.ideas.clear();
        this.ideas.addAll(ideaSet);
        this.ideas.forEach(idea -> {
            addVoter(idea.getAuthor());
        });
    }

    public void addIdea(Idea idea) {
        if (!this.ideas.contains(idea)) {
            this.ideas.add(idea);
            addVoter(idea.getAuthor());
        }
    }

    public void removeIdea(Idea idea) {
        this.ideas.remove(idea);
        removeVoter(idea.getAuthor());
    }

    public List<User> getVoters() {
        return voters;
    }

    public void setVoters(List<User> voters) {
        this.voters = voters;
    }

    public void addVoter(User voter) {
        if (!this.voters.contains(voter)) {
            this.voters.add(voter);
        }
    }

    public void removeVoter(User voter) {
        this.voters.remove(voter);
    }

    public int getVotes() {
        return this.voters.size();
    }

    public FeatureProposalState getState() {
        return state;
    }

    public void setState(FeatureProposalState state) {
        this.state = state;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

}
