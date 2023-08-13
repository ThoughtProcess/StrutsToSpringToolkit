@SuppressWarnings({ "requires-automatic", "requires-transitive-automatic" })
module StrutsToSpringToolkit.jspServices.main {
    requires transitive org.jsoup;
    requires transitive org.apache.logging.log4j;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires jakarta.annotation;

    exports com.rombalabs.strutstospringtoolkit.jspservices;
}