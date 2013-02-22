package name.kan.ppr.model.report;

import name.kan.jdbc.Transactional;
import name.kan.ppr.model.DatePeriod;
import name.kan.ppr.model.account.AccountRepository;
import name.kan.ppr.model.txn.TxnTypeEntity;
import name.kan.ppr.model.txn.TxnTypeRepository;

import javax.inject.Inject;
import java.util.Currency;
import java.util.List;

/**
 * @author kan
 * @since 2013-02-16 01:00
 */
public class LedgerReportService
{
	@Inject
	private AccountRepository accountRepository;

	@Inject
	private TxnTypeRepository txnTypeRepository;

	@Transactional
	public LedgerSummaryReport createReport(final DatePeriod datePeriod, final Currency currency)
	{
		final LedgerSummaryReport report = new LedgerSummaryReport();
		report.setDatePeriod(datePeriod);
		report.setCurrency(currency);

		final List<TxnTypeEntity>  topLevelTypes = txnTypeRepository.findTopLevelTypes();
		for(final TxnTypeEntity topLevelType : topLevelTypes)
		{
			final LedgerSummaryGroup topLevelGroup = new LedgerSummaryGroup();
			topLevelGroup.setName(topLevelType.getName());
//			fillSecondLevel(topLevelType, topLevelGroup);
			report.getGroups().add(topLevelGroup);
		}

		return report;
	}
}
