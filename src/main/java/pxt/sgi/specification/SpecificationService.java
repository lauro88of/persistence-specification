package pxt.sgi.specification;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Lauro Oliveira Freitas
 * @user lauroof
 * 2 de jun de 2020 
 * @param <T>
 */
public abstract class SpecificationService<T> {

	private final Log logger = LogFactory.getLog(SpecificationService.class);
	
	public SpecificationService() {
		super();
	}
	
	public List<T> findBySpecification(String search) {
	    List<SearchCriteria> listaCriteria = searchForSearchCriteriaList(search);
	    logger.debug("findBySpecification : Params: String search");
	    logger.debug(search);
	    return this.findBySpecification(listaCriteria);
	}

	public List<SearchCriteria> searchForSearchCriteriaList(String search) {
		return SpecificationHelper.stringForSearchCriteria(search);
	}

	public List<T> findBySpecification(List<SearchCriteria> specs) {
		SpecificationsBuilder<T> builder = new SpecificationsBuilder<>();
		for(SearchCriteria spec : specs) {
			builder.with(spec);
		}
		logger.debug("findBySpecification : Params: List<SearchCriteria> specs");
		 logger.debug(builder);
	    return getJpaSpecificationExecutor().findAll(Specification.where(builder.build()));
	}

	public List<T> findBySpecification(Specification< T> spec) {
		logger.debug("findBySpecification : Params: Specification< T> spec");
		logger.debug(spec);
		return getJpaSpecificationExecutor().findAll(Specification.where(spec));
	}

	public Optional<T> findOneBySpecification(Specification< T> spec) {
		logger.debug("findOneBySpecification");
		logger.debug(spec);
		return  getJpaSpecificationExecutor().findOne(Specification.where(spec));
	}

	public abstract JpaSpecificationExecutor<T> getJpaSpecificationExecutor();
}