package id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception;

public class UploadImageException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Image is corrupted. Please reupload your image or contact the administrator";

    public UploadImageException(String message) {
        super(message);
    }

    public UploadImageException() {
        super(DEFAULT_MESSAGE);
    }

}
