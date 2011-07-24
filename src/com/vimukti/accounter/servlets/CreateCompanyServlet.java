package com.vimukti.accounter.servlets;

import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserPermissions;
import com.vimukti.accounter.web.client.data.BizantraConstants;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class CreateCompanyServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String view = "/sites/signup.jsp";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Company company = doCreateCompany(request);
		if (company != null) {
			initSessionFactory(company, request);
			request.setAttribute("successmessage",
					"Company has been created sucessfully");
			dispatchView(request, response, view);
		} else {
			dispatchView(request, response, view);
		}
	}

	private void initSessionFactory(Company company,
			HttpServletRequest request) {
		try {
			init(company, request);
		} catch (Throwable e) {
			throw new ExceptionInInitializerError(e);
		}

	}

	private void init(Company company, HttpServletRequest request) {
		// HibernateUtil.rebuildSessionFactory();
		Session session = HibernateUtil.openSession(
				company.getCompanyDomainName(), true);
		// Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(company);
			CollaberIdentity identity = new CollaberIdentity(
					RolePermissions.ADMIN, SecureUtils.createID(),
					company.getCompanyDomainName());
			identity.setEncryptedID(Security.getBytes(Security
					.createSpaceEncKey()));
			identity.setEmailID(request.getParameter("emailId").toLowerCase()
					.trim());
			identity.setPassword(request.getParameter("password"));
			identity.setPasswordChanged(true);
			// identity.setCompanyName(company.getCompanyDomainName());
			initializeBizantraUserPreference(identity);
			session.save(identity);

			// createDefaultSpace(identity, session, false, request, company);

			// createPersonalSpace(identity, session, true, request);

			// createHrWorkspace(identity, session);
			// createHolidayProcessWorkspace(identity, session);

			createFinanceSpace(identity, session, request, company);

			// SpaceGroup defaultGroup = identity
			// .getSpaceGroup(BizantraConstants.DEFAULT_GROUP_NAME);
			// defaultGroup.getSpaces().add(hrWorkspace);
			// defaultGroup.getSpaces().add(bizantraSpace);
			// defaultGroup.getSpaces().add(finaceSpace);
			// defaultGroup.getSpaces().add(marketingSpace);
			// defaultGroup.getSpaces().add(holidayCalendar);
			// defaultGroup.getSpaces().add(purchasesSpace);
			// defaultGroup.getSpaces().add(salesSpace);
			// defaultGroup.getSpaces().add(holidayProcessWorkSpace);
			//
			// session.save(defaultGroup);
			session.saveOrUpdate(identity);
			UsersMailSendar.sendMailToDefaultUser(identity,
					company.getCompanyDisplayName());
			try {
				tx.commit();
			} catch (RuntimeException re) {
				try {
					tx.rollback();
				} catch (RuntimeException re2) {

				}
			}

			// Create Attachment Directory for company
			File file = new File(ServerConfiguration.getAttachmentsDir(company
					.getCompanyDomainName()));

			if (!file.exists()) {
				file.mkdir();
			}

		} catch (HibernateException e) {
			e.printStackTrace();
			// deleteCompany(company.getCompanyDomainName());
		} finally {
			session.close();
		}
	}

	private void deleteCompany(String companyDomainName) {
		Session session = HibernateUtil.openSession(LOCAL_DATABASE);
		Company companyByDomainName = getCompanyByDomainName(companyDomainName);
		session.delete(companyByDomainName);
		// Delete the schema
		Query query = session
				.createSQLQuery("DROP SCHEMA " + companyDomainName);
		session.close();

	}

	private Company getCompanyByDomainName(String domainName) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("get.company.from.bizantra");
		Company company = (Company) query.uniqueResult();
		return company;

	}

	private ClientUser createDefaultUser(HttpServletRequest request,
			String identityID) {
		ClientUser admin = new ClientUser();
		admin.setID(identityID);
		admin.setFirstName(request.getParameter("firstName"));
		admin.setLastName(request.getParameter("lastName"));
		admin.setFullName(admin.getFirstName() + " " + admin.getLastName());
		admin.setEmailId(request.getParameter("emailId").toLowerCase().trim());
		admin.setUserRole(RolePermissions.ADMIN);
		admin.setActive(true);
		admin.setCanDoUserManagement(true);
		admin.setAdmin(true);
		ClientUserPermissions permissions = new ClientUserPermissions();
		permissions.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		permissions.setTypeOfInvoices(RolePermissions.TYPE_YES);
		permissions.setTypeOfExpences(RolePermissions.TYPE_APPROVE);
		permissions.setTypeOfSystemSettings(RolePermissions.TYPE_YES);
		permissions.setTypeOfViewReports(RolePermissions.TYPE_YES);
		permissions.setTypeOfPublishReports(RolePermissions.TYPE_YES);
		permissions.setTypeOfLockDates(RolePermissions.TYPE_YES);
		admin.setPermissions(permissions);
		// admin.setAddress1(request.getParameter("address"));
		// admin.setCity(request.getParameter("city"));
		// admin.setCountry(request.getParameter("country"));
		admin.setCompany(request.getParameter("companyName"));
		// admin.setZipCode(request.getParameter("zip"));
		// admin.setProvince(request.getParameter("provence"));
		// admin.setMobile(request.getParameter("mobile"));
		// admin.setWorkphone(request.getParameter("workingPh"));
		// admin.setAcessPermissions(BizantraConstants.SUPER_USER_ROLE);

		return admin;
	}

	

	private Company doCreateCompany(HttpServletRequest request) {
		Company company = getCompany(request);
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);

		Transaction tx = session.beginTransaction();
		if (!validation(request)) {
			return null;
		}
		try {

			// TODO write sql query for creating database in .xml file
			Query query = session.createSQLQuery("CREATE SCHEMA "
					+ company.getCompanyDomainName());
			query.executeUpdate();
			session.save(company);
			tx.commit();
			return company;
		} catch (RuntimeException re) {
			re.printStackTrace();
			tx.rollback();
			request.setAttribute("errormessage",
					"Company creation failed, please try with different company name.");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			request.setAttribute("errormessage",
					"Company creation failed, please try with different company name.");
		} finally {
			session.close();
		}
		return null;
	}

	private Company getCompany(HttpServletRequest request) {
		String companyName = request.getParameter("companyName");
		// String noOfUsers = request.getParameter("nooofusers");
		// String noOfLiteUsers = request.getParameter("noofliteusers");
		String address = request.getParameter("address");
		String city = request.getParameter("city");
		String country = request.getParameter("country");
		String zip = request.getParameter("zip");
		String province = request.getParameter("provence");
		Date expirationDate = new Date();
		expirationDate.setYear(expirationDate.getYear() + 10);
		Company company = new Company(companyName, companyName,
				expirationDate);
		// company.setTotalNoOfUsers(Integer.parseInt(noOfUsers));
		// company.setTotalNoOfLiteUsers(Integer.parseInt(noOfLiteUsers));
		company.setTotalSize(1024 * 1024 * 1024);
		// company.setID(SecureUtils.createID());
		// company.setCompanyDomainName(name);
		// company.setCompanyDisplayName(name);
		// company.setSubscriptionType("FULL");
		// company.setInitialUsers(Integer.parseInt(noOfUsers));
		// company.setInitialSize(1073741824);
		// company.setTotalNoOfUsers(Integer.parseInt(noOfUsers));
		company.setBizantraVersion(Integer.parseInt(request
				.getParameter("companyType")));
		company.setAddress(address);
		company.setCity(city);
		company.setCountry(country);
		company.setZip(zip);
		company.setProvince(province);
		company.setDateStyle(2);
		if (company.getDateStyle() == 2) {
			company.setCountryCode("United Kingdom");
		} else {
			// TODO HAVE TO BE SET COUNTRY CODE BASE DON DATE STYLE PRESENT
			// GIVING DEFAULT ONE IS 'United Kingdom'
			company.setCountryCode("United Kingdom");
		}
		// company.setTime("Y");
		// company.setPeriod(1);
		// company.setExpirationDate(company.getExpirationDateByDate("Y"));

		StringBuilder holidayStartString = new StringBuilder();
		holidayStartString.append(request.getParameter("startDateDate"));
		holidayStartString.append(", ");
		holidayStartString.append(request.getParameter("startDateMonth"));
		company.setHolidayStartDate(holidayStartString.toString());

		company.setSubsType(BizantraConstants.TRIAL_SUBSCRIPTION);
		company.setSubscriberEmail(request.getParameter("emailId")
				.toLowerCase().trim());

		return company;
	}

	public void dispatchView(HttpServletRequest request,
			HttpServletResponse response, String view) {
		try {
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher(view);
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean validation(HttpServletRequest request) {
		Boolean flag = true;
		String message = "";
		// String firstName = request.getParameter("firstName");
		// String lastName = request.getParameter("lastName");
		String companyName = request.getParameter("companyName");
		// String emailId = request.getParameter("emailId");
		// String password = request.getParameter("password");
		// String confirmPassword = request.getParameter("confirmPassword");
		// boolean isFieldsEmpty = firstName == null || firstName.isEmpty()
		// || lastName == null || lastName.isEmpty()
		// || companyName == null || companyName.isEmpty()
		// || emailId == null || emailId.isEmpty() || password == null
		// || password.isEmpty() || confirmPassword == null
		// || confirmPassword.isEmpty();
		// if (isFieldsEmpty) {
		// message = "Please enter the mandatory fields";
		// flag = false;
		// }

		// if (companyName == null || companyName == "") {
		// message = "Company name is not valid.";
		// flag = false;
		// }
		if (BizantraService.isCompanyExits(companyName)) {
			if (!message.isEmpty())
				message = message + ", Company with this name is already exist";
			else
				message = "Company with this name is already exist";
			flag = false;
		}
		// if (request.getParameter("nooofusers") == null
		// || request.getParameter("nooofusers") == "") {
		// message = "Company must have a number of User";
		// flag = false;
		// }
		// if (!request.getParameter("nooofusers").matches("[0-9]+")) {
		// message = "Company user number must be a positive numaric number";
		// flag = false;
		// }
		// String companypwd = request.getParameter("companypwd");
		// if (!companypwd.equals("ltsDf835")) {
		// message = "Company Password is Wrong";
		// flag = false;
		// }

		// if (emailId != null
		// && !emailId.isEmpty()
		// && !emailId
		// .matches("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$"))
		// {
		// if (!message.isEmpty())
		// message = message + ", Invalid Email Id for default user";
		// else
		// message = "Invalid Email Id for default user";
		// flag = false;
		// }
		// if (password != null && !password.isEmpty() && password.length() < 6)
		// {
		// if (!message.isEmpty())
		// message = message + ", Password strength should be minimum 6";
		// else
		// message = "Password strength should be minimum 6";
		// flag = false;
		// } else if (password != null && !password.isEmpty()
		// && confirmPassword != null && !confirmPassword.isEmpty()
		// && !password.equals(confirmPassword)) {
		// if (!message.isEmpty())
		// message = message + ", Passwords are not matched";
		// else
		// message = "Passwords are not matched";
		// flag = false;
		// }
		request.setAttribute("errormessage", message);
		return flag;
	}

	private void initializeBizantraUserPreference(CollaberIdentity identity) {
		BizantraUserPreferences userPreferences = new BizantraUserPreferences();
		userPreferences.setDefaultPreferences();
		identity.setUserPreferences(userPreferences);

	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// response.sendRedirect(view);
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher(view);
		dispatcher.forward(request, response);
	}
}
