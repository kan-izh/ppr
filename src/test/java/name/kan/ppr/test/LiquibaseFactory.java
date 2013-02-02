package name.kan.ppr.test;

import liquibase.Liquibase;

/**
 * @author kan
 * @since 2013-02-02 10:35
 */
public interface LiquibaseFactory
{
	Liquibase get(final String changeLogFileName);
}
