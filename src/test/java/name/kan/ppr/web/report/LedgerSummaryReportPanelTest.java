package name.kan.ppr.web.report;

import name.kan.junit.PreviewWriter;
import name.kan.ppr.model.report.LedgerSummaryGroup;
import name.kan.ppr.model.report.LedgerSummaryLine;
import name.kan.ppr.model.report.LedgerSummaryReport;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author kan
 * @since 2013-05-10 20:18
 */
public class LedgerSummaryReportPanelTest
{
	@Rule public PreviewWriter writer = new PreviewWriter("pages");
	final WicketTester tester = new WicketTester();

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void rendering() throws Exception
	{
		final LedgerSummaryReport report = new LedgerSummaryReport();
		report.setCurrency(Currency.getInstance("GBP"));
		report.setOpeningBalanceAmount(new BigDecimal("227.63"));
		report.setClosingBalanceAmount(new BigDecimal("358.43"));
		final LedgerSummaryGroup cartTransGroup = new LedgerSummaryGroup();
		report.getGroups().add(cartTransGroup);
		cartTransGroup.setName("Shopping cart transactions");
		final LedgerSummaryLine cartTransLine1 = new LedgerSummaryLine();
		cartTransGroup.getLines().add(cartTransLine1);
		cartTransLine1.setName("Shopping cart payment received");
		cartTransLine1.setGross(new BigDecimal("5562.43"));
		final LedgerSummaryLine cartTransLine2 = new LedgerSummaryLine();
		cartTransGroup.getLines().add(cartTransLine2);
		cartTransLine2.setName("Payment received");
		cartTransLine2.setGross(new BigDecimal("60"));
		cartTransLine2.setCredit(true);
		cartTransLine2.setFee(new BigDecimal("2.44"));
		final Model<LedgerSummaryReport> model = Model.of(report);
		tester.startComponentInPage(new LedgerSummaryReportPanel("report", model));
		final String output = tester.getLastResponseAsString();
		writer.write(output);
	}
}
