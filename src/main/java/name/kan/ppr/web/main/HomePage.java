package name.kan.ppr.web.main;

import name.kan.ppr.model.DatePeriod;
import name.kan.ppr.model.report.LedgerReportService;
import name.kan.ppr.model.report.LedgerSummaryReport;
import name.kan.ppr.web.csv.UploadCsvFormPanel;
import name.kan.ppr.web.report.LedgerSummaryReportPanel;
import name.kan.wicket.behavior.HideNullModelBehavior;
import org.apache.wicket.extensions.yui.calendar.DateField;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.joda.time.LocalDate;

import javax.inject.Inject;
import java.util.Currency;
import java.util.Date;
import java.util.Map;

/**
 * @author kan
 * @since 2013-01-24 21:37
 */
public class HomePage extends WebPage
{
	private static final long serialVersionUID = 992503113234757275L;

	public HomePage()
	{
		add(new UploadCsvFormPanel("upload"));
		add(new LedgerSummaryReportDetailsForm("report"));
	}

	private static class LedgerSummaryReportDetailsForm extends StatelessForm<LedgerSummaryReportDetailsForm>
	{
		private static final long serialVersionUID = 2228318245053068029L;
		@Inject
		private LedgerReportService ledgerReportService;

		private final IModel<LedgerSummaryReport> reportModel = Model.of();
		private Date from;
		private Date to;

		public LedgerSummaryReportDetailsForm(final String report)
		{
			super(report);
			setModel(CompoundPropertyModel.of(Model.of(this)));
			add(new FeedbackPanel("feedbackPanel", new ContainerFeedbackMessageFilter(this)));
			add(new ConfiguredDateField("from").setRequired(true));
			add(new ConfiguredDateField("to").setRequired(true));
			add(new LedgerSummaryReportPanel("report", reportModel).add(HideNullModelBehavior.INSTANCE));
		}

		@Override
		protected void onSubmit()
		{
			final DatePeriod period = new DatePeriod(LocalDate.fromDateFields(from), LocalDate.fromDateFields(to));
			final Currency currency = Currency.getInstance("GBP");
			final LedgerSummaryReport report = ledgerReportService.createReport(period, currency);
			reportModel.setObject(report);
		}

		private static class ConfiguredDateField extends DateField
		{
			private static final long serialVersionUID = 4984738886427980546L;

			public ConfiguredDateField(final String id)
			{
				super(id);
			}

			@Override
			protected void configure(final Map<String, Object> widgetProperties)
			{
				widgetProperties.put("navigator", true);
			}
		}
	}

}
