package hexlet.code.dto;

public class BasePage {
    private String flashMessage;
    private String flashType;

    public final String getFlashMessage() {
        return flashMessage;
    }

    public final void setFlashMessage(String newFlashMessage) {
        this.flashMessage = newFlashMessage;
    }

    public final String getFlashType() {
        return flashType;
    }

    public final void setFlashType(String newFlashType) {
        this.flashType = newFlashType;
    }
}
