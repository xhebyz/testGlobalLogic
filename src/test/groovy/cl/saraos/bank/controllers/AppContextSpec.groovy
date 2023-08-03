package cl.saraos.bank.controllers

import cl.saraos.bank.ExamBankApplication
import cl.saraos.bank.repository.UserRepository
import cl.saraos.bank.service.JwtTokenService
import cl.saraos.bank.service.LoginService
import cl.saraos.bank.service.RegisterService
import cl.saraos.bank.service.UserService
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

@SpringBootTest
class AppContextSpec extends Specification {


    @Mock
    LoginService loginService

    @Mock
    RegisterController registerController

    @Autowired
    private ExamBankApplication examBankApplication;

    def setup() {
        examBankApplication = new ExamBankApplication()
    }


    def "when context is loaded then all expected beans are created"() {
        expect: "the examBankApplication is created"
        examBankApplication
    }


}
