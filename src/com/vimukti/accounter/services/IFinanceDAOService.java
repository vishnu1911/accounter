/**
 * 
 */
package com.vimukti.accounter.services;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.Entry;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.PaySalesTaxEntries;
import com.vimukti.accounter.core.PayVATEntries;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.ReceiveVATEntries;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionMakeDeposit;
import com.vimukti.accounter.core.TransferFund;
import com.vimukti.accounter.core.VATReturn;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.core.Lists.DepreciableFixedAssetsList;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetLinkedAccountMap;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetList;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.core.Lists.OverDueInvoicesList;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.core.Lists.SalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.SellingOrDisposingFixedAssetList;
import com.vimukti.accounter.web.client.core.Lists.TempFixedAsset;
import com.vimukti.accounter.web.client.core.reports.AccountBalance;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeList;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReport;

/**
 * @author Fernandez
 * 
 */
public interface IFinanceDAOService {

	// public <T extends IAccounterCore> String createObject(T t)
	// throws DAOException;
	//
	// public <T extends IAccounterCore> Boolean updateObject(T t)
	// throws DAOException;
	//
	// public Boolean deleteObject(AccounterCoreType type, String id)
	// throws DAOException;

	public <T extends IAccounterCore> T getObjectById(AccounterCoreType type,
			long id) throws DAOException;

	public <T extends IAccounterCore> T getObjectByName(AccounterCoreType type,
			String name) throws DAOException;

	public <T extends IAccounterCore> List<T> getObjects(AccounterCoreType type)
			throws DAOException;

	// public Boolean updateCompanyPreferences(ClientCompanyPreferences
	// preferences)
	// throws DAOException;
	//
	// public Boolean updateCompany(ClientCompany clientCompany)
	// throws DAOException;

	/**
	 * This method is to check whether an Object is deletable or not.
	 * 
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return true if the object related to the given string is deletable,
	 *         otherwise return false.
	 * @throws DAOException
	 */
	// public <T extends IAccounterServerCore> Boolean canDelete(
	// AccounterCoreType clazz, String id) throws DAOException;

	public void alterFiscalYear(final FiscalYear fiscalYear)
			throws DAOException;

	/**
	 * Company Home page widgets Related Dao Methods
	 */

	/*
	 * ==========================================================================
	 * ======
	 */
	// IAccounterGUIDAOService methods
	/*
	 * ==========================================================================
	 * =======
	 */

	// public ProfitAndLoss getProfitAndLossReport()
	// throws DAOException;
	public List<OverDueInvoicesList> getOverDueInvoices() throws DAOException;

	public List<EnterBill> getBillsOwed() throws DAOException;

	public List<Estimate> getLatestQuotes() throws DAOException;

	// public List<ExpensesThisFiscalYear> getExpensesThisFiscalYear(
	// Company company) throws DAOException;

	public List<CreditCardCharge> getCreditCardChargesThisMonth(final long date)
			throws DAOException;

	public List<Customer> getLatestCustomers() throws DAOException;

	public List<Vendor> getLatestVendors() throws DAOException;

	public List<Item> getLatestItems() throws DAOException;

	public List<PaymentsList> getLatestPayments() throws DAOException;

	public List<CashSales> getLatestCashSales() throws DAOException;

	public List<CustomerRefund> getLatestCustomerRefunds() throws DAOException;

	public List<BillsList> getLatestBills() throws DAOException;

	public List<CashPurchase> getLatestCashPurchases() throws DAOException;

	public List<WriteCheck> getLatestChecks() throws DAOException;

	public List<MakeDeposit> getLatestDeposits() throws DAOException;

	public List<TransferFund> getLatestFundsTransfer() throws DAOException;

	public List<InvoicesList> getLatestInvoices() throws DAOException;

	public List<ReceivePayment> getLatestReceivePayments() throws DAOException;

	public List<PaymentsList> getLatestVendorPayments() throws DAOException;

	public List<Item> getLatestSalesItems() throws DAOException;

	public List<Item> getLatestPurchaseItems() throws DAOException;

	/**
	 * Other Dao Methods required for GUI
	 */

	// To auto generate the next Transaction number for all types of
	// Transactions
	public String getNextTransactionNumber(int transactionType)
			throws DAOException;

	public Long getNextIssuePaymentCheckNumber(long account2)
			throws DAOException;

	// To auto generate the next Voucher number for all vouchers.
	public String getNextVoucherNumber() throws DAOException;

	// To auto generate the next Check number.
	public Long getNextCheckNumber(long account) throws DAOException;

	// To auto generate the next Nominal Code for a given account type with in
	// the range.
	public Long getNextNominalCode(int accountType) throws DAOException;

	public String getNextFixedAssetNumber() throws DAOException;

	// To check whether an Account is a Tax Agency Account or not
	public boolean isTaxAgencyAccount(long account) throws DAOException;

	// To check whether an Account is a Sales Tax Payable Account or not
	public boolean isSalesTaxPayableAccount(long accountId) throws DAOException;

	// To check whether an Account is a Sales Tax Payable Account or not
	public boolean isSalesTaxPayableAccountByName(String accountName)
			throws DAOException;

	// To get all the Estimates/Quotes in a company
	public List<Estimate> getEstimates() throws DAOException;

	// To get the Estimates/Quotes of a particular customer in the company
	public List<Estimate> getEstimates(long customer) throws DAOException;

	// To check whether an Invoice or Vendor Bill can be Voidable and Editable
	// or not
	public boolean canVoidOrEdit(long invoiceOrVendorBillId)
			throws DAOException;

	// To display the Item combo box of Transaction Item lines in Creating
	// Creating Invoice,Cash Sale, Customer Credit Memo, Customer Refund
	public List<Item> getSalesItems() throws DAOException;

	// To display the Item combo box of Transaction Item lines in Creating Enter
	// Bill,Cash Purchase, Vendor Credit Memo Transactions
	public List<Item> getPurchaseItems() throws DAOException;

	// To get the Credits and Payments of a particular Customer in a company
	public List<CreditsAndPayments> getCustomerCreditsAndPayments(long customer)
			throws DAOException;

	// To get the Credits and Payments of a particular Vendor in a company
	public List<CreditsAndPayments> getVendorCreditsAndPayments(long vendor)
			throws DAOException;

	// To get Make Deposit by passing the Transaction Make Deposit
	public TransactionMakeDeposit getTransactionMakeDeposit(
			long transactionMakeDepositId) throws DAOException;

	// To get all the Invoices and CustomerRefunds of a particular customer
	// which are not paid and display as the Transaction ReceivePayments in
	// ReceivePayment
	public List<ReceivePaymentTransactionList> getTransactionReceivePayments(
			long customerId, long paymentDate) throws DAOException,
			ParseException;

	// To get all the Vendor Bills and MakeDeposit(New Vendor Entries) which are
	// not paid and display as the Transaction PayBills in PayBill
	public List<PayBillTransactionList> getTransactionPayBills()
			throws DAOException;

	// To get all the Vendor Bills and MakeDeposit(New Vendor Entries) of a
	// particular Vendor which are not paid and display as the Transaction
	// PayBills in PayBill
	public List<PayBillTransactionList> getTransactionPayBills(long vendorId)
			throws DAOException;

	// To get all the Write Checks in a company
	public List<IssuePaymentTransactionsList> getChecks() throws DAOException;

	// To get the Write Checks related to a particular Account in a company
	public List<IssuePaymentTransactionsList> getChecks(long account)
			throws DAOException;

	// To get a particular Journal Entry
	public JournalEntry getJournalEntry(long journalEntryId)
			throws DAOException;

	// To get all the Journal Entries in a company
	public List<JournalEntry> getJournalEntries() throws DAOException;

	// To get all the Entries of a particular journal entry
	public List<Entry> getEntries(long journalEntryId) throws DAOException;

	// to get the Account Register of a particular account
	// public AccountRegister getAccountRegister(String accountId)
	// throws DAOException;

	// To get all Customer Refunds , Vendor Payments,Cash Purchases, Credit Card
	// Charge and all Write Checks
	public List<PaymentsList> getPaymentsList() throws DAOException;

	// To get all Invoices, Customer Credit Memo, Cash Sales and Write Checks ->
	// for Customer
	public List<InvoicesList> getInvoiceList() throws DAOException;

	// To get all Customer Refunds and Write Checks -> for Customer
	public List<CustomerRefundsList> getCustomerRefundsList()
			throws DAOException;

	// To get all Vendor Bills, Vendor Credit Memo, Cash Purchase and Credit
	// Card Charge and Write Check -> Vendor
	public List<BillsList> getBillsList(boolean isExpensesList)
			throws DAOException;

	// To get all Vendor Payments, PayBills, Write Check -> Vendor and TaxAgency
	public List<ReceivePaymentsList> getReceivePaymentsList()
			throws DAOException;

	// To get all Vendor Payments, PayBills, Write Check -> Vendor and TaxAgency
	public List<PaymentsList> getVendorPaymentsList() throws DAOException;

	// To display the liabilityAccount combo box of New Tax Agency window
	public List<Account> getTaxAgencyAccounts() throws DAOException;

	public List<ClientTransactionMakeDeposit> getTransactionMakeDeposits()
			throws DAOException;

	public void test() throws Exception;

	public List<PaySalesTaxEntries> getTransactionPaySalesTaxEntriesList(
			long billsDueOnOrBefore) throws DAOException;

	public List<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
			long customerId) throws DAOException;

	public List<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(
			long vendorId) throws DAOException;

	public List<SalesOrdersList> getSalesOrders(boolean orderByDate);

	public List<PurchaseOrdersList> getPurchaseOrders(boolean orderByDate);

	public List<SalesOrdersList> getSalesOrdersForCustomer(long customerID);

	public List<SalesOrdersList> getPurchaseOrdersForVendor(long vendorID);

	List<SalesOrdersList> getSalesOrdersList() throws DAOException;

	List<PurchaseOrdersList> getPurchaseOrdersList() throws DAOException;

	List<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(long vendorId)
			throws DAOException;

	List<FixedAssetList> getFixedAssets(int status) throws DAOException;

	List<SellingOrDisposingFixedAssetList> getSellingOrDisposingFixedAssets()
			throws DAOException;

	public void runDepreciation(long depreciationFrom, long depreciationTo,
			FixedAssetLinkedAccountMap linkedAccounts) throws DAOException;

	public DepreciableFixedAssetsList getDepreciableFixedAssets(
			long depreciationFrom, long depreciationTo) throws DAOException;

	public ClientFinanceDate getDepreciationLastDate() throws DAOException;

	public void rollBackDepreciation(long rollBackDepreciationTo)
			throws AccounterException;

	public double getCalculatedDepreciatedAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			long depreciationFrom, long depreciationTo) throws DAOException;

	public double getCalculatedRollBackDepreciationAmount(
			long rollBackDepreciationTo) throws DAOException;

	public double getCalculatedRollBackDepreciationAmount(long fixedAssetID,
			long rollBackDepreciationTo) throws DAOException;

	public FixedAssetSellOrDisposeReviewJournal getReviewJournal(
			TempFixedAsset fixedAsset) throws DAOException;

	// public void changeDepreciationStartDateTo(long newStartDate)
	// throws DAOException;

	public List<ClientFinanceDate> getFinancialYearStartDates()
			throws DAOException;

	public List<ClientFinanceDate> getAllDepreciationFromDates()
			throws DAOException;

	// public void changeFiscalYearsStartDateTo(long newStartDate)
	// throws DAOException;

	/*
	 * ==========================================================================
	 * ======================
	 */
	// IAccounterReportsDAOService methods
	/*
	 * ==========================================================================
	 * ======================
	 */

	public List<AccountBalance> getAccountBalances() throws DAOException;

	public List<TrialBalance> getTrialBalance(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public List<AgedDebtors> getAgedDebtors(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public List<AgedDebtors> getAgedDebtors(FinanceDate startDate,
			FinanceDate endDate, int intervalDays, int throughDaysPassOut)
			throws DAOException;

	public List<AgedDebtors> getAgedCreditors(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public List<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<SalesByCustomerDetail> getSalesByCustomerSummary(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<SalesByCustomerDetail> getSalesByItemDetail(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<SalesByCustomerDetail> getSalesByItemSummary(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<TransactionHistory> getCustomerTransactionHistory(
			FinanceDate startDate, FinanceDate endDate)
			throws AccounterException;

	public List<SalesByCustomerDetail> getPurchasesByVendorDetail(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<SalesByCustomerDetail> getPurchasesByVendorSummary(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<SalesByCustomerDetail> getPurchasesByItemDetail(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<SalesByCustomerDetail> getPurchasesByItemSummary(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<TransactionHistory> getVendorTransactionHistory(
			FinanceDate startDate, FinanceDate endDate)
			throws AccounterException;

	public List<AmountsDueToVendor> getAmountsDueToVendor(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<MostProfitableCustomers> getMostProfitableCustomers(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<MostProfitableCustomers> getProfitabilityByCustomerDetail(
			long customer, FinanceDate startDate, FinanceDate endDate)
			throws DAOException;

	public List<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<Transaction> getRegister(Account account) throws DAOException;

	public List<AccountRegister> getAccountRegister(FinanceDate startDate,
			FinanceDate endDate, long accountId) throws DAOException;

	public List<TransactionDetailByAccount> getTransactionDetailByAccount(
			final FinanceDate startDate, final FinanceDate endDate)
			throws DAOException;

	public List<SalesTaxLiability> getSalesTaxLiabilityReport(
			final FinanceDate startDate, final FinanceDate endDate)
			throws AccounterException;

	public List<Customer> getTransactionHistoryCustomers(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public List<Vendor> getTransactionHistoryVendors(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public List<Item> getSalesReportItems(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public List<Item> getPurchaseReportItems(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public ClientFinanceDate[] getMinimumAndMaximumTransactionDate()
			throws AccounterException;

	public List<TrialBalance> getBalanceSheetReport(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public List<TrialBalance> getProfitAndLossReport(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public List<TrialBalance> getCashFlowReport(FinanceDate startDate,
			FinanceDate endDate) throws AccounterException;

	public List<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, FinanceDate startDate, FinanceDate endDate)
			throws DAOException;

	public List<SalesByCustomerDetail> getSalesByItemDetail(String itemName,
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, FinanceDate startDate, FinanceDate endDate)
			throws DAOException;

	public List<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, FinanceDate startDate, FinanceDate endDate)
			throws DAOException;

	public List<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, final FinanceDate startDate,
			final FinanceDate endDate) throws DAOException;

	public List<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			final String taxItemName, final FinanceDate startDate,
			final FinanceDate endDate) throws DAOException;

	public List<OpenAndClosedOrders> getOpenSalesOrders(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public List<OpenAndClosedOrders> getClosedSalesOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<OpenAndClosedOrders> getCompletedSalesOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<OpenAndClosedOrders> getPurchaseOrders(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public List<OpenAndClosedOrders> getSalesOrders(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public List<OpenAndClosedOrders> getCanceledSalesOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<OpenAndClosedOrders> getOpenPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<OpenAndClosedOrders> getClosedPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<OpenAndClosedOrders> getCompletedPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<OpenAndClosedOrders> getCanceledPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	// For UK

	public Map<String, Double> getVATReturnBoxes(FinanceDate startDate,
			FinanceDate endDate) throws DAOException;

	public VATReturn getVATReturnDetails(TAXAgency vatAgency,
			FinanceDate fromDate,

			FinanceDate toDate) throws DAOException, InvalidOperationException;

	public List<VATSummary> getPriorReturnVATSummary(TAXAgency vatAgency,
			FinanceDate endDate) throws DAOException, ParseException;

	public List<VATDetail> getPriorVATReturnVATDetailReport(
			TAXAgency vatAgency, FinanceDate endDate) throws DAOException,
			ParseException;

	public List<VATDetail> getVATDetailReport(FinanceDate startDate,
			FinanceDate endDate) throws DAOException, ParseException;

	public List<VATSummary> getVAT100Report(TAXAgency vatAgency,
			FinanceDate fromDate, FinanceDate toDate) throws DAOException,
			ParseException;

	public List<UncategorisedAmountsReport> getUncategorisedAmountsReport(
			FinanceDate fromDate, FinanceDate toDate) throws DAOException,
			ParseException;

	public List<VATItemDetail> getVATItemDetailReport(FinanceDate fromDate,
			FinanceDate toDate) throws DAOException, ParseException;

	public List<VATItemSummary> getVATItemSummaryReport(FinanceDate fromDate,
			FinanceDate toDate) throws DAOException, ParseException;

	public List<PayVATEntries> getPayVATEntries();

	public List<ReceiveVATEntries> getReceiveVATEntries();

	public List<ECSalesListDetail> getECSalesListDetailReport(String payeeName,
			FinanceDate fromDate, FinanceDate toDate) throws DAOException,
			ParseException;

	public KeyFinancialIndicators getKeyFinancialIndicators()
			throws DAOException;

	public List<ECSalesList> getECSalesListReport(FinanceDate fromDate,
			FinanceDate toDate) throws DAOException, ParseException;

	public List<ReverseChargeListDetail> getReverseChargeListDetailReport(
			String payeeName, FinanceDate fromDate, FinanceDate toDate)
			throws DAOException, ParseException;

	public List<ReverseChargeList> getReverseChargeListReport(
			FinanceDate fromDate, FinanceDate toDate) throws DAOException,
			ParseException;

	public void createTaxes(int[] vatReturnType) throws DAOException;

	public List<PayeeList> getPayeeList(int transactionCategory)
			throws DAOException;

	public List<ExpenseList> getExpenseReportByType(int status,
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	/*
	 * 
	 * ==========================================================================
	 * ==================
	 */
	/*
	 * ==========================================================================
	 * ==================
	 */

	public List<VATItemDetail> getVATItemDetailReport(String vatItemId,
			FinanceDate fromDate, FinanceDate toDate) throws DAOException,
			ParseException;

	public String getNextCustomerNumber() throws DAOException;

	public List<DepositDetail> getDepositDetail(FinanceDate startDate,
			FinanceDate endDate);

	public List<CheckDetailReport> getCheckDetailReport(long paymentmethod,
			FinanceDate startDate, FinanceDate endDate) throws DAOException;

	public List<PayeeStatementsList> getPayeeStatementsList(long id,
			long transactionDate, FinanceDate fromDate, FinanceDate toDate,
			int noOfDays, boolean isEnabledOfZeroBalBox,
			boolean isEnabledOfLessthanZeroBalBox,
			double lessThanZeroBalanceValue,
			boolean isEnabledOfNoAccountActivity,
			boolean isEnabledOfInactiveCustomer) throws DAOException;

	public List<Double> getGraphPointsforAccount(int chartType, long accountNo)
			throws DAOException;

	public List<BillsList> getEmployeeExpensesByStatus(String userName,
			int status) throws DAOException;

	public boolean changeMyPassword(String emailId, String oldPassword,
			String newPassword) throws DAOException;

	public List<ClientUserInfo> getAllUsers();
	
	public List<PayeeStatementsList> getCustomerStatement(long customer, long fromDate, long toDate);
}