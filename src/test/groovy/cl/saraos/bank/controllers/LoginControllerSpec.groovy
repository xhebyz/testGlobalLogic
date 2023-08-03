package cl.saraos.bank.controllers

import cl.saraos.bank.entity.PhoneEntity
import cl.saraos.bank.entity.UserEntity
import cl.saraos.bank.repository.PhoneRepository
import cl.saraos.bank.repository.UserRepository
import cl.saraos.bank.service.JwtTokenService
import cl.saraos.bank.service.LoginService
import cl.saraos.bank.service.UserService
import groovy.json.JsonSlurper
import org.junit.Assert
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import java.text.SimpleDateFormat
import java.util.stream.Collectors

@WebMvcTest(LoginController)
@ComponentScan("cl.saraos.bank")
class LoginControllerSpec extends Specification{

    MockMvc mockMvc

    @Autowired
    WebApplicationContext webApplicationContext

    @Autowired
    private LoginService loginService

    @Autowired
    UserService userService

    @Autowired
    JwtTokenService jwtTokenService

    @SpringBean
    UserRepository userRepository = Mock()

    @SpringBean
    PhoneRepository phoneRepository = Mock()

    def inputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a")

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    def "test login api ok"() {
        given:

        def token = jwtTokenService.generateToken("email@aaa.cl")


        def request = String.format("""{
    "token" : "%s"
}
""", token)

        def jsonResponseExpected = """
{
    "id": 3,
    "created": "ago 03, 2023 01:58:24 PM",
    "lastLogin": "ago 03, 2023 01:58:24 PM",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBhYWEuY2wiLCJleHAiOjE2OTEwMTU0ODB9.ZJ8tLuQuD8if57idR1YTLF2njVvgCihnhfVkJcU8T4s",
    "name": "name",
    "email": "email@aaa.cl",
    "password": "\$2a\$10\$/tvLgIoVvxNA1NW6Snnwn.h3dPy.JRtoOAVHUEh8vhbfy1wMh4r4y",
    "phones": [
        {
            "number": 122344,
            "citycode": 11,
            "contrycode": "CL"
        }
    ],
    "active": true
}
"""


        def responseExpected = new JsonSlurper().parseText(jsonResponseExpected)

        List<PhoneEntity> phones = responseExpected.phones.stream()
                .map(phone -> {
                    PhoneEntity.builder().contrycode(phone.contrycode).citycode(phone.citycode).number(phone.number)
                            .build()
                }).collect(Collectors.toList())


        def user = UserEntity.builder()
                .lastLogin(inputFormat.parse(responseExpected.lastLogin))
                .createdAt(inputFormat.parse(responseExpected.created))
                .name(responseExpected.name)
                .password(responseExpected.password)
                .email(responseExpected.email)
                .password(responseExpected.password)
                .phones(phones)
                .isActive(true)
                .build()
        1 * userRepository.save(_) >> user
        1 * userRepository.findAllByEmail("email@aaa.cl") >> Arrays.asList(
                user
        )
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/login").
                contentType(MediaType.APPLICATION_JSON).
                content(request)
        ).andReturn().response

        then:
        result.status == HttpStatus.OK.value()
        result.contentType == MediaType.APPLICATION_JSON_VALUE

        and:
        def responseJson = new JsonSlurper().parseText(result.contentAsString)
        responseJson.name == responseExpected.name
        responseJson.email == responseExpected.email
        Assert.assertNotNull(responseJson.phones)
        Assert.assertNotNull(responseJson.token)
        Assert.assertNotNull(responseJson.created)
        Assert.assertNotNull(responseJson.lastLogin)
        Assert.assertNotNull(responseJson.id)


    }

}
