package io.duna.core.net.ssl;

public class SslOptions {
    private boolean enabled;
    private boolean useSelfSignedCertificate;
    private String certificateChain;
    private String certificateKey;
    private String certificatePassword;

    public SslOptions() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isUseSelfSignedCertificate() {
        return useSelfSignedCertificate;
    }

    public void setUseSelfSignedCertificate(boolean useSelfSignedCertificate) {
        this.useSelfSignedCertificate = useSelfSignedCertificate;
    }

    public String getCertificateChain() {
        return certificateChain;
    }

    public void setCertificateChain(String certificateChain) {
        this.certificateChain = certificateChain;
    }

    public String getCertificateKey() {
        return certificateKey;
    }

    public void setCertificateKey(String certificateKey) {
        this.certificateKey = certificateKey;
    }

    public String getCertificatePassword() {
        return certificatePassword;
    }

    public void setCertificatePassword(String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }
}
