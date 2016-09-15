package com.m4sg.crm4marketingsunset;

import com.bazaarvoice.dropwizard.assets.ConfiguredAssetsBundle;
import com.hubspot.jackson.jaxrs.PropertyFilteringMessageBodyWriter;
import com.m4c.model.entity.*;
import com.m4c.model.entity.online.*;
import com.m4c.model.entity.online.CustomerNote;
import com.m4c.model.entity.sistemas.*;
import com.m4c.model.entity.sistemas.Position;
import com.m4c.model.entity.tc.Contract;
import com.m4c.model.entity.tc.ManifestContract;
import com.m4sg.crm4marketingsunset.resources.*;
import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * Hello world!
 *
 */
public class App extends Application<CRM4MarketingSunsetConfiguration>
{
    public static void main( String[] args ) throws Exception {
        new App().run(args);


    }

    @Override
    public void initialize(Bootstrap<CRM4MarketingSunsetConfiguration> bootstrap) {
        //
        bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/", "/uploads/"));
        bootstrap.addBundle(hibernateBundleM4C);
//        bootstrap.addBundle(hibernateBundleOnlineDB);
        bootstrap.addBundle(hibernateBundleInventarioDB);
    }

    @Override
    public void run(CRM4MarketingSunsetConfiguration configuration, Environment environment) throws Exception {
        configureCors(environment);
        environment.jersey().register(new PropertyFilteringMessageBodyWriter());
        environment.jersey().register(new CampaignResource(environment.getValidator()));
        environment.jersey().register(new OfferResource(environment.getValidator()));
        environment.jersey().register(new MealPlanResource(environment.getValidator()));
        environment.jersey().register(new DestinationResource(environment.getValidator()));
        environment.jersey().register(new CountryResource(environment.getValidator()));
        environment.jersey().register(new TransportationResource(environment.getValidator()));
        environment.jersey().register(new HookResource(environment.getValidator()));
        environment.jersey().register(new HotelResource(environment.getValidator()));
        environment.jersey().register(new StateResource(environment.getValidator()));
        environment.jersey().register(new LanguageResource(environment.getValidator()));
        environment.jersey().register(new SegmentResource(environment.getValidator()));
        environment.jersey().register(new ReservationGroupResource(environment.getValidator()));
        environment.jersey().register(new MerchantResource(environment.getValidator()));
        environment.jersey().register(new CertificateResource(environment.getValidator()));
        environment.jersey().register(new UserIntranetResource(environment.getValidator()));
        environment.jersey().register(new CertCustomerResource(environment.getValidator()));
        environment.jersey().register(new CustomerResource(environment.getValidator()));
        environment.jersey().register(new CertLoginResource(environment.getValidator()));
        environment.jersey().register(new TitleResource(environment.getValidator()));
        environment.jersey().register(new FeatureResource(environment.getValidator()));
        environment.jersey().register(new EdoCivilResource(environment.getValidator()));
        environment.jersey().register(new CustomerNoteResource(environment.getValidator()));
        environment.jersey().register(new EcertResource(environment.getValidator()));
        environment.jersey().register(new CallCenterResource(environment.getValidator()));
        environment.jersey().register(new UserResource(environment.getValidator()));
        environment.jersey().register(new BankResource(environment.getValidator()));
        environment.jersey().register(new CardTypeResource(environment.getValidator()));
        environment.jersey().register(new SaleResource(environment.getValidator()));
        environment.jersey().register(new InventoryFoliosCertificateResource(environment.getValidator()));
        environment.jersey().register(new SubServiceResource(environment.getValidator()));
        environment.jersey().register(new SellerResource(environment.getValidator()));
        environment.jersey().register(new ExtraBonusResource(environment.getValidator()));
        environment.jersey().register(new ReservationStatusResource(environment.getValidator()));
        environment.jersey().register(new NoteTypeResource(environment.getValidator()));
        environment.jersey().register(new MoodResource(environment.getValidator()));
        environment.jersey().register(new DepartmentResource(environment.getValidator()));
        environment.jersey().register(new AlertResource(environment.getValidator()));
        environment.jersey().register(new AssistCardResource(environment.getValidator()));
        environment.jersey().register(new AuditResource(environment.getValidator()));
        environment.jersey().register(new AppApiResource(environment.getValidator()));
        environment.jersey().register(new DNCLResource());
        environment.jersey().register(new CommisionReportResource());
        environment.jersey().register(new BasicAuthProvider<SimplePrincipal>(  new SimpleAuthenticator(), "Web Service Realm"));

    }

    private void configureCors(Environment environment) {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowCredentials", "true");
    }

    // TODO: We are using M4C database and not onlineDB for state and country maybe we can stay with only one data source
    private static final HibernateBundle<CRM4MarketingSunsetConfiguration> hibernateBundleM4C =
            new HibernateBundle<CRM4MarketingSunsetConfiguration>(
                    Campaign.class,
                    CallCenter.class,
                    ResOnlineConfig.class,
                    Program.class,Segment.class,
                    User.class,Hotel.class,
                    Bonus.class,MealPlan.class,
                    ReservationGroup.class,
                    Merchant.class,
                    Offer.class, Destination.class, Hook.class,
                    Transportation.class, State.class,
                    Language.class, HotelLanguage.class,FoliosCertificado.class,
                    HotelImage.class, OfferLanguage.class,
                    OfferDestination.class, Country.class,
                    Title.class,Features.class,CustomerFeatures.class,Customer.class,
                    CustomerCard.class,Status.class,Reservation.class,Pax.class,
                    PaymentReservation.class,ContractService.class,PreCall.class,
                    SubService.class,CategoryService.class,Service.class,InvoiceDetail.class,
                    Invoice.class,ReservationStatus.class,Pickup.class,Operator.class,Sale.class,SubService.class,
                    EdoCivil.class,
                    CertCustomer.class,
                    Department.class,
                    CertsConfig.class,
                    com.m4c.model.entity.CustomerNote.class,
                    com.m4c.model.entity.NoteType.class,
                    CertLogin.class,
                    Bank.class,
                    CardType.class,
                    PaymentService.class,
                    DataCard.class,
                    AppApi.class,
                   // com.m4c.model.entity.online.NoteType.class,
                    Contract.class,
                    ManifestContract.class,
                    ExtraBonus.class,
                    ExtraBonusId.class,
                    Certificate.class,
                    CertType.class,
                    CertContact.class,
                    CardType.class,ImageCertificate.class,
                    BatchCertificateFolios.class,
                    BatchCertificates.class,
                    Promotion.class,
                    Promocion.class,
                    Payment.class,
                    Bank.class,
                    LogM4SG.class,
                    DataCardId.class,
                    Sellers.class,
                    Alert.class,
                    AssistCard.class,
                    DNCL.class, Survey.class
                  ) {
                @Override
                public DataSourceFactory getDataSourceFactory(CRM4MarketingSunsetConfiguration configuration) {
                    return configuration.getDatabaseM4C();
                }
            };


    private static final HibernateBundle<CRM4MarketingSunsetConfiguration> hibernateBundleOnlineDB =
            new HibernateBundle<CRM4MarketingSunsetConfiguration>(
                    CustomerNote.class,

                    State.class,Country.class
            ) {
        @Override
        public DataSourceFactory getDataSourceFactory(CRM4MarketingSunsetConfiguration configuration) {
            return configuration.getDatabaseOnlineDB();
        }
    };
    private static final HibernateBundle<CRM4MarketingSunsetConfiguration> hibernateBundleInventarioDB =
            new HibernateBundle<CRM4MarketingSunsetConfiguration>(Area.class,
                    Location.class,
                    Position.class,
                    TypeUser.class,
                    UserIntranet.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(CRM4MarketingSunsetConfiguration configuration) {
            return configuration.getDatabaseInventario();
        }
    };

    public static HibernateBundle<CRM4MarketingSunsetConfiguration> getHibernateBundleM4C() {
        return hibernateBundleM4C;
    }

    /*public static HibernateBundle<CRM4MarketingSunsetConfiguration> getHibernateBundleOnlineDB() {
        return hibernateBundleOnlineDB;
    }*/

    public static HibernateBundle<CRM4MarketingSunsetConfiguration> getHibernateBundleInventarioDB() {
        return hibernateBundleInventarioDB;
    }
}
