package lof.specification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationGenericService<T> {

	List<T> findBySpecification(String search);

	List<T> findBySpecification(List<SearchCriteria> specs);

	List<T> findBySpecification(Specification<T> spec);

	Optional<T> findOneBySpecification(Specification<T> spec);

}