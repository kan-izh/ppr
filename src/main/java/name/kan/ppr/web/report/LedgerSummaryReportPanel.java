package name.kan.ppr.web.report;

import name.kan.ppr.model.report.LedgerSummaryGroup;
import name.kan.ppr.model.report.LedgerSummaryLine;
import name.kan.ppr.model.report.LedgerSummaryReport;
import name.kan.ppr.web.wicket.model.MoneyModel;
import name.kan.ppr.web.wicket.model.PositiveMoneyModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

/**
 * @author kan
 * @since 2013-05-10 20:05
 */
public class LedgerSummaryReportPanel extends Panel
{
	private static final long serialVersionUID = -6010970254612090272L;

	public LedgerSummaryReportPanel(final String id, final IModel<LedgerSummaryReport> model)
	{
		super(id, CompoundPropertyModel.of(model));
		add(new Label("datePeriod.from"));
		final IModel<Currency> currency = PropertyModel.of(model, "currency");
		final Locale locale = getLocale();
		add(new Label("openingBalanceAmount", MoneyModel.of(
				locale,
				PropertyModel.<BigDecimal>of(model, "openingBalanceAmount"),
				currency)
		));
		add(new Label("datePeriod.to"));
		add(new Label("closingBalanceAmount", MoneyModel.of(
				locale,
				PropertyModel.<BigDecimal>of(model, "closingBalanceAmount"),
				currency)
		));
		add(new LedgerSummaryGroupListView("groups", currency));
	}

	private static class LedgerSummaryGroupListView extends PropertyListView<LedgerSummaryGroup>
	{
		private static final long serialVersionUID = -3383435681134224402L;
		private final IModel<Currency> currency;

		public LedgerSummaryGroupListView(final String id, final IModel<Currency> currency)
		{
			super(id);
			this.currency = currency;
		}

		@Override
		protected void populateItem(final ListItem<LedgerSummaryGroup> item)
		{
			item.add(new Label("name"));
			item.add(new LedgerSummaryLineListView("lines", currency));
			item.add(new WebMarkupContainer("header.net"));
			item.add(new WebMarkupContainer("header.fee"));
			item.add(new WebMarkupContainer("header.total"));
		}
	}

	private static class LedgerSummaryLineListView extends PropertyListView<LedgerSummaryLine>
	{
		private static final long serialVersionUID = 8244298252813952419L;
		private final IModel<Currency> currency;

		public LedgerSummaryLineListView(final String id, final IModel<Currency> currency)
		{
			super(id);
			this.currency = currency;
		}

		@Override
		protected void populateItem(final ListItem<LedgerSummaryLine> item)
		{
			item.add(new Label("name"));
			final IModel<BigDecimal> gross = PropertyModel.of(item.getModel(), "gross");
			final IModel<BigDecimal> fee = PropertyModel.of(item.getModel(), "fee");
			item.add(new Label("debit", MoneyModel.of(getLocale(), new PositiveMoneyModel(true, gross), currency)));
			item.add(new Label("credit", MoneyModel.of(getLocale(), new PositiveMoneyModel(false, gross), currency)));
			item.add(new Label("fee", MoneyModel.of(getLocale(), fee, currency)));
			final AbstractReadOnlyModel<BigDecimal> net = new AbstractReadOnlyModel<BigDecimal>()
			{
				private static final long serialVersionUID = 7313288604196058422L;

				@Override
				public BigDecimal getObject()
				{
					final BigDecimal grossVal = gross.getObject();
					final BigDecimal feeVal = fee.getObject();
					if(grossVal == null || feeVal == null)
						return null;
					return grossVal.subtract(feeVal);
				}
			};
			item.add(new Label("net", MoneyModel.of(getLocale(), net, currency)));
			item.add(new Label("total", MoneyModel.of(getLocale(), gross, currency)));
		}
	}
}
