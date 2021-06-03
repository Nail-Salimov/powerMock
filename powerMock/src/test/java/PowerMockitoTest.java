import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.powermock.api.mockito.PowerMockito.*;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SomeService.class, InternalService.class})
public class PowerMockitoTest {

    @Test
    public void finalMethodTest() {
        SomeService someService = spy(new SomeService());
        when(someService.helloMethod()).thenReturn("Hello, PowerMockito");
        System.out.println(someService.helloMethod());
    }

    @Test
    public void callPrivateMethodTest() throws Exception {

        String message = "Hello PowerMockito";
        String expectation = "Expectation";

        SomeService mock = spy(new SomeService());
        doReturn(expectation).when(mock, "printMessage", message);

        String actual = mock.privateCall(message);
        assertEquals(expectation, actual);
    }

    @Test
    public void staticMethodTest(){

        String str = "Hello, PowerMockito";
        SomeService someService = new SomeService();

        //создает мок для всех статических методов
        mockStatic(InternalService.class);
        when(InternalService.getStringLength(str)).thenReturn(10);

        int length = someService.length(str);
        assertEquals(length, 10);
    }

    @Test
    public void deletingMethodBodyTest(){

        ChildSomeService child = new ChildSomeService();
        suppress(methodsDeclaredIn(SomeService.class));
        child.someWork();
    }

    @Test
    public void mockNewObjectTest() throws Exception {

        InternalService service = new InternalService();
        service.setPrivateField("Hello 2");

        whenNew(InternalService.class).withAnyArguments().thenReturn(service);

        String value = new SomeServiceFactory()
                .createInternalService().getPrivateField();
        assertEquals(value, "Hello 1");
    }

    @Test
    public void privateFieldTest(){

        String value = "Hello 3";
        InternalService service = new InternalService();

        Whitebox.setInternalState(service, "privateField", value);
        assertEquals(value, service.getPrivateField());
    }

}
