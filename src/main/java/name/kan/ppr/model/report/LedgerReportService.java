package name.kan.ppr.model.report;

import name.kan.jdbc.Transactional;
import name.kan.ppr.model.DatePeriod;
import name.kan.ppr.model.account.AccountRepository;
import name.kan.ppr.model.txn.TxnTypeEntity;
import name.kan.ppr.model.txn.TxnTypeRepository;
import org.joda.time.DateTimeZone;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	@Inject
	private Provider<Connection> connectionProvider;

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
			fillSecondLevel(report, topLevelType, topLevelGroup);
			report.getGroups().add(topLevelGroup);
		}

		return report;
	}

	private void fillSecondLevel(final LedgerSummaryReport report, final TxnTypeEntity topLevelType, final LedgerSummaryGroup group)
	{
		final List<TxnTypeEntity> children = txnTypeRepository.findChildren(topLevelType);
		if(children.isEmpty())
			fillByAccount(report, topLevelType, group);
		else
			fillByType(report, children, group);
	}

	private void fillByType(final LedgerSummaryReport report, final List<TxnTypeEntity> children, final LedgerSummaryGroup group)
	{
		try
		{
			try(final PreparedStatement st = connectionProvider.get().prepareStatement("" +
					"SELECT SUM(t.gross) AS gross, SUM(t.fee) AS fee\n" +
					"FROM txn t\n" +
					"WHERE t.type_id=?\n" +
					"	AND t.currency=?\n" +
					"	AND t.date_time BETWEEN ? AND ?"))
			{
				for(final TxnTypeEntity child : children)
				{
					st.setLong(1, child.getId());
					st.setInt(2, report.getCurrency().getNumericCode());
					final DatePeriod period = report.getDatePeriod();
					st.setDate(3, new Date(period.getFrom().toDateMidnight(DateTimeZone.UTC).getMillis()));
					st.setDate(4, new Date(period.getTo().toDateMidnight(DateTimeZone.UTC).getMillis()));
					final ResultSet rs = st.executeQuery();
					rs.next();
					final LedgerSummaryLine line = new LedgerSummaryLine();
					line.setName(child.getName());
					line.setGross(rs.getBigDecimal("gross"));
					line.setFee(rs.getBigDecimal("fee"));
					group.getLines().add(line);
				}
			}
		} catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void fillByAccount(final LedgerSummaryReport report, final TxnTypeEntity type, final LedgerSummaryGroup group)
	{
		try
		{
			try(final PreparedStatement st = connectionProvider.get().prepareStatement("" +
					"SELECT a.name, SUM(t.gross) AS gross, SUM(t.fee) AS fee\n" +
					"FROM txn t\n" +
					"	INNER JOIN account a ON t.account_id = a.id\n" +
					"WHERE t.type_id=?\n" +
					"	AND t.currency=?\n" +
					"	AND t.date_time BETWEEN ? AND ?\n" +
					"GROUP BY a.id, a.name\n" +
					"ORDER BY a.name\n"))
			{
				st.setLong(1, type.getId());
				st.setInt(2, report.getCurrency().getNumericCode());
				final DatePeriod period = report.getDatePeriod();
				st.setDate(3, new Date(period.getFrom().toDateMidnight(DateTimeZone.UTC).getMillis()));
				st.setDate(4, new Date(period.getTo().toDateMidnight(DateTimeZone.UTC).getMillis()));
				for(final ResultSet rs = st.executeQuery(); rs.next(); )
				{
					final LedgerSummaryLine line = new LedgerSummaryLine();
					line.setName(rs.getString("name"));
					line.setGross(rs.getBigDecimal("gross"));
					line.setFee(rs.getBigDecimal("fee"));
					group.getLines().add(line);
				}
			}
		} catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}
