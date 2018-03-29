package edu.tamu.app.model;

import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.FetchMode.SELECT;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class FeatureProposal extends AbstractIdea {

    @OneToMany(fetch = EAGER, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE })
    @JoinTable(uniqueConstraints = @UniqueConstraint(columnNames = { "feature_proposal_id", "ideas_id" }))
    @Fetch(value = SELECT)
    private List<Idea> ideas;

    @OneToMany(fetch = EAGER, cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE })
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, scope = User.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinTable(uniqueConstraints = @UniqueConstraint(columnNames = { "feature_proposal_id", "voters_id" }))
    @Fetch(value = SELECT)
    private List<User> voters;

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
        this.ideas = new ArrayList<Idea>();
        this.voters = new ArrayList<User>();
    }

    public List<Idea> getIdeas() {
        return ideas;
    }

    public void setIdeas(List<Idea> ideas) {
        this.ideas.forEach(idea -> {
            removeVoter(idea.getAuthor());
        });
        this.ideas = ideas;
        this.ideas.forEach(idea -> {
            addVoter(idea.getAuthor());
        });
    }

    public void addIdea(Idea idea) {
        this.ideas.add(idea);
        addVoter(idea.getAuthor());
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
        this.voters.add(voter);
    }

    public void removeVoter(User voter) {
        this.voters.remove(voter);
    }

    public int getVotes() {
        return this.voters.size();
    }

}
