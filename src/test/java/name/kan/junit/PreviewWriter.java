package name.kan.junit;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author kan
 * @since 2013-05-11 17:54
 */
public class PreviewWriter extends TestWatcher
{
	private final String name;
	private Description description;

	public PreviewWriter(final String name)
	{
		this.name = name;
	}

	@Override
	protected void starting(final Description description)
	{
		this.description = description;
	}

	public void write(final String output) throws IOException
	{
		final Class<?> clazz = description.getTestClass();
		final File classLocation = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getFile());
		final File pagesFile = new File(classLocation, name);
		final File classFile = new File(pagesFile, getClass().getName());
		classFile.mkdirs();
		final File testFile = new File(classFile, description.getMethodName() + ".html");
		try(final FileOutputStream fos = new FileOutputStream(testFile);
			final Writer writer = new OutputStreamWriter(fos, Charset.forName("UTF-8")))
		{
			writer.write(output);
		}
	}
}
