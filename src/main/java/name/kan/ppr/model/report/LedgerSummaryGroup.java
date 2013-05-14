package name.kan.ppr.model.report;

import com.google.common.collect.Lists;
import name.kan.ppr.model.AbstractNamedEntity;

import java.util.List;

/**
 * @author kan
 * @since 2013-02-15 20:09
 */
public class LedgerSummaryGroup extends AbstractNamedEntity
{
	private static final long serialVersionUID = -4595965068184817085L;

	private List<LedgerSummaryLine> lines = Lists.newArrayList();

	public List<LedgerSummaryLine> getLines()
	{
		return lines;
	}
}
