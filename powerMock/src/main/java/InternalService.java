import loggers.Logger;

public class InternalService {

    private String privateField;
    private static final String STATIC_FIELD = "STATIC";

    public InternalService(){};

    public InternalService(String privateField) {
        this.privateField = privateField;
    }

    public static int getStringLength(String str) {
        return str.length();
    }

    public String getPrivateField() {
        return privateField;
    }

    public void setPrivateField(String privateField) {
        this.privateField = privateField;
    }

    public void methodForLogger(){
        Logger.debug(privateField);
    }
}
