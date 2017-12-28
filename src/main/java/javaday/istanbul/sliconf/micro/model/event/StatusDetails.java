package javaday.istanbul.sliconf.micro.model.event;

import java.util.List;

/**
 * Event'in durumunu (kullanilabilir olma) belirten sinif.
 * Hangi bilgiler eksik hangi bilgiler girilmis. Event'in mobilden kullanimi icin
 * hangi bilgilerin girilmesi gerektigini gosteren bilgi
 */
public class StatusDetails {

    private int percentage;
    private List<String> passed;
    private List<String> failed;
    private List<String> optionalPassed;
    private List<String> optionalFailed;

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public List<String> getPassed() {
        return passed;
    }

    public void setPassed(List<String> passed) {
        this.passed = passed;
    }

    public List<String> getFailed() {
        return failed;
    }

    public void setFailed(List<String> failed) {
        this.failed = failed;
    }

    public List<String> getOptionalPassed() {
        return optionalPassed;
    }

    public void setOptionalPassed(List<String> optionalPassed) {
        this.optionalPassed = optionalPassed;
    }

    public List<String> getOptionalFailed() {
        return optionalFailed;
    }

    public void setOptionalFailed(List<String> optionalFailed) {
        this.optionalFailed = optionalFailed;
    }
}
