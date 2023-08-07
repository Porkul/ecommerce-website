package com.katrinporkul.ecommerce.config;

import com.katrinporkul.ecommerce.entity.*;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// only applies to REST APIs provided by Spring Data REST.
// It does not apply to regular @RestControllers that we create manually.
// As a result, for your CheckoutController we need to add CORS support, hence the need for MyAppConfig.java.
@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {
    //    @Autowired
    //    public MyDataRestConfig(EntityManager theEntityManager) {
    //        entityManager = theEntityManager;
    //    }
    @Value("${allowed.origins}")
    private String[] allowedOrigins;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH};

        // disable HTTP methods: PUT, POST, DELETE and PATCH => Read only.
        disableHttpMethods(Product.class, config, theUnsupportedActions);
        disableHttpMethods(ProductCategory.class, config, theUnsupportedActions);
        disableHttpMethods(Country.class, config, theUnsupportedActions);
        disableHttpMethods(State.class, config, theUnsupportedActions);
        disableHttpMethods(Order.class, config, theUnsupportedActions);

        // call an internal helper method
        exposeIds(config);

        // configure cors mapping
        cors.addMapping(config.getBasePath() + "/**").allowedOrigins(allowedOrigins);
    }

    private static void disableHttpMethods(Class theClass, RepositoryRestConfiguration config,
                                           HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // expose entity ids for operations like search by category where we need id

        // - get a list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // - create an array of the entity types
        List<Class> entityClasses = new ArrayList<>();

        // - get the entity types for the entities
        for (EntityType tempEntityType : entities) {
            entityClasses.add(tempEntityType.getJavaType());
        }

        // - expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }

}



//    In simple terms, by default Spring Data REST does not expose the ids. Hence we need to add configuration code for exposing the IDs. Here's the basic process
//
//        1. Get a list of all entity classes from the entity manager
//
//        2. Put them in an array list, converting accordingly
//
//        3. Expose the entity id for the entities in hte array.