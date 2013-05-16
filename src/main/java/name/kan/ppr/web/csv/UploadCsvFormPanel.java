package name.kan.ppr.web.csv;

import name.kan.ppr.parser.PaypalCsvParser;
import name.kan.ppr.parser.PaypalParserCallback;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

/**
* @author kan
* @since 2013-05-13 22:14
*/
public class UploadCsvFormPanel extends Panel
{
	private static final long serialVersionUID = -9118084455231573215L;

	public UploadCsvFormPanel(final String id)
	{
		super(id);
		add(new UploadCsvForm("form"));
	}

	private static class UploadCsvForm extends StatelessForm<Void>
	{
		private static final long serialVersionUID = -5853371439319266235L;
		private final FileUploadField csvFile = new FileUploadField("csvFile");

		@Inject
		private PaypalCsvParser csvParser;

		@Inject
		private PaypalParserCallback parserCallback;

		public UploadCsvForm(final String id)
		{
			super(id);
			add(new FeedbackPanel("feedbackPanel", new ContainerFeedbackMessageFilter(this)));
			add(csvFile.setRequired(true));
		}

		@Override
		protected void onSubmit()
		{
			final FileUpload fileUpload = csvFile.getFileUpload();
			try
			{
				final InputStream is = fileUpload.getInputStream();
				csvParser.parse(is, parserCallback);
			} catch(IOException e)
			{
				getFeedbackMessages().error(csvFile, e.getLocalizedMessage());
				return;
			}
			getFeedbackMessages().success(this, "File " + fileUpload.getClientFileName() + " uploaded successfully");
		}
	}
}
