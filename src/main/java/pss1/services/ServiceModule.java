
package pss1.services;

import java.io.File;
import java.util.Collections;

import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.h2.jdbcx.JdbcConnectionPool;

import com.mysema.query.sql.H2Templates;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.rdfbean.model.FetchStrategy;
import com.mysema.rdfbean.model.FileIdSequence;
import com.mysema.rdfbean.model.IdSequence;
import com.mysema.rdfbean.model.PredicateWildcardFetch;
import com.mysema.rdfbean.model.Repository;
import com.mysema.rdfbean.object.Configuration;
import com.mysema.rdfbean.object.ConfigurationBuilder;
import com.mysema.rdfbean.rdb.RDBRepository;
import com.mysema.rdfbean.tapestry.TransactionalAdvisor;

import pss1.domain.User;
import pss1.domain.Identifiable;

/**
 * ServiceModule provides service bindings and RDFBean configuration elements
 *
 */
public final class ServiceModule {

    @Match({ "UseDAO" })
    public static void adviseTransactions(TransactionalAdvisor advisor,
            MethodAdviceReceiver receiver) {
        advisor.addTransactionCommitAdvice(receiver);
    }
    
    public static void bind(ServiceBinder binder) {
        binder.bind(UserDAO.class, UserDAOImpl.class);
    }

    public static Configuration buildConfiguration() {
        ConfigurationBuilder builder = new ConfigurationBuilder();        
        builder.addClass(Identifiable.class).addId("id").addProperties();
        builder.addClass(User.class).addProperties();
        builder.setFetchStrategies(Collections.<FetchStrategy> singletonList(new PredicateWildcardFetch()));
        return builder.build();
    }

    public static Repository buildRepository(Configuration configuration,
            RegistryShutdownHub hub) {
        JdbcConnectionPool dataSource = JdbcConnectionPool.create("jdbc:h2:target/data/h2", "sa", "");
        dataSource.setMaxConnections(30);
        IdSequence idSequence = new FileIdSequence(new File("target/data", "ids"));
        SQLTemplates templates = new H2Templates();
        final RDBRepository repository = new RDBRepository(configuration, dataSource, templates, idSequence);
        hub.addRegistryShutdownListener(new RegistryShutdownListener() {
            @Override
            public void registryDidShutdown() {
                repository.close();
            }
        });
        return repository;
    }

    private ServiceModule() {
        
    }
}