package lof.specification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

/**
 * @author Lauro Oliveira Freitas
 * @user lauroof
 * 2 de jun de 2020 
 * @param <T>
 */
public class SpecificationsBuilder<T> {
     
    private final List<SearchCriteria> params;
    public SpecificationsBuilder() {
        params = new ArrayList<>();   
    }
 
    public SpecificationsBuilder< T > with(String key, String operation, Serializable value) {
    	return with(new SearchCriteria(key, operation, value));
    }
    
    
    
    public SpecificationsBuilder< T > with(SearchCriteria spec) {
        params.add(spec);
        return this;
    }
    
 
    public Specification<T> build() {
        if (params.isEmpty()) {
            return null;
        }
 
        List<Specification <T> > specs = params.stream()
          .map(GenericSpecification< T >::new)
          .collect(Collectors.toList());
        
        Specification< T > result = specs.get(0);
 
        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate() ? Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }       
        return result;
    }

	@Override
	public String toString() {
		return params == null ? "" : params.toString();
	}
    
    
    
    
}