import com.example.TemporaryDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(/*classes = SpringExpressionTest.AppConfig.class*/)
@WebAppConfiguration
public class SpringExpressionTest {

    @Autowired
    MockHttpSession session;

    @Autowired
    @Qualifier("sessionScopedTemporaryDirectory")
    TemporaryDirectory sessionScopedTemporaryDirectory;

    @Autowired
    @Qualifier("requestScopedTemporaryDirectory")
    TemporaryDirectory requestScopedTemporaryDirectory;

    @Test
    public void test() {

        String expressionValue = "1 * 10 + 1";

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(expressionValue);
        Integer calculationResult = expression.getValue(Integer.class);
        System.out.println(calculationResult);
        assertThat(calculationResult, is(11));

    }

    @Test
    public void test2() {

        Staff staff = new Staff();
        staff.setId("S00001");
        staff.setName("Spring 太郎");

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("name");
        String name = expression.getValue(staff, String.class);
        System.out.println(name);
        assertThat(name, is("Spring 太郎"));

    }

    @Test
    public void test3() {

        Staff staff = new Staff();
        staff.setId("S00001");
        staff.setName("Spring 太郎");

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("joinedYear");
        expression.setValue(staff, "2000");

        int joinedYear = staff.getJoinedYear();

        System.out.println(joinedYear);
        assertThat(joinedYear, is(2000));

    }

    @Test
    public void test4() {

        Staff staff = new Staff();
        staff.setId("S00001");
        staff.setName("Spring 太郎");

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("Staff Name : #{name}", ParserContext.TEMPLATE_EXPRESSION);
        String name = expression.getValue(staff, String.class);
        System.out.println(name);
        assertThat(name, is("Staff Name : Spring 太郎"));

    }

    @Test
    public void test5() throws IOException {

        System.out.println(sessionScopedTemporaryDirectory.getDirectory());

        new File(sessionScopedTemporaryDirectory.getDirectory(),"a.txt").createNewFile();


        session.invalidate();

    }


    @Test
    public void test6() throws IOException {

        System.out.println(requestScopedTemporaryDirectory.getDirectory());

        new File(requestScopedTemporaryDirectory.getDirectory(),"a.txt").createNewFile();


        session.invalidate();

    }


    static class Staff {
        private String id;
        private String name;
        private int joinedYear;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getJoinedYear() {
            return joinedYear;
        }

        public void setJoinedYear(int joinedYear) {
            this.joinedYear = joinedYear;
        }
    }

//    @Configuration
//    @EnableAspectJAutoProxy
//    @ComponentScan("com.example")
//    static class AppConfig{
//
//    }

}
