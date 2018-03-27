package edu.tamu.app.model.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import edu.tamu.app.model.FeatureProposal;
import edu.tamu.app.model.repo.custom.FeatureProposalRepoCustom;

public interface FeatureProposalRepo extends JpaRepository<FeatureProposal, Long>, FeatureProposalRepoCustom, JpaSpecificationExecutor<FeatureProposal> {

    public Page<FeatureProposal> findAll(Specification<FeatureProposal> specification, Pageable pageable);

    public List<FeatureProposal> findAllByServiceId(Long id);

    public void delete(FeatureProposal idea);

}
