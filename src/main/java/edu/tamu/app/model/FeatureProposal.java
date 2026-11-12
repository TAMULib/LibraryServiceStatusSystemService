package edu.tamu.app.model;

import static jakarta.persistence.FetchType.EAGER;
import static org.hibernate.annotations.FetchMode.SELECT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;

import edu.tamu.app.enums.FeatureProposalState;
import edu.tamu.app.model.validation.FeatureProposalValidator;

@Entity
public class FeatureProposal extends AbstractIdea {

    @OneToMany(fetch = EAGER, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE }, mappedBy = "featureProposal")
    @Fetch(value = SELECT)
    private List<Idea> ideas;

    @ManyToMany(fetch = EAGER, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE })
    @JoinTable(uniqueConstraints = @UniqueConstraint(columnNames = { "feature_proposal_id", "voters_id" }))
    @Fetch(value = SELECT)
    private List<User> voters;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeatureProposalState state;

    @Column(nullable = false)
    private Boolean isPrivate;

    public FeatureProposal() {
        super();
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
        this.modelValidator = new FeatureProposalValidator();
        this.ideas = new ArrayList<Idea>();
        this.voters = new ArrayList<User>();
        this.state = FeatureProposalState.IN_PROGRESS;
        this.isPrivate = false;
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

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

}
