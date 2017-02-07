package io.duna.core.net.ssl;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.io.File;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SslContextFactory {

    private static final Logger logger = Logger.getLogger(SslContextFactory.class.getName());

    public static SslContext create(SslOptions options) {
        if (!options.isEnabled()) return null;

        if (options.isUseSelfSignedCertificate()) {
            try {
                logger.fine(() -> "Creating self-signed certificate");

                SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
                return SslContextBuilder
                    .forServer(
                        selfSignedCertificate.certificate(),
                        selfSignedCertificate.privateKey())
                    .build();
            } catch (CertificateException | SSLException ex) {
                logger.log(Level.SEVERE, ex,
                    () -> "Could not create a self-signed certificate.");
                return null;
            }
        }

        File certificateChain = new File(options.getCertificateChain());
        if (!certificateChain.exists() || !certificateChain.isFile()) {
            logger.severe(() -> "Certificate chain file not found: " +
                certificateChain.getAbsolutePath());
        }

        File certificateKey = new File(options.getCertificateKey());
        if (!certificateKey.exists() || !certificateKey.isFile()) {
            logger.severe(() -> "Certificate chain file not found: " +
                certificateKey.getAbsolutePath());
        }

        try {
            logger.fine(() -> "Using OpenSSL as context provider");

            SslContext result = SslContextBuilder
                .forServer(certificateChain, certificateKey, options.getCertificatePassword())
                .sslProvider(SslProvider.OPENSSL)
                .build();

            logger.fine(() -> "SSL context created successfully");

            return result;
        } catch (SSLException ex) {
            logger.log(Level.FINE, ex,
                () -> "Error while trying to create an SSL context with OpenSSL");
        }

        try {
            logger.fine(() -> "Using the JDK as context provider");

            SslContext result = SslContextBuilder
                .forServer(certificateChain, certificateKey, options.getCertificatePassword())
                .sslProvider(SslProvider.JDK)
                .build();

            logger.fine(() -> "SSL context created successfully");

            return result;
        } catch (SSLException ex) {
            logger.log(Level.SEVERE, ex, () -> "Error while trying to create an SSL context");
            return null;
        }
    }
}
