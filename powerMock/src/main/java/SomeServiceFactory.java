public class SomeServiceFactory {

    public InternalService createInternalService(){
        InternalService service = new InternalService();
        service.setPrivateField("Hello 1");
        return service;
    }
}
