package edu.tamu.app.model.repo.custom;

import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.FeatureProposal;
import edu.tamu.app.model.Idea;
import edu.tamu.weaver.auth.model.Credentials;

public interface FeatureProposalRepoCustom {

    public FeatureProposal create(Idea idea);

    public FeatureProposal create(FeatureProposal featureProposal, Credentials credentials) throws UserNotFoundException;

    public FeatureProposal update(FeatureProposal featureProposal);

    public void delete(FeatureProposal featureProposal);

}
