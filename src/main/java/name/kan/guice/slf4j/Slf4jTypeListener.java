package name.kan.guice.slf4j;

import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Errors;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author kan
 * @since 2013-05-14 19:05
 */
class Slf4jTypeListener implements TypeListener
{
	public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {

		for (Field field : typeLiteral.getRawType().getDeclaredFields()) {
			if (field.getType() == Logger.class
					&& field.isAnnotationPresent(InjectLogger.class)) {
				typeEncounter.register(new Slf4jMembersInjector<I>(field));
			}
		}
	}

	private static class Slf4jMembersInjector<I> implements MembersInjector<I>
	{
		private final Field field;
		private final Logger logger;

		public Slf4jMembersInjector(final Field field)
		{
			this.field = field;
			this.logger = LoggerFactory.getLogger(field.getDeclaringClass());
			field.setAccessible(true);
		}

		@Override
		public void injectMembers(final Object instance)
		{
			final Errors errors = new Errors();
			try
			{
				field.set(instance, logger);
			} catch(IllegalAccessException e)
			{
				errors.errorInUserCode(e, "Error injecting %s into field %s.%n"
						+ " Reason: %s", logger, field, e);
			}
			errors.throwConfigurationExceptionIfErrorsExist();
		}
	}
}
