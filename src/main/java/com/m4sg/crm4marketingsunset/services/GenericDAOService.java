package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Optional;
import com.m4c.model.entity.LogM4SG;
import com.m4c.model.entity.online.CertLogin;
import com.m4c.model.entity.sistemas.UserIntranet;
import com.m4sg.crm4marketingsunset.App;
import com.m4sg.crm4marketingsunset.core.ErrorRepresentation;
import com.m4sg.crm4marketingsunset.core.dto.AuditDTO;
import com.m4sg.crm4marketingsunset.dao.*;
import com.m4sg.crm4marketingsunset.util.Utils;
import org.hibernate.SessionFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by sistemas on 07/01/2015.
 */
public abstract class GenericDAOService {

    protected SessionFactory sessionFactoryM4C;
//    protected SessionFactory sessionFactoryOnlineDB;
    protected SessionFactory sessionFactoryInventario;
    protected CampaignDAO campaignDAO;
    protected CallCenterDAO callCenterDAO;
    protected SegmentDAO segmentDAO;
    protected MerchantDAO merchantDAO;
    protected CertificateDAO certificateDAO;
    protected FoliosCertificateDAO foliosCertificateDAO;
    protected OfferDAO offerDAO;
    protected CountryDAO countryDAO;
    protected MealPlanDAO mealPlanDAO;
    protected DestinationDAO destinationDAO;
    protected TransportationDAO transportationDAO;
    protected HookDAO hookDAO;
    protected HotelDAO hotelDAO;
    protected StateDAO stateDAO;
    protected LanguageDAO languageDAO;
    protected ReservationGroupDAO reservationGroupDAO;
    protected OfferLanguageDAO offerLanguageDAO;
    protected HotelImageDAO hotelImageDAO;
    protected HotelLanguageDAO hotelLanguageDAO;
    protected TitleDAO titleDAO;
    protected CustomerDAO customerDAO;
    protected FeatureDAO featureDAO;
    protected UserIntranetDAO userIntranetDAO;
    protected CertCustomerDAO certCustomerDAO;
    protected CertLoginDAO certLoginDAO;
    protected SaleDAO saleDAO;
    protected SubServiceDAO subServiceDAO;
    protected ContractServiceDAO contractServiceDAO;
    protected EdoCivilDAO edoCivilDAO;
    protected OfferDestinationDAO offerDestinationDAO;
    protected CustomerFeaturesDAO customerFeaturesDAO;
    protected ImageCertificateDAO imageCertificateDAO;
    protected CustomerNoteDAO customerNoteDAO;
    protected CustomerNotePkDAO customerNotePkDAO;
    protected StatusDAO statusDAO;
    protected NoteTypeDAO noteTypeDAO;
    protected CertsConfigDAO certsConfigDAO;
    protected BatchCertificatesDAO batchCertificatesDAO;
    protected BatchCertificateFoliosDAO batchCertificateFoliosDAO;
    protected PaymentDAO paymentDAO;
    protected UserDAO userDAO;
    protected BancosDAO bancosDAO;
    protected CardTypeDAO cardTypeDAO;
    protected PaymentServiceDAO paymentServiceDAO;
    protected LogM4SGDAO logM4SGDAO;
    protected DataCardDAO dataCardDAO;
    protected SellerDAO sellerDAO;
    protected ExtraBonusDAO extraBonusDAO;
    protected ReservationStatusDAO reservationStatusDAO;
    protected DepartmentDAO departmentDAO;
    protected AlertDAO alertDAO;
    protected AssistCardDAO assistCardDAO;
    protected ReservationDAO reservationDAO;
    protected PaxDAO paxDAO;
    protected ProgramDAO programDAO;
    protected DNCLDAO dnclDAO;
    protected AppApiDAO appApiDAO;
    protected CommisionReportDAO commisionReportDAO;


//    protected PromocionDAO promocionDAO;




    private final static String CAMPAIGN_DAO_TAG = CampaignDAO.class.getSimpleName();
    private final static String CALLCENTER_DAO_TAG = CallCenterDAO.class.getSimpleName();
    private final static String SEGMENT_DAO_TAG = SegmentDAO.class.getSimpleName();
    private final static String MERCHANT_DAO_TAG = MerchantDAO.class.getSimpleName();
    private final static String CERTIFICATE_DAO_TAG = CertificateDAO.class.getSimpleName();
    private final static String FOLIOSCERTIFICATE_DAO_TAG = FoliosCertificateDAO.class.getSimpleName();
    private final static String OFFER_DAO_TAG = OfferDAO.class.getSimpleName();
    private final static String COUNTRY_DAO_TAG = CountryDAO.class.getSimpleName();
    private final static String MEAL_PLAN_DAO_TAG = MealPlanDAO.class.getSimpleName();
    private final static String DESTINATION_DAO_TAG = DestinationDAO.class.getSimpleName();
    private final static String TRANSPORTATION_DAO_TAG = TransportationDAO.class.getSimpleName();
    private final static String HOOK_DAO_TAG = HookDAO.class.getSimpleName();
    private final static String HOTEL_DAO_TAG = HotelDAO.class.getSimpleName();
    private final static String STATE_DAO_TAG = StateDAO.class.getSimpleName();
    private final static String LANGUAGE_DAO_TAG = LanguageDAO.class.getSimpleName();
    private final static String RESERVATION_GROUP_DAO_TAG = ReservationGroupDAO.class.getSimpleName();
    private final static String OFFER_LANGUAGE_DAO_TAG = OfferLanguageDAO.class.getSimpleName();
    private final static String HOTEL_IMAGE_DAO_TAG = HotelImageDAO.class.getSimpleName();
    private final static String HOTEL_LANGUAGE_DAO_TAG = HotelLanguageDAO.class.getSimpleName();
    private final static String TITLE_DAO_TAG = TitleDAO.class.getSimpleName();
    private final static String CUSTOMER_DAO_TAG = CustomerDAO.class.getSimpleName();
    private final static String FEATURE_DAO_TAG = FeatureDAO.class.getSimpleName();
    private final static String CERT_CUSTOMER_DAO_TAG = CertCustomerDAO.class.getSimpleName();
    private final static String USER_INTRANET_DAO_TAG = UserIntranetDAO.class.getSimpleName();
    private final static String CERT_LOGIN_DAO_TAG= CertLoginDAO.class.getSimpleName();
    private final static String SALE_DAO_TAG= SaleDAO.class.getSimpleName();
    private final static String SUB_SERVICE_TAG= SubServiceDAO.class.getSimpleName();
    private final static String CONTRACT_SERVICE_TAG= ContractServiceDAO.class.getSimpleName();
    private final static String EDO_CIVIL_DAO_TAG = EdoCivilDAO.class.getSimpleName();
    private final static String OFFER_DESTINATION_DAO_TAG = OfferDestinationDAO.class.getSimpleName();
    private final static String CUSTOMER_FEATURES_DAO_TAG = CustomerFeaturesDAO.class.getSimpleName();
    private final static String IMAGE_CERTIFICATE_DAO_TAG = ImageCertificateDAO.class.getSimpleName();
    private final static String CUSTOMER_NOTES_DAO_TAG = CustomerNoteDAO.class.getSimpleName();
    private final static String CUSTOMER_NOTE_PK_DAO_TAG = CustomerNotePkDAO.class.getSimpleName();
    private final static String NOTE_TYPE_DAO_TAG = NoteTypeDAO.class.getSimpleName();
    private final static String STATUS_DAO_TAG = StatusDAO.class.getSimpleName();
    private final static String CERTSCONFIG_DAO_TAG = CertsConfigDAO.class.getSimpleName();
    private final static String BATCHCERTIFICATES_DAO_TAG = BatchCertificatesDAO.class.getSimpleName();
    private final static String BATCHCERTIFICATE_FOLIOS_DAO_TAG = BatchCertificateFoliosDAO.class.getSimpleName();
    private final static String PAYMENT_DAO_TAG = PaymentDAO.class.getSimpleName();
    private final static String USER_DAO_TAG = UserDAO.class.getSimpleName();
    private final static String BANCOS_DAO_TAG = BancosDAO.class.getSimpleName();
    private final static String CARD_TYPE_DAO_TAG = CardTypeDAO.class.getSimpleName();
    private final static String PAYMENT_SERVICE_DAO_TAG = PaymentServiceDAO.class.getSimpleName();
    private final static String LOGM4SG_DAO_TAG = LogM4SGDAO.class.getSimpleName();
    private final static String DATACARD_DAO_TAG = DataCardDAO.class.getSimpleName();
    private final static String SELLER_DAO_TAG = SellerDAO.class.getSimpleName();
    private final static String EXTRA_BONUS_DAO_TAG = ExtraBonusDAO.class.getSimpleName();
    private final static String RESERVATION_STATUS_DAO_TAG = ReservationStatusDAO.class.getSimpleName();
    private final static String DEPARTMENT_DAO_TAG = DepartmentDAO.class.getSimpleName();
    private final static String ALERT_DAO_TAG = AlertDAO.class.getSimpleName();
    private final static String ASSISCARD_DAO_TAG = AssistCardDAO.class.getSimpleName();
    private final static String RESERVATION_DAO_TAG = ReservationDAO.class.getSimpleName();
    private final static String PAX_DAO_TAG = PaxDAO.class.getSimpleName();
    private final static String PROGRAM_DAO_TAG = ProgramDAO.class.getSimpleName();
    private final static String DNCL_DAO_TAG = DNCLDAO.class.getSimpleName();
    private final static String APPAPI_DAO_TAG = AppApiDAO.class.getSimpleName();
    private final static String COMMISION_REPORT_DAO = CommisionReportDAO.class.getSimpleName();

//    private final static String PROMOCION_DAO_TAG = PromocionDAO.class.getSimpleName();




    public GenericDAOService(Class<?>...daos)  {
        this.sessionFactoryM4C = App.getHibernateBundleM4C().getSessionFactory();
//        this.sessionFactoryOnlineDB = App.getHibernateBundleOnlineDB().getSessionFactory();
        this.sessionFactoryInventario = App.getHibernateBundleInventarioDB().getSessionFactory();
        for(Class dao:daos){
            getInstancesOfM4C(dao);
//            getInstancesOfOnlineDB(dao);
            getInstancesOfInventario(dao);
        }
    }

    private void getInstancesOfInventario(Class dao) {
        if(dao.getSimpleName().equals(USER_INTRANET_DAO_TAG)){
            userIntranetDAO = new UserIntranetDAO(sessionFactoryInventario);
        }
    }

   /* private void getInstancesOfOnlineDB(Class dao) {



    }*/

    private void getInstancesOfM4C(Class dao) {
        if(dao.getSimpleName().equals(STATE_DAO_TAG)){
            stateDAO = new StateDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(COUNTRY_DAO_TAG)){
            countryDAO = new CountryDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(CERTIFICATE_DAO_TAG)){
            certificateDAO = new CertificateDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(CAMPAIGN_DAO_TAG)){
            campaignDAO = new CampaignDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(CALLCENTER_DAO_TAG)){
            callCenterDAO = new CallCenterDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(SEGMENT_DAO_TAG)){
            segmentDAO = new SegmentDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(MERCHANT_DAO_TAG)){
            merchantDAO = new MerchantDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(FOLIOSCERTIFICATE_DAO_TAG)){
            foliosCertificateDAO = new FoliosCertificateDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(OFFER_DAO_TAG)){
            offerDAO = new OfferDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(MEAL_PLAN_DAO_TAG)){
            mealPlanDAO = new MealPlanDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(DESTINATION_DAO_TAG)){
            destinationDAO = new DestinationDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(TRANSPORTATION_DAO_TAG)){
            transportationDAO = new TransportationDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(HOOK_DAO_TAG)){
            hookDAO = new HookDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(HOTEL_DAO_TAG)){
            hotelDAO = new HotelDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(LANGUAGE_DAO_TAG)){
            languageDAO = new LanguageDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(RESERVATION_GROUP_DAO_TAG)){
            reservationGroupDAO = new ReservationGroupDAO(sessionFactoryM4C);
        } else if(dao.getSimpleName().equals(OFFER_LANGUAGE_DAO_TAG)) {
            offerLanguageDAO = new OfferLanguageDAO(sessionFactoryM4C);
        } else if(dao.getSimpleName().equals(HOTEL_IMAGE_DAO_TAG)) {
            hotelImageDAO = new HotelImageDAO(sessionFactoryM4C);
        } else if(dao.getSimpleName().equals(HOTEL_LANGUAGE_DAO_TAG)) {
            hotelLanguageDAO = new HotelLanguageDAO(sessionFactoryM4C);
        } else if(dao.getSimpleName().equals(TITLE_DAO_TAG)) {
            titleDAO = new TitleDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(CUSTOMER_DAO_TAG)) {
            customerDAO = new CustomerDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(FEATURE_DAO_TAG)) {
            featureDAO = new FeatureDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(SALE_DAO_TAG)) {
            saleDAO = new SaleDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(SUB_SERVICE_TAG)) {
            subServiceDAO = new SubServiceDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(CONTRACT_SERVICE_TAG)) {
            contractServiceDAO = new ContractServiceDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(EDO_CIVIL_DAO_TAG)) {
            edoCivilDAO = new EdoCivilDAO(sessionFactoryM4C);
        } else if(dao.getSimpleName().equals(CERT_CUSTOMER_DAO_TAG)){
            certCustomerDAO = new CertCustomerDAO(sessionFactoryM4C);
        } else if(dao.getSimpleName().equals(CERT_LOGIN_DAO_TAG)){
            certLoginDAO = new CertLoginDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(DESTINATION_DAO_TAG)){
//            perfilUserDAO = new PerfilUserDAO(sessionFactoryOnlineDB);
            destinationDAO = new DestinationDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(OFFER_DESTINATION_DAO_TAG)){
            offerDestinationDAO = new OfferDestinationDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(CUSTOMER_FEATURES_DAO_TAG)){
            customerFeaturesDAO = new CustomerFeaturesDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(IMAGE_CERTIFICATE_DAO_TAG)){
            imageCertificateDAO = new ImageCertificateDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(CUSTOMER_NOTES_DAO_TAG)){
            customerNoteDAO = new CustomerNoteDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(CUSTOMER_NOTE_PK_DAO_TAG)){
            customerNotePkDAO = new CustomerNotePkDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(NOTE_TYPE_DAO_TAG)){
            noteTypeDAO = new NoteTypeDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(STATUS_DAO_TAG)){
            statusDAO = new StatusDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(CERTSCONFIG_DAO_TAG)){
            certsConfigDAO = new CertsConfigDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(BATCHCERTIFICATES_DAO_TAG)){
            batchCertificatesDAO = new BatchCertificatesDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(BATCHCERTIFICATE_FOLIOS_DAO_TAG)){
            batchCertificateFoliosDAO = new BatchCertificateFoliosDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(PAYMENT_DAO_TAG)){
            paymentDAO = new PaymentDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(USER_DAO_TAG)){
            userDAO = new UserDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(BANCOS_DAO_TAG)){
            bancosDAO = new BancosDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(CARD_TYPE_DAO_TAG)){
            cardTypeDAO = new CardTypeDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(PAYMENT_SERVICE_DAO_TAG)){
            paymentServiceDAO = new PaymentServiceDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(LOGM4SG_DAO_TAG)){
            logM4SGDAO = new LogM4SGDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(DATACARD_DAO_TAG)){
            dataCardDAO = new DataCardDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(SELLER_DAO_TAG)){
            sellerDAO = new SellerDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(EXTRA_BONUS_DAO_TAG)){
            extraBonusDAO = new ExtraBonusDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(RESERVATION_STATUS_DAO_TAG)){
            reservationStatusDAO = new ReservationStatusDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(DEPARTMENT_DAO_TAG)){
            departmentDAO = new DepartmentDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(ALERT_DAO_TAG)){
            alertDAO = new AlertDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(ASSISCARD_DAO_TAG)){
            assistCardDAO = new AssistCardDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(RESERVATION_DAO_TAG)){
            reservationDAO = new ReservationDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(PAX_DAO_TAG)){
            paxDAO = new PaxDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(PROGRAM_DAO_TAG)){
            programDAO = new ProgramDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(DNCL_DAO_TAG)){
            dnclDAO = new DNCLDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(APPAPI_DAO_TAG)){
            appApiDAO = new AppApiDAO(sessionFactoryM4C);
        }else if(dao.getSimpleName().equals(COMMISION_REPORT_DAO)){
            commisionReportDAO = new CommisionReportDAO(sessionFactoryM4C);
        }

//        else if(dao.getSimpleName().equals(PROMOCION_DAO_TAG)){
//            promocionDAO = new PromocionDAO(sessionFactoryM4C);
//        }
    }

    public boolean saveLog(AuditDTO audit){

        Optional<UserIntranet> userIntranetOptional=userIntranetDAO.find(audit.getUsername());
        Optional<CertLogin> certLoginOptional=new CertLoginService().findById(audit.getUsername());
        if(!userIntranetOptional.isPresent()&&!certLoginOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorRepresentation("username", "Username does not exists.")).build());
        }

        String username="";
        String name="";
        if(userIntranetOptional.isPresent()){
            username=userIntranetOptional.get().getUsername();
            name=userIntranetOptional.get().getName();

        }else{
            username=certLoginOptional.get().getUser();
            name=certLoginOptional.get().getName();
            //customer.setUserName(certLoginOptional.get().getUser());
        }
        LogM4SG logM4SG=new LogM4SG(username,name,audit.getIp(),
                audit.getModulo(),audit.getVentana(),audit.getAccion(),
                new Date(),audit.getDetalle(),audit.getIdBooking()!=null?audit.getIdBooking().intValue():null,
                null
        );


        logM4SGDAO.saveOrUpdate(logM4SG);

        return true;
    }
}
