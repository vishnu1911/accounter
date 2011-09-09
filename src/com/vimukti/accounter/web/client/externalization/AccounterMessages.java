package com.vimukti.accounter.web.client.externalization;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public interface AccounterMessages extends Messages {
	public String userName(String loginUserName);

	public String failedTransaction(String transName);

	public String pleaseEnter(String itemName);

	public String pleaseEnterHTML(String title);

	public String failedTogetCreditsListAndPayments(String name);

	public SafeHtml addComparativeButton();

	public SafeHtml addNewLine();

	public String contactDetailsHtml();

	SafeHtml companyCommentHtml();

	SafeHtml companySettingsTitle();

	public SafeHtml allHTML();

	public SafeHtml aboutThisFieldHelp();

	public SafeHtml changePasswordHTML();

	SafeHtml conversionBalanaceHeader();

	SafeHtml conversionCommet();

	SafeHtml conversionDateButton();

	SafeHtml conversionHTML();

	public SafeHtml creaditHTML();

	public SafeHtml removeHTML();

	public SafeHtml previousHTML();

	public SafeHtml nextHTML();

	public SafeHtml logoutHTML();

	SafeHtml deleteHtml();

	public String paypalEmailHtml();

	public String startFiscalHTML();

	public String endFiscalHTML();

	SafeHtml footerComment();

	SafeHtml generalSettingsHeading();

	public String fiscalStartEndCompreHTML();

	SafeHtml helpContent();

	public SafeHtml helpCenter();

	public SafeHtml helpHTML();

	SafeHtml invoiceBrandingHTML();

	SafeHtml invoiceComment();

	SafeHtml logoComment();

	public SafeHtml upload();

	public SafeHtml undoneHtml();

	SafeHtml uploadLogo();

	SafeHtml userHTML();

	SafeHtml usersComment();

	public SafeHtml labelHTML();

	public SafeHtml conversationDateSelectionHTML();

	public SafeHtml bodyFooter();

	public SafeHtml sureToDelete(String string);

	public String wecantDeleteThisTheme(String themeName);

	public String pleaseselectvalidtransactionGrid(String string);

	public String noRecordsToShow();

	public String selectIndustryInfoHTML();

	public String selectIndustry();

	public String userGuidelinesMessage();

	public String industrySelectNote();

	public String journalEntryVendor(String name);

	public String invalidAccountNumber(String name);

	public String startSetupInfo();

	public String skipSetupInfo();

	public String statementlist();

	public String statementQuestion();

	public String managingList();

	public String recommendedAccounts();

	public String whyshoudIUseRecommended();

	public String recommendedNote();

	public String newCustomer(String customerName[]);

	public String addANewCustomer(String customerName);

	public String addaNewCustomer(String customerName);

	public String aNewCustomerRefund(String newCustomerRefund[]);

	public String addaNewCustomerRefund(String customerString);

	public String cashBasis(String cashBasis);

	public String custRefund(String customerRefund);

	public String Customer(String customer);

	public String customerAddress(String customerAddress[]);

	public String customerAlreadyExistsWithName(
			String customerAlreadyExistsWithName[]);

	public String customerAlreadyExistsWithNameAndNo(
			String customerAlreadyExistsWithNameAndNo[]);

	public String customerAlreadyExistsWithNumber(
			String customerAlreadyExistsWithNumber);

	public String customerBalance(String customerBalance);

	public String customerBillTo(String customerBillTo);

	public String customerCredit(String customerCredit);

	public String customerPayment(String customer);

	public String customerPrePayment(String prepayment);

	public String customerRefund(String customerRefund);

	public String customerRefunds(String customerRefunds);

	public String customerSince(String customerSince);

	public String customerStatement(String customerStatement);

	public String customerTransactionHistory(String customerTransactionHistory);

	public String customerUpdatedSuccessfully(String customerUpdated);

	public String customerVATCode(String customerVATCode);

	public String customers(String customers);

	public String customersAndReceivable(String customersAndReceivable);

	public String customersHome(String customersHome);

	public String customersList(String customersList);

	public String duplicationCustomerNameNotAllowed(String dupCustomerNotAllowed);

	public String euVATExemptCustomer(String euvVATExemptCustomer);

	public String failedToGetRecievePayments(String failedToGetRecievePayments);

	public String failedToLoadCustomerHome(String failedToLoadCustomerHome);

	public String getCustomersRefundListViewHeading(
			String getCustomersRefundListViewHeading);

	public String manageCustomerGroup(String manageCustomerGroup);

	public String customerTo(String customerName);

	public String customerID(String customerID);

	public String customerFrom(String cuString);

	public String mergeCustomers(String mergeCustomers);

	public String mostProfitableCustomer(String mostProfitableCustomer);

	public String mostProfitableCustomers(String mostProfitableCustomers);

	public String msg(String msg);

	public String nameOrNumberIsAlreadyInUse(String nameOrNumberIsAlreadyInUse);

	public String newCustomer(String newCustomer);

	public String newCustomerCreated(String newCustomerCreated);

	public String newCustomerGroup(String newCustomerGroup);

	public String newCustomerPayment(String newCustomerPayment);

	public String noCreditsforthiscustomer(String noCreditsforthiscustomer);

	public String noEstimatesAndSalesOrderForCustomer(
			String noEstimatesAndSalesOrderForCustomer);

	public String noEstimatesForCustomer(String noEstimatesForCustomer);

	public String noQuotesAndSalesOrderForCustomer(
			String noQuotesAndSalesOrderForCustomer);

	public String noQuotesForCustomer(String noQuotesForCustomer);

	public String onlineSalesByCustomerSummary(
			String onlineSalesByCustomerSummary);

	public String salesByCustomerSummary(String salesByCustomerSummary);

	public String toAddCustomerGroup(String toAddCustomerGroup);

	public String unableToGet(String string);

	public String useCustomerId(String useCustomerId);

	public String youcannotchangeaCustomertypetoVendortype(
			String youcannotchangeaCustomertypetoVendortype, String msgString);

	public String howDoYouReferYourCustoemrs();

	public String journalEntryCustomer(String journalEntryCustomerString);

	public String bothCustomerAndVendor(String CustomerString,
			String vendorString);

	public String addANewVendorPayment(String addANewVendorPaymentString);

	public String amountDueToVendor(String amountDueToVendor);

	public String amountsDueToVendor(String amountsDueToVendorsString);

	public String duplicationOfVendorNameAreNotAllowed(
			String duplicationOfVendorString);

	public String euVATExemptVendor(String euVATExemptVendorString);

	public String newVendorPrePayment(String newVendorPrePaymentString);

	public String noPurchaseOrderForVendor(String noPurchaseOrderForVendorString);

	public String vendorItemNo(String vendorItemNoString);

	public String vendorPay(String vendorPayString);

	public String vendorPaymentList(String vendorPaymentListString);

	public String vendorsGroupList(String vendorsGroupListString);

	public String todaysVendorPayments(String todaysVendorPayments);

	public String useVendorId(String useVendorIdString);

	public String noBillsAreAvailableFirstAddABill(String noBillsString);

	public String accandaccumulatedDepreciationAccShouldnotbesame(
			String accandaccumulatedDepreciationString);

	public String account(String accountString);

	public String accountCategory(String acountCategoryString);

	public String accountFrom(String accountFromString);

	public String accountInformation(String accountInformationString);

	public String accountName(String accountName);

	public String accountNumber(String accountNumber);

	public String accountReceivable(String accountReceivable);

	public String accountType(String accounttype);

	public String accountUpdationFailed(String accountUpdationFailed);

	public String accounterCategoryList(String accounterCategoryList);

	public String accounts(String accountsString);

	public String accountsPayable(String accountsPayableString);

	public String addBankAccount(String addBankAccount);

	public String addanewAccount(String addanewAccount);

	public String adjustmentAccount(String accountAString);

	public String alreadyAccountExist(String alreadyAccountExistString);

	public String assetAccountYouHaveSelectedNeedsLinkedAccumulatedDepreciationAccount(
			String msgString);

	public String accountRegister(String accRegisterString);

	public String accumulatedDepreciationAccount(
			String accumulatedDepreciationAccountString);

	public String bankAccount(String bankString);

	public String bankAccountInformation(String bankacInfoString);

	public String bankAccountNo(String bankAccNoString);

	public String bankAccountNumber(String bankString);

	public String bankAccountNumberColon(String bankAccountNumberColonString);

	public String bankAccountType(String bankAccountString);

	public String bankAccounts(String bankAccountsString);

	public String cashAccount(String cashACString);

	public String cashBackAccount(String cashString);

	public String cashBackAccountShouldBeSelected(String msgString);

	public String cashFlowCategory(String cashFlowCategoryString);

	public String categoryList(String categoryListString);

	public String chartOfAccount(String chartOfAccountString);

	public String chartOfAccounts(String chartOfAccountsString);

	public String chartOfAccountsInformation(
			String chartOfAccountsInformationString);

	public String conversionAccount(String conversionAccounStringt);

	public String createanyadditionalbankaccounts(String msgString);

	public String creditCardAccountInformation(String msgString);

	public String depreciationAccount(String depreciationAccountString);

	public String discountAccount(String discountAccountString);

	public String duplicationOfAccountsIsNotAllowed(String msgString);

	public String expenseAccount(String expenseAccountString);

	public String failedtoGetListofAccounts(String msgString);

	public String failedtogetAccountReceivablechartvalues(String msgString);

	public String failedtogetBankaccountchartvalues(String msgString);

	public String financeCategoryList(String financeCategoryListString);

	public String financialAccount(String financialAccountString);

	public String fromAccount(String fromAccountString);

	public String goToAccountReceivable(String msgString);

	public String goToAccountsPayable(String msgString);

	public String incomeAccount(String incomeAccountString);

	public String incomeAndExpenseAccounts(String msgString);

	public String liabilityAccount(String liabilityAccountString);

	public String mergeFinancialAccounts(String msgString);

	public String mergeVendors(String vendorString);

	public String mergeAccounts(String accountString);

	public String newAccount(String msgString);

	public String newBankAccount(String msgString);

	public String newBankCategory(String msgString);

	public String noDiscountAccountSelected(String msgString);

	public String nominalCodeItem(String nominalCodeItemString);

	public String otherNominalAccounts(String msgString);

	public String panNumber(String panNumberString);

	public String pleaseChooseAnAccount(String msgString);

	public String pleaseSelectPayFromAccount(String msgString);

	public String purchaseLiabilityAccount(String msgString);

	public String reconcileAccount(String msgString);

	public String salesLiabilityAccount(String msgString);

	public String selectAccountType(String msgString);

	public String selectPaymentMethod(String msgString);

	public String showAllAccounts(String msgString);

	public String stockAdjustmentAccount(String msgString);

	public String stockItemExpenseAccount(String msgString);

	public String stockItemIncomeAccont(String msgString);

	public String taxAccount(String taxString);

	public String theAccountNumberchosenisincorrectPleaschooseaNumberbetween(
			String msgString);

	public String theAccountNumberchosenisincorrectPleasechooseaNumberbetween1100and1179(
			String msgstring);

	public String thisIsConsideredACashAccount(String msgString);

	public String toAccount(String toAccountString);

	public String toTransferFunds(String msgString);

	public String transactionDetailByAccount(String msgString);

	public String transactionDetailsByAccount(String msgString);

	public String unableToFetchAccountsList(String msgString);

	public String useAccountNos(String msgString);

	public String vatAccount(String vatAccountString);

	public String writeOffAccount(String writeOffAccountString);

	public String youCantDeleteThisAccount(String msgString);

	public String reviewIncomeAndExpensesAccounts(String msgString);

	public String howIsYourCompanyOrganizedDesc(String msgString);

	public String selectRequiredAccounts(String msgString);

	public String howDoYouReferYourAccounts();

	public String taxAgencyFinanceAcount(String msgString);

	public String cannotUsePurchaseItem(String msgString);

	public String cannotUseSalesItem(String msgString);

	public String journalEntryAccount(String msgString);

	public String invalidNumber(String msgString);

	public String expenseInformation(String msgString);

	public String customerName(String customer);

	public String accountPayable(String account);

	public String customerLists(String customer);

	public String customerNumber(String customer);

	public String customerGroupList(String customer);

	public String customerCreditNote(String customer);

	public String customerGroup(String customer);

	public String customerGroupAlreadyExists(String customer);

	public String customerList(String customer);

	public String customerRefundIssued(String customer);

	public String customerAlreadyExistsWithNameAndNo(String customer);

	public String customerAlreadyExistsWithName(String customer);

	public String customerNumberShouldBeNumber(String customer);

	public String customerNumberShouldBePos(String customer);

	public String pleaseSelectCustomer(String customer);

	public String customerOrderNumber(String customer);

	public String customerOrderNo(String customer);

	public String vendorAlreadyExistsWithTheNameAndNumber(String vendor);

	public String statementDescription();

	public SafeHtml setupComplitionDesc();

	public String vendorAlreadyExistsWithTheName(String vendor);

	public String pleaseEnterVendorNumberItShouldNotBeEmpty(String vendor);

	public String vendorAlreadyExistsWithTheNumber(String vendor);

	public String vendorNumberShouldBeNumber(String vendor);

	public String vendorNumberShouldBePos(String vendor);

	public String addANewVendor(String msgString);

	public String failedToLoadVendorsHome(String msgString);

	public String manageVendorGroup(String manageVendorGroupString);

	public String newVendor(String newVendorString);

	public String newVendorCreated(String newVendorString);

	public String newVendorGroup(String newVendorGroupString);

	public String noCreditsForThisVendor(String msgString);

	public String noPurchaseOrderAndItemReceiptForVendor(String venString);

	public String noPurchaseOrdersForVendor(String vendorString);

	public String pleaseSelectTheVendor(String msgString);

	public String pleaseSelectVendor(String selectVendorString);

	public String preferredVendor(String msgString);

	public String purchaseByVendorDetail(String purchaseByVendorString);

	public String purchaseByVendorSummary(String venSummaryString);

	public String toAddVendorGroup(String vendorString);

	public String vendor1099(String msgString);

	public String vendorAddress(String vendorAddressString);

	public String vendorBalance(String balanceString);

	public String vendorBendor(String bendorString);

	public String vendorBill(String billString);

	public String vendorBlah(String msgString);

	public String vendorCredit(String vendorCreditString);

	public String vendorCreditMemo(String vendorCreditString);

	public String vendorGroup(String vendorGroupString);

	public String vendorGroupAlreadyExists(String msgString);

	public String vendorGroupList(String vendorGroupListString);

	public String vendorHome(String vendorHomeString);

	public String vendorItems(String vendorItemsString);

	public String vendorInformation(String vendorInformationString);

	public String vendorList(String vendorListString);

	public String vendorLists(String vendorListsString);

	public String vendorMemo(String vendorMemoString);

	public String vendorName(String vendorNameString);

	public String vendorOrderNo(String vendorString);

	public String vendorPayment(String vendorPaymentString);

	public String vendorPaymentIssued(String vendorString);

	public String vendorPaymentMethod(String msgString);

	public String vendorPayments(String vendorPaymentsString);

	public String vendorPaymentsList(String msgString);

	public String vendorPrePayment(String vendorPrePaymentString);

	public String vendorPrePayments(String msgString);

	public String vendorProductNo(String vendorProductNoString);

	public String vendorServiceNo(String msgString);

	public String vendorServices(String vendorServicesString);

	public String vendorSince(String vendorSinceString);

	public String vendorTransactionHistory(String transactionString);

	public String vendorUpdatedSuccessfully(String msgString);

	public String vendorVatCode(String vendorVatCodeString);

	public String vendorTDSCode(String vendorTdsCodeString);

	public String vendorWhendor(String vendorWhendorString);

	public String vendorpay(String vendorpayString);

	public String vendors(String vendorsString);

	public String vendorsAndPayables(String vendorsString);

	public String vendorsHome(String vendorsHomeString);

	public String billstrackingmanageCashflowStep1(String cashFlowString);

	public String vatIDValidationDesc();

	public String salesByCustomerDetail(String custString);

	public String accounterUseYourForms(String custString, String vendorString);

	public String addaNewVendor(String vendorString);

	public String howDoYouReferYourVendors();

	public String vendorsList(String vendorString);

	public String accountNo(String accountNoString);

	public String accountRangeExceededPlsChoose0001and999(String accString);

	public String accountRangeExceededPlsChoose1001and1999(String msgString);

	public String accountRangeExceededPlsChoose2001and2999(String accString);

	public String accountRangeExceededPlsChoose3001and3999(String accString);

	public String accountRangeExceededPlsChoose4001and4999(String accString);

	public String accountRangeExceededPlsChoose5001and5999(String accString);

	public String accountRangeExceededPlsChoose6001and6999(String msgString);

	public String accountRangeExceededPlsChoose7001and7999(String msgString);

	public String accountRangeExceededPlsChoose9001and9500(String accString);

	public String accountRangeExceededPlsChoose9501and9999(String accString);

	public String accountToDebitForSale(String msgString);

	public String accountsList(String string);

	public String createNewAccount(String accString);

	public String defaultAccounts(String defaultAccString);

	public String jobresellAccount(String jobresellAccountString);

	public String billstrackingdescription(String accString);

	public String makeDepositCashBackAccount(String accString);

	public String theFinanceCategoryNoShouldBeBetween1100And1179(
			String accString);

	public String theFinanceCategoryNoShouldBeBetween(String accountString);

	public String accrualBasis(String accountString);

	public String timetrackingflowStep1(String customer);

	public String getindustryhead();

	public String trackingtimehead();

	public String shouldNotbeZero(String amountString);

	public String shouldBePositive(String name);

	public String noTransactionsTo(String paySalesTax);

	public String vendorID(String vendor);

	public String vendorTo(String vendorTo);

	public String vendorFrom(String vendorFrom);

	public String notMove(String customer);

	public String accountTo(String account);

	public String mergeDescription(String desc);

	public String location(String location);

	public String useTerminologyFor(String location);

	public String locationTracking(String location);

	public String selectWhichWeHaveInOurCompanyOrAddNew(String comboType);

	public String selectWhichWeHaveInOurCompany(String comboType);

	public String selectDateWhenTransactioCreated(String transaction);

	public String giveTheNameAccordingToYourID(String transaction);

	public String selectTypeOfThis(String Account);

	public String writeCommentsForThis(String transaction);

	public String selectDateUntilDue(String transaction);

	public String customerMeaning(String customer);

	public String vendorsSelected(String vendor);

	public String SelectVendorsToTrack1099(String vendor);

	public String accountsSelected(String account);

	public String companyCustomers(String customer);

	public String companySuppliers(String vendor);

	public String setupVendorsAndAccounts(String vendor, String account);

	public String accountNumberToolTipDesc(String starting, String ending);

	public String selectDateOfBirth(String viewName);

	public String selectDateOfHire(String viewName);

	public String valueCannotBe0orlessthan0(String amount);

	public String salesPersonExpenseAccountTooltip(String account);

	public String salesPersonJobToolTip();

	public String giveYourCompany(String type);

	public String salesPersonMeaning();

	public String giveOpeningBalanceToThis(String viewName);

	public String giveNoTo(String viewName);

	public String giveOf(String fieldName, String transactionName);

	public String clickThisTo(String text, String viewName);

	public String clickThisObjToOpen(String objType, String resultant);

	public String allTransactionDetails(String text);

	public String clickThisToOpenNew(String viewName);

	public String clickThisToOpen(String next);

	public String clickToAddItem(String viewName);

	public String clickToAddContact(String viewName);

	public String giveOfYour(String lowerCase, String company);

	public String setupCancelMessgae();

	public SafeHtml registeredDetails();

	public SafeHtml tradingDetails();

	public SafeHtml companyOtherDetails();

	public SafeHtml bankingAndOtherFinancialDetails();

	public SafeHtml customerAndvendorSettings();

	public SafeHtml doYouUseAndHowDoYouRefer();

	public SafeHtml ageingAndSellingDetails();

	public SafeHtml adminInfo();

	public SafeHtml basicInfo();

	public SafeHtml comapnyInfo();

	public SafeHtml otherDetails();

	public String getSalesByLocationDetails(String locationString);

	public SafeHtml employeeSettings();

	public String venodrsThatMeetThreshold(String vendor);

	public String vendorsBelowThreshold(String vendor);

	public String non1099Vendors(String vendor);

	public SafeHtml changeVendors(String vendor);

	public SafeHtml changeAccounts(String account);

	public String assignAccounts(String account);

	public String dearCustomer(String custMsg);

	public String salesByLocationSummary(String locationString);

	public SafeHtml trackBills();

	public String invoiceMailMessage(String customer, String number,
			ClientFinanceDate date);

	public String selectAccountsToAssign(String account);

	public String to(String string, String string2);

	public String MISC1099TransactionDetailByVendor(String vendor);
}
