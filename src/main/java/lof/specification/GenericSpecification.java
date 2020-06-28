package lof.specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lof.specification.Utils.ConstantsSpecification;
import lof.specification.Utils.SpecificationHelper;

/**
 * @author Lauro Oliveira Freitas
 * @user lauroof
 * 2 de jun de 2020 
 * @param <T>
 */
@Service
public class GenericSpecification<T> implements Specification<T> {

	private static final long serialVersionUID = 8110311496894319719L;

	private SearchCriteria criteria;
	
	public GenericSpecification() {
		super();
	}

	public GenericSpecification(SearchCriteria criteria) {
		super();
		this.criteria = criteria;
	}
	
	private void addDelimiter() {
		if (!criteria.getValue().toString().contains(ConstantsSpecification.TIME_DELIMITER)) {
			criteria.setValue(criteria.getValue().toString() + ConstantsSpecification.TIME_DELIMITER + "00:00:00");
		}
	}

	private void removeDelimiter() {
		if (criteria.getValue().toString().contains(ConstantsSpecification.TIME_DELIMITER)) {
			criteria.setValue(criteria.getValue().toString().split(ConstantsSpecification.TIME_DELIMITER)[0]);
		}
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Object attribute = new SpecificationHelper<String, T>().get(root, criteria.getKey()).getJavaType();
		switch (criteria.getOperation()) {
			case OperationsCriteria.GT:
				return greaterThanOrEqualTo(root, builder, attribute);
			case OperationsCriteria.LK:
				return likeTo(root, builder, attribute);
			case OperationsCriteria.LT:
				return lessThanOrEqualTo(root, builder, attribute);
			case OperationsCriteria.EQ:
				return equalTo(root, builder, attribute);
			case OperationsCriteria.IN:
				return inTo(root, builder, attribute);
			case OperationsCriteria.ORDER:
				return orderTo(root, builder, attribute, query);
			default:
				return null;
		}
	}



	private Predicate orderTo(Root<T> root, CriteriaBuilder builder, Object attribute,  CriteriaQuery<?> query) {
		if("desc".equalsIgnoreCase(criteria.getValue().toString())) {
			query.orderBy(builder.desc(new SpecificationHelper<String, T>().get(root, criteria.getKey())));			
		}
		
		if("asc".equalsIgnoreCase(criteria.getValue().toString())) {
			query.orderBy(builder.asc(new SpecificationHelper<String, T>().get(root, criteria.getKey())));			
		}

		return null;
	}

	private Predicate likeTo(Root<T> root, CriteriaBuilder builder, Object attribute) {
		if (attribute == String.class) {
			return builder.like(builder.lower(new SpecificationHelper<String, T>().get(root, criteria.getKey())),
					"%" + criteria.getValue().toString().toLowerCase() + "%");
		} else {
			return builder.equal(new SpecificationHelper<Object, T>().get(root, criteria.getKey()),
					criteria.getValue());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Predicate equalTo(Root<T> root, CriteriaBuilder builder, Object attribute) {
		if (attribute == String.class) {
			return builder.like(builder.lower(new SpecificationHelper<String, T>().get(root, criteria.getKey())),
					criteria.getValue().toString().toLowerCase());
		}
		if (attribute == Boolean.class) {
			if(criteria.getValue() instanceof String ) {
				return builder.equal(new SpecificationHelper<Object, T>().get(root, criteria.getKey()),	"true".equalsIgnoreCase(criteria.getValue().toString()));
			} 
			return builder.equal(new SpecificationHelper<Object, T>().get(root, criteria.getKey()),	criteria.getValue());
		}
		if (new SpecificationHelper<String, T>().get(root, criteria.getKey()).getJavaType().isEnum()) {
			Class clazz = new SpecificationHelper<String, T>().get(root, criteria.getKey()).getJavaType();
			return builder.equal(new SpecificationHelper<Enum<?>, T>().get(root, criteria.getKey()), Enum.valueOf(clazz, criteria.getValue().toString()));
		}
		
		else {
			return builder.equal(new SpecificationHelper<Object, T>().get(root, criteria.getKey()),	criteria.getValue());
		}
	}

	private Predicate lessThanOrEqualTo(Root<T> root, CriteriaBuilder builder, Object attribute) {
		if (attribute == LocalDate.class) {
			removeDelimiter();
			return builder.lessThanOrEqualTo(new SpecificationHelper<LocalDate, T>().get(root, criteria.getKey()),
					LocalDate.parse(criteria.getValue().toString()));
		} else if (attribute == LocalDateTime.class) {
			addDelimiter();
			return builder.lessThanOrEqualTo(
					new SpecificationHelper<LocalDateTime, T>().get(root, criteria.getKey()),
					LocalDateTime.parse(criteria.getValue().toString()));
		} else if (attribute == Date.class) {
			return builder.lessThanOrEqualTo(new SpecificationHelper<Date, T>().get(root, criteria.getKey()),
					(Date) criteria.getValue());
		} else {
			return builder.lessThanOrEqualTo(new SpecificationHelper<String, T>().get(root, criteria.getKey()),
					criteria.getValue().toString());
		}
	}

	private Predicate greaterThanOrEqualTo(Root<T> root, CriteriaBuilder builder, Object attribute) {
		if (attribute == LocalDate.class) {
			removeDelimiter();
			return builder.greaterThanOrEqualTo(new SpecificationHelper<LocalDate, T>().get(root, criteria.getKey()),
					LocalDate.parse(criteria.getValue().toString()));
		} else if (attribute == LocalDateTime.class) {
			addDelimiter();
			return builder.greaterThanOrEqualTo(
					new SpecificationHelper<LocalDateTime, T>().get(root, criteria.getKey()),
					LocalDateTime.parse(criteria.getValue().toString()));
		} else if (attribute == Date.class) {
			return builder.greaterThanOrEqualTo(new SpecificationHelper<Date, T>().get(root, criteria.getKey()),
					(Date) criteria.getValue());
		} else {
			return builder.greaterThanOrEqualTo(new SpecificationHelper<String, T>().get(root, criteria.getKey()),
					criteria.getValue().toString());
		}
	}
	
	private Predicate inTo(Root<T> root, CriteriaBuilder builder, Object attribute) {
		
		if (attribute == String[].class) {
			In<String> inClause = builder.in(new SpecificationHelper<String, T>().get(root, criteria.getKey()));
			String[] values = (String[])  criteria.getValue();
			for(String value : values) {
				inClause.value(value);
			}
			return inClause;
		}
		if (new SpecificationHelper<String, T>().get(root, criteria.getKey()).getJavaType().isEnum()) {
			Class clazz = new SpecificationHelper<String, T>().get(root, criteria.getKey()).getJavaType();
			In<Enum<?>> inClause = builder.in(new SpecificationHelper<Enum<?>, T>().get(root, criteria.getKey()));
			Enum[] values = (Enum[]) criteria.getValue();
			for(Enum<?> value : values) {
				inClause.value(Enum.valueOf(clazz, value.toString()));
			}
			return inClause;
		} else {
			In<Object> inClause = builder.in(new SpecificationHelper<Object, T>().get(root, criteria.getKey()));
			Object[] values = (Object[])  criteria.getValue();
			for(Object value : values) {
				inClause.value(value);
			}
			return inClause;
		}
	}

}
