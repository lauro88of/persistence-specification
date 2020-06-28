# persistence-specification
spring data specification join example.
This project is an implementation of Specificication and JPA in Spring Data.

## Demo Project

https://github.com/lauro88of/demo-persistence-specification
Clone:
https://github.com/lauro88of/demo-persistence-specification.git

## Uses
Add in pom:
<dependency>
   <groupId>lof</groupId>
  <artifactId>persistence-specification</artifactId>
  <version>1.0.0</version>
</dependency>

Repositorys extends JpaSpecificationExecutor, package org.springframework.data.jpa.repository.JpaSpecificationExecutor;, 
Example:
```java
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

}
```
Implementation Service extends SpecificationService, package lof.specification.
Interface Service extends SpecificationGenericService, package lof.specification.
Example:
```java
public interface ProductService extends SpecificationGenericService<Product> {

}
```
And:
```java
public class ProductServiceImpl extends SpecificationService<Product> implements ProductService {

	@Autowired ProductRepository productRepository;

	@Override
	public JpaSpecificationExecutor<Product> getJpaSpecificationExecutor() {
		return productRepository;
	}
	
	
	public Product findOneExample() {
		SpecificationsBuilder<Product> builder = new SpecificationsBuilder<>();
		builder.with("code", OperationsCriteria.EQ, 2020L);
		return findOneBySpecification(builder.build()).orElse(null);
	}
	public List<Product> findListExample() {
		SpecificationsBuilder<Product> builder = new SpecificationsBuilder<>();
		builder.with("category.name", OperationsCriteria.IN, new String[] {"Mobile", "Clouthes"});
		return findBySpecification(builder.build());
	}
}
```

In Code uses SpecificationBuilder for construct searchCriteria.
Example:
```java
SpecificationsBuilder<Product> builder = new SpecificationsBuilder<>();
		builder.with("category.name", OperationsCriteria.IN, new String[] {"Mobile", "Clouthes"});
		return findBySpecification(builder.build());
```
  
