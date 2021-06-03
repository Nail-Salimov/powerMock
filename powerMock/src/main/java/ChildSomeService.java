public class ChildSomeService extends SomeService{

    @Override
    public void someWork(){
        System.out.println("Work started");
        super.someWork();
        System.out.println("Work is done");
    }
}
