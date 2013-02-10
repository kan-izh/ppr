package name.kan.ppr.web.main;

import name.kan.ppr.model.txn.TxnType;
import name.kan.ppr.model.txn.TxnTypeRepository;
import org.apache.wicket.markup.html.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * @author kan
 * @since 2013-01-24 21:37
 */
public class HomePage extends WebPage
{
	private static final Logger log = LoggerFactory.getLogger(HomePage.class);
	@Inject
	private TxnTypeRepository txnTypeRepository;

	public HomePage()
	{
		final TxnType txnType = txnTypeRepository.obtainByName("test");
		log.info("type = {}", txnType);
	}
}
