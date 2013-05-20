package name.kan.ppr.model.report;

import com.google.inject.Guice;
import com.google.inject.Inject;
import name.kan.guice.slf4j.Slf4jLoggerInjectorModule;
import name.kan.jdbc.TransactionalModule;
import name.kan.ppr.model.DatePeriod;
import name.kan.ppr.model.account.AccountModule;
import name.kan.ppr.model.txn.TxnModule;
import name.kan.ppr.test.DbModule;
import name.kan.ppr.test.LiquibaseWorker;
import name.kan.ppr.test.StaticInjectModule;
import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author kan
 * @since 2013-02-17 18:30
 */
public class LedgerReportServiceTest
{
	private static final Currency GBP = Currency.getInstance("GBP");
	@Inject
	private static LiquibaseWorker liquibaseWorker;

	@Inject
	private static LedgerReportService ledgerReportService;

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		Guice.createInjector(
				new Slf4jLoggerInjectorModule(),
				new DbModule(),
				new TransactionalModule(),
				new AccountModule(),
				new TxnModule(),
				new StaticInjectModule(LedgerReportServiceTest.class)
		);

		liquibaseWorker.setUp("name/kan/ppr/model/report/reportData.xml");
	}

	@Test
	public void testCreateReport() throws Exception
	{
		final DatePeriod period = new DatePeriod(LocalDate.parse("2011-04-01"), LocalDate.parse("2011-07-31"));
		final LedgerSummaryReport report = ledgerReportService.createReport(period, GBP);
		assertEquals(period, report.getDatePeriod());
		assertEquals(GBP, report.getCurrency());

		final List<LedgerSummaryGroup> groups = report.getGroups();
		assertEquals(4, groups.size());
		final LedgerSummaryGroup shoppingCart = groups.get(0);
		final LedgerSummaryGroup currencyConversion = groups.get(1);
		final LedgerSummaryGroup payments = groups.get(2);
		final LedgerSummaryGroup bankAccounts = groups.get(3);

		assertEquals("Shopping card transactions", shoppingCart.getName());
		assertEquals("Currency Conversion", currencyConversion.getName());
		assertEquals("Payments", payments.getName());
		assertEquals("Withdraw Funds to a Bank Account", bankAccounts.getName());

		final List<LedgerSummaryLine> paymentLine = payments.getLines();
		assertEquals(1, paymentLine.size());
		final LedgerSummaryLine ppExpCo = paymentLine.get(0);
		assertEquals("Web Accept Payment Sent", ppExpCo.getName());

		final LedgerSummaryLine shoppingCartPayments = shoppingCart.getLines().get(0);
		assertEquals("Shopping Cart Payment Received", shoppingCartPayments.getName());
		assertEquals(new BigDecimal("-107.00"), shoppingCartPayments.getGross());
		assertEquals(new BigDecimal("4.44"), shoppingCartPayments.getFee());
	}

}
