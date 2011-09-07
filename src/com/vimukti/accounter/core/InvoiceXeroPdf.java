package com.vimukti.accounter.core;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.MiniTemplator;

/**
 * this class is used to generate Invoice report in PDF format
 * 
 * @author vimukti28
 * 
 */
public class InvoiceXeroPdf implements PrintTemplete {
	private Invoice invoice;
	private BrandingTheme brandingTheme;
	private int maxDecimalPoints;
	// private static final String templateFileName = "templetes" +
	// File.separator
	// + "InvoiceTemplete.html";
	private Company company;

	public InvoiceXeroPdf(Invoice invoice, BrandingTheme brandingTheme,
			Company company) {
		this.invoice = invoice;
		this.brandingTheme = brandingTheme;
		this.company = company;
		this.maxDecimalPoints = getMaxDecimals(invoice);

	}

	public String getFileName() {
		return "Invoice_" + this.invoice.getNumber();
	}

	public String getTempleteName() {

		String templeteName = brandingTheme.getInvoiceTempleteName();

		return "templetes" + File.separator + "ProfessionalInvoice" + ".html";

	}

	@Override
	public String getPdfData() {
		String outPutString = "";
		MiniTemplator t;

		// TODO for displaying the company address

		String cmpAdd = "";
		Address cmpTrad = company.getTradingAddress();
		if (cmpTrad.getType() == Address.TYPE_COMPANY_REGISTRATION) {
			if (cmpTrad != null)
				cmpAdd = forUnusedAddress(cmpTrad.getAddress1(), false)
						+ forUnusedAddress(cmpTrad.getStreet(), false)
						+ forUnusedAddress(cmpTrad.getCity(), false)
						+ forUnusedAddress(cmpTrad.getStateOrProvinence(),
								false)
						+ forUnusedAddress(cmpTrad.getZipOrPostalCode(), false)
						+ forUnusedAddress(cmpTrad.getCountryOrRegion(), false);
		}

		if (cmpAdd.equals("")) {
			// String contactDetails = brandingTheme.getContactDetails() != null
			// ? brandingTheme
			// .getContactDetails() : this.company.getName();
			cmpAdd = forNullValue(company.getTradingName());
		} else {
			cmpAdd = forNullValue(company.getTradingName()) + "<br/>" + cmpAdd;
		}

		try {
			t = new MiniTemplator(getTempleteName());
			String image = getImage();

			// setting logo Image
			if (brandingTheme.isShowLogo()) {

				t.addBlock("logo");
			}

			// TODO for company trading name and address
			t.setVariable("comapany", cmpAdd);

			// TODO For setting the Contact Details
			String contactDetails = forNullValue(brandingTheme
					.getContactDetails());
			t.setVariable("companyDetails", contactDetails);

			// setting invoice number

			String invNumber = forNullValue(invoice.getNumber());
			if (invNumber.trim().length() > 0) {

				t.setVariable("invoiceNumber", invNumber);

				t.addBlock("invNumberHead");
			}

			// setting invoice date

			String invDate = invoice.getDate().toString();
			if (invDate.trim().length() > 0) {

				t.setVariable("invoiceDate", invDate);

				t.addBlock("invDateHead");
			}

			// for vat details
			String vatLabel = getVendorString("VAT Rate", "Tax Rate");
			t.setVariable("VATRate", vatLabel);

			String vatAmountLabel = getVendorString("VAT Amount", "Tax Amount");
			t.setVariable("VATAmount", vatAmountLabel);

			// for checking the show Column Headings
			if (brandingTheme.isShowColumnHeadings()) {

				if (brandingTheme.isShowTaxColumn()) {
					t.addBlock("vatBlock");
				}
				t.addBlock("showLabels");
			}

			// setting item description quantity, unit price, total price and
			// vat details
			List<TransactionItem> transactionItems = invoice
					.getTransactionItems();
			for (Iterator iterator = transactionItems.iterator(); iterator
					.hasNext();) {
				TransactionItem item = (TransactionItem) iterator.next();

				String description = forNullValue(item.getDescription());
				String qty = forZeroAmounts(getDecimalsUsingMaxDecimals(item
						.getQuantity().getValue(), null, maxDecimalPoints));
				String unitPrice = forZeroAmounts(largeAmountConversation(item
						.getUnitPrice()));
				String totalPrice = largeAmountConversation(item.getLineTotal());
				String vatRate = String.valueOf(Utility.getVATItemRate(
						item.getTaxCode(), true));
				String vatAmount = getDecimalsUsingMaxDecimals(
						item.getVATfraction(), null, 2);

				t.setVariable("description", description);
				t.setVariable("quantity", qty);
				t.setVariable("itemUnitPrice", unitPrice);
				t.setVariable("itemTotalPrice", totalPrice);
				if (brandingTheme.isShowTaxColumn()) {
					t.setVariable("itemVatRate", vatRate);
					t.setVariable("itemVatAmount", vatAmount);
					t.addBlock("vatValueBlock");
				}
				t.addBlock("invoiceRecord");
			}
			// for displaying sub total, vat total, total
			String subtotal = largeAmountConversation(invoice.getNetAmount());
			t.setVariable("subTotal", subtotal);
			if (brandingTheme.isShowTaxColumn()) {

				String vatlabel = getVendorString("VAT Total", "Tax Total");
				t.setVariable("vatlabel", vatlabel);
				t.setVariable("vatTotal", largeAmountConversation((invoice
						.getTotal() - invoice.getNetAmount())));
				t.addBlock("VatTotal");
			}
			String total = largeAmountConversation(invoice.getTotal());
			t.setVariable("total", total);
			t.setVariable("blankText", invoice.getMemo());
			t.addBlock("itemDetails");

			t.setVariable("dueDate", invoice.getDueDate().toString());
			t.addBlock("dueDateDetails");

			t.setVariable("termsAndPaymentAdvice",
					brandingTheme.getTerms_And_Payment_Advice());
			t.addBlock("termsAndAdvice");

			String email = forNullValue(brandingTheme.getPayPalEmailID());
			if (email.trim().length() > 0) {
				t.setVariable("email", email);
				t.addBlock("paypalemail");
			}

			// setting the theme styles
			t.setVariable("fontStyle", brandingTheme.getFont());
			t.setVariable("font", brandingTheme.getFontSize());
			t.setVariable("bottomMargin",
					String.valueOf(brandingTheme.getBottomMargin()));
			t.setVariable("topMargin",
					String.valueOf(brandingTheme.getTopMargin()));
			t.setVariable("title", brandingTheme.getOverDueInvoiceTitle());
			// t.setVariable("addressPadding",
			// String.valueOf(brandingTheme.getAddressPadding()));

			// TODO for displaying regestration address and Company Registration
			// Number
			String regestrationAddress = "";
			Address reg = company.getRegisteredAddress();
			if (reg.getType() == Address.TYPE_COMPANY) {
				if (reg != null)
					regestrationAddress = ("&nbsp;Registered Address: "
							+ reg.getAddress1()
							+ forUnusedAddress(reg.getStreet(), true)
							+ forUnusedAddress(reg.getCity(), true)
							+ forUnusedAddress(reg.getStateOrProvinence(), true)
							+ forUnusedAddress(reg.getZipOrPostalCode(), true)
							+ forUnusedAddress(reg.getCountryOrRegion(), true) + ".");
			}

			regestrationAddress = (company.getFullName() + regestrationAddress + ((company
					.getRegistrationNumber() != null && !company
					.getRegistrationNumber().equals("")) ? "<br/>Company Registration No: "
					+ company.getRegistrationNumber()
					: ""));

			if (regestrationAddress != null
					&& regestrationAddress.trim().length() > 0) {
				if (brandingTheme.isShowRegisteredAddress()) {

					t.setVariable("regestrationAddress", regestrationAddress);
					t.addBlock("regestrationAddress");
				}
			}

			t.addBlock("theme");

			outPutString = t.getFileString();
			return outPutString;
		} catch (Exception e) {
			System.err.println("error in......." + e.getMessage());
		}
		return null;
	}

	/*
	 * For Max DecimalPoints
	 */
	private int getMaxDecimals(Invoice inv) {
		String qty;
		String max;
		int temp = 0;
		for (TransactionItem item : inv.getTransactionItems()) {
			qty = String.valueOf(item.getQuantity());
			max = qty.substring(qty.indexOf(".") + 1);
			if (!max.equals("0")) {
				if (temp < max.length()) {
					temp = max.length();
				}
			}

		}
		return temp;
	}

	public String forUnusedAddress(String add, boolean isFooter) {
		if (isFooter) {
			if (add != null && !add.equals(""))
				return ", " + add;
		} else {
			if (add != null && !add.equals(""))
				return add + "<br/>";
		}
		return "";
	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	private String getLogoAlignment() {
		String logoAlignment = null;
		if (brandingTheme.getPageSizeType() == 1) {
			logoAlignment = "left";
		} else {
			logoAlignment = "right";
		}
		return logoAlignment;
	}

	private String getVendorString(String forUk, String forUs) {
		return company.getAccountingType() == company.ACCOUNTING_TYPE_US ? forUs
				: forUk;
	}

	private String getDecimalsUsingMaxDecimals(double quantity, String amount,
			int maxDecimalPoint) {
		String qty = "";
		String max;
		if (maxDecimalPoint != 0) {
			if (amount == null)
				qty = String.valueOf(quantity);
			else
				qty = amount;
			max = qty.substring(qty.indexOf(".") + 1);
			if (maxDecimalPoint > max.length()) {
				for (int i = max.length(); maxDecimalPoint != i; i++) {
					qty = qty + "0";
				}
			}
		} else {
			qty = String.valueOf((long) quantity);
		}

		String temp = qty.contains(".") ? qty.replace(".", "-").split("-")[0]
				: qty;
		return insertCommas(temp)
				+ (qty.contains(".") ? "."
						+ qty.replace(".", "-").split("-")[1] : "");
	}

	public String forZeroAmounts(String amount) {
		String[] amt = amount.replace(".", "-").split("-");
		if (amt[0].equals("0")) {
			return "";
		}
		return amount;
	}

	private static String insertCommas(String str) {

		if (str.length() < 4) {
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) + ","
				+ str.substring(str.length() - 3, str.length());
	}

	private String largeAmountConversation(double amount) {
		String amt = Utility.decimalConversation(amount);
		amt = getDecimalsUsingMaxDecimals(0.0, amt, 2);
		return (amt);
	}

	public String getImage() {

		StringBuffer original = new StringBuffer();
		// String imagesDomain = "/do/downloadFileFromFile?";

		original.append("<img src='file:///");
		original.append(ServerConfiguration.getAttachmentsDir() + "/"
				+ company.getAccountingType() + "/"
				+ brandingTheme.getFileName());
		original.append("'/>");

		return original.toString();

	}

}
