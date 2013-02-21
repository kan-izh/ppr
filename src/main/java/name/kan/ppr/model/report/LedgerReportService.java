package name.kan.ppr.model.report;

import name.kan.jdbc.Transactional;
import name.kan.ppr.model.DatePeriod;
import name.kan.ppr.model.account.AccountRepository;

import javax.inject.Inject;
import java.util.Currency;

/**
 * @author kan
 * @since 2013-02-16 01:00
 */
public class LedgerReportService
{
	@Inject
	private AccountRepository accountRepository;

	@Transactional
	public LedgerSummaryReport createReport(final DatePeriod datePeriod, final Currency currency)
	{
		final LedgerSummaryReport report = new LedgerSummaryReport();
		report.setDatePeriod(datePeriod);
		report.setCurrency(currency);

		return report;
	}
}
