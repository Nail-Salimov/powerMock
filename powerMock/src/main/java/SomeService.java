public class SomeService {

    public final String helloMethod() {
        return "Hello World!";
    }

    public int length(String str){
        return InternalService.getStringLength(str);
    }

    protected void someWork(){
        System.out.println("Some work");
    }

    private String printMessage(String message) {
        return message;
    }

    public String privateCall(String message) {
        return printMessage(message);
    }
}
