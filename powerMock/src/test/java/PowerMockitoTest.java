
import loggers.ConsoleLogger;
import loggers.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.powermock.api.mockito.PowerMockito.*;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SomeService.class, InternalService.class, Logger.class})
public class PowerMockitoTest {

    //работа с final методами
    @Test
    public void finalMethodTest() {
        SomeService someService = spy(new SomeService());
        when(someService.helloMethod()).thenReturn("Hello, PowerMockito");
        System.out.println(someService.helloMethod());
    }

    //работа с private методами
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
    public void mockAllStaticMethodsTest() {

        String str = "Hello, PowerMockito";
        SomeService someService = new SomeService();

        //создает мок для всех статических методов
        mockStatic(InternalService.class);
        when(InternalService.getStringLength(str)).thenReturn(10);

        int length = someService.length(str);
        assertEquals(length, 10);
    }

    //заглушка на определенный статический метод
    @Test
    public void stubStaticMethod() {
        stub(method(InternalService.class, "getStringLength")).toReturn(10);
        assertEquals(InternalService.getStringLength("1234"), 10);
    }

    //сделать все методы неисполненными
    @Test
    public void suppressMethodBodyTest1() {

        ChildSomeService child = new ChildSomeService();
        suppress(methodsDeclaredIn(SomeService.class));
        child.someWork();
    }

    @Test
    public void suppressMethodBodyTest2() {

        //со spy не работает
        ChildSomeService child = spy(new ChildSomeService());
        suppress(methodsDeclaredIn(SomeService.class));
        child.someWork();
    }

    //сделать конструктор неисполняемым
    @Test
    public void suppressConstructorTest() {
        suppress(constructorsDeclaredIn(InternalService.class));
        System.out.println(new InternalService("3").getPrivateField());
    }

    //мок на конструктор
    @Test
    public void mockConstructorTest() throws Exception {

        InternalService service = new InternalService();
        service.setPrivateField("Hello 2");

        //мок конструктора
        whenNew(InternalService.class).withAnyArguments().thenReturn(service);

        assertSame(new InternalService(), service);
    }

    //замена возвращаемых значений статического метода с условием
    @Test
    public void replaceTest(){

        replace(method(InternalService.class, "getStringLength")).with(
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] arguments) throws Throwable {
                        if(arguments[0].equals("3")) {
                            return 3;
                        } else {
                            return 0;
                        }
                    }
                }
        );

        assertEquals(InternalService.getStringLength("3"), 3);
        assertEquals(InternalService.getStringLength("4"), 0);
    }

    //замена статического метода другим статическим методом
    @Test
    public void replaceMethodWithAnotherMethodTest(){
        replace(method(Logger.class, "debug", String.class))
                .with(method(ConsoleLogger.class, "print"));

        new InternalService("world").methodForLogger();
    }

    //присваивание значения приватной переменной
    @Test
    public void privateFieldTest() {

        String value = "Hello 3";
        InternalService service = new InternalService();

        Whitebox.setInternalState(service, "privateField", value);
        assertEquals(value, service.getPrivateField());
    }

}
